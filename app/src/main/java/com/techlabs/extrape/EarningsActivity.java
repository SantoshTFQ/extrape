package com.techlabs.extrape;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.techlabs.extrape.user.SharedPrefManager;
import com.techlabs.extrape.utiles.ApiUrls;
import com.techlabs.extrape.utiles.MySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EarningsActivity extends AppCompatActivity {

    TextView txtTotal, txtWithdrawn, txtPending, txtReady,txtPendingWithdrw;
    Button btnWithdraw;
    String userId;// = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnings);
        userId = SharedPrefManager.getInstance(this).getUserId();

        txtTotal = findViewById(R.id.txtTotal);
        txtWithdrawn = findViewById(R.id.txtWithdrawn);
        txtPendingWithdrw = findViewById(R.id.txtWithdrawnPending);
        txtPending = findViewById(R.id.txtPending);
        txtReady = findViewById(R.id.txtReady);
        btnWithdraw = findViewById(R.id.btnWithdraw);

        fetchEarnings();

        btnWithdraw.setOnClickListener(v -> {
            String ready = txtReady.getText().toString().replace("₹", "").trim();
            final double readyAmt;
            try {
                readyAmt = Double.parseDouble(ready);
            } catch (Exception e) {
                return;
            }

            if (readyAmt <= 0) {
                Toast.makeText(this, "No balance available to withdraw", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Confirm Withdraw")
                    .setMessage("Withdraw ₹" + readyAmt + " to your account?")
                    .setPositiveButton("Yes", (d, w) -> requestWithdraw(readyAmt))
                    .setNegativeButton("Cancel", null)
                    .show();
        });

    }

    private void fetchEarnings() {
        String url = ApiUrls.GET_USER_EARNINGS + "?user_id=" + userId;
        StringRequest req = new StringRequest(Request.Method.GET, url,
                res -> {
                    try {
                        JSONObject o = new JSONObject(res);
                        if (o.getString("status").equals("success")) {
                            JSONObject d = o.getJSONObject("data");
                            txtTotal.setText("₹" + d.optString("total_earnings"));
                            txtWithdrawn.setText("₹" + d.optString("withdrawn"));
                            txtPending.setText("₹" + d.optString("pending_approval"));
                            txtReady.setText("₹" + d.optString("ready_withdraw"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                err -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
        );
        MySingleton.getInstance(this).addToRequestQueue(req);
    }

    private void requestWithdraw(double amount) {
        String url = ApiUrls.REQUEST_WITHDRAW;
        StringRequest req = new StringRequest(Request.Method.POST, url,
                res -> {
                    try {
                        JSONObject o = new JSONObject(res);
                        Toast.makeText(this, o.optString("message"), Toast.LENGTH_SHORT).show();
                        if (o.getString("status").equals("success")) fetchEarnings();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                err -> Toast.makeText(this, "Failed to process request", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("user_id", userId);
                p.put("amount", String.valueOf(amount));
                return p;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(req);
    }
}
