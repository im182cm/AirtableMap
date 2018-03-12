package philip.com.airtablemap.view;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import philip.com.airtablemap.R;
import philip.com.airtablemap.contract.MapsContract;
import philip.com.airtablemap.model.Repository;
import philip.com.airtablemap.model.vo.AirtableUserInfo;
import philip.com.airtablemap.model.vo.Input;
import philip.com.airtablemap.model.dto.Record;
import philip.com.airtablemap.presenter.MapsPresenter;
import philip.com.airtablemap.util.ConstValue;
import philip.com.airtablemap.util.GeoUtils;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, MapsContract.View {
    private final String LOG_TAG = MapActivity.class.getSimpleName();
    private MapsContract.Presenter mPresenter;

    // for layout
    private LinearLayout mMarkerInfoWindow, mMarkerContent;
    private Button mSaveBt, mDeleteBt, mAddFieldBt;
    private List<Input> mMarkerInputViews;

    // for map
    private GoogleMap mMap;

    // for create marker
    private boolean isCreating = false;

    private String mAddressField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        List<Record> records = null;
        AirtableUserInfo airtableUserInfo = getIntent().getParcelableExtra(ConstValue.EXTRA_AIRTABLE_USER_INFORMATION);

        mAddressField = airtableUserInfo.getAddressField();

        if (getIntent().hasExtra(ConstValue.EXTRA_RECORDS)) {
            Log.d(LOG_TAG, getIntent().getParcelableArrayListExtra(ConstValue.EXTRA_RECORDS).toString());
            records = getIntent().getParcelableArrayListExtra(ConstValue.EXTRA_RECORDS);
        }

        mMarkerInputViews = new ArrayList<>();

        initLayout();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mPresenter = new MapsPresenter(this, Repository.getInstance(), records, airtableUserInfo);
    }

    /**
     * Initialize layouts.
     */
    private void initLayout() {
        mMarkerInfoWindow = (LinearLayout) findViewById(R.id.marker_window_info);
        mMarkerContent = (LinearLayout) findViewById(R.id.contentFrame);
        mSaveBt = (Button) findViewById(R.id.bt_save);
        mDeleteBt = (Button) findViewById(R.id.bt_delete);
        mAddFieldBt = (Button) findViewById(R.id.bt_add_field);

        mSaveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Input> inputs = new ArrayList<>();
                for (Input input : mMarkerInputViews) {
                    inputs.add(new Input(input));
                }

                if (isCreating) {
                    mPresenter.createRecord(inputs);
                } else {
                    mPresenter.updateRecord(inputs);
                }
            }
        });

        mDeleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMarkerInfoWindow.setVisibility(View.GONE);
                mPresenter.removeMarker();
            }
        });

        mAddFieldBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView("", "", null);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mPresenter.addMarkers();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                isCreating = true;

                clearMapWindowInfo();
                // Auto complete address field by geo location.
                addView(mAddressField, GeoUtils.latLngToString(latLng), GeoUtils.latLngToString(latLng));

                mPresenter.addMarker(new MarkerOptions().position(latLng).draggable(true));
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d(LOG_TAG, "marker clicked");
                clearMapWindowInfo();

                mPresenter.showMarkerInfoWindow(marker);
                return true;
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                mMarkerInfoWindow.setVisibility(View.GONE);
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                mPresenter.dragMarker(marker);
            }
        });
    }

    @Override
    public void setPresenter(MapsContract.Presenter presenter) {

    }

    @Override
    public void onError(String message) {
        Toast.makeText(MapActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Marker addMarker(MarkerOptions markerOptions) {
        return mMap.addMarker(markerOptions);
    }

    @Override
    public void addView(String field, String fieldValue, String location) {
        View root = getLayoutInflater().inflate(R.layout.map_window_info_input, null);
        EditText fieldEd = root.findViewById(R.id.edit_field_name);
        AutoCompleteTextView valueEd = root.findViewById(R.id.edit_field_value);

        fieldEd.setText(field);
        valueEd.setText(fieldValue);

        // for Auto complete the location
        if (mAddressField.equals(field)) {
            valueEd.setAdapter(new ArrayAdapter<String>(MapActivity.this, R.layout.support_simple_spinner_dropdown_item, new String[]{location}));
        }

        mMarkerInputViews.add(new Input(fieldEd, valueEd));
        mMarkerContent.addView(root);
    }

    @Override
    public void moveCamera(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(11));
    }

    @Override
    public String getStringFromInt(int id) {
        return getString(id);
    }

    @Override
    public void createDone() {
        isCreating = false;
    }

    @Override
    public void hideMarkerInfoWindow() {
        if (mMarkerInfoWindow.getVisibility() == View.VISIBLE) {
            mMarkerInfoWindow.setVisibility(View.GONE);
        }
    }

    @Override
    public void showToast(int message) {
        Toast.makeText(MapActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Clear Map Info Window
     */
    private void clearMapWindowInfo() {
        mMarkerContent.removeAllViews();
        mMarkerInputViews.clear();
        mMarkerInfoWindow.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (mMarkerInfoWindow.getVisibility() == View.VISIBLE) {
            mMarkerInfoWindow.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}
