package com.techlabs.extrape;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.techlabs.extrape.model.ReelModel;
import com.techlabs.extrape.utiles.ApiUrls;
import com.techlabs.extrape.utiles.MySingleton;
import com.techlabs.extrape.user.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReelSetupActivity extends AppCompatActivity {
    RecyclerView recyclerReels;
    ArrayList<ReelModel> reelList = new ArrayList<>();
    ReelAdapter adapter;
    String userId;
    TextView txtNoReels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reel_setup);

        recyclerReels = findViewById(R.id.recyclerReels);
        txtNoReels = findViewById(R.id.txtNoReelsfound);

        recyclerReels.setLayoutManager(new LinearLayoutManager(this));

        userId = SharedPrefManager.getInstance(this).getUserId();
        //userId = "1";
        adapter = new ReelAdapter(reelList, userId, this);
        recyclerReels.setAdapter(adapter);

        fetchReels();
    }

    private void fetchReels() {
        String url = ApiUrls.GET_USER_REELS + "?user_id=" + userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray data = response.getJSONArray("data");
                        if (data.length() == 0) {
                            txtNoReels.setVisibility(View.VISIBLE);
                        } else {
                            txtNoReels.setVisibility(View.GONE);
                        }
                        reelList.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            ReelModel m = new ReelModel();
                            m.setId(obj.getString("ig_media_id"));
                            m.setCaption(obj.getString("caption"));
                            m.setThumb(obj.getString("thumbnail_url"));
                            m.setPermalink(obj.getString("permalink"));
                            m.setConfigured(obj.getInt("is_configured") == 1);
                            m.setAutomationEnabled(obj.optInt("automation_enabled", 0) == 1);
                            m.setClicks(obj.optInt("total_clicks", 0));
                            m.setDms(obj.optInt("dms_sent", 0));
                            reelList.add(m);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("ReelSetupActivity", "Volley Error: ", error);
                    Toast.makeText(this, "Failed to load reels", Toast.LENGTH_SHORT).show();
                }
        );

        MySingleton.getInstance(this).addToRequestQueue(request);
    }

}
