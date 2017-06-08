package com.ceyu.carsteward.main.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.tools.BaiduLBS;
import com.ceyu.carsteward.common.tools.FileUtils;
import com.ceyu.carsteward.common.tools.ImagePickException;
import com.ceyu.carsteward.common.tools.ImagePicker;
import com.ceyu.carsteward.common.tools.ImageUtils;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.main.view.WeatherFacadeView;
import com.ceyu.carsteward.packet.router.PacketUI;
import com.ceyu.carsteward.service.ServiceMainFragment;
import com.ceyu.carsteward.tribe.router.TribeUI;
import com.ceyu.carsteward.tribe.ui.TribeEvent;
import com.ceyu.carsteward.tribe.ui.TribeMainFragment;
import com.ceyu.carsteward.tribe.views.TribeActionBar;
import com.ceyu.carsteward.tuan.main.TuanEvent;
import com.ceyu.carsteward.tuan.router.TuanUI;
import com.ceyu.carsteward.user.bean.User;
import com.ceyu.carsteward.user.main.UserMainFragment;
import com.ceyu.carsteward.user.router.UserUI;
import com.ceyu.carsteward.wiki.ui.WikiMainFragment;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateConfig;

import java.io.IOException;
import java.util.Set;

/**
 * Created by chen on 15/6/1.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{

    private final String MAIN_TAG_HOME = "HOME";
    private final String MAIN_TAG_TRIBE = "TRIBE";
//    private final String MAIN_TAG_WIKI = "WIKI";
    private final String MAIN_TAG_SERVICE = "SERVICE";
    private final String MAIN_TAG_USER = "USER";

    //之前选中的tabid
    private WindowManager windowManager;
    private FragmentTabHost mTabHost;
    private ImagePicker imagePicker;
    private TribeActionBar tribeActionBar;
    private TribeMainFragment tribeMainFragment;
    private ServiceMainFragment serviceMainFragment;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                progressDialog.show(MainActivity.this);
            }else{
                dismissDialog(MainActivity.this);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // tags
        String[] tags = {
                MAIN_TAG_HOME,
                MAIN_TAG_TRIBE,
//                MAIN_TAG_WIKI,
                MAIN_TAG_SERVICE,
                MAIN_TAG_USER
        };

        // title
        final int titleId[] = {
                R.string.home_title,
                R.string.tribe_title,
//                R.string.wiki_title,
                R.string.service_title,
                R.string.user_title
        };

        //icon
        final int iconId[] = {
                R.drawable.main_tab_home_selector,
                R.drawable.main_tab_tribe_selector,
//                R.drawable.main_tab_baike_selector,
                R.drawable.main_tab_server_selector,
                R.drawable.main_tab_use_selector
        };

        // fragments
        Class fragments[] = {
                HomeMainFragment.class,
                TribeMainFragment.class,
//                WikiMainFragment.class,
                ServiceMainFragment.class,
                UserMainFragment.class,
        };
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.main_content);
        int count = fragments.length;
        for (int i = 0; i < count; i++) {
            View tabView = getTabItemView(titleId[i], iconId[i]);
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tags[i]).setIndicator(tabView);
            mTabHost.addTab(tabSpec, fragments[i], null);
        }

        initTitleViews();
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                View view = null;
                if (tabId.equals(MAIN_TAG_HOME)) {
                    view = tabViews[0];
                } else if(tabId.equals(MAIN_TAG_SERVICE)){
                    view = LayoutInflater.from(MainActivity.this).inflate(R.layout.common_actionbar_layout, null);
                    TextView titleView = (TextView) view.findViewById(R.id.action_bar_title_id);
                    titleView.setText(getResources().getString(R.string.bang_bang_service));
                    view.findViewById(R.id.action_bar_back_id).setVisibility(View.GONE);
                }else if (tabId.equals(MAIN_TAG_USER)){
                    view = tabViews[1];
                    TextView tvTitle = (TextView) view.findViewById(R.id.action_bar_title_id);
                    tvTitle.setText(getResources().getString(R.string.user_activity_title));
                    view.findViewById(R.id.action_bar_back_id).setVisibility(View.GONE);
                    view.findViewById(R.id.action_bar_back_id).setOnClickListener(MainActivity.this);
                }else if (tabId.equals(MAIN_TAG_TRIBE)){
                    tribeActionBar = new TribeActionBar(MainActivity.this);
                    tribeActionBar.setOnPublishClickedListener(new TribeActionBar.PublishClickListener() {
                        @Override
                        public void onPublishClickedListener() {
                            if (imagePicker == null){
                                imagePicker = new ImagePicker(MainActivity.this);
                            }
                            imagePicker.showDialog(getResources().getString(R.string.tribe_choice_publish_image));
                        }
                    });
                    view = tribeActionBar;
                }
                ActionBar bar = MainActivity.this.getSupportActionBar();
                ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
                if (bar != null && view != null){
                    bar.setCustomView(view, lp);
                }
            }
        });
        mTabHost.onTabChanged(MAIN_TAG_HOME);
        //友盟提示更新
        UpdateConfig.setDebug(true);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);

        //开启推送服务
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();

        processPushIntent(getIntent());

        User user = ((AppContext)getApplicationContext()).getActiveUser();
        AppConfig appConfig = AppConfig.getInstance(this);
        boolean showedHand = appConfig.getShowedHand();
        if (user.isNew() && !showedHand){
            showHandView();
            appConfig.setShowedHand(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        serviceMainFragment = (ServiceMainFragment) getSupportFragmentManager().findFragmentByTag(MAIN_TAG_SERVICE);
        if (serviceMainFragment != null){
            serviceMainFragment.setOnSearchOrderClickedListener(new ServiceMainFragment.OnSearchOrderClickListener() {
                @Override
                public void onSearchOrderClicked() {
                    mTabHost.setCurrentTabByTag(MAIN_TAG_USER);
                }
            });
        }
        tribeMainFragment = (TribeMainFragment) getSupportFragmentManager().findFragmentByTag(MAIN_TAG_TRIBE);
        if (tribeMainFragment != null){
            tribeMainFragment.setOnTribeMainLoadData(new TribeMainFragment.OnTribeMainLoadDataListener() {
                @Override
                public void onTribeMainLoadData(int messageCount) {
                    tribeActionBar.showRedDot(messageCount > 0);
                }
            });
        }
        return super.onCreateView(name, context, attrs);
    }

    private View getTabItemView(int titleId, int icon) {
        View view = getLayoutInflater().inflate(R.layout.main_tab_indicator_item, null);
        TextView textView = (TextView) view.findViewById(R.id.main_tab_title_id);
        textView.setText(titleId);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(icon), null, null);
        return view;
    }

    private long pressTime = 0;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - pressTime < 3 * 1000){
            super.onBackPressed();
        }else {
            pressTime = System.currentTimeMillis();
            UIHelper.ToastMessage(this, getResources().getString(R.string.out_of_app_once_click));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processPushIntent(intent);
    }

    private void processPushIntent(Intent intent){
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            Set<String> keySet = bundle.keySet();
            if (keySet != null){
                for (String key : keySet) {
                    String value = bundle.getString(key);
                    if (value != null){
                        if (value.equals("home")){
                            break;
                        }else if (value.equals("coupons")){
                            MainRouter.getInstance(this).showActivity(ModuleNames.Packet, PacketUI.takePacket);
                            break;
                        }else if (value.contains("group")){
                            String[] array = value.split(":");
                            Bundle bundle1 = new Bundle();
                            bundle1.putString(TuanEvent.shopId, array[1].replace("{", "").replace("}", ""));
                            MainRouter.getInstance(this).showActivity(ModuleNames.Tuan, TuanUI.tuanContent, bundle1);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        BaiduLBS.getInstanse(this).uploadLocation();
        super.onResume();
    }

    @Override
    public void onClick(View view){
        mTabHost.setCurrentTab(0);//返回首页
    }

    private View[] tabViews;

    private void initTitleViews(){
        tabViews = new View[2];
        tabViews[0] = new WeatherFacadeView(MainActivity.this); //天气
        tabViews[1] = getLayoutInflater().inflate(R.layout.common_actionbar_layout, null);  //普通titlebar
        //右侧退出键
        LinearLayout rightLayout = (LinearLayout)tabViews[1].findViewById(R.id.action_bar_right_layout);
        TextView textView = (TextView) getLayoutInflater().inflate(R.layout.common_action_bar_text, null);
        if (textView != null){
            textView.setText(getString(R.string.change_account));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainRouter.getInstance(MainActivity.this).showActivity(ModuleNames.User, UserUI.userLogin);
                }
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            rightLayout.addView(textView, lp);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == ImageUtils.DEFAULT_CAMERA_TAG || requestCode == ImageUtils.DEFAULT_GALARY_TAG){
            if (resultCode == Activity.RESULT_OK){
                new Thread(){
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(1);
                        try {
                            Bitmap bitmap = imagePicker.onImageResult(requestCode, data);
                            if (bitmap != null){
                                FileUtils fileUtils = FileUtils.getInstance(MainActivity.this);
                                try {
                                    String publishPath = fileUtils.putPublishBitmap(ImageUtils.PublishImageName, bitmap);
                                    if (!StringUtils.isEmpty(publishPath)){
                                        Bundle bundle = new Bundle();
                                        bundle.putString(TribeEvent.publishImagePath, publishPath);
                                        MainRouter.getInstance(MainActivity.this).showActivity(ModuleNames.Tribe, TribeUI.tribePublic, bundle);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        UIHelper.ToastMessage(MainActivity.this, getString(R.string.record_pickimagefale));
                                    }
                                });
                            }
                        }catch (ImagePickException e){
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UIHelper.ToastMessage(MainActivity.this, getString(R.string.record_pickimagefale));
                                }
                            });
                        }finally {
                            mHandler.sendEmptyMessage(2);
                        }
                    }
                }.start();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showHandView(){
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 类型
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // 设置flag
        int flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题

        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;

        final View view = LayoutInflater.from(this).inflate(R.layout.home_hand_view_layout, null);
        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        View handView = view.findViewById(R.id.home_hand_view_id);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) handView.getLayoutParams();
        layoutParams.setMargins(45, actionBarHeight * 5 / 3, 0, 0);
        handView.setLayoutParams(layoutParams);

        view.setClickable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (windowManager != null) {
                    windowManager.removeView(view);
                }
            }
        });
        windowManager.addView(view, params);
    }
}
