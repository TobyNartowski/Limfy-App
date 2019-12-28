package pl.tobynartowski.limfy.ui.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.model.BluetoothData;
import pl.tobynartowski.limfy.utils.DataUtils;
import pl.tobynartowski.limfy.utils.ViewUtils;

public class FallActivity extends AppCompatActivity {

    private static final long TIME_SECONDS = 31000;
    private Vibrator vibrator;
    private static AtomicBoolean fallDetected = new AtomicBoolean(false);

    public static boolean isFallDetected() {
        return fallDetected.get();
    }

    public static void setFallDetected(boolean fallDetected) {
        FallActivity.fallDetected.set(fallDetected);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall);
        ViewUtils.makeFullscreen(getWindow());
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrate(1000);

        TextView counter = findViewById(R.id.fall_counter);
        new CountDownTimer(TIME_SECONDS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) millisUntilFinished / 1000;
                counter.setText(String.format(Locale.getDefault(), "%d", seconds));
                if (seconds == 20) {
                    vibrate(250);
                } else if (seconds == 10) {
                    vibrate(500);
                } else if (seconds == 5) {
                    vibrate(750);
                } else if (seconds < 5) {
                    vibrate(100);
                }
            }

            @Override
            public void onFinish() {
                setFallDetected(false);
                BluetoothData.getInstance().setFall(false);
            }
        }.start();
    }

    private void vibrate(long millis) {
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(millis);
            }
        }

    }
}
