package com.ceyu.carsteward.self.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.car.main.CarEvent;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.maintain.main.MaintainEvent;
import com.ceyu.carsteward.maintain.router.MaintainUI;
import com.ceyu.carsteward.self.adapter.SelfMechanicAdapter;
import com.ceyu.carsteward.self.bean.MechanicCommentDetail;
import com.ceyu.carsteward.self.bean.SelfMechanicBean;
import com.ceyu.carsteward.self.bean.SelfMechanicComments;
import com.ceyu.carsteward.self.bean.SelfMechanicList;
import com.ceyu.carsteward.self.router.SelfRouter;
import com.ceyu.carsteward.self.router.SelfUI;
import com.ceyu.carsteward.user.bean.User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chen on 15/9/17.
 */
public class SelfMechanicActivity extends BaseActivity {
    private SelfMechanicAdapter mechanicAdapter;
    private CommentAdapter commentAdapter;
    private PullToRefreshListView mechanicListView;
    private TextView stepView;
    private ListView commentListView;
    private User activeUser;
    private Context mContext;
    private CheImageLoader imageLoader;
    private int page = 1;
    private boolean isLoading = false;
    private boolean resetMechanic = false;
    private String defaultToken, defaultName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_mechainc_activity_layout);
        setTitle(R.string.self_choice_mechanic);
        mContext = this;
        imageLoader = new CheImageLoader(requestQueue, mContext);
        activeUser = ((AppContext) getApplicationContext()).getActiveUser();
        stepView = (TextView) findViewById(R.id.self_choice_mechanic_next_step);
        mechanicListView = (PullToRefreshListView) findViewById(R.id.self_choice_engineer_list);
        mechanicListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            resetMechanic = bundle.getBoolean(SelfEvent.choiceMechanic);
            defaultToken = bundle.getString(SelfEvent.mechanicToken);
        }
        mechanicAdapter = new SelfMechanicAdapter(mContext);
        mechanicListView.setAdapter(mechanicAdapter);
        mechanicListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isLoading) {
                    return;
                }
                getData(true);
            }
        });

        progressDialog.show(mContext);
        getData(false);

        mechanicAdapter.setOnCommentClickListener(new SelfMechanicAdapter.OnCommentClickListener() {
            @Override
            public void onDetailClick(SelfMechanicBean bean) {
                if (bean != null) {
                    SelfMechanicComments comments = bean.get_comment();
                    if (comments != null) {
                        commentListView.setVisibility(View.VISIBLE);
                        commentAdapter.setData(comments.getDetails());
                        commentAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
//        mechanicAdapter.setOnChoiceClickListener(new SelfMechanicAdapter.OnChoiceClickListener() {
//            @Override
//            public void onChoiceClick(String mechanicToken, String mechanicName) {
//                if (resetMechanic) {

//                } else {
//                    if (bundle != null) {
//                        bundle.putString(CarEvent.mechanicToken, mechanicToken);
//                        SelfRouter.getInstance(mContext).showActivity(SelfUI.selfTime, bundle);
//                    }
//                }
//            }
//        });
        mechanicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    position--;
                }
                SelfMechanicBean bean = (SelfMechanicBean) mechanicAdapter.getItem(position);
                if (bean != null){
                    if (resetMechanic){
                        Intent resetBundle = new Intent();
                        resetBundle.putExtra(SelfEvent.choiceMechanicName, bean.get_name());
                        resetBundle.putExtra(SelfEvent.choiceMechanicToken, bean.get_token());
                        setResult(SelfEvent.CHOICE_MECHANIC_RESULT, resetBundle);
                        finish();
                    }else {
                        defaultToken = bean.get_token();
                        mechanicAdapter.setDefaultID(defaultToken);
                        mechanicAdapter.notifyDataSetChanged();
                    }

                }
            }
        });

        mechanicAdapter.setOnImageClickListener(new SelfMechanicAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(String[] res) {
                if (res == null) {
                    return;
                }
                ArrayList<String> arrayList = new ArrayList<>();
                for (String s : res) {
                    arrayList.add(s);
                }
                Bundle b = new Bundle();
                b.putStringArrayList(MaintainEvent.photoList, arrayList);
                MainRouter.getInstance(mContext).showActivity(ModuleNames.Maintain, MaintainUI.getShopPhoto, b);
            }
        });
        stepView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundle != null) {
                    bundle.putString(CarEvent.mechanicToken, defaultToken);
                    SelfRouter.getInstance(mContext).showActivity(SelfUI.selfTime, bundle);
                }
            }
        });
        commentListView = (ListView) findViewById(R.id.self_mechanic_comment_list);
        View topView = LayoutInflater.from(mContext).inflate(R.layout.self_comment_top_view_layout, null);
        commentListView.addHeaderView(topView);
        commentAdapter = new CommentAdapter(mContext);
        commentListView.setAdapter(commentAdapter);
        commentListView.setVisibility(View.GONE);
    }

    private void getData(boolean more) {
        isLoading = true;
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        if (more) {
            page++;
        }
        map.put("page", String.valueOf(page));
        CheJSONObjectRequest request = new CheJSONObjectRequest(SelfURLs.getMechanic, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                isLoading = false;
                dismissDialog(SelfMechanicActivity.this);
                SelfMechanicList mechanicList = SelfMechanicList.fromJson(response);
                if (mechanicList != null) {
                    List<SelfMechanicBean> beans = mechanicList.getMechanicBeans();
                    if (beans != null && beans.size() > 0){
                        SelfMechanicBean bean = beans.get(0);
                        if (!resetMechanic){
                            defaultToken = bean.get_token();
                        }
                        defaultName = bean.get_name();
                    }
                    mechanicAdapter.setData(beans, defaultToken);
                    mechanicAdapter.notifyDataSetChanged();
                    mechanicListView.onRefreshComplete();
                    if (mechanicList.isMore()) {
                        mechanicListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                    } else {
                        mechanicListView.setMode(PullToRefreshBase.Mode.DISABLED);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading = false;
                dismissDialog(SelfMechanicActivity.this);
                HandleVolleyError.showErrorMessage(mContext, error);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private class CommentAdapter extends BaseAdapter {

        private List<MechanicCommentDetail> details;
        private LayoutInflater layoutInflater;

        public CommentAdapter(Context cnt) {
            layoutInflater = LayoutInflater.from(cnt);
            details = new ArrayList<>();
        }

        public void setData(List<MechanicCommentDetail> data) {
            if (data != null) {
                this.details = data;
            }
        }

        @Override
        public int getCount() {
            return details.size();
        }

        @Override
        public Object getItem(int position) {
            return details.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PlaceHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.self_mechanic_comment_item_layout, parent, false);
                TextView nameView = (TextView) convertView.findViewById(R.id.self_comment_name);
                TextView detailView = (TextView) convertView.findViewById(R.id.self_comment_content);
                RatingBar bar = (RatingBar) convertView.findViewById(R.id.self_comment_rating);
                holder = new PlaceHolder();
                holder.nameView = nameView;
                holder.contentView = detailView;
                holder.bar = bar;
                convertView.setTag(holder);
            } else {
                holder = (PlaceHolder) convertView.getTag();
            }
            MechanicCommentDetail detail = details.get(position);
            if (detail != null) {
                holder.nameView.setText(detail.get_user());
                holder.contentView.setText(detail.get_info());
                holder.bar.setRating(detail.get_assess());
            }
            return convertView;
        }

        private class PlaceHolder {
            private TextView nameView, contentView;
            private RatingBar bar;
        }
    }

    @Override
    public void onBackClick() {
        if (commentListView.getVisibility() == View.VISIBLE) {
            commentListView.setVisibility(View.GONE);
        } else {
            super.onBackClick();
        }
    }

    @Override
    public void onBackPressed() {
        if (commentListView.getVisibility() == View.VISIBLE) {
            commentListView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}
