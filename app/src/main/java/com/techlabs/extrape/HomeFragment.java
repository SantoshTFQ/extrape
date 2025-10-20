package com.techlabs.extrape;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.techlabs.extrape.user.SharedPrefManager;
import com.techlabs.extrape.utiles.ApiUrls;
import com.techlabs.extrape.utiles.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    TextView txtTotal, txtWithdrawn, txtPending, txtReady, txtPendingWithdrw;
    TextView txtClicks, txtCTA, txtDMs, txtComments;
    String userId;// = "1"; // get from SharedPrefManager later
    Button btnWithdraw;
    @Nullable
    @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view =inflater.inflate(R.layout.activity_earnings_new2, container, false);
            // Inflate the new dashboard layout

        // ✅ Use getContext() instead of 'this' for Fragments
        userId = SharedPrefManager.getInstance(requireContext()).getUserId();

        // ✅ Always find views from 'view'
        txtTotal = view.findViewById(R.id.txtTotal);
        txtWithdrawn = view.findViewById(R.id.txtWithdrawn);
        txtPendingWithdrw = view.findViewById(R.id.txtWithdrawnPending);
        txtPending = view.findViewById(R.id.txtPending);
        txtReady = view.findViewById(R.id.txtReady);
        btnWithdraw = view.findViewById(R.id.btnWithdraw);

        txtClicks = view.findViewById(R.id.txtlinkClicks);
        txtCTA = view.findViewById(R.id.txtCTAID);
        txtComments = view.findViewById(R.id.txtCommentSent);
        txtDMs = view.findViewById(R.id.txtDmSent);

        // Load data
        fetchEarnings();
        fetchAnalytics();

        // ✅ Button Click
        btnWithdraw.setOnClickListener(v -> {
            String ready = txtReady.getText().toString().replace("₹", "").trim();
            final double readyAmt;
            try {
                readyAmt = Double.parseDouble(ready);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Invalid amount format", Toast.LENGTH_SHORT).show();
                return;
            }

            if (readyAmt <= 0) {
                Toast.makeText(requireContext(), "No balance available to withdraw", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirm Withdraw")
                    .setMessage("Withdraw ₹" + readyAmt + " to your account?")
                    .setPositiveButton("Yes", (d, w) -> requestWithdraw(readyAmt))
                    .setNegativeButton("Cancel", null)
                    .show();
        });

            return view;
        }
    private void fetchAnalytics() {
        ProgressDialog dialog = ProgressDialog.show(getActivity(), "Loading Analytics", "Please wait...", false, false);
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
                            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Parse Error", Toast.LENGTH_SHORT).show();
                        Log.e("ANALYTICS", e.toString());
                    }
                },
                error -> {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Network Error_home_fragment_fanatcs:" + error.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("ANALYTICS_ERR", error.toString());
                });

        MySingleton.getInstance(getActivity()).addToRequestQueue(req);
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
                err -> Toast.makeText(getActivity(), "Network error_home_fgment_fearn:", Toast.LENGTH_SHORT).show()
        );
        MySingleton.getInstance(getActivity()).addToRequestQueue(req);
    }

    private void requestWithdraw(double amount) {
        String url = ApiUrls.REQUEST_WITHDRAW;
        StringRequest req = new StringRequest(Request.Method.POST, url,
                res -> {
                    try {
                        JSONObject o = new JSONObject(res);
                        Toast.makeText(getActivity(), o.optString("message"), Toast.LENGTH_SHORT).show();
                        if (o.getString("status").equals("success")) fetchEarnings();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                err -> Toast.makeText(getActivity(), "Failed to process request", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("user_id", userId);
                p.put("amount", String.valueOf(amount));
                return p;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(req);
    }
}