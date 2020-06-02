package com.apps.customsharingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.AppHolder> {
    List<ShareApp> shareAppList;
    ShareAppClickListner shareAppClickListner;
    ShareAdapter(List<ShareApp> shareAppList,ShareAppClickListner shareAppClickListner){
        this.shareAppList = shareAppList;
        this.shareAppClickListner = shareAppClickListner;
    }
    @NonNull
    @Override
    public AppHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_item_layout,parent,false);

        return new AppHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppHolder holder, int position) {
        final ShareApp shareApp = shareAppList.get(position);
        holder.ivAppIcon.setImageDrawable(shareApp.getDrawable());
        holder.tvAppName.setText(shareApp.getAppName());
        holder.ivAppIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAppClickListner.onShareAppClick(shareApp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shareAppList.size();
    }



    class AppHolder extends RecyclerView.ViewHolder{

        ImageView ivAppIcon;
        TextView tvAppName;
        public AppHolder(@NonNull View itemView) {
            super(itemView);
            ivAppIcon = itemView.findViewById(R.id.ivAppIcon);
            tvAppName = itemView.findViewById(R.id.tvAppName);
        }
    }

    public interface ShareAppClickListner {
        void onShareAppClick(ShareApp shareApp);
    }
}
