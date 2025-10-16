package com.techlabs.extrape.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.techlabs.extrape.R;
//import com.techlabs.extrape.VerifyOtpActivity;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText edtPhone;
    Button btnSendOtp;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtPhone = findViewById(R.id.edtPhone);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        mAuth = FirebaseAuth.getInstance();

        btnSendOtp.setOnClickListener(v -> {
            String phone = edtPhone.getText().toString().trim();
            if (phone.isEmpty() || phone.length() != 10) {
                edtPhone.setError("Enter valid number");
                return;
            }
            String fullPhone = "+91" + phone;
            Intent i = new Intent(this, VerifyOtpActivity.class);
            i.putExtra("phone", fullPhone);
            startActivity(i);
        });
    }
}