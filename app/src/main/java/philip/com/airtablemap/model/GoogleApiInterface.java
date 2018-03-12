package philip.com.airtablemap.model;

import io.reactivex.Single;
import philip.com.airtablemap.model.dto.GoogleGeoResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit Interface for Google geo API.
 */

public interface GoogleApiInterface {
    String baseUrl = "https://maps.googleapis.com/maps/api/geocode/";

    @GET("json")
    Single<GoogleGeoResponse> requestGeoAPI(@Query("address") String address, @Query("key") String apiKey, @Query("latlng") String latlng);
}
