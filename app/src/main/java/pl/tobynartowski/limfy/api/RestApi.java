package pl.tobynartowski.limfy.api;

import com.google.gson.JsonObject;

import pl.tobynartowski.limfy.model.AnalysisWrapper;
import pl.tobynartowski.limfy.model.BodyData;
import pl.tobynartowski.limfy.model.Contact;
import pl.tobynartowski.limfy.model.Disease;
import pl.tobynartowski.limfy.model.Measurement;
import pl.tobynartowski.limfy.model.MeasurementAverageWrapper;
import pl.tobynartowski.limfy.model.TokenResponse;
import pl.tobynartowski.limfy.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

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
    Call<JsonObject> postMeasurements(@Body Measurement measurement);

    @DELETE("api/v1/users/{username}")
    Call<Void> deleteEmptyUser(@Path("username") String username);

    @POST("api/v1/body-data")
    Call<Void> addBodyData(@Body BodyData bodyData);

    @POST("api/v1/contacts")
    Call<Void> addContact(@Body Contact contact);

    @PATCH("api/v1/users/{id}/contact")
    Call<Void> updateContact(@Path("id") String username, @Body Contact contact);

    @GET("api/v1/users/{id}/contact")
    Call<Contact> getContact(@Path("id") String username);

    @GET("api/v1/users/{id}/measurements/average")
    Call<MeasurementAverageWrapper> getMeasurements(@Path("id") String id);

    @GET("api/v1/users/{id}/body-data")
    Call<BodyData> getBodyData(@Path("id") String id);

    @PATCH("api/v1/users/{id}/body-data")
    Call<Void> setBodyData(@Path("id") String id, @Body BodyData bodyData);

    @GET("api/v1/users/{id}/analyses")
    Call<AnalysisWrapper> getAnalyses(@Path("id") String id);

    @GET
    Call<Disease> getDisease(@Url String url);
}
