package pl.tobynartowski.limfy;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import pl.tobynartowski.limfy.utils.ViewUtils;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.makeFullscreen(getWindow());

        findViewById(R.id.register_arrow_back).setOnClickListener((view) -> {
            startActivity(new Intent(this, LoginActivity.class));
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }
}
