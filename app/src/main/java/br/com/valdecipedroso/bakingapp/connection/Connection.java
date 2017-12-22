package br.com.valdecipedroso.bakingapp.connection;

import android.app.Application;
import android.support.annotation.NonNull;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gemeos_valdeci on 18/12/2017.
 */

public class Connection {

    private static final String BASE_URL = "http://go.udacity.com/";

    public static final String NUTELLA_PIE = "1PQR7U9IqN3B3wVn0IYoQx1RfYkAr2cIC";
    public static final String BROWNIES = "1nK-02IIkkAeEF__jPLoSoNhoPEgTKupi";
    public static final String YELLOW_CAKE = "1XAy0kLO6Pdih-pPrKtBqEuCHmWvbR0TM";
    public static final String CHEESECAKE = "1uf3DcWEYdjIseJ-y2OOUqE8c78PFHFaQ";
    public static final String DEFAULT_IMAGE = "1mAi87Tg2Tnksx6eWIfpTPUOC04mwYF9v";

    private static final long CACHE_SIZE = 10 * 1024 * 1024;    // 10 MB

    @NonNull
    public static OkHttpClient provideOkHttp(Application application) {
        Cache cache = new Cache(application.getCacheDir(), CACHE_SIZE);

        return new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .cache(cache)
                .build();
    }

    @NonNull
    public static Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static BakingApp build(Application application) {
        return provideRetrofit(provideOkHttp(application)).create(BakingApp.class);
    }
}
