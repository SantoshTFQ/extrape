package com.techlabs.extrape;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techlabs.extrape.model.EarningModel;

import java.util.ArrayList;

public class EarningAdapter extends RecyclerView.Adapter<EarningAdapter.VH>{
    ArrayList<EarningModel> list; Context ctx;
    public EarningAdapter(ArrayList<EarningModel> l, Context c){list=l;ctx=c;}
    @NonNull
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v){
        return new VH(LayoutInflater.from(ctx).inflate(R.layout.item_earning,p,false));
    }
    public void onBindViewHolder(@NonNull VH h,int i){
        EarningModel m=list.get(i);
        h.txtStore.setText(m.getStore());
        h.txtAmt.setText("₹"+m.getAmount());
        h.txtStatus.setText(m.getStatus());
        h.txtEarn.setText("Earning ₹"+m.getEarning());
        h.txtDate.setText(m.getApprovalDate());
    }
    public int getItemCount(){return list.size();}
    static class VH extends RecyclerView.ViewHolder{
        TextView txtStore,txtAmt,txtStatus,txtEarn,txtDate;
        VH(View v){super(v);
            txtStore=v.findViewById(R.id.txtStore);
            txtAmt=v.findViewById(R.id.txtAmt);
            txtStatus=v.findViewById(R.id.txtStatus);
            txtEarn=v.findViewById(R.id.txtEarn);
            txtDate=v.findViewById(R.id.txtDate);
        }
    }
}
