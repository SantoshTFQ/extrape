package com.techlabs.extrape;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.techlabs.extrape.utiles.ApiUrls;
import com.techlabs.extrape.utiles.MySingleton;

import org.json.JSONObject;

public class EarningsActivity2 extends AppCompatActivity {
    TextView txtEstimated, txtUser, txtAdmin;
    String userId = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnings);

        txtEstimated = findViewById(R.id.txtEstimated);
        txtUser = findViewById(R.id.txtUser);
        txtAdmin = findViewById(R.id.txtAdmin);

        fetchEarnings();
    }

    private void fetchEarnings() {
        String url = ApiUrls.GET_USER_EARNINGS + "?user_id=" + userId;
        StringRequest req = new StringRequest(Request.Method.GET, url,
                res -> {
                    try {
                        JSONObject o = new JSONObject(res);
                        if (o.getString("status").equals("success")) {
                            JSONObject d = o.getJSONObject("data");
                            txtEstimated.setText("₹" + d.optString("estimated_earnings"));
                            txtUser.setText("₹" + d.optString("user_earnings"));
                            txtAdmin.setText("₹" + d.optString("admin_commission"));
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                },
                err -> Toast.makeText(this, "Failed: " + err.toString(), Toast.LENGTH_SHORT).show()
        );
        MySingleton.getInstance(this).addToRequestQueue(req);
    }
}
