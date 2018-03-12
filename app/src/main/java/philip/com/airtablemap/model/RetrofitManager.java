package philip.com.airtablemap.model;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit Manager with Singleton.
 */

public class RetrofitManager {
    private final AirtableApiInterface airtableApiIF;

    private final GoogleApiInterface googleApiIF;

    private RetrofitManager() {
        this.airtableApiIF = new Retrofit.Builder()
                .baseUrl(AirtableApiInterface.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(createOkHttpClient())
                .build()
                .create(AirtableApiInterface.class);

        this.googleApiIF = new Retrofit.Builder()
                .baseUrl(GoogleApiInterface.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(createOkHttpClient())
                .build()
                .create(GoogleApiInterface.class);
    }

    private static class Singleton {
        private static final RetrofitManager instance = new RetrofitManager();
    }

    public static AirtableApiInterface getAirtable() {
        return Singleton.instance.airtableApiIF;
    }

    public static GoogleApiInterface getGoogle() {
        return Singleton.instance.googleApiIF;
    }

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        return builder.build();
    }
}
