package philip.com.airtablemap.util;

import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;

/**
 * Geo utils
 */

public class GeoUtils {
    private static final String LOG_TAG = GsonUtil.class.getSimpleName();
    private static final String sGeoLocationRegExp = "^(\\-?\\d+(\\.\\d+)?),\\s*(\\-?\\d+(\\.\\d+)?)$";

    /**
     * If it is LatLng then return, but if it is not return null.
     * To make LatLng to address, I should call Google API, and I do not want to call API in util class.
     */
    public static LatLng getLatLng(String location) {
        if (isGeoLocation(location)) {
            String[] strings = location.split(",\\s*");

            return new LatLng(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
        } else {
            return null;
        }
    }

    /**
     * Check if is geo location.
     */
    public static boolean isGeoLocation(String location) {
        if (TextUtils.isEmpty(location)) {
            return false;
        }

        return location.matches(sGeoLocationRegExp);
    }

    /**
     * Change LatLng to String.
     */
    public static String latLngToString(LatLng latLng) {
        if (latLng == null) {
            return null;
        } else {
            return String.format("%s, %s", latLng.latitude, latLng.longitude);
        }
    }
}
