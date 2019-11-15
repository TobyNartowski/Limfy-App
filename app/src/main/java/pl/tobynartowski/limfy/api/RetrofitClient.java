package pl.tobynartowski.limfy.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String API_URL = "http://145.239.31.229:8082/api/v1/";
    private static final String API_USER = "client";
    private static final String API_SECRET = "bf12ce50–6094–408e–84b0–1f2a84fe7b66";

    private static RetrofitClient instance;
    private Retrofit retrofit;

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public RestApi getApi() {
        return retrofit.create(RestApi.class);
    }
}
