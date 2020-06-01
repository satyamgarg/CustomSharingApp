package com.apps.customsharingapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class AppItemListDialogFragment extends BottomSheetDialogFragment {

    public static final String TAG = AppItemListDialogFragment.class.getName();
    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private static List<ResolveInfo> mAppLists;
    private static Context mContext;
    private static PackageManager mPackageManager;
    private AppsAdapter mAppsAdapter;


    public static AppItemListDialogFragment newInstance(Context context, List<ResolveInfo> appLists) {
        mAppLists = appLists;
        mContext = context;
        mPackageManager = context.getPackageManager();
        final AppItemListDialogFragment fragment = new AppItemListDialogFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_ITEM_COUNT, mAppLists.size());
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sharing_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final GridView gridViewApps = view.findViewById(R.id.gridViewApps);

        mAppsAdapter = new AppsAdapter(mContext, mAppLists);
        gridViewApps.setAdapter(mAppsAdapter);
        gridViewApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                ResolveInfo resolveInfo = (ResolveInfo) mAppsAdapter.getItem(position);
                ActivityInfo activity = resolveInfo.activityInfo;
                ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                        activity.name);
                Intent email = new Intent(Intent.ACTION_SEND);
                email.addCategory(Intent.CATEGORY_LAUNCHER);
                email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                email.setComponent(name);
                startActivity(email);
            }
        });


    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView appName;
        private final ImageView ivAppIcon;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_sharing_app, parent, false));
            ivAppIcon = itemView.findViewById(R.id.ivAppIcon);
            appName = itemView.findViewById(R.id.tvAppName);
        }
    }
/*
    private class AppItemAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final Context mContext;
        private final List<ResolveInfo> mAppLists;

        public AppItemAdapter(Context mContext, List<ResolveInfo> mAppLists) {
            this.mContext = mContext;
            this.mAppLists = mAppLists;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ResolveInfo item = mAppLists.get(position);
            holder.appName.setText(item.activityInfo.name);
            holder.ivAppIcon.setImageDrawable(item.activityInfo.);
        }

        @Override
        public int getItemCount() {
            return mAppLists.size();
        }

    }*/


    public class AppsAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<ResolveInfo> mApps;

        public AppsAdapter(Context context, List<ResolveInfo> mApps) {
            this.inflater = LayoutInflater.from(context);
            this.mApps = mApps;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHendler hendler;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_sharing_app, null);
                hendler = new ViewHendler();
                hendler.textLable = (TextView) convertView.findViewById(R.id.tvAppName);
                hendler.iconImage = (ImageView) convertView.findViewById(R.id.imageViewIcon);
                convertView.setTag(hendler);
            } else {
                hendler = (ViewHendler) convertView.getTag();
            }
            ResolveInfo info = this.mApps.get(position);
            hendler.iconImage.setImageDrawable(info.loadIcon(mPackageManager));
            hendler.textLable.setText(info.loadLabel(mPackageManager));

            return convertView;

        }

        class ViewHendler {
            TextView textLable;
            ImageView iconImage;
        }


        public final int getCount() {
            return mApps.size();
        }

        public final Object getItem(int position) {
            return mApps.get(position);
        }

        public final long getItemId(int position) {
            return position;
        }
    }


}