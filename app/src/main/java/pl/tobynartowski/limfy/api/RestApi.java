package pl.tobynartowski.limfy.api;

import com.google.gson.JsonObject;

import pl.tobynartowski.limfy.model.Measurement;
import pl.tobynartowski.limfy.model.TokenResponse;
import pl.tobynartowski.limfy.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApi {

    @GET("api/v1")
    Call<Void> checkServerConnection();

    @FormUrlEncoded
    @POST("oauth/token")
    Call<TokenResponse> getToken(
            @Header("Authorization") String authorization,
            @Field("grant_type") String grantType,
            @Field("username") String username,
            @Field("password") String password
    );

    @POST("api/v1/users")
    Call<User> registerUser(@Body User user);

    @GET("api/v1/users/username/{username}")
    Call<JsonObject> getUserId(@Path("username") String username);

    @POST("api/v1/measurements")
    Call<Void> postMeasurements(@Body Measurement measurement);
}
