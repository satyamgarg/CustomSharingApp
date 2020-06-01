package com.apps.customsharingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Intent email = new Intent(Intent.ACTION_SEND);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PackageManager pm = getPackageManager();
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{});
        email.putExtra(Intent.EXTRA_SUBJECT, "Hi");
        email.putExtra(Intent.EXTRA_TEXT, "Hi,This is Test");
        email.setType("text/plain");

        List<ResolveInfo> launchables = pm.queryIntentActivities(email, 0);

        Collections.sort(launchables,
                new ResolveInfo.DisplayNameComparator(pm));

        showBottomSheet(launchables);
    }

    public void showBottomSheet(List<ResolveInfo> launchables) {
        AppItemListDialogFragment addPhotoBottomDialogFragment =
                AppItemListDialogFragment.newInstance(MainActivity.this, launchables);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                AppItemListDialogFragment.TAG);
    }

}
