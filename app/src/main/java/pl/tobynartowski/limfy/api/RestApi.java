package pl.tobynartowski.limfy.api;

import pl.tobynartowski.limfy.model.TokenResponse;
import pl.tobynartowski.limfy.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RestApi {

    @POST("api/v1/users")
    Call<User> registerUser(@Body User user);

    @FormUrlEncoded
    @POST("oauth/token")
    Call<TokenResponse> getToken(
            @Header("Authorization") String authorization,
            @Field("grant_type") String grantType,
            @Field("username") String username,
            @Field("password") String password
    );
}
