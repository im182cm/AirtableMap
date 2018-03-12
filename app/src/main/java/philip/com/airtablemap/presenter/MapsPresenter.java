package philip.com.airtablemap.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import philip.com.airtablemap.R;
import philip.com.airtablemap.contract.MapsContract;
import philip.com.airtablemap.model.CallbackWrapper;
import philip.com.airtablemap.model.Repository;
import philip.com.airtablemap.model.RetrofitManager;
import philip.com.airtablemap.model.dto.AirtableResponse;
import philip.com.airtablemap.model.dto.GoogleGeoResponse;
import philip.com.airtablemap.model.dto.Record;
import philip.com.airtablemap.model.vo.AirtableUserInfo;
import philip.com.airtablemap.model.vo.Input;
import philip.com.airtablemap.util.GeoUtils;
import philip.com.airtablemap.util.GsonUtil;

/**
 * Presenter for MapActivity
 */

public class MapsPresenter implements MapsContract.Presenter {
    private static final String LOG_TAG = MapsPresenter.class.getSimpleName();
    private final MapsContract.View mView;
    private final Repository mRepository;

    private List<Record> mRecords;
    private AirtableUserInfo mAirUserInfo = null;

    private Marker mSelectedMarker = null;
    private boolean isFirstLocation = true;

    private boolean isCreating = false;

    public MapsPresenter(@NonNull MapsContract.View view, @NonNull Repository repository, @Nullable List<Record> records, @NonNull AirtableUserInfo airtableUserInfo) {
        this.mView = view;
        this.mRepository = repository;
        this.mRecords = records;
        this.mAirUserInfo = airtableUserInfo;

        mView.setPresenter(this);
    }

    @Override
    public void addMarkers() {
        for (final Record record : mRecords) {
            Map<String, String> fields = record.getFields();
            String addressValue = fields.get(mAirUserInfo.getAddressField());

            LatLng latLng = GeoUtils.getLatLng(addressValue);
            if (latLng == null) {
                requestGeoAPI(addressValue, null, new CallbackWrapper<GoogleGeoResponse>(mView) {
                    @Override
                    public void onSuccess(GoogleGeoResponse response) {
                        if (response.getLatLng() != null) {
                            mView.addMarker(new MarkerOptions().position(response.getLatLng()).title(record.getId()).snippet(GsonUtil.toJson(record.getFields())).draggable(true));

                            if (isFirstLocation) {
                                isFirstLocation = false;
                                mView.moveCamera(response.getLatLng());
                            }
                        }
                    }
                });
            } else {
                mView.addMarker(new MarkerOptions().position(latLng).title(record.getId()).snippet(GsonUtil.toJson(record.getFields())).draggable(true));
            }

            if (isFirstLocation && latLng != null) {
                isFirstLocation = false;
                mView.moveCamera(latLng);
            }
        }
    }

    @Override
    public void addMarker(MarkerOptions markerOptions) {
        isCreating = true;
        mSelectedMarker = mView.addMarker(markerOptions);
    }

    @Override
    public void showMarkerInfoWindow(@NonNull Marker marker) {
        mSelectedMarker = marker;

        Map<String, String> fields = GsonUtil.fromJsonHashMap(marker.getSnippet());

        for (String key : fields.keySet()) {
            mView.addView(key, fields.get(key), GeoUtils.latLngToString(marker.getPosition()));
        }
    }

    @Override
    public void removeMarker() {
        if (mSelectedMarker == null) {
            return;
        }
        // [TODO] check if it is creating or removing from origin
        if (isCreating){
            mSelectedMarker.remove();
            mSelectedMarker = null;
            isCreating = false;

            mView.showToast(R.string.toast_marker_canceled);
        }

        mRepository.start(RetrofitManager.getAirtable().deleteRecord(mAirUserInfo.getBaseId(), mAirUserInfo.getTableName(), mSelectedMarker.getTitle(), mAirUserInfo.getApiKey()), new CallbackWrapper<AirtableResponse>(mView) {
            @Override
            public void onSuccess(AirtableResponse response) {
                if (response.isDeleted()) {
                    mSelectedMarker.remove();
                    mSelectedMarker = null;

                    mView.showToast(R.string.toast_marker_removed);
                }
            }
        });
    }

    @Override
    public void createRecord(List<Input> inputs) {
        Map<String, String> hashMap = new HashMap<>();
        for (Input input : inputs) {
            if (!TextUtils.isEmpty(input.getName()) && !TextUtils.isEmpty(input.getValue())) {
                hashMap.put(input.getName(), input.getValue());
            }
        }
        mSelectedMarker.setSnippet(GsonUtil.getInstance().toJson(hashMap));

        JsonObject jsonObject = makeJsonBody(hashMap);

        mRepository.start(RetrofitManager.getAirtable().createRecord(mAirUserInfo.getBaseId(), mAirUserInfo.getTableName(), mAirUserInfo.getApiKey(), jsonObject), new CallbackWrapper<AirtableResponse>(mView) {
            @Override
            public void onSuccess(AirtableResponse response) {
                Log.d(LOG_TAG, "success");
                Log.d(LOG_TAG, response.toString());
                Log.d(LOG_TAG, "id" + response.getId());

                mSelectedMarker.setTitle(response.getId());
                mView.showToast(R.string.toast_marker_created);
                mView.createDone();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mSelectedMarker.remove();
                mSelectedMarker = null;

                mView.createDone();
                mView.hideMarkerInfoWindow();
            }
        });
    }

    @Override
    public void updateRecord(List<Input> inputs) {
        if (mSelectedMarker == null) {
            return;
        }

        Map<String, String> hashMap = new HashMap<>();
        for (Input input : inputs) {
            if (!TextUtils.isEmpty(input.getName()) && !TextUtils.isEmpty(input.getValue())) {
                hashMap.put(input.getName(), input.getValue());
            }
        }

        updateRecord(hashMap);
    }

    @Override
    public void dragMarker(Marker marker) {
        mSelectedMarker = marker;
        final Map<String, String> hashMap = GsonUtil.fromJsonHashMap(marker.getSnippet());
        if (GeoUtils.isGeoLocation(hashMap.get(mAirUserInfo.getAddressField()))) {
            hashMap.put(mAirUserInfo.getAddressField(), GeoUtils.latLngToString(marker.getPosition()));

            updateRecord(hashMap);
        } else {
            requestGeoAPI(null, marker.getPosition(), new CallbackWrapper<GoogleGeoResponse>(mView) {

                @Override
                public void onSuccess(GoogleGeoResponse response) {
                    hashMap.put(mAirUserInfo.getAddressField(), response.getAddress());

                    updateRecord(hashMap);
                }
            });
        }
    }

    private void requestGeoAPI(@Nullable String addressValue, @Nullable LatLng latLng, @NonNull CallbackWrapper<GoogleGeoResponse> callbackWrapper) {
        if (TextUtils.isEmpty(addressValue) && TextUtils.isEmpty(GeoUtils.latLngToString(latLng))) {
            return;
        }
        mRepository.start(RetrofitManager.getGoogle().requestGeoAPI(addressValue, mView.getStringFromInt(R.string.google_geocoding_key), GeoUtils.latLngToString(latLng)), callbackWrapper);
    }

    public void updateRecord(final Map<String, String> hashMap) {
        JsonObject jsonObject = makeJsonBody(hashMap);

        mRepository.start(RetrofitManager.getAirtable().updateRecord(mAirUserInfo.getBaseId(), mAirUserInfo.getTableName(), mSelectedMarker.getTitle(), mAirUserInfo.getApiKey(), jsonObject), new CallbackWrapper<AirtableResponse>(mView) {
            @Override
            public void onSuccess(AirtableResponse response) {
                mSelectedMarker.setSnippet(GsonUtil.getInstance().toJson(hashMap));
                mSelectedMarker = null;

                mView.showToast(R.string.toast_marker_updated);
            }
        });
    }

    /**
     * Used JsonObject as a body because I do not know the fields in the user's grid view.
     */
    private JsonObject makeJsonBody(Map<String, String> map) {
        JsonObject jsonObject = new JsonObject();
        JsonElement jsonElement = GsonUtil.getInstance().toJsonTree(map);
        jsonObject.add("fields", jsonElement);

        return jsonObject;
    }
}
