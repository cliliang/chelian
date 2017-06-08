package com.ceyu.carsteward.wxapi;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by chen on 15/6/30.
 */
public class WXPayTask extends AsyncTask<Float, Void, Map<String,String>> {

    private String orderId;
    private IWXAPI msgApi;
    private Context mContext;
    private String payBody;
    private ProgressDialog progressDialog;
    public WXPayTask(Context cnt, IWXAPI api, String body, String order, ProgressDialog dialog){
        this.mContext = cnt;
        this.orderId = order;
        this.msgApi = api;
        this.payBody = body;
        this.progressDialog = dialog;
    }

    @Override
    protected Map<String,String>  doInBackground(Float... params) {
        String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
        String entity = genProductArgs(params[0],orderId);
        byte[] buf = WXUtils.httpPost(url, entity);
        String content = new String(buf);
        Map<String,String> xml=WXUtils.decodeXml(content);
        return xml;
    }

    @Override
    protected void onPostExecute(Map<String,String> result) {
        progressDialog.dismiss();
        if (result.containsKey("return_code")){
            String return_code = result.get("return_code");
            if (!StringUtils.isEmpty(return_code) && return_code.equals("SUCCESS")){
                sendPayReq(result.get("prepay_id"));
            }else {
                UIHelper.ToastMessage(mContext, result.get("return_msg"));
            }
        }
    }
    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    private void sendPayReq(String payId) {
        PayReq req = WXUtils.genPayReq(payId);
        msgApi.registerApp(WXUtils.APP_ID);
        msgApi.sendReq(req);
    }



    private String genProductArgs(float totalMoney, String orderId) {
        StringBuffer xml = new StringBuffer();
        try {
            String	nonceStr = WXUtils.genNonceStr();
            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<>();
            packageParams.add(new BasicNameValuePair("appid", WXUtils.APP_ID));
            packageParams.add(new BasicNameValuePair("body", new String(payBody.getBytes("utf-8"), "iso8859-1")));
            packageParams.add(new BasicNameValuePair("mch_id", WXUtils.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", "http://app3.cheliantime.com/notify/wechat.php"));
            packageParams.add(new BasicNameValuePair("out_trade_no",orderId));
            packageParams.add(new BasicNameValuePair("spbill_create_ip","123.57.137.48"));
//            packageParams.add(new BasicNameValuePair("total_fee", String.valueOf((int)(totalMoney * 100)))); //总金额
            packageParams.add(new BasicNameValuePair("total_fee", String.valueOf(1))); //总金额
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            String sign = WXUtils.genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));

            return WXUtils.toXml(packageParams);

        } catch (Exception e) {
            Log.e("chen", "genProductArgs fail, ex = " + e.getMessage());
            return null;
        }
    }
}
