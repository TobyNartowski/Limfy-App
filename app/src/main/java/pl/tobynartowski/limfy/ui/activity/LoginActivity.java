package pl.tobynartowski.limfy.ui.activity;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonObject;

import java.io.IOException;

import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.api.RetrofitClient;
import pl.tobynartowski.limfy.misc.SwipeTouchListener;
import pl.tobynartowski.limfy.model.TokenResponse;
import pl.tobynartowski.limfy.utils.UserUtils;
import pl.tobynartowski.limfy.utils.ViewUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private boolean permission = true;
    private boolean autoLogin = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                ViewUtils.showToast(this, getResources().getString(R.string.error_agreement));
                permission = false;
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.makeFullscreen(getWindow());

        if (UserUtils.getInstance(this).getId() != null) {
            Call<Void> checkServerCall = RetrofitClient.getInstance().getApi().checkServerConnection();
            checkServerCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 401) {
                        UserUtils.getInstance(LoginActivity.this).destroySession();
                        findViewById(R.id.login_group).setVisibility(View.VISIBLE);
                        autoLogin = false;

                    } else {
                        new Handler().postDelayed(LoginActivity.this::switchToAppActivity, 500);
                        autoLogin = true;
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    ViewUtils.showToast(LoginActivity.this,
                            getResources().getString(R.string.error_connection) + ": " + t.getMessage());
                }
            });
        } else {
            autoLogin = false;
            findViewById(R.id.login_group).setVisibility(View.VISIBLE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        if (getIntent().getStringExtra("new") != null) {
            ViewUtils.showToast(this, getResources().getString(R.string.info_registered));
        } else if ("server".equals(getIntent().getStringExtra("error"))) {
            ViewUtils.showToast(this, "Wystąpił błąd serwera, zaloguj się ponownie");
        }

        findViewById(R.id.login_layout).setOnTouchListener(new SwipeTouchListener(this) {
            @Override
            public void onSwipeTop() {
                switchToRegisterActivity();
            }
        });

        findViewById(R.id.login_button_register).setOnClickListener((view) -> switchToRegisterActivity());
        findViewById(R.id.login_button_login).setOnClickListener((view) -> {
            String passedLogin = ((TextView) findViewById(R.id.login_field_login)).getText().toString();
            String passedPassword = ((TextView) findViewById(R.id.login_field_password)).getText().toString();

            if (passedLogin.isEmpty() || passedPassword.isEmpty()) {
                ViewUtils.showToast(this, getResources().getString(R.string.error_fill));
            } else {
                Call<TokenResponse> tokenCall = RetrofitClient.getInstance().getApi().getToken(
                        RetrofitClient.getInstance().getAuthorizationCredentials(),
                        "password", passedLogin, UserUtils.hashPassword(passedPassword));
                tokenCall.enqueue(new Callback<TokenResponse>() {
                    @Override
                    public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                        if (!permission) {
                            ViewUtils.showToast(LoginActivity.this, getResources().getString(R.string.error_agreement));
                        } else {
                            switch (response.code()) {
                                case 400:
                                    ViewUtils.showToast(LoginActivity.this,
                                            getResources().getString(R.string.error_credentials_invalid));
                                    break;
                                case 200:
                                    if (response.body() != null) {
                                        String bearer = response.body().getAccessToken();
                                        RetrofitClient.getInstance().addToken(response.body().getAccessToken());

                                        Call<JsonObject> userIdCall = RetrofitClient.getInstance().getApi().getUserId(passedLogin);
                                        userIdCall.enqueue(new Callback<JsonObject>() {
                                            @Override
                                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                                switch (response.code()) {
                                                    case 200:
                                                        JsonObject jsonObject = response.body();
                                                        if (jsonObject == null) {
                                                            throw new IllegalStateException("Server endpoint error");
                                                        }
                                                        UserUtils.getInstance(LoginActivity.this)
                                                                .setSession(jsonObject.get("id").getAsString(), bearer);
                                                        break;
                                                    case 404:
                                                    default:
                                                        ViewUtils.showToast(LoginActivity.this,
                                                                getResources().getString(R.string.error_internal) + ": " + response.code());
                                                        break;
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                                ViewUtils.showToast(LoginActivity.this,
                                                        getResources().getString(R.string.error_connection) + ": " + t.getMessage());
                                                view.setEnabled(true);
                                            }
                                        });

                                        switchToAppActivity();
                                        break;
                                    }
                                default:
                                    ViewUtils.showToast(LoginActivity.this,
                                            getResources().getString(R.string.error_internal) + ": " + response.code());
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TokenResponse> call, Throwable t) {
                        ViewUtils.showToast(LoginActivity.this,
                                getResources().getString(R.string.error_connection) + ": " + t.getMessage());
                        view.setEnabled(true);
                    }
                });
            }
        });
    }

    public void switchToAppActivity() {
        startActivity( new Intent(this, AppViewActivity.class),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

        new Handler().postDelayed(() -> {
            TranslateAnimation imageAnimation = new TranslateAnimation(0, 0, 0, -2500);
            imageAnimation.setFillAfter(false);
            imageAnimation.setDuration(1500);

            if (!autoLogin) {
                TranslateAnimation groupAnimation = new TranslateAnimation(0, 0, 0, 2500);
                groupAnimation.setFillAfter(false);
                groupAnimation.setDuration(1500);
                findViewById(R.id.login_group).startAnimation(groupAnimation);
            }

            findViewById(R.id.login_image).startAnimation(imageAnimation);
        }, 300);
    }

    public void switchToRegisterActivity() {
        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @Override
    public void onBackPressed() {}
}
