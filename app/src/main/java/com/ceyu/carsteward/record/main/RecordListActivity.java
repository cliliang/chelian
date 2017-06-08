package com.ceyu.carsteward.record.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utility;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;
import com.ceyu.carsteward.record.adapter.RecordListAdapter;
import com.ceyu.carsteward.record.bean.RecordBean;
import com.ceyu.carsteward.record.bean.RecordList;
import com.ceyu.carsteward.record.router.RecordUI;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/6/30.
 * 养车记录列表
 */
public class RecordListActivity extends BaseActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener, PullToRefreshBase.OnRefreshListener2<ListView>{

    private ProgressDialog dialog;
    //判断是否刷新界面的flag，如果进入了上传养车记录界面，onresume里刷新；
    private boolean flag;
    //列表
    private PullToRefreshListView lvMain;
    //是否正在刷新
    private boolean isRefreshing;
    //分页加载的页数
    private int currentPage;
    //列表适配器
    private RecordListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_mainlist);
        flag = true;
        setTitle(R.string.home_block_string_record);
        findViewById(R.id.tv_record_mainlist_toupload).setOnClickListener(this);//上传养车记录
        lvMain = (PullToRefreshListView) findViewById(R.id.lv_record_mainlist);
        lvMain.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        lvMain.setOnRefreshListener(this);
    }

    @Override
    protected void onResume() {

        super.onResume();

        if(flag){
            currentPage=0;
            lvMain.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
            getRecordList();
            //if(adapter!=null) adapter.cleanData();
            adapter = null;
        }

    }

    public void getRecordList() {

        isRefreshing = true;
        flag = false;
        dialog = ProgressDialog.getInstance();
        dialog.show(this, true);
        AppContext appContext = (AppContext)getApplicationContext();
        //http://app3.cheliantime.com/app/?act=record,list&token=UID:4767&page=3
        CheJSONObjectRequest request = new CheJSONObjectRequest(RecordURLs.getList,
                new Utility.ParamsBuilder(appContext)
                        .set("page", ++currentPage)
                        .build(),
                this, this);
        Utility.LogUtils.e("record",RecordURLs.getList +"&token="+appContext.getActiveUser().getToken()+"&page="+currentPage);
        appContext.queue().add(request);

    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Utility.LogUtils.ex(volleyError);
        dialog.dismiss();
        isRefreshing = false;
        lvMain.onRefreshComplete();
        UIHelper.ToastMessage(this, getString(R.string.network_not_connected));
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        Utility.LogUtils.e("record", jsonObject.toString());
        dialog.dismiss();
        isRefreshing = false;
        lvMain.onRefreshComplete(true);
        if(jsonObject == null){
            noData();
            return;
        }
        loadData(RecordList.parse(jsonObject));
    }

    private void loadData(RecordList list){
        Utility.LogUtils.e("record", list.toString());
        if((list==null||list.getList()==null||list.getList().size()<1)&&currentPage == 1){  //无记录
           noData();
        }else{
            if( !list.isHasMore() ){
                lvMain.setMode(PullToRefreshBase.Mode.DISABLED);
                if(currentPage>1) UIHelper.ToastMessage(this, getString(R.string.last_page));
            }
            loadData(list.getList());
        }
    }

    private void loadData(List<RecordBean> list){
        if(adapter==null){
            adapter = new RecordListAdapter(this);
            adapter.setData(list);
            lvMain.setAdapter(adapter);
        }else{
            adapter.setData(list);
            adapter.notifyDataSetChanged();
        }
    }

    public void addHeader(){
        noData2();
    }

    private void noData(){
        new AlertDialog.Builder(this).
                setTitle(getString(R.string.record_doupload))
                .setMessage(getString(R.string.record_goodness)+"\n"+
                        getString(R.string.record_goodness1)+"\n"+
                        getString(R.string.record_goodness2)+"\n"+
                        getString(R.string.record_goodness3))
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RecordListActivity.this.onClick(null);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        noData2();
                    }
                })
                .show();
    }

/*
    2.2版的无养车记录显示上传养车记录的好处，并且在用户点击后跳转到上传养车记录页面
    3.0版改到上传界面去了，保留以防再次拍脑门
    */
    private void noData2(){
        lvMain.setAdapter(new NoRecordAdapter());
        lvMain.setOnItemClickListener((new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecordListActivity.this.onClick(null);
            }
        }));
        lvMain.setMode(PullToRefreshBase.Mode.DISABLED);
    }

    //跳转到养车记录页面
    @Override
    public void onClick(View view){
        flag = true;
        MainRouter.getInstance(this).showActivity(ModuleNames.Record, RecordUI.uploadRecord);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    //上拉加载更多
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if(isRefreshing) return;
        getRecordList();
    }

/*
。。。
2.2版为如果用户有记录则不显示提示，如果列表为空则显示提示（上传养车记录的好处）
  <string name="record_goodness">上传养车记录的好处</string>
    <string name="record_goodness1">1 下次保养、维修时有参照标准（爱车的病例）</string>
    <string name="record_goodness2">2 卖车时能卖高价</string>
    <string name="record_goodness3">3 养车账单，不再糊里糊涂</string>
3.0版一拍脑门又改到了上传界面去了
以防下一版又改回来，注释保留
*/

    private class NoRecordAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return RecordListActivity.this.getLayoutInflater().inflate(R.layout.record_item_norecord, null, false);
        }
    }

}


