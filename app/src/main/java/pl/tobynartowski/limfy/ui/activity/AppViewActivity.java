package pl.tobynartowski.limfy.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import pl.tobynartowski.limfy.Limfy;
import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.ui.ViewPageAdapter;
import pl.tobynartowski.limfy.utils.BluetoothUtils;
import pl.tobynartowski.limfy.utils.UserUtils;
import pl.tobynartowski.limfy.utils.ViewUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_view);
        ViewUtils.makeFullscreen(getWindow());

        ImageView connectIcon = findViewById(R.id.app_connect_icon);
        if (BluetoothUtils.isConnected()) {
            connectIcon.setImageResource(R.drawable.drawable_disconnect);
        } else {
            connectIcon.setImageResource(R.drawable.drawable_connect);
        }

        connectIcon.setOnClickListener(v -> {
            if (BluetoothUtils.isConnected()) {
                // DEVELOPMENT
                BluetoothUtils.setConnected(false);
                connectIcon.setImageResource(R.drawable.drawable_connect);
                ViewUtils.showToast(this, getResources().getString(R.string.app_view_disconnected));
//                BluetoothUtils.disconnect();
            } else {
                startActivity(new Intent(Limfy.getContext(), ConnectActivity.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(10);
        viewPager.setAdapter(new ViewPageAdapter(this));
        viewPager.setCurrentItem(1, false);
    }

    public void logout() {
        UserUtils.getInstance(this).destroySession();
        startActivity(new Intent(AppViewActivity.this, LoginActivity.class),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @Override
    public void onBackPressed() {}
}
