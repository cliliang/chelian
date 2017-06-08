package com.ceyu.carsteward.main.guard;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.ui.BaseFragment;
import com.ceyu.carsteward.main.router.MainUI;
import com.ceyu.carsteward.main.router.MajorRouter;
import com.ceyu.carsteward.user.bean.User;
import com.ceyu.carsteward.user.router.UserUI;

public class GuardFragment extends BaseFragment {
    public final static String PARAM_LAYOUTID = "layoutId";
    private int layoutId = -1;

    public static GuardFragment newInstance(Bundle bundle) {
        GuardFragment fragment = new GuardFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        layoutId = bundle.getInt(PARAM_LAYOUTID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(layoutId, container, false);
        final Activity activity = getActivity();
        if (activity != null){
            Button button = (Button) view.findViewById(R.id.guard_into_app);
            if (button != null){
                button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        updateGuardVersion(activity);
                        AppContext appContext = (AppContext) activity.getApplicationContext();
                        User user = appContext.getActiveUser();
                        if (user == null){
                            MainRouter.getInstance(activity).showActivity(ModuleNames.User, UserUI.userLogin);
                        }else {
                            MajorRouter.getInstance(activity).showActivity(MainUI.MainActivity);
                        }
                        activity.finish();
                    }
                });
            }
        }
        return view;
    }

    private void updateGuardVersion(Activity activity){
        AppConfig appconfig = AppConfig.getInstance(activity);
        appconfig.setVersionCode(appconfig.HOME_GUIDE_VERSION);
    }
}
