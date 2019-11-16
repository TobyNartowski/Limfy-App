package pl.tobynartowski.limfy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.api.RetrofitClient;
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

        findViewById(R.id.register_arrow_back).setOnClickListener((view) -> {
            switchToLoginActivity(null);
        });

        findViewById((R.id.register_button_register)).setOnClickListener((view) -> {
            String passedLogin = ((TextView) findViewById(R.id.register_field_login)).getText().toString();
            String passedPassword = ((TextView) findViewById(R.id.register_field_password)).getText().toString();

            if (passedLogin.isEmpty() || passedPassword.isEmpty()) {
                ViewUtils.showToast(this, "Uzupełnij wszystkie pola!");
            } else {
                User user = new User(passedLogin, UserUtils.hashPassword(passedPassword));

                Call<User> registerCall = RetrofitClient.getInstance().getApi().registerUser(user);
                view.setEnabled(false);
                registerCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        switch (response.code()) {
                            case 201:
                                switchToLoginActivity(user.getUsername());
                                break;
                            case 409:
                                ViewUtils.showToast(RegisterActivity.this,
                                        getResources().getString(R.string.error_conflict));
                                view.setEnabled(true);
                                break;
                            default:
                                ViewUtils.showToast(RegisterActivity.this,
                                        getResources().getString(R.string.error_internal)
                                                + ": " + response.code());
                                view.setEnabled(true);
                                break;
                        }

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        ViewUtils.showToast(RegisterActivity.this,
                                getResources().getString(R.string.error_connection) + ": " + t.getMessage());
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
