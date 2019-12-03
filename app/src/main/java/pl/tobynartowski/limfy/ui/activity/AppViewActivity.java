package pl.tobynartowski.limfy.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.ui.ViewPageAdapter;
import pl.tobynartowski.limfy.utils.ViewUtils;

public class AppViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_view);
        ViewUtils.makeFullscreen(getWindow());

        ((ViewPager2) findViewById(R.id.view_pager)).setAdapter(new ViewPageAdapter(this));
    }
}
