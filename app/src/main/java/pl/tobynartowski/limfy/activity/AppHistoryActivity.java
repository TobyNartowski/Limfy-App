package pl.tobynartowski.limfy.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.utils.SwipeTouchListener;
import pl.tobynartowski.limfy.utils.ViewUtils;

public class AppHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_history);
        ViewUtils.makeFullscreen(getWindow());

        findViewById(R.id.app_history_layout).setOnTouchListener(new SwipeTouchListener(this) {
            @Override
            public void onSwipeRight() {
                startActivity(new Intent(AppHistoryActivity.this, AppHeartActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {}
}
