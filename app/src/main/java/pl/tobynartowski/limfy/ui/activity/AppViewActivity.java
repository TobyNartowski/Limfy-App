package pl.tobynartowski.limfy.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import java.util.concurrent.atomic.AtomicInteger;

import pl.tobynartowski.limfy.Limfy;
import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.api.RetrofitClient;
import pl.tobynartowski.limfy.model.AnalysisWrapper;
import pl.tobynartowski.limfy.model.BluetoothData;
import pl.tobynartowski.limfy.model.BodyData;
import pl.tobynartowski.limfy.model.Disease;
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

    private AtomicInteger initialQueries = new AtomicInteger(0);
    private static final int ALL_QUERIES = 3;

    private AtomicInteger analysesLoaded = new AtomicInteger(0);
    private int allAnalyses = 0;

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

    private void ready() {
        if (initialQueries.incrementAndGet() >= ALL_QUERIES) {
            onLoaded();
        }
    }

    private void analysisLoaded() {
        if (analysesLoaded.incrementAndGet() >= allAnalyses) {
            ready();
        }
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
                    if (response.code() == 200 && response.body() != null && response.body().getEmbedded() != null) {
                        DataUtils.getInstance().setMeasurements(response.body().getEmbedded().getMeasurements());
                    }
                    ready();
                }

                @Override
                public void onFailure(Call<MeasurementAverageWrapper> call, Throwable t) {
                    ViewUtils.showToast(AppViewActivity.this,
                            getResources().getString(R.string.error_internal) + ": " + t.getMessage());
                    ready();
                }
            });

            RetrofitClient.getInstance().getApi().getBodyData(userId).enqueue(new Callback<BodyData>() {
                @Override
                public void onResponse(Call<BodyData> call, Response<BodyData> response) {
                    if (response.code() == 200 && response.body() != null) {
                        DataUtils.getInstance().setBodyData(response.body());
                    }
                    ready();
                }

                @Override
                public void onFailure(Call<BodyData> call, Throwable t) {
                    ViewUtils.showToast(AppViewActivity.this,
                            getResources().getString(R.string.error_internal) + ": " + t.getMessage());
                    ready();
                }
            });

            RetrofitClient.getInstance().getApi().getAnalyses(userId).enqueue(new Callback<AnalysisWrapper>() {
                @Override
                public void onResponse(Call<AnalysisWrapper> call, Response<AnalysisWrapper> response) {
                    if (response.code() == 200 && response.body() != null && response.body().getEmbedded() != null) {
                        DataUtils.getInstance().setAnalyses(response.body().getEmbedded().getAnalyses());
                        allAnalyses = DataUtils.getInstance().getAnalyses().size();
                        analysesLoaded = new AtomicInteger(0);

                        if (allAnalyses == 0) {
                            ready();
                        }

                        DataUtils.getInstance().getAnalyses().forEach(a ->
                                RetrofitClient.getInstance().getApi().getDisease(a.getDiseaseURI().toString())
                                        .enqueue(new Callback<Disease>() {
                            @Override
                            public void onResponse(Call<Disease> call, Response<Disease> response) {
                                if (response.code() == 200 && response.body() != null) {
                                    a.setDisease(response.body());
                                }
                                analysisLoaded();
                            }

                            @Override
                            public void onFailure(Call<Disease> call, Throwable t) {
                                ViewUtils.showToast(AppViewActivity.this,
                                        getResources().getString(R.string.error_internal) + ": " + t.getMessage());
                                analysisLoaded();
                            }
                        }));
                    } else {
                        ready();
                    }
                }

                @Override
                public void onFailure(Call<AnalysisWrapper> call, Throwable t) {
                    ViewUtils.showToast(AppViewActivity.this,
                            getResources().getString(R.string.error_internal) + ": " + t.getMessage());
                    ready();
                }
            });
        }
    }

    public void logout() {
        UserUtils.getInstance(this).destroySession();
        DataUtils.getInstance().setMeasurements(null);
        DataUtils.getInstance().setAnalyses(null);
        DataUtils.getInstance().setBodyData(null);
        BluetoothUtils.disconnect();
        BluetoothData.getInstance().clearData();

        startActivity(new Intent(AppViewActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    public void onConnectionBroken() {
        runOnUiThread(() -> {
            ((ImageView) findViewById(R.id.app_connect_icon)).setImageResource(R.drawable.drawable_connect);
            ViewUtils.showToast(this, getResources().getString(R.string.error_device_disconnected));
        });
    }

    public synchronized void startFallActivity() {
        if (BluetoothData.getInstance().isFall() && !FallActivity.isFallDetected()) {
            FallActivity.setFallDetected(true);
            BluetoothData.getInstance().setFall(false);
            startActivity(new Intent(AppViewActivity.this, FallActivity.class));
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }
    }

    @Override
    public void onBackPressed() {}
}
