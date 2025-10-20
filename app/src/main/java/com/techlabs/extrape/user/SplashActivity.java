package com.techlabs.extrape.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.techlabs.extrape.MainActivity;
import com.techlabs.extrape.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imgLogo = findViewById(R.id.imgLogo);
        TextView txtAppName = findViewById(R.id.txtAppName);

        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        imgLogo.startAnimation(fade);
        txtAppName.startAnimation(fade);

        new Handler().postDelayed(() -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            // Check Firebase + SharedPref user ID
            if (mAuth.getCurrentUser() != null &&
                    SharedPrefManager.getInstance(this).getUserId() != null) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }

            finish();
        }, 2000); // 2 seconds splash delay
    }
}
