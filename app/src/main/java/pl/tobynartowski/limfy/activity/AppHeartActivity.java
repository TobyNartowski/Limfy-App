package pl.tobynartowski.limfy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.utils.ViewUtils;

public class AppHeartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_heart);
        ViewUtils.makeFullscreen(getWindow());
    }
}
