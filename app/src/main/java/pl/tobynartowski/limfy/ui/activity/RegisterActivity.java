package pl.tobynartowski.limfy.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URI;

import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.api.RetrofitClient;
import pl.tobynartowski.limfy.misc.SwipeTouchListener;
import pl.tobynartowski.limfy.model.User;
import pl.tobynartowski.limfy.utils.UserUtils;
import pl.tobynartowski.limfy.utils.ViewUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.makeFullscreen(getWindow());
        findViewById((R.id.register_button_register)).setEnabled(true);

        findViewById(R.id.register_arrow_back).setOnClickListener((view) -> switchToLoginActivity(null));
        findViewById(R.id.register_layout).setOnTouchListener(new SwipeTouchListener(this) {
            @Override
            public void onSwipeBottom() {
                switchToLoginActivity(null);
            }
        });

        findViewById((R.id.register_button_register)).setOnClickListener((view) -> {
            String passedLogin = ((TextView) findViewById(R.id.register_field_login)).getText().toString();
            String passedPassword = ((TextView) findViewById(R.id.register_field_password)).getText().toString();

            if (passedLogin.isEmpty() || passedPassword.isEmpty()) {
                ViewUtils.showToast(this, getResources().getString(R.string.error_fill));
            } else {
                User user = new User(passedLogin, UserUtils.hashPassword(passedPassword));

                Call<User> registerCall = RetrofitClient.getInstance().getApi().registerUser(user);
                view.setEnabled(false);
                registerCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        switch (response.code()) {
                            case 201:
                                Intent intent = new Intent(RegisterActivity.this, RegisterDetailsActivity.class);
                                if (response.body() != null && response.body().getSelf() != null) {
                                    intent.putExtra("self",  response.body().getSelf().toString());
                                    intent.putExtra("username", user.getUsername());
                                }
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                                break;
                            case 409:
                                ViewUtils.showToast(RegisterActivity.this,
                                        getResources().getString(R.string.error_conflict));
                                view.setEnabled(true);
                                break;
                            default:
                                ViewUtils.showToast(RegisterActivity.this,
                                        getResources().getString(R.string.register_error_internal)
                                                + ": " + response.code());
                                view.setEnabled(true);
                                break;
                        }

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        ViewUtils.showToast(RegisterActivity.this,
                                getResources().getString(R.string.error_internal) + ": " + t.getMessage());
                        view.setEnabled(true);
                    }
                });
            }
        });
    }

    private void switchToLoginActivity(String value) {
        Intent intent = new Intent(this, LoginActivity.class);
        if (value != null) {
            intent.putExtra("new", value);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }
}
