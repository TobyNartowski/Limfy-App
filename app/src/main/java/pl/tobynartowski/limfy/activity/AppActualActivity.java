package pl.tobynartowski.limfy.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.model.DeviceData;
import pl.tobynartowski.limfy.utils.ViewUtils;

public class AppActualActivity extends AppCompatActivity implements Observer {

    private boolean heartShow = false;
    private int totalSteps;

    private void loadData(int heartbeat, int steps) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00##");
        ((TextView) findViewById(R.id.app_actual_text_heartbeat)).setText(getResources().getString(R.string.app_actual_value_heartbeat, heartbeat));
        ((TextView) findViewById(R.id.app_actual_text_steps)).setText(String.format(Locale.getDefault(), "%d", steps));
        ((TextView) findViewById(R.id.app_actual_text_distance)).setText(getResources().getString(R.string.app_actual_value_distance, decimalFormat.format(0.792 * steps)));
        ((TextView) findViewById(R.id.app_actual_text_calories)).setText(getResources().getString(R.string.app_actual_value_calories, decimalFormat.format(steps / 20.0)));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_actual);
        ViewUtils.makeFullscreen(getWindow());
        DeviceData.getInstance().addObserver(this);
        loadData(0, 0);

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


    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof DeviceData) {
            DeviceData deviceData = (DeviceData) o;
            totalSteps += deviceData.getSteps();
            loadData(deviceData.getHeartbeat(), totalSteps);
        }
    }
}
