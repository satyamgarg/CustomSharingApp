package com.apps.customsharingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class AppItemListDialogFragment extends BottomSheetDialogFragment implements ShareAdapter.ShareAppClickListner {

    public static final String TAG = AppItemListDialogFragment.class.getName();
    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private static List<ResolveInfo> mAppLists;
    private static Context mContext;


    public static AppItemListDialogFragment newInstance(Context context, List<ResolveInfo> appLists) {
        mAppLists = appLists;
        mContext = context;
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
        return inflater.inflate(R.layout.share_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText("Hi. Check out this news article <link> on the Dhani app. Get updated and use my link and we both will immediately get Free Dhani Cash in our Dhani Wallets. Your Dhani Cash will get added only to you in your Dhani Wallet.");


        PackageManager packageManager = mContext.getPackageManager();
        List<ShareApp> shareApps = new ArrayList<>();

        for (int i = 0; i < mAppLists.size(); i++) {

            ResolveInfo resolveInfo = mAppLists.get(i);
            ShareApp shareApp = new ShareApp();

            String pkgName = resolveInfo.activityInfo.packageName;
            shareApp.setPackageName(pkgName);
            try {
                Drawable drawable = packageManager.getApplicationIcon(packageManager.getApplicationInfo(pkgName, PackageManager.GET_META_DATA));
                shareApp.setDrawable(drawable);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String appName = resolveInfo.loadLabel(packageManager).toString();

            shareApp.setAppName(appName);
            shareApps.add(shareApp);
        }

        RecyclerView recyclerView = view.findViewById(R.id.rvSharableList);


        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(layoutManager);
        final ShareAdapter shareAdapter = new ShareAdapter(shareApps, this);
        recyclerView.setAdapter(shareAdapter);


    }

    @Override
    public void onShareAppClick(ShareApp shareApp) {
        shareCode(this.getActivity(), "Hi. Check out this news article <link> on the Dhani app. Get updated and use my link and we both will immediately get Free Dhani Cash in our Dhani Wallets. Your Dhani Cash will get added only to you in your Dhani Wallet.", "Send To", shareApp.getPackageName());
    }


    public void shareCode(Activity activity, final String message, final String title, final String packageName) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.setPackage(packageName);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, message);
        activity.startActivity(Intent.createChooser(sharingIntent, title));

    }

}