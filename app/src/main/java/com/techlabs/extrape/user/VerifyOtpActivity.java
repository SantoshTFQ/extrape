package com.techlabs.extrape.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techlabs.extrape.R;
import com.techlabs.extrape.utiles.ApiUrls;

public class VerifyOtpActivity extends AppCompatActivity {
    TextInputEditText edtOtp;
    Button btnVerify;
    String verificationId, phone;
    FirebaseAuth mAuth;
    TextView txtErrorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        edtOtp = findViewById(R.id.edtOtp);
        btnVerify = findViewById(R.id.btnVerify);
        mAuth = FirebaseAuth.getInstance();
        //txtErrorMsg.findViewById(R.id.txterror);

        phone = getIntent().getStringExtra("phone");

        sendOtp(phone);

        btnVerify.setOnClickListener(v -> {
            String code = edtOtp.getText().toString().trim();
            if (code.isEmpty()) return;
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signIn(credential);
        });
    }

    private void sendOtp(String phone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential credential) {
                                signIn(credential);
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                Toast.makeText(VerifyOtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                //txtErrorMsg.setVisibility(View.VISIBLE);
                               // txtErrorMsg.setText("error");

                            }

                            @Override
                            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken token) {
                                verificationId = s;
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signIn(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Sync with PHP backend to get internal user_id
                        syncUserWithPHP(user.getUid(), user.getPhoneNumber());
                    } else {
                        Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void syncUserWithPHP(String uid, String phone) {
        String url = ApiUrls.SYNC_FIREBASE_USER;
        StringRequest req = new StringRequest(Request.Method.POST, url,
                resp -> {
                    try {
                        org.json.JSONObject o = new org.json.JSONObject(resp);
                        if (o.getString("status").equals("success")) {
                            int userId = o.getInt("user_id");
                            SharedPrefManager.getInstance(this).saveUserId(String.valueOf(userId));
                            startActivity(new Intent(this, HomeActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, o.optString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                },
                err -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("firebase_uid", uid);
                p.put("mobile", phone);
                return p;
            }
        };
        Volley.newRequestQueue(this).add(req);
    }
}