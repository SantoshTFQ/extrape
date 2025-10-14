package com.techlabs.extrape;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.ViewHolder> {

    private List<String> items;

    public DemoAdapter(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = items.get(position);
        holder.productName.setText(item);
        holder.productId.setText("PROD" + (position + 1));
        holder.clicks.setText(String.valueOf((int) (Math.random() * 1000)));
        holder.price.setText("$99.99");
        holder.commission.setText("$10.00");
        holder.approvedDate.setText("2024-08-20");
        holder.refId.setText("REF" + (position + 1));
        holder.extraRefId.setText("EXTRA" + (position + 1));
        holder.status.setText("Approved");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productId, clicks, price, commission, approvedDate, refId, extraRefId, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productId = itemView.findViewById(R.id.product_id);
            clicks = itemView.findViewById(R.id.clicks);
            price = itemView.findViewById(R.id.price);
            commission = itemView.findViewById(R.id.commission);
            approvedDate = itemView.findViewById(R.id.approved_date);
            refId = itemView.findViewById(R.id.ref_id);
            extraRefId = itemView.findViewById(R.id.extra_ref_id);
            status = itemView.findViewById(R.id.status);
        }
    }
}