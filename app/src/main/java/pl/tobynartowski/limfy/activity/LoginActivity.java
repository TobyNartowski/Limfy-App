package pl.tobynartowski.limfy.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.utils.ViewUtils;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.makeFullscreen(getWindow());

        if (getIntent().getStringExtra("new") != null) {
            ViewUtils.showToast(this, "Konto założone, możesz się teraz zalogować");
        }

        findViewById(R.id.login_button_register).setOnClickListener((view) -> {
            startActivity(new Intent(this, RegisterActivity.class));
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        });

        findViewById(R.id.login_button_login).setOnClickListener((view) -> {
            String passedLogin = ((TextView) findViewById(R.id.login_field_login)).getText().toString();
            String passedPassword = ((TextView) findViewById(R.id.login_field_password)).getText().toString();

            if (passedLogin.isEmpty() || passedPassword.isEmpty()) {
                ViewUtils.showToast(this, "Uzupełnij wszystkie pola!");
            } else {
//             TODO: Login here via REST here
                TranslateAnimation groupAnimation = new TranslateAnimation(0, 0, 0, 2500);
                groupAnimation.setFillAfter(false);
                groupAnimation.setDuration(1500);
                findViewById(R.id.login_group).startAnimation(groupAnimation);

                TranslateAnimation imageAnimation = new TranslateAnimation(0, 0, 0, -2500);
                imageAnimation.setFillAfter(false);
                imageAnimation.setDuration(1500);
                findViewById(R.id.login_image).startAnimation(imageAnimation);

                new Handler().postDelayed(() -> startActivity(new Intent(this, ConnectActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()), 1000);
            }
        });
    }
}
