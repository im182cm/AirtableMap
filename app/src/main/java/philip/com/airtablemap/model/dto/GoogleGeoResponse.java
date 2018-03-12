package philip.com.airtablemap.model.dto;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Data from google geo API
 */
public class GoogleGeoResponse {
    List<Result> results;

    public LatLng getLatLng() {
        if (results != null && results.size() > 0)
            return new LatLng(results.get(0).geometry.location.lat, results.get(0).geometry.location.lng);
        else
            return null;
    }

    public String getAddress() {
        if (results != null && results.size() > 0)
            return results.get(0).formatted_address;
        else
            return null;
    }
}

class Result {
    String formatted_address;
    Geometry geometry;
}

class Geometry {
    Location location;
}

class Location {
    double lat;
    double lng;
}
