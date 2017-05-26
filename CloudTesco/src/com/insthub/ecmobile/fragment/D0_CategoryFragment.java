package com.insthub.ecmobile.fragment;


import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.insthub.BeeFramework.fragment.BaseFragment;
import com.insthub.BeeFramework.model.BusinessResponse;
import com.insthub.BeeFramework.view.MyViewGroup;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.activity.B1_ProductListActivity;
import com.insthub.ecmobile.activity.D1_CategoryActivity;
import com.insthub.ecmobile.activity.D2_FilterActivity;
import com.insthub.ecmobile.adapter.D0_CategoryAdapter;
import com.insthub.ecmobile.adapter.D0_GridViewAdapter;
import com.insthub.ecmobile.model.SearchModel;
import com.insthub.ecmobile.model.VoiceSearchModel;
import com.insthub.ecmobile.model.VoiceSearchModel.VoiceSearch;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.insthub.ecmobile.protocol.CATEGORY;
import com.insthub.ecmobile.protocol.FILTER;
import com.insthub.ecmobile.zxing.CaptureActivity;
import com.umeng.analytics.MobclickAgent;

public class D0_CategoryFragment extends BaseFragment implements OnClickListener, BusinessResponse, IXListViewListener, VoiceSearch {
	
	private View view;
	private Button btn[];
	private Button input;
	private ImageView search;
	private ImageButton voice;
	private MyViewGroup layout;
	
	private SearchModel searchModel;
	private XListView parentListView;
	D0_CategoryAdapter parentListAdapter;
	
    //============================================
    
	private TextView toolsTextViews[];
	private View views[];
	private ScrollView scrollView;
	private int scrllViewWidth = 0, scrollViewMiddle = 0;
	private ViewPager shop_pager;
	private int currentItem=0;
	private ShopAdapter shopAdapter;
	private ImageButton scanning;
	
    //============================================
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 
		view = inflater.inflate(R.layout.d0_category, null);
		
		input = (Button) view.findViewById(R.id.search_input);
		search = (ImageView) view.findViewById(R.id.search_search);
		voice = (ImageButton) view.findViewById(R.id.search_voice);
		scanning = (ImageButton) view.findViewById(R.id.search_scanning); 
		layout = (MyViewGroup) view.findViewById(R.id.search_layout);
		scrollView = (ScrollView) view.findViewById(R.id.tools_scrlllview);
		//========================================
        parentListView = (XListView) view.findViewById(R.id.parent_list);
		shopAdapter = new ShopAdapter(getActivity().getSupportFragmentManager());
	
		//========================================
		input.setOnClickListener(this);
		search.setOnClickListener(this);
		voice.setOnClickListener(this);
		scanning.setOnClickListener(this);
		
		if (null == searchModel)
        {
            searchModel = new SearchModel(getActivity());
            searchModel.searchCategory();
        }
		
		searchModel.addResponseListener(this);
        
        parentListView.setPullLoadEnable(false);
        parentListView.setPullRefreshEnable(false);
        parentListView.setXListViewListener(this, 1);
        parentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position -1 < searchModel.categoryArrayList.size())
                {
                    CATEGORY category = searchModel.categoryArrayList.get(position -1);
                    try
                    {
                        if (category.children.size() > 0)
                        {
                            Intent it = new Intent(getActivity(),D1_CategoryActivity.class);
                            it.putExtra("category",category.toJson().toString());
                            it.putExtra("category_name", searchModel.categoryArrayList.get(position-1).name);
                            getActivity().startActivity(it);
                            getActivity().overridePendingTransition(R.anim.push_right_in,
                                    R.anim.push_right_out);
                        }
                        else
                        {
                            Intent intent = new Intent(getActivity(), B1_ProductListActivity.class);
                            FILTER filter = new FILTER();
                            filter.category_id = String.valueOf(category.id);
                            intent.putExtra(B1_ProductListActivity.FILTER,filter.toJson().toString());
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.push_right_in,
                                    R.anim.push_right_out);
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
		
        
        CategorySetAdapter();
        
		addKeywords();
		return view;
	} 
	
	
	public void CategorySetAdapter() {
		if (searchModel.readCategoryCache() != null) {
			if (null == parentListAdapter) {
				parentListAdapter = new D0_CategoryAdapter(getActivity(), searchModel.categoryArrayList);
			}
			parentListView.setAdapter(parentListAdapter);
			showToolsView();
			initPager();
		}
	}
    

	@Override
	public void onClick(View v) {
		String tag;
		Intent intent;
		switch (v.getId()) {
		case R.id.search_search:

			break;
		case R.id.search_scanning:
			Intent openCameraIntent = new Intent(getActivity(), CaptureActivity.class);
			startActivityForResult(openCameraIntent, 0);
			break;
		case R.id.search_voice:
			VoiceSearchModel voicesearchmodel = new VoiceSearchModel(getActivity(), this);
			voicesearchmodel.showRecognizerDialog();//弹出语音搜索框
			break;
		case R.id.search_input:
			// TODO Auto-generated method stub
			Intent it = new Intent(getActivity(), D2_FilterActivity.class);
			startActivityForResult(it, 1);
			break;
		}
	}
	
	// 动态添加button，并设置监听
	public void addKeywords() {
		
		if(searchModel.list.size() > 0) {
			
			layout.removeAllViews();
			btn = new Button[searchModel.list.size()];
			for(int i=0; i<searchModel.list.size(); i++)
            {
				View view = LayoutInflater.from(getActivity()).inflate(R.layout.button_view, null);
				btn[i] = (Button) view.findViewById(R.id.search_keyword);
				btn[i].setText(searchModel.list.get(i).toString());
				layout.addView(view);
			}
			
			for(int k = 0; k < searchModel.list.size(); k++) {
				btn[k].setTag(k);
				btn[k].setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int a = (Integer) v.getTag();
                        try
                        {
                            Intent intent = new Intent(getActivity(), B1_ProductListActivity.class);
                            FILTER filter = new FILTER();
                            filter.keywords = btn[a].getText().toString();
                            intent.putExtra(B1_ProductListActivity.FILTER,filter.toJson().toString());
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.push_right_in,
                                    R.anim.push_right_out);
                        }
                        catch (JSONException e)
                        {

                        }
					}
				});
			}
		}
	}
	

	

	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) {
		try {
			if (url.endsWith(ApiInterface.SEARCHKEYWORDS)) {
				addKeywords();
			}
			if (url.endsWith(ApiInterface.CATEGORY)) {
				if (null == parentListAdapter) {
					parentListAdapter = new D0_CategoryAdapter(getActivity(), searchModel.categoryArrayList);
				} else {
					parentListAdapter.notifyDataSetChanged();
					return;
				}
				parentListView.setAdapter(parentListAdapter);
				showToolsView();
				initPager();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("OnMessageResponse ==============  "+ e );
		}
	}


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        input.setText("");
        MobclickAgent.onPageStart("Search");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("Search");
    }
    
/*    private SpeechListener listener = new SpeechListener()
    {

        @Override
        public void onData(byte[] arg0) {
        }

        @Override
        public void onCompleted(SpeechError error) {
            if(error != null) {
                Toast.makeText(getActivity(), "登陆失败", Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void onEvent(int arg0, Bundle arg1) {
        }
    };*/

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
	
	
	public ArrayList<CATEGORY> getCategoryArrayList (){
		return searchModel.categoryArrayList;
	}
	
	
	//====================================================
	
	/**
	 * 动态生成显示items中的textview
	 */
	private void showToolsView() {
		LinearLayout toolsLayout = (LinearLayout) view.findViewById(R.id.tools);
		toolsTextViews = new TextView[searchModel.categoryArrayList.size()];
		views = new View[searchModel.categoryArrayList.size()];
		
		for (int i = 0; i < searchModel.categoryArrayList.size(); i++) {
			View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_b_top_nav_layout, null);
			view.setId(i);
			view.setOnClickListener(toolsItemListener);
			TextView textView = (TextView) view.findViewById(R.id.text);
			textView.setText(searchModel.categoryArrayList.get(i).name);
			toolsLayout.addView(view);
			toolsTextViews[i] = textView;
			views[i] = view;
		}
		changeTextColor(0);
	}
	
	
	
	
	private View.OnClickListener toolsItemListener =new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			shop_pager.setCurrentItem(v.getId());
		}
	};
	
	
	
	/**
     * initPager<br/>
     * 初始化ViewPager控件相关内容
     */
	private void initPager() {
		shop_pager=(ViewPager)view.findViewById(R.id.goods_pager);
		shop_pager.setAdapter(shopAdapter);		
		shop_pager.setOnPageChangeListener(onPageChangeListener);
	}
	
	/**
	 * OnPageChangeListener<br/>
	 * 监听ViewPager选项卡变化事的事件
	 */
	
	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int arg0) {
			if(shop_pager.getCurrentItem() != arg0)
			{
				shop_pager.setCurrentItem(arg0);
			}
			if(currentItem!=arg0)
			{
				changeTextColor(arg0);
				changeTextLocation(arg0);
			}
			currentItem=arg0;
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
	
	
	/**
	 * ViewPager 加载选项卡
	 * @author Administrator
	 *
	 */
	private class ShopAdapter extends FragmentStatePagerAdapter {
		public ShopAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public Fragment getItem(int arg0) {
			Fragment fragment = new Category_Grid_Fragment();
			Bundle bundle = new Bundle();
			String str = searchModel.categoryArrayList.get(arg0).name;
			bundle.putString("typename", str);
			bundle.putSerializable("categoryArrayList", searchModel.categoryArrayList.get(arg0).children);  
			fragment.setArguments(bundle);
			return fragment;
		}
		
		@Override
		public int getCount() {
			return searchModel.categoryArrayList.size();
		}
	}
	
	
	/**
	 * 改变textView的颜色
	 * @param id
	 */
	private void changeTextColor(int id) {
		for (int i = 0; i < toolsTextViews.length; i++) {
			if(i != id){
			   toolsTextViews[i].setBackgroundResource(android.R.color.transparent);
			   toolsTextViews[i].setTextColor(0xff000000);
			}
		}
		toolsTextViews[id].setBackgroundResource(android.R.color.white);
		toolsTextViews[id].setTextColor(0xffff5d5e);
	}
	
	
	/**
	 * 改变栏目位置
	 * 
	 * @param clickPosition
	 */
	private void changeTextLocation(int clickPosition) {
		int x = (views[clickPosition].getTop() - getScrollViewMiddle(views[clickPosition].getTop()) + (getViewheight(views[clickPosition])  / 2));
		scrollView.smoothScrollTo(0, x);
	}
	
	/**
	 * 返回scrollview的中间位置
	 * 
	 * @return
	 */
	private int getScrollViewMiddle(int itemheight) {
		if (scrollViewMiddle == 0)
			scrollViewMiddle = itemheight / 8/*getScrollViewheight() / 2*/;
		return scrollViewMiddle;
	}
	
	/**
	 * 返回ScrollView的宽度
	 * 
	 * @return
	 */
	private int getScrollViewheight() {
		if (scrllViewWidth == 0)
			scrllViewWidth = scrollView.getBottom() - scrollView.getTop();
		return scrllViewWidth;
	}
	
	/**
	 * 返回view的宽度
	 * 
	 * @param view
	 * @return
	 */
	private int getViewheight(View view) {
		return view.getBottom() - view.getTop();
	}
	
	
	
	@SuppressLint("ValidFragment")
	public class Category_Grid_Fragment extends Fragment {
		private D0_GridViewAdapter adapter;
		private ProgressBar progressBar;
		private ImageView hint_img;
		private GridView listView;
		private CATEGORY type;
		private String typename;
		private ArrayList<CATEGORY> categoryArrayList;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.d0_category_gridview, null);
			progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
			hint_img = (ImageView) view.findViewById(R.id.hint_img);
			listView = (GridView) view.findViewById(R.id.listView);
			
			typename = getArguments().getString("typename");
			categoryArrayList = (ArrayList<CATEGORY>) getArguments().getSerializable("categoryArrayList");
			
			((TextView)view.findViewById(R.id.toptype)).setText(typename);
			
			adapter = new D0_GridViewAdapter(getActivity(), categoryArrayList);
			listView.setAdapter(adapter);
			
			return view;
		}
	}

	/*
	 * 语音搜索的回调
	 *  (non-Javadoc)
	 * @see com.insthub.ecmobile.model.VoiceSearchModel.VoiceSearch#VoiceSearchjump(android.content.Context)
	 */
	@Override
	public void VoiceSearchjump(Context context, String resultBuffer) {
		try {
			// TODO Auto-generated method stub
			FILTER filter = new FILTER();
			filter.brand_id = "0";
			filter.category_id = "0";
			Intent it = new Intent(context, B1_ProductListActivity.class);
			filter.keywords = resultBuffer;
			it.putExtra("filter", filter.toJson().toString());
			it.putExtra("genre", "");
			context.startActivity(it);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("VoiceSearchjump =========   "+e);
		}
	}
	
}
