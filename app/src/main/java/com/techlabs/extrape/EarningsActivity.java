package com.techlabs.extrape;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.techlabs.extrape.user.SharedPrefManager;
import com.techlabs.extrape.utiles.ApiUrls;
import com.techlabs.extrape.utiles.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EarningsActivity extends AppCompatActivity {

    TextView txtTotal, txtWithdrawn, txtPending, txtReady, txtPendingWithdrw;
    TextView txtClicks, txtCTA, txtDMs, txtComments;
    String userId;// = "1"; // get from SharedPrefManager later
    Button btnWithdraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnings_new);
        userId = SharedPrefManager.getInstance(this).getUserId();

        txtTotal = findViewById(R.id.txtTotal);
        txtWithdrawn = findViewById(R.id.txtWithdrawn);
        txtPendingWithdrw = findViewById(R.id.txtWithdrawnPending);
        txtPending = findViewById(R.id.txtPending);
        txtReady = findViewById(R.id.txtReady);
        btnWithdraw = findViewById(R.id.btnWithdraw);

        txtClicks = findViewById(R.id.txtlinkClicks);
        txtCTA = findViewById(R.id.txtCTAID);
        txtComments = findViewById(R.id.txtCommentSent);
        txtDMs = findViewById(R.id.txtDmSent);

        fetchEarnings();
        fetchAnalytics();

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

    private void fetchAnalytics() {
        ProgressDialog dialog = ProgressDialog.show(this, "Loading Analytics", "Please wait...", false, false);
        String url = ApiUrls.GET_USER_ANALYTICS + "?user_id=" + userId;

        StringRequest req = new StringRequest(Request.Method.GET, url,
                response -> {
                    dialog.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("status").equals("success")) {
                            JSONObject data = obj.getJSONObject("data");
                            txtClicks.setText(data.optString("total_clicks", "0"));
                            double CTA = (((double) ((data.optInt("total_comments", 0) + data.optInt("total_dms", 0)) / 2) / data.optInt("total_clicks", 0)) * 100);
                            String formattedNumber = String.format("%.2f", CTA);
                            txtCTA.setText(formattedNumber + "%");
                            txtDMs.setText(data.optString("total_dms", "0"));
                            txtComments.setText(data.optString("total_comments", "0"));
                        } else {
                            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Parse Error", Toast.LENGTH_SHORT).show();
                        Log.e("ANALYTICS", e.toString());
                    }
                },
                error -> {
                    dialog.dismiss();
                    Toast.makeText(this, "Network Error_analytics: " + error.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("ANALYTICS_ERR", error.toString());
                });

        MySingleton.getInstance(this).addToRequestQueue(req);
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
                err -> Toast.makeText(this, "Network error_earnings", Toast.LENGTH_SHORT).show()
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
