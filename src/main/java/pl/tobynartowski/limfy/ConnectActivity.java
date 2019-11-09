package pl.tobynartowski.limfy;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import pl.tobynartowski.limfy.utils.ViewUtils;

public class ConnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        ViewUtils.makeFullscreen(getWindow());

        findViewById(R.id.connect_image).setOnClickListener((view) -> {
            View progressBar = findViewById(R.id.connect_progress);
            progressBar.setVisibility(View.VISIBLE);

            // TODO: Turn on bluetooth and connect to the device here
            new Handler().postDelayed(() -> {
                ((ImageView) view).setImageResource(R.drawable.dummy_connect_on);
                progressBar.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(() ->
                        startActivity(new Intent(this, AppActual.class),
                                ActivityOptions.makeSceneTransitionAnimation(this).toBundle()), 1000);
            }, 1500);
        });
    }
}
