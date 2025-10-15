package com.techlabs.extrape;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.techlabs.extrape.utiles.ApiUrls;
import com.techlabs.extrape.utiles.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnalyticsActivity extends AppCompatActivity {

    TextView txtTodayClicks, txtMonthClicks, txtTotalClicks, txtDMs, txtComments;
    String userId = "1"; // get from SharedPrefManager later

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        txtTodayClicks = findViewById(R.id.txtTodayClicks);
        txtMonthClicks = findViewById(R.id.txtMonthClicks);
        txtTotalClicks = findViewById(R.id.txtTotalClicks);
        txtDMs = findViewById(R.id.txtDMs);
        txtComments = findViewById(R.id.txtComments);

        fetchAnalytics();
        ImageButton btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(v -> fetchAnalytics());

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
                            txtTodayClicks.setText(data.optString("clicks_today", "0"));
                            txtMonthClicks.setText(data.optString("clicks_month", "0"));
                            txtTotalClicks.setText(data.optString("total_clicks", "0"));
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
                    Toast.makeText(this, "Network Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("ANALYTICS_ERR", error.toString());
                });

        MySingleton.getInstance(this).addToRequestQueue(req);
    }
/*
    private void fetchGraphData() {
        String url = ApiUrls. + "?user_id=" + userId;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                res -> {
                    try {
                        JSONArray arr = res.getJSONArray("data");
                        List<Map.Entry> entries = new ArrayList<>();
                        for (int i=0; i<arr.length(); i++) {
                            JSONObject o = arr.getJSONObject(i);
                            entries.add(new Map.Entry(i, (float) o.getDouble("clicks")));
                        }
                        LineDataSet set = new LineDataSet(entries, "Clicks (Last 7 Days)");
                        set.setLineWidth(2f);
                        set.setCircleRadius(4f);
                        set.setValueTextSize(10f);
                        LineData data = new LineData(set);
                        chartClicks.setData(data);
                        chartClicks.invalidate();
                    } catch(Exception e){ e.printStackTrace(); }
                },
                err -> Toast.makeText(this,"Graph load failed",Toast.LENGTH_SHORT).show()
        );
        MySingleton.getInstance(this).addToRequestQueue(req);
    }
*/
}
