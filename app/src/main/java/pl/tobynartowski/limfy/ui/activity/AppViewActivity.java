package pl.tobynartowski.limfy.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import pl.tobynartowski.limfy.Limfy;
import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.api.RetrofitClient;
import pl.tobynartowski.limfy.model.MeasurementAverageWrapper;
import pl.tobynartowski.limfy.ui.ViewPageAdapter;
import pl.tobynartowski.limfy.utils.BluetoothUtils;
import pl.tobynartowski.limfy.utils.DataUtils;
import pl.tobynartowski.limfy.utils.UserUtils;
import pl.tobynartowski.limfy.utils.ViewUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppViewActivity extends AppCompatActivity {

    @Override
    protected void onResumeFragments() {
        ImageView connectIcon = findViewById(R.id.app_connect_icon);
        if (BluetoothUtils.isConnected()) {
            connectIcon.setImageResource(R.drawable.drawable_disconnect);
        } else {
            connectIcon.setImageResource(R.drawable.drawable_connect);
        }

        super.onResumeFragments();
    }

    public void onLoaded() {
        ImageView connectIcon = findViewById(R.id.app_connect_icon);
        connectIcon.setVisibility(View.VISIBLE);
        if (BluetoothUtils.isConnected()) {
            connectIcon.setImageResource(R.drawable.drawable_disconnect);
        } else {
            connectIcon.setImageResource(R.drawable.drawable_connect);
        }

        connectIcon.setOnClickListener(v -> {
            if (BluetoothUtils.isConnected()) {
                connectIcon.setImageResource(R.drawable.drawable_connect);
                ViewUtils.showToast(this, getResources().getString(R.string.app_view_disconnected));
                BluetoothUtils.disconnect();
            } else {
                startActivity(new Intent(Limfy.getContext(), ConnectActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        ConstraintLayout layout = findViewById(R.id.app_view_layout);
        layout.removeView(findViewById(R.id.app_view_progress));

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(10);
        viewPager.setAdapter(new ViewPageAdapter(AppViewActivity.this));
        viewPager.setCurrentItem(1, false);
        viewPager.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_view);
        ViewUtils.makeFullscreen(getWindow());

        new Handler().postDelayed(this::loadDataFromServer, 500);
    }

    public void loadDataFromServer() {
        String userId = UserUtils.getInstance(AppViewActivity.this).getId();
        if (userId != null && !userId.isEmpty()) {
            RetrofitClient.getInstance().getApi().getMeasurements(userId).enqueue(new Callback<MeasurementAverageWrapper>() {
                @Override
                public void onResponse(Call<MeasurementAverageWrapper> call, Response<MeasurementAverageWrapper> response) {
                    if (response.code() == 200 && response.body() != null) {
                        DataUtils.getInstance().setMeasurements(response.body().getEmbedded().getMeasurements());
                        onLoaded();
                    }
                }

                @Override
                public void onFailure(Call<MeasurementAverageWrapper> call, Throwable t) {
                    ViewUtils.showToast(AppViewActivity.this,
                            getResources().getString(R.string.error_internal) + ": " + t.getMessage());
                }
            });
        }
    }

    public void logout() {
        UserUtils.getInstance(this).destroySession();
        BluetoothUtils.disconnect();

        startActivity(new Intent(AppViewActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    public void onConnectionBroken() {
        runOnUiThread(() -> {
            ((ImageView) findViewById(R.id.app_connect_icon)).setImageResource(R.drawable.drawable_connect);
            ViewUtils.showToast(this, getResources().getString(R.string.error_device_disconnected));
        });
    }

    @Override
    public void onBackPressed() {}
}
