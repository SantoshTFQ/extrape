package com.techlabs.extrape;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.techlabs.extrape.utiles.ApiUrls;
import com.techlabs.extrape.utiles.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReelSetupDetailActivity extends AppCompatActivity {

    ImageView imgReelThumb;
    TextView txtCaption, txtGeneratedLink;
    EditText edtProductUrl, edtCommentText, edtDmText;
    Switch switchAutomation;
    Button btnGenerateLink, btnChangeLink, btnSaveSetup;
    ProgressDialog progressDialog;

    String userId = "1", reelId, caption, thumb;
    String generatedLink = "", trackingLink = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reel_setup_detail);

        imgReelThumb = findViewById(R.id.imgReelThumb);
        txtCaption = findViewById(R.id.txtCaption);
        edtProductUrl = findViewById(R.id.edtProductUrl);
        btnGenerateLink = findViewById(R.id.btnGenerateLink);
        btnChangeLink = findViewById(R.id.btnChangeLink);
        txtGeneratedLink = findViewById(R.id.txtGeneratedLink);
        switchAutomation = findViewById(R.id.switchAutomation);
        edtCommentText = findViewById(R.id.edtCommentText);
        edtDmText = findViewById(R.id.edtDmText);
        btnSaveSetup = findViewById(R.id.btnSaveSetup);

        // Get Intent Data
        reelId = getIntent().getStringExtra("reel_id");
        caption = getIntent().getStringExtra("caption");
        thumb = getIntent().getStringExtra("thumb");
        boolean isConfigured = getIntent().getBooleanExtra("configured", false);
        btnSaveSetup.setText(isConfigured ? "Update Setup" : "Save Setup");


        txtCaption.setText(caption);
        Glide.with(this).load(thumb).into(imgReelThumb);

        btnGenerateLink.setOnClickListener(v -> generateAffiliateLink());
        btnChangeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtProductUrl.setVisibility(View.VISIBLE);
                btnGenerateLink.setVisibility(View.VISIBLE);
                btnChangeLink.setVisibility(View.GONE);
            }
        });

        btnSaveSetup.setOnClickListener(v -> saveSetup());
        loadExistingSetup();

    }

    private void loadExistingSetup() {
        String url = ApiUrls.GET_REEL_SETUP + "?user_id=" + userId + "&ig_media_id=" + reelId;
        ProgressDialog progressDialog = ProgressDialog.show(this, "Loading Setup", "Please wait...", false, false);

        StringRequest req = new StringRequest(Request.Method.GET, url,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("status").equals("success")) {
                            JSONObject data = obj.getJSONObject("data");

                            String affiliateUrl = data.optString("affiliate_url", "");
                            boolean automation = data.optInt("automation_enabled", 0) == 1;
                            String commentText = data.optString("comment_text", "");
                            String dmText = data.optString("dm_text", "");

                            if (!affiliateUrl.isEmpty()) {
                                generatedLink = affiliateUrl;
                                txtGeneratedLink.setText("Affiliate Link: " + affiliateUrl);
                                txtGeneratedLink.setVisibility(View.VISIBLE);
                                edtProductUrl.setVisibility(View.GONE);
                                btnGenerateLink.setVisibility(View.GONE);

                            } else {
                                btnGenerateLink.setVisibility(View.VISIBLE);
                                edtProductUrl.setVisibility(View.VISIBLE);
                                edtProductUrl.setVisibility(View.GONE);
                                edtProductUrl.setVisibility(View.GONE);

                            }
                            switchAutomation.setChecked(automation);
                            edtCommentText.setText(commentText);
                            edtDmText.setText(dmText);
                            if (automation) {
                                edtCommentText.setVisibility(View.VISIBLE);
                                edtDmText.setVisibility(View.VISIBLE);
                            } else {
                                edtCommentText.setVisibility(View.GONE);
                                edtDmText.setVisibility(View.GONE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed to load setup", Toast.LENGTH_SHORT).show();
                });

        MySingleton.getInstance(this).addToRequestQueue(req);
    }


    private void generateAffiliateLink() {
        String productUrl = edtProductUrl.getText().toString().trim();
        if (productUrl.isEmpty()) {
            Toast.makeText(this, "Please enter a product URL", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = ProgressDialog.show(this, "Generating Link", "Please wait...", false, false);

        StringRequest req = new StringRequest(Request.Method.POST, ApiUrls.GENERATE_AFFILIATE,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("status").equals("success")) {
                            String affiliate = obj.optString("affiliate_url");
                            String tracking = obj.optString("tracking_url");
                            //generatedLink = affiliate.isEmpty() ? tracking : affiliate;
                            generatedLink = affiliate;
                            trackingLink = tracking;

                            txtGeneratedLink.setText("Affiliate Link: " + generatedLink+"\n" + "Tracking Link: " + trackingLink );
                            txtGeneratedLink.setVisibility(View.VISIBLE);
                            edtDmText.setText("Product Link:\n" + generatedLink);
                            edtCommentText.setText("Thanks for using Extrape! Check DM for the link.");
                            Toast.makeText(this, "Link generated successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed: " + obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                    Log.e("VOLLEY_ERROR", error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("product_url", productUrl);
                params.put("ig_media_id", reelId);
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(req);
    }

    private void saveSetup() {
        if (generatedLink.isEmpty()) {
            Toast.makeText(this, "Generate affiliate link first!", Toast.LENGTH_SHORT).show();
            return;
        }

        String commentText = edtCommentText.getText().toString().trim();
        String dmText = edtDmText.getText().toString().trim();
        String automation = switchAutomation.isChecked() ? "1" : "0";

        progressDialog = ProgressDialog.show(this, "Saving...", "Please wait...", false, false);

        StringRequest req = new StringRequest(Request.Method.POST, ApiUrls.SAVE_REEL_SETUP,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("status").equals("success")) {
                            Toast.makeText(this, "Setup saved!", Toast.LENGTH_SHORT).show();
                            finish(); // Go back to list
                        } else {
                            Toast.makeText(this, "Failed: " + obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Network Error: " + error.toString(), Toast.LENGTH_LONG).show();
                    Log.e("VOLLEY_ERROR", error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("ig_media_id", reelId);
                params.put("affiliate_url", generatedLink);
                params.put("tracking_url", trackingLink);
                params.put("automation_enabled", automation);
                params.put("comment_text", commentText);
                params.put("dm_text", dmText);
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(req);
    }
}
