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

public class ReelAdapter extends RecyclerView.Adapter<ReelAdapter.ViewHolder> {
    ArrayList<ReelModel> list;
    String userId;
    Context context;

    public ReelAdapter(ArrayList<ReelModel> list, String userId, Context context){
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

        h.btnSetup.setOnClickListener(v -> {
            h.panelSetup.setVisibility(h.panelSetup.getVisibility()==View.VISIBLE ? View.GONE : View.VISIBLE);
        });

        h.btnGenerate.setOnClickListener(v -> {
            String productUrl = h.inputProductUrl.getText().toString().trim();
            if(productUrl.isEmpty()){Toast.makeText(context,"Enter URL",Toast.LENGTH_SHORT).show(); return;}
            String url = "https://yourdomain.com/api/generateAffiliate.php";
            StringRequest req = new StringRequest(Request.Method.POST, url,
                    resp -> {
                        try {
                            JSONObject o = new JSONObject(resp);
                            h.textAffiliate.setText("Affiliate Link: "+o.getString("affiliate_url"));
                        } catch(Exception e){}
                    },
                    err -> Toast.makeText(context,"Error generating",Toast.LENGTH_SHORT).show()){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> p=new HashMap<>();
                    p.put("user_id",userId);
                    p.put("product_url",productUrl);
                    return p;
                }
            };
            Volley.newRequestQueue(context).add(req);
        });

        h.btnSave.setOnClickListener(v -> {
            StringRequest req = new StringRequest(Request.Method.POST,"https://yourdomain.com/api/saveReelSetup.php",
                    resp -> Toast.makeText(context,"Saved!",Toast.LENGTH_SHORT).show(),
                    err -> Toast.makeText(context,"Error saving",Toast.LENGTH_SHORT).show()){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> p=new HashMap<>();
                    p.put("user_id",userId);
                    p.put("ig_media_id",m.getId());
                    p.put("affiliate_url",h.textAffiliate.getText().toString().replace("Affiliate Link: ",""));
                    p.put("automation_enabled",h.switchAutomation.isChecked()?"1":"0");
                    p.put("comment_text",h.inputComment.getText().toString());
                    p.put("dm_text",h.inputDM.getText().toString());
                    return p;
                }
            };
            Volley.newRequestQueue(context).add(req);
        });
    }

    @Override
    public int getItemCount() {return list.size();}

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageThumb;
        TextView textCaption, textStatus, textAffiliate;
        Button btnSetup, btnGenerate, btnSave;
        LinearLayout panelSetup;
        EditText inputProductUrl, inputComment, inputDM;
        Switch switchAutomation;
        ViewHolder(View v){
            super(v);
            imageThumb=v.findViewById(R.id.imageThumb);
            textCaption=v.findViewById(R.id.textCaption);
            textStatus=v.findViewById(R.id.textStatus);
            textAffiliate=v.findViewById(R.id.textAffiliate);
            btnSetup=v.findViewById(R.id.btnSetup);
            btnGenerate=v.findViewById(R.id.btnGenerate);
            btnSave=v.findViewById(R.id.btnSave);
            panelSetup=v.findViewById(R.id.panelSetup);
            inputProductUrl=v.findViewById(R.id.inputProductUrl);
            inputComment=v.findViewById(R.id.inputComment);
            inputDM=v.findViewById(R.id.inputDM);
            switchAutomation=v.findViewById(R.id.switchAutomation);
        }
    }
}
