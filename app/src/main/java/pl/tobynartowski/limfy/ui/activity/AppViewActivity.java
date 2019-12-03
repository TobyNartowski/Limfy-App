package pl.tobynartowski.limfy.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import pl.tobynartowski.limfy.Limfy;
import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.ui.ViewPageAdapter;
import pl.tobynartowski.limfy.utils.ViewUtils;

public class AppViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_view);
        ViewUtils.makeFullscreen(getWindow());

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPageAdapter(this));
        viewPager.setCurrentItem(1, false);
    }

    public void changeToConnectActivity() {
        Intent intent = new Intent(Limfy.getContext(), ConnectActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
}
