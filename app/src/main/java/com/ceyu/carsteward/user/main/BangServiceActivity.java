package com.ceyu.carsteward.user.main;

import android.os.Bundle;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.BaseActivity;

/**
 * Created by chen on 15/6/11.
 */
public class BangServiceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.bang_service_list));
        setContentView(R.layout.bang_service_layout);
        TextView listView = (TextView) findViewById(R.id.user_bang_service_text);
        String content = Utils.getFromAssets(this, "agreement.txt");
        if (!StringUtils.isEmpty(content)){
            listView.setText(content);
        }
    }
}
