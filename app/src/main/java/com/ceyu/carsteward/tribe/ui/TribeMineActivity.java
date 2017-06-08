package com.ceyu.carsteward.tribe.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.ui.BaseActivity;

/**
 * Created by chen on 15/9/2.
 */
public class TribeMineActivity extends BaseActivity {

    private final String TAG_COMMENT = "tag_comment";
    private final String TAG_PUBLISH = "tag_publish";
    private final String TAG_MESSAGE = "tag_message";

    private TribeMyCommentFragment commentFragment;
    private TribeMyPublishFragment publishFragment;
    private TribeMyMessageFragment messageFragment;
    private FragmentTabHost mTabHost;
    private TextView messageDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tribe_mine_activity_layout);
        String[] tags = {
                TAG_COMMENT,
                TAG_PUBLISH,
                TAG_MESSAGE
        };
        int[] titles = {
                R.string.tribe_my_comment,
                R.string.tribe_my_edit,
                R.string.tribe_my_message
        };
        Class[] fragments = {
                TribeMyCommentFragment.class,
                TribeMyPublishFragment.class,
                TribeMyMessageFragment.class
        };
        mTabHost = (FragmentTabHost) findViewById(R.id.tribe_mine_tab_host);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tribe_mine_content);
        mTabHost.getTabWidget().setDividerDrawable(null);
        int count = fragments.length;
        for (int i = 0; i < count; i++) {
            View tabView = getTabItemView(titles[i]);
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tags[i]).setIndicator(tabView);
            mTabHost.addTab(tabSpec, fragments[i], null);
        }
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                updateTab();
                if (tabId.equals(TAG_COMMENT)){
                    setTitle(getResources().getString(R.string.tribe_my_comment));
                }else if (tabId.equals(TAG_PUBLISH)){
                    setTitle(getResources().getString(R.string.tribe_my_edit));
                }else {
                    setTitle(getResources().getString(R.string.tribe_my_message));
                }
            }
        });
        setTitle(getResources().getString(R.string.tribe_my_comment));
        View view = mTabHost.getTabWidget().getChildTabViewAt(0);
        TextView tabView = (TextView) view.findViewById(R.id.tribe_mine_tab_title);
        View tabLayout = view.findViewById(R.id.tribe_mine_tab_layout);
        tabView.setTextColor(getResources().getColor(R.color.white));
        tabLayout.setBackgroundColor(getResources().getColor(R.color.orange));

        View messageTab = mTabHost.getTabWidget().getChildTabViewAt(2);
        messageDot = (TextView) messageTab.findViewById(R.id.tribe_action_more_dot);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (commentFragment == null){
            commentFragment = (TribeMyCommentFragment) getSupportFragmentManager().findFragmentByTag(TAG_COMMENT);
        }
        if (publishFragment == null){
            publishFragment = (TribeMyPublishFragment) getSupportFragmentManager().findFragmentByTag(TAG_PUBLISH);
        }
        if (messageFragment == null){
            messageFragment = (TribeMyMessageFragment) getSupportFragmentManager().findFragmentByTag(TAG_MESSAGE);
        }
        if (commentFragment != null){
            commentFragment.setOnCommentLoadListener(new TribeMyCommentFragment.OnCommentLoadedListener() {
                @Override
                public void onCommentLoad(int count) {
                    if (count > 0){
                        messageDot.setVisibility(View.VISIBLE);
                        messageDot.setText(String.valueOf(count));
                    }else {
                        messageDot.setVisibility(View.GONE);
                    }
                }
            });
        }
        if (publishFragment != null){
            publishFragment.setOnPublishLoadListener(new TribeMyPublishFragment.OnPublishLoadListener() {
                @Override
                public void onPublishLoad(int count) {
                    if (count > 0){
                        messageDot.setVisibility(View.VISIBLE);
                        messageDot.setText(String.valueOf(count));
                    }else {
                        messageDot.setVisibility(View.GONE);
                    }
                }
            });
        }
        if (messageFragment != null){
            messageFragment.setOnMessageLoadListener(new TribeMyMessageFragment.OnMessageLoadListener() {
                @Override
                public void onMessageLoad(int count) {
                    if (count > 0){
                        messageDot.setVisibility(View.VISIBLE);
                        messageDot.setText(String.valueOf(count));
                    }else {
                        messageDot.setVisibility(View.GONE);
                    }
                }
            });
        }
        return super.onCreateView(parent, name, context, attrs);
    }

    private void updateTab(){
        for (int i = 0; i < mTabHost.getTabWidget().getTabCount(); i++){
            View view = mTabHost.getTabWidget().getChildTabViewAt(i);
            TextView tabView = (TextView) view.findViewById(R.id.tribe_mine_tab_title);
            View tabLayout = view.findViewById(R.id.tribe_mine_tab_layout);
            if (mTabHost.getCurrentTab() == i){
                tabView.setTextColor(getResources().getColor(R.color.white));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.orange));
            }else {
                tabView.setTextColor(getResources().getColor(R.color.text_hint));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.white));
            }
        }
    }

    private View getTabItemView(int title){
        View view = getLayoutInflater().inflate(R.layout.tribe_mine_tab_item_layout, null);
        TextView titleView = (TextView) view.findViewById(R.id.tribe_mine_tab_title);
        titleView.setText(title);
        return view;
    }
}
