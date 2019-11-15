package pl.tobynartowski.limfy.api;

import pl.tobynartowski.limfy.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestApi {

    @POST("users")
    Call<User> registerUser(@Body User user);
}
