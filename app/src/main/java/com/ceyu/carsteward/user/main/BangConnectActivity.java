package com.ceyu.carsteward.user.main;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.ui.BaseActivity;

import java.util.Locale;

/**
 * Created by chen on 15/6/11.
 */
public class BangConnectActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.bang_connect_us));
        setContentView(R.layout.user_bang_connect_layout);
        TextView textView = (TextView) findViewById(R.id.bang_version_code);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            textView.setText(String.format(Locale.US, getResources().getString(R.string.bang_version_content), info.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
