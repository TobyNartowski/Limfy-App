package pl.tobynartowski.limfy.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
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
import pl.tobynartowski.limfy.api.RestUpdater;
import pl.tobynartowski.limfy.model.BluetoothData;
import pl.tobynartowski.limfy.utils.BluetoothUtils;
import pl.tobynartowski.limfy.utils.DummyDataUtils;
import pl.tobynartowski.limfy.utils.ViewUtils;

public class AppActualActivity extends AppCompatActivity implements Observer {

    private boolean heartShow = false;

    private void loadData(int heartbeat, int steps) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00##");
        runOnUiThread(() -> {
            ((TextView) findViewById(R.id.app_actual_text_heartbeat)).setText(getResources().getString(R.string.app_actual_value_heartbeat, heartbeat));
            ((TextView) findViewById(R.id.app_actual_text_steps)).setText(String.format(Locale.getDefault(), "%d", steps));
            ((TextView) findViewById(R.id.app_actual_text_distance)).setText(getResources().getString(R.string.app_actual_value_distance, decimalFormat.format(0.792 * steps)));
            ((TextView) findViewById(R.id.app_actual_text_calories)).setText(getResources().getString(R.string.app_actual_value_calories, decimalFormat.format(steps / 20.0)));
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_actual);
        ViewUtils.makeFullscreen(getWindow());
        BluetoothData.getInstance().addObserver(this);
        loadData(BluetoothData.getInstance().getHeartbeat(), BluetoothData.getInstance().getTotalSteps());

        Timer heartTimer = new Timer();
        heartTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
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
        }, 0, 600);
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof BluetoothData) {
            BluetoothData bluetoothData = (BluetoothData) o;

            /* DEVELOPMENT
            if (bluetoothData.isDisconnected()) {
                BluetoothUtils.disconnect();
                runOnUiThread(() -> {
                    Intent connectionBrokenIntent = new Intent(AppActualActivity.this, ConnectActivity.class);
                    connectionBrokenIntent.putExtra("error", "connection");
                    startActivity(connectionBrokenIntent,
                            ActivityOptions.makeSceneTransitionAnimation(AppActualActivity.this).toBundle());
                });
            }
            */
            loadData(bluetoothData.getHeartbeat(), BluetoothData.getInstance().getTotalSteps());
        }
    }

    @Override
    public void onBackPressed () {
        // DEVELOPMENT
//        BluetoothUtils.disconnect();
        DummyDataUtils.getInstance().stopTimers();
        BluetoothUtils.setConnected(false);

        stopService(new Intent(AppActualActivity.this, RestUpdater.class));
        startActivity(new Intent(AppActualActivity.this, ConnectActivity.class),
                ActivityOptions.makeSceneTransitionAnimation(AppActualActivity.this).toBundle());
    }
}
