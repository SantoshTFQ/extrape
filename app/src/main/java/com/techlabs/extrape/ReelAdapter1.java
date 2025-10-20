package com.techlabs.extrape;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.techlabs.extrape.model.ReelModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReelAdapter1 extends RecyclerView.Adapter<ReelAdapter1.ViewHolder> {

    ArrayList<ReelModel> list;
    String userId;
    Context context;

    public ReelAdapter1(ArrayList<ReelModel> list, String userId, Context context) {
        this.list = list;
        this.userId = userId;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_reel_setup, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {
        ReelModel m = list.get(i);
        Glide.with(context).load(m.getThumb()).into(h.imageThumb);
        h.textCaption.setText(m.getCaption());
        h.textStatus.setText(m.isConfigured() ? "Status: Configured ✅" : "Status: Not Configured");

        // Toggle setup panel visibility
        h.btnSetup.setOnClickListener(v ->
                h.panelSetup.setVisibility(h.panelSetup.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE)
        );

        // 🔹 Generate Affiliate Link (Now works with new generateAffiliate.php JSON)
        h.btnGenerate.setOnClickListener(v -> {
            String productUrl = h.inputProductUrl.getText().toString().trim();
            if (productUrl.isEmpty()) {
                Toast.makeText(context, "Enter URL", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = "https://yourdomain.com/api/generateAffiliate.php";
            StringRequest req = new StringRequest(Request.Method.POST, url,
                    resp -> {
                        try {
                            JSONObject o = new JSONObject(resp);
                            if (o.getString("status").equals("success")) {
                                JSONObject data = o.getJSONObject("data");
                                String trackingLink = data.getString("tracking_url");
                                h.textAffiliate.setText("Affiliate Link: " + trackingLink);
                                Toast.makeText(context, "Link generated successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, o.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Parse error", Toast.LENGTH_SHORT).show();
                        }
                    },
                    err -> Toast.makeText(context, "Network error: " + err.getMessage(), Toast.LENGTH_SHORT).show()) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> p = new HashMap<>();
                    p.put("user_id", userId);
                    p.put("product_url", productUrl);
                    return p;
                }
            };
            Volley.newRequestQueue(context).add(req);
        });

        // 🔹 Save Reel Automation Setup
        h.btnSave.setOnClickListener(v -> {
            String affiliateUrl = h.textAffiliate.getText().toString().replace("Affiliate Link: ", "").trim();
            if (affiliateUrl.isEmpty()) {
                Toast.makeText(context, "Generate affiliate link first!", Toast.LENGTH_SHORT).show();
                return;
            }

            String comment = h.inputComment.getText().toString().trim();
            String dm = h.inputDM.getText().toString().trim();
            String automation = h.switchAutomation.isChecked() ? "1" : "0";

            StringRequest req = new StringRequest(Request.Method.POST, "https://yourdomain.com/api/saveReelSetup.php",
                    resp -> {
                        try {
                            JSONObject o = new JSONObject(resp);
                            if (o.getString("status").equals("success")) {
                                Toast.makeText(context, o.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, o.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error saving setup", Toast.LENGTH_SHORT).show();
                        }
                    },
                    err -> Toast.makeText(context, "Network error saving", Toast.LENGTH_SHORT).show()) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> p = new HashMap<>();
                    p.put("user_id", userId);
                    p.put("ig_media_id", m.getId());
                    p.put("affiliate_url", affiliateUrl);
                    p.put("automation_enabled", automation);
                    p.put("comment_text", comment);
                    p.put("dm_text", dm);
                    return p;
                }
            };
            Volley.newRequestQueue(context).add(req);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageThumb;
        TextView textCaption, textStatus, textAffiliate;
        Button btnSetup, btnGenerate, btnSave;
        LinearLayout panelSetup;
        EditText inputProductUrl, inputComment, inputDM;
        Switch switchAutomation;

        ViewHolder(View v) {
            super(v);
            imageThumb = v.findViewById(R.id.imageThumb);
            textCaption = v.findViewById(R.id.textCaption);
            textStatus = v.findViewById(R.id.textStatus);
            textAffiliate = v.findViewById(R.id.textAffiliate);
            btnSetup = v.findViewById(R.id.btnSetup);
            btnGenerate = v.findViewById(R.id.btnGenerate);
            btnSave = v.findViewById(R.id.btnSave);
            panelSetup = v.findViewById(R.id.panelSetup);
            inputProductUrl = v.findViewById(R.id.inputProductUrl);
            inputComment = v.findViewById(R.id.inputComment);
            inputDM = v.findViewById(R.id.inputDM);
            switchAutomation = v.findViewById(R.id.switchAutomation);
        }
    }
}
