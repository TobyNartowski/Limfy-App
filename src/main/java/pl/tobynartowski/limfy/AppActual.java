package pl.tobynartowski.limfy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import pl.tobynartowski.limfy.utils.ViewUtils;

public class AppActual extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_actual);
        ViewUtils.makeFullscreen(getWindow());
    }
}
