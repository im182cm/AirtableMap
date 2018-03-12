package philip.com.airtablemap.contract;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import philip.com.airtablemap.model.vo.Input;
import philip.com.airtablemap.view.BaseView;


/**
 * Contract in MapActivity both with Presenter and View
 */
public interface MapsContract {
    interface View extends BaseView<Presenter> {
        /**
         * Add marker on the map.
         */
        Marker addMarker(MarkerOptions markerOptions);

        /**
         * Add view on marker info window.
         */
        void addView(String field, String fieldValue, String location);

        /**
         * Move map camera to LatLng.
         */
        void moveCamera(LatLng latLng);

        /**
         * To use getString().
         */
        String getStringFromInt(int id);

        /*
        * Marker is created.
        */
        void createDone();

        /**
         * To hide marker info window.
         */
        void hideMarkerInfoWindow();


        /**
         * Show toast message.
         */
        void showToast(int message);
    }

    interface Presenter {
        /**
         * Add markers when MapsAcitivty Starts.
         */
        void addMarkers();

        /**
         * add Marker on the map.
         */
        void addMarker(MarkerOptions markerOptions);

        /**
         * show Window when clicks a marker.
         */
        void showMarkerInfoWindow(Marker marker);

        /**
         * remove marker on the map.
         */
        void removeMarker();

        /**
         * call create record API to Airtable.
         */
        void createRecord(List<Input> inputs);

        /**
         * call update record API to Airtable.
         */
        void updateRecord(List<Input> inputs);

        /**
         * call update record API to Airtable.
         */
        void dragMarker(Marker marker);
    }
}
