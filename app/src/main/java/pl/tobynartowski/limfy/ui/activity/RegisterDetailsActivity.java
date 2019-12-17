package pl.tobynartowski.limfy.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import pl.tobynartowski.limfy.Limfy;
import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.api.RetrofitClient;
import pl.tobynartowski.limfy.utils.ViewUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);
        ViewUtils.makeFullscreen(getWindow());

    }

    @Override
    public void onBackPressed() {
        String username = getIntent().getStringExtra("username");
        if (username != null) {
            RetrofitClient.getInstance().getApi().deleteEmptyUser(username).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 204) {
                        ViewUtils.showToast(Limfy.getContext(), getResources().getString(R.string.register_not_registered));
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    ViewUtils.showToast(RegisterDetailsActivity.this,
                            getResources().getString(R.string.error_connection) + ": " + t.getMessage());
                }
            });
        } else {
            throw new IllegalStateException();
        }

        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

    public void switchToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("new", getIntent().getStringExtra("username"));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

    public void handleError() {
        ViewUtils.showToast(RegisterDetailsActivity.this,
                getResources().getString(R.string.error_connection));

        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }
}
