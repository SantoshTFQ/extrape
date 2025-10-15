package com.techlabs.extrape;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.techlabs.extrape.model.EarningModel;
import com.techlabs.extrape.utiles.MySingleton;

import org.json.JSONArray;
import org.json.JSONObject;
import com.techlabs.extrape.utiles.ApiUrls;

import java.util.ArrayList;
import java.util.Calendar;

public class EarningHistoryActivity extends AppCompatActivity {
    RecyclerView recyclerEarnings;
    ArrayList<EarningModel> list = new ArrayList<>();
    EarningAdapter adapter;
    String userId = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earning_history);
        recyclerEarnings = findViewById(R.id.recyclerEarnings);
        recyclerEarnings.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EarningAdapter(list, this);
        recyclerEarnings.setAdapter(adapter);

        fetchEarnings(null, null, null, null);
        Button btnFilter = findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(v -> showFilterDialog());

    }

    public void fetchEarnings(String status, String store, String fromDate, String toDate) {
        String url = ApiUrls.GET_EARN_HISTORY + "?user_id=" + userId;
        if (status!=null) url += "&status=" + status;
        if (store!=null) url += "&store=" + store;
        if (fromDate!=null && toDate!=null) url += "&from_date=" + fromDate + "&to_date=" + toDate;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                res -> {
                    try {
                        JSONArray arr = res.getJSONArray("data");
                        list.clear();
                        for (int i=0;i<arr.length();i++){
                            JSONObject o = arr.getJSONObject(i);
                            EarningModel m = new EarningModel();
                            m.setStore(o.getString("store_name"));
                            m.setAmount(o.getDouble("amount"));
                            m.setStatus(o.getString("status"));
                            m.setVisitorId(o.getString("visitor_id"));
                            m.setOrderId(o.getString("order_id"));
                            m.setApprovalDate(o.optString("approval_date"));
                            m.setEarning(o.getDouble("earning_amount"));
                            list.add(m);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e){e.printStackTrace();}
                },
                err -> Toast.makeText(this,"Network error",Toast.LENGTH_SHORT).show()
        );
        MySingleton.getInstance(this).addToRequestQueue(req);
    }
    private void showFilterDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_filter_earning, null);
        EditText edtFrom = view.findViewById(R.id.edtFromDate);
        EditText edtTo = view.findViewById(R.id.edtToDate);
        EditText edtStore = view.findViewById(R.id.edtStore);
        Spinner spnStatus = view.findViewById(R.id.spnStatus);
        Button btnApply = view.findViewById(R.id.btnApply);

        // Spinner setup
        String[] statuses = {"", "Pending", "Approved", "Rejected"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statuses);
        spnStatus.setAdapter(adapter);

        // Date pickers
        edtFrom.setOnClickListener(v -> showDatePicker(edtFrom));
        edtTo.setOnClickListener(v -> showDatePicker(edtTo));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        btnApply.setOnClickListener(v -> {
            String status = spnStatus.getSelectedItem().toString().trim();
            String store = edtStore.getText().toString().trim();
            String from = edtFrom.getText().toString().trim();
            String to = edtTo.getText().toString().trim();
            fetchEarnings(status.isEmpty()?null:status,
                    store.isEmpty()?null:store,
                    from.isEmpty()?null:from,
                    to.isEmpty()?null:to);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showDatePicker(EditText target) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                (view, y, m, d) -> {
                    String date = y + "-" + String.format("%02d", (m+1)) + "-" + String.format("%02d", d);
                    target.setText(date);
                }, year, month, day);
        dpd.show();
    }

}
