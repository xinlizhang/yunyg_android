package com.insthub.ecmobile.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.external.maxwin.view.XListView;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.ecmobile.EcmobileManager;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.adapter.D0_CategoryAdapter;
import com.insthub.ecmobile.protocol.CATEGORY;
import com.insthub.ecmobile.protocol.FILTER;
import com.umeng.analytics.MobclickAgent;

public class D1_CategoryActivity extends BaseActivity implements IXListViewListener
{

    private ImageView backButton;
    private TextView title;

    private XListView childListView;
    D0_CategoryAdapter childListAdapter;
    CATEGORY category ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d1_category);

        
        backButton = (ImageView)findViewById(R.id.top_view_back);
        title = (TextView) findViewById(R.id.top_view_text);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        childListView = (XListView)findViewById(R.id.child_list);
        childListView.setPullLoadEnable(false);
        childListView.setPullRefreshEnable(false);
        childListView.setXListViewListener(this, 1);
        String categoryStr = getIntent().getStringExtra("category");
        title.setText(getIntent().getStringExtra("category_name"));
        
        try
        {
            JSONObject jsonObject = new JSONObject(categoryStr);
            category = new  CATEGORY();
            category.fromJson(jsonObject);
            childListAdapter = new D0_CategoryAdapter(this,this.category.children);
            childListView.setAdapter(childListAdapter);
            childListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position - 1 < category.children.size())
                    {
                        CATEGORY item = category.children.get(position - 1);

                        try
                        {
                            Intent intent = new Intent(D1_CategoryActivity.this, B1_ProductListActivity.class);
                            FILTER filter = new FILTER();
                            filter.category_id = String.valueOf(item.id);
                            intent.putExtra(B1_ProductListActivity.FILTER,filter.toJson().toString());
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                        }
                        catch (JSONException e)
                        {

                        }
                    }
                }
            });
        }
        catch (JSONException e)
        {

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(EcmobileManager.getUmengKey(this)!=null){
            MobclickAgent.onPageStart("CategoryList");
            MobclickAgent.onResume(this, EcmobileManager.getUmengKey(this),"");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(EcmobileManager.getUmengKey(this)!=null){
            MobclickAgent.onPageEnd("CategoryList");
            MobclickAgent.onPause(this);
        }
    }
    
	@Override
	public void onRefresh(int id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onLoadMore(int id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onLoadState(int state) {
		// TODO Auto-generated method stub
		
	}
}
