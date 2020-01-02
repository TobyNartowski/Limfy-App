package pl.tobynartowski.limfy.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.model.BluetoothData;
import pl.tobynartowski.limfy.utils.DataUtils;
import pl.tobynartowski.limfy.utils.ViewUtils;

public class FallActivity extends AppCompatActivity {

    private static final long TIME_SECONDS = 31000;
    private Vibrator vibrator;
    private CountDownTimer countDownTimer;
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

        ((TextView) findViewById(R.id.fall_text_sub))
                .setText(getResources().getString(R.string.fall_text_sub, DataUtils.getInstance().getContact().getNumber()));

        TextView counter = findViewById(R.id.fall_counter);
        countDownTimer = new CountDownTimer(TIME_SECONDS, 1000) {
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
                this.cancel();
                setFallDetected(false);
                BluetoothData.getInstance().setFall(false);

                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager != null) {
                    if (ContextCompat.checkSelfPermission(FallActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(FallActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS}, 1);
                    }

                    Location location = null;
                    for (String provider : locationManager.getProviders(true)) {
                        location = locationManager.getLastKnownLocation(provider);
                        if (location != null) {
                            break;
                        }
                    }

                    if (location != null) {
                        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
                        String message = String.format(Locale.getDefault(), "Urządzenie wykryło upadek twojego znajomego, który może potrzebować twojej pomocy! Jego aktualne położenie: http://maps.google.com/?q=%s,%s",
                                df.format(location.getLatitude()), df.format(location.getLongitude()));

                        SmsManager sms = SmsManager.getDefault();
                        ArrayList<String> messageArray = sms.divideMessage(message);
                        sms.sendMultipartTextMessage(DataUtils.getInstance().getContact().getNumber(), null, messageArray, null, null);

                        ViewUtils.showToast(FallActivity.this, String.format(Locale.getDefault(), "%s został powiadomiony!", DataUtils.getInstance().getContact().getNumber()));
                    } else {
                        ViewUtils.showToast(FallActivity.this, "Wystąpił błąd przy wysyłaniu wiadomości");
                    }
                } else {
                    ViewUtils.showToast(FallActivity.this, "Wystąpił błąd przy wysyłaniu wiadomości");
                }

                onBackPressed();
            }
        }.start();

        findViewById(R.id.fall_button_ok).setOnClickListener(v -> {
            countDownTimer.cancel();
            ViewUtils.showToast(FallActivity.this, "Powiadomienie nie zostało wysłane");
            setFallDetected(false);
            BluetoothData.getInstance().setFall(false);
            onBackPressed();
        });

        findViewById(R.id.fall_button_call).setOnClickListener(v -> countDownTimer.onFinish());
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

    @Override
    public void onBackPressed() {
        if (!fallDetected.get()) {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        }
    }
}
