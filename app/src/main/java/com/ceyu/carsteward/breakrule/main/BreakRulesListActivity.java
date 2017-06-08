package com.ceyu.carsteward.breakrule.main;

import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.app.Config;
import com.ceyu.carsteward.breakrule.adapter.DetalListAdatper;
import com.ceyu.carsteward.breakrule.adapter.NoRecordAdapter;
import com.ceyu.carsteward.breakrule.bean.BreakRulesBean;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utility;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/6/29.
 * 某车违章详情列表
 */
public class BreakRulesListActivity extends BaseActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breakrule_record);
        setTitle(getString(R.string.breakrules_title_details));
        ProgressDialog.getInstance().show(this);
        String cid = getIntent().getStringExtra(BreakRulesURLs.TAG_CID);
        connect(cid);
    }

    private void connect(String cid){
        AppContext appContext = (AppContext)getApplicationContext();
        if(Config.isDevelopeMode()) Utility.LogUtils.e("weizhang", "URL===>"+BreakRulesURLs.breakrulesList +"\nParams===>"+ new Utility.ParamsBuilder(appContext).set("cid",cid).build().toString());
        CheJSONObjectRequest request = new CheJSONObjectRequest(BreakRulesURLs.breakrulesList,
                new Utility.ParamsBuilder(appContext).set("cid",cid).build(),
                this,this);
        //appContext.queue().add(request);
        requestQueue.add(request);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Utility.LogUtils.ex(volleyError);
        ProgressDialog.getInstance().dismiss();
        UIHelper.ToastMessage(this, R.string.http_exception_error);
        ListView lvMain = (ListView) findViewById(R.id.breakrules_record_list);
        NoRecordAdapter adapterNo = new NoRecordAdapter(this);
        lvMain.setAdapter(adapterNo);
    }

    @Override
    public void onResponse(JSONObject jsonObject) {



        Utility.LogUtils.e("weizhang", jsonObject.toString());
        ProgressDialog.getInstance().dismiss();
        BreakRulesBean.ErrorInfoBean error = BreakRulesBean.parseError(jsonObject);
        if(error!=null){    //发送来的是错误信息
            if(error.getInfo().equals("B0014")){    //B0014车辆信息不全

                setResult(BreakRulesURLs.RESULT_TOEDITCAR);
                this.finish();
            }else if(error.getInfo().equals("B0015")){  //B0015无定位信息

                setResult(BreakRulesURLs.RESULT_TOSETTINGLOC);
                this.finish();
            }
        }else {
            BreakRulesBean listbean = BreakRulesBean.parse0(jsonObject);
            ListView lvMain = (ListView) findViewById(R.id.breakrules_record_list);

            if (listbean.hasData()) {
                DetalListAdatper adatper = new DetalListAdatper(this);
                Utility.LogUtils.e("weizhang","list===>"+listbean.getList().toString());
                adatper.injectList(listbean.getList());
                lvMain.setAdapter(adatper);
            } else {
                NoRecordAdapter adapterNo = new NoRecordAdapter(this);
                lvMain.setAdapter(adapterNo);
            }
            requestQueue.stop();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(BreakRulesURLs.RESULT_NOTHINGH);
        this.finish();
    }

    @Override
    public void onBackClick() {
        onBackPressed();
    }
}
