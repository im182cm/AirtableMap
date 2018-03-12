package philip.com.airtablemap.model;

import com.google.gson.JsonObject;

import io.reactivex.Single;
import philip.com.airtablemap.model.dto.AirtableResponse;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit interface for Airtiable API
 */
public interface AirtableApiInterface {
    String baseUrl = "https://api.airtable.com/v0/";

    @GET("{baseId}/{tableName}")
    Single<AirtableResponse> retrieveRecords(@Path("baseId") String baseId, @Path("tableName") String tableName, @Query("api_key") String apiKey, @Query("view") String viewName);

    @DELETE("{baseId}/{tableName}/{id}")
    Single<AirtableResponse> deleteRecord(@Path("baseId") String baseId, @Path("tableName") String tableName, @Path("id") String id, @Query("api_key") String apiKey);

    @PUT("{baseId}/{tableName}/{id}")
    Single<AirtableResponse> updateRecord(@Path("baseId") String baseId, @Path("tableName") String tableName, @Path("id") String id, @Query("api_key") String apiKey, @Body JsonObject body);

    @POST("{baseId}/{tableName}")
    Single<AirtableResponse> createRecord(@Path("baseId") String baseId, @Path("tableName") String tableName, @Query("api_key") String apiKey, @Body JsonObject body);
}
