package pl.tobynartowski.limfy.api;

import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import pl.tobynartowski.limfy.Limfy;
import pl.tobynartowski.limfy.utils.UserUtils;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    final static String API_URL = "http://145.239.31.229:8082/";
    private static final String API_USER = "dfc0b23ad5ea4bbf8e0e218ec1715864";
    private static final String API_SECRET = "8f5a49bcb63d44b5b3921d35dc6b092f";

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

        String bearer = UserUtils.getInstance(Limfy.getContext()).getBearer();
        if (bearer != null) {
            addToken(bearer);
        }
    }

    public RestApi getApi() {
        return retrofit.create(RestApi.class);
    }

    public String getAuthorizationCredentials() {
        return "Basic " + Base64.encodeToString((API_USER + ":" + API_SECRET).getBytes(StandardCharsets.ISO_8859_1), Base64.NO_WRAP);
    }

    public void addToken(String token) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request originalRequest = chain.request();

            Request.Builder requestBuilder = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .method(originalRequest.method(), originalRequest.body());

            return chain.proceed(requestBuilder.build());
        }).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }
}
