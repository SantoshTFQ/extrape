package com.techlabs.extrape;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techlabs.extrape.model.ReelModel;

import java.util.ArrayList;

public class ReelAdapter extends RecyclerView.Adapter<ReelAdapter.ViewHolder> {

    private final ArrayList<ReelModel> list;
    private final String userId;
    private final Context context;

    public ReelAdapter(ArrayList<ReelModel> list, String userId, Context context) {
        this.list = list;
        this.userId = userId;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_reel_setup2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {
        ReelModel m = list.get(i);

        Glide.with(context)
                .load(m.getThumb())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(h.imageThumb);

        h.textCaption.setText(m.getCaption());

        // Status labels
        if (m.isConfigured()) {
            h.textStatus.setText("Configured ✅");
            h.textStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            h.textStatus.setText("Not Configured ❌");
            h.textStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }

        // Automation badge
        if (m.isAutomationEnabled()) {
            h.textAutomation.setVisibility(View.VISIBLE);
            h.textAutomation.setText("⚙️ Automation ON");
            h.textAutomation.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));

        } else {
            h.textAutomation.setVisibility(View.VISIBLE);
            h.textAutomation.setText("⚙️ Automation OFF");
            h.textAutomation.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }

        // Click to open detail setup screen
        h.btnSetup.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReelSetupDetailActivity.class);
            intent.putExtra("reel_id", m.getId());
            intent.putExtra("caption", m.getCaption());
            intent.putExtra("thumb", m.getThumb());
            intent.putExtra("configured", m.isConfigured());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageThumb;
        TextView textCaption, textStatus, textAutomation;
        Button btnSetup;

        ViewHolder(View v) {
            super(v);
            imageThumb = v.findViewById(R.id.imageThumb);
            textCaption = v.findViewById(R.id.textCaption);
            textStatus = v.findViewById(R.id.textStatus);
            textAutomation = v.findViewById(R.id.textAutomation);
            btnSetup = v.findViewById(R.id.btnSetup);
        }
    }
}
