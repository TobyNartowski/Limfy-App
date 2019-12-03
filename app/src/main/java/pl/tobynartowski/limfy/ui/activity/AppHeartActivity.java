package pl.tobynartowski.limfy.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.utils.BluetoothUtils;
import pl.tobynartowski.limfy.misc.SwipeTouchListener;
import pl.tobynartowski.limfy.utils.ViewUtils;

public class AppHeartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_heart);
        ViewUtils.makeFullscreen(getWindow());

        findViewById(R.id.app_heart_layout).setOnTouchListener(new SwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                startActivity(new Intent(AppHeartActivity.this, AppHistoryActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }

            @Override
            public void onSwipeRight() {
                if (BluetoothUtils.isConnected()) {
                    startActivity(new Intent(AppHeartActivity.this, AppActualActivity.class));
                } else {
                    startActivity(new Intent(AppHeartActivity.this, ConnectActivity.class));
                }
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }

        });
    }

    @Override
    public void onBackPressed() {}
}
