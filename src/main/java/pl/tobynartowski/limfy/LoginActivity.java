package pl.tobynartowski.limfy;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import pl.tobynartowski.limfy.utils.ViewUtils;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.makeFullscreen(getWindow());

        findViewById(R.id.login_button_register).setOnClickListener((view) -> {
            startActivity(new Intent(this, RegisterActivity.class));
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });
    }
}
