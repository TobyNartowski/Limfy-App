package pl.tobynartowski.limfy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.utils.ViewUtils;

public class AppActualActivity extends AppCompatActivity {

    private boolean heartShow = true;

    private void loadData() {
        ((TextView) findViewById(R.id.app_actual_text_heartrate)).setText("73 BPM");
        ((TextView) findViewById(R.id.app_actual_text_steps)).setText("1,214");
        ((TextView) findViewById(R.id.app_actual_text_distance)).setText("1,457 km");
        ((TextView) findViewById(R.id.app_actual_text_calories)).setText("60,7 cal");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_actual);
        ViewUtils.makeFullscreen(getWindow());
        loadData();

        Timer heartTimer = new Timer();
        heartTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                synchronized (this) {
                    runOnUiThread(() -> {
                        View image = findViewById(R.id.app_actual_icon_heart);
                        if (heartShow) {
                            image.setVisibility(View.VISIBLE);
                        } else {
                            image.setVisibility(View.INVISIBLE);
                        }
                        heartShow = !heartShow;
                    });
                }
            }
        }, 0, 600);
    }
}
