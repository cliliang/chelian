package com.ceyu.carsteward.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.ui.AppManager;
import com.ceyu.carsteward.engineer.main.EngineerEvent;
import com.ceyu.carsteward.engineer.main.EngineerInfoActivity;
import com.ceyu.carsteward.engineer.main.EngineerPayActivity;
import com.ceyu.carsteward.maintain.bean.MaintainOrderDetail;
import com.ceyu.carsteward.maintain.main.MaintainComboActivity;
import com.ceyu.carsteward.maintain.main.MaintainEvent;
import com.ceyu.carsteward.maintain.main.MaintainMainActivity;
import com.ceyu.carsteward.maintain.main.MaintainOrderActivity;
import com.ceyu.carsteward.maintain.main.MaintainReserveActivity;
import com.ceyu.carsteward.maintain.main.MaintainShopListActivity;
import com.ceyu.carsteward.maintain.main.ReservePaySuccessActivity;
import com.ceyu.carsteward.self.ui.SelfChoiceTimeActivity;
import com.ceyu.carsteward.self.ui.SelfMainActivity;
import com.ceyu.carsteward.self.ui.SelfMechanicActivity;
import com.ceyu.carsteward.self.ui.SelfOrderActivity;
import com.ceyu.carsteward.self.ui.SelfOrderReserveActivity;
import com.ceyu.carsteward.self.ui.SelfShopMapActivity;
import com.ceyu.carsteward.tuan.main.TuanContentActivity;
import com.ceyu.carsteward.tuan.main.TuanMainActivity;
import com.ceyu.carsteward.tuan.main.TuanOrderActivity;
import com.ceyu.carsteward.tuan.main.TuanReserveActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, WXUtils.APP_ID);

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.i("chen", "pay resp:===>" +resp.toString());
        AppConfig appConfig = AppConfig.getInstance(WXPayEntryActivity.this);
        int orderType = appConfig.getOrderType();
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            if (orderType == AppConfig.engineerOrder){
                Intent intent = new Intent(this, EngineerInfoActivity.class);
                intent.putExtra(EngineerEvent.payResult, resp.errCode);
                startActivity(intent);
            }else if (orderType == AppConfig.reserveOrder){
                processMaintainPay();
                Intent intent = new Intent(this, ReservePaySuccessActivity.class);
                intent.putExtra(AppConfig.orderType, AppConfig.reserveOrder);
                startActivity(intent);
            }else if (orderType == AppConfig.tuanOrder){
                processTuanPay();
                Intent intent = new Intent(this, ReservePaySuccessActivity.class);
                intent.putExtra(AppConfig.orderType, AppConfig.tuanOrder);
                startActivity(intent);
            }else if (orderType == AppConfig.selfOrder){
                processSelfPay();
                String sn = appConfig.getCurrentOrder();
                Intent intent = new Intent(this, SelfOrderActivity.class);
                intent.putExtra(MaintainEvent.orderSN, sn);
                startActivity(intent);
            }
            finish();
        } else if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL){
            UIHelper.ToastMessage(this, getResources().getString(R.string.cancel_pay));
            if (orderType == AppConfig.reserveOrder){
                processMaintainPay();
                String sn = appConfig.getCurrentOrder();
                Intent intent = new Intent(this, MaintainOrderActivity.class);
                intent.putExtra(MaintainEvent.orderSN, sn);
                startActivity(intent);
            }else if (orderType == AppConfig.tuanOrder){
                processTuanPay();
                String sn = appConfig.getCurrentOrder();
                Intent intent = new Intent(this, TuanOrderActivity.class);
                intent.putExtra(MaintainEvent.orderSN, sn);
                startActivity(intent);
            }else if (orderType == AppConfig.selfOrder){
                processSelfPay();
                String sn = appConfig.getCurrentOrder();
                Intent intent = new Intent(this, SelfOrderActivity.class);
                intent.putExtra(MaintainEvent.orderSN, sn);
                startActivity(intent);
            }
            finish();
        }else {
            UIHelper.ToastMessage(this, getResources().getString(R.string.pay_fail));
            processMaintainPay();
            if (orderType == AppConfig.reserveOrder){
                String sn = appConfig.getCurrentOrder();
                Intent intent = new Intent(this, MaintainOrderActivity.class);
                intent.putExtra(MaintainEvent.orderSN, sn);
                startActivity(intent);
            }
            finish();
        }
    }

    private void processMaintainPay(){
        AppManager appManager = AppManager.getAppManager();
        appManager.finishActivity(MaintainMainActivity.class);
        appManager.finishActivity(MaintainShopListActivity.class);
        appManager.finishActivity(MaintainComboActivity.class);
        appManager.finishActivity(MaintainReserveActivity.class);
        appManager.finishActivity(MaintainOrderActivity.class);
    }

    private void processTuanPay(){
        AppManager appManager = AppManager.getAppManager();
        appManager.finishActivity(TuanMainActivity.class);
        appManager.finishActivity(TuanContentActivity.class);
        appManager.finishActivity(TuanReserveActivity.class);
        appManager.finishActivity(TuanOrderActivity.class);
    }

    private void processSelfPay(){
        AppManager appManager = AppManager.getAppManager();
        appManager.finishActivity(SelfMainActivity.class);
        appManager.finishActivity(SelfShopMapActivity.class);
        appManager.finishActivity(SelfMechanicActivity.class);
        appManager.finishActivity(SelfChoiceTimeActivity.class);
        appManager.finishActivity(SelfOrderReserveActivity.class);
    }
}