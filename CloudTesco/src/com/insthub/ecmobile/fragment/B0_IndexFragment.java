package com.insthub.ecmobile.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.external.maxwin.view.XListViewHeader;
import com.external.viewpagerindicator.PageIndicator;
import com.insthub.BeeFramework.Utils.UpdateManager;
import com.insthub.BeeFramework.fragment.BaseFragment;
import com.insthub.BeeFramework.model.BusinessResponse;
import com.insthub.BeeFramework.view.MyDialog;
import com.insthub.BeeFramework.view.MyListView;
import com.insthub.ecmobile.EcmobileApp;
import com.insthub.ecmobile.EcmobileManager;
import com.insthub.ecmobile.EcmobileManager.RegisterApp;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.activity.B1_ProductListActivity;
import com.insthub.ecmobile.activity.D2_FilterActivity;
import com.insthub.ecmobile.activity.E4_HistoryActivity;
import com.insthub.ecmobile.activity.E7_OrderDetailsActivity;
import com.insthub.ecmobile.adapter.B0_IndexAdapter;
import com.insthub.ecmobile.adapter.B1_ViewPagerAdapter;
import com.insthub.ecmobile.adapter.Bee_PageAdapter;
import com.insthub.ecmobile.model.ConfigModel;
import com.insthub.ecmobile.model.HomeModel;
import com.insthub.ecmobile.model.LoginModel;
import com.insthub.ecmobile.model.ShoppingCartModel;
import com.insthub.ecmobile.model.VoiceSearchModel;
import com.insthub.ecmobile.model.VoiceSearchModel.VoiceSearch;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.insthub.ecmobile.protocol.FILTER;
import com.insthub.ecmobile.protocol.GOODORDER;
import com.insthub.ecmobile.protocol.PLAYER;
import com.insthub.ecmobile.zxing.CaptureActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.MobclickAgent;


public class B0_IndexFragment extends BaseFragment 
	implements BusinessResponse,XListView.IXListViewListener, RegisterApp, OnPageChangeListener, VoiceSearch
{
    //轮播间隔时间  
    protected static final long MSG_DELAY = 5000;
    
    private ViewPager bannerViewPager;
    private PageIndicator mIndicator;
    private MyListView mListView;
    private B0_IndexAdapter listAdapter;
    private TabsFragment mTabsFragment;

    private ArrayList<View> bannerListView;
    private Bee_PageAdapter bannerPageAdapter;
    FrameLayout bannerView;

    private View mTouchTarget;
    private ShoppingCartModel shoppingCartModel;

	private HomeModel dataModel ;

//	private ImageView back;
//	private TextView title;
    private LinearLayout title_right_button;
    private FrameLayout b0_Input_box_layout;
    private TextView headUnreadTextView;
    private ImageButton enter_classification;
    private ImageButton category_btn; //分类的按钮
    private ImageButton voice_search ;//语音搜索
    private ImageButton scanning ;//扫一扫
//    private RelativeLayout article_search ; 
    private Button search_input;
    
    private UpdateManager manager; //版本更新
    Handler exithandler ;
	
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    
    private ImageHandler handler = new ImageHandler(new WeakReference<B0_IndexFragment>(this));
    
	
	/**
	 * 装点点的ImageView数组
	 */
	private ImageView[] tips;
	
	/**
	 * 装ImageView数组
	 */
	private ImageView[] mImageViews;
	
	/**
	 * 图片资源id
	 */
	private int[] imgIdArray ;
	
	ViewGroup group  ;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		manager = new UpdateManager(getActivity());
		
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        View mainView = inflater.inflate(R.layout.b0_index,null);
        
        mTabsFragment = new TabsFragment();
        search_input = (Button) mainView.findViewById(R.id.search_input);
        search_input.setOnClickListener(inputListener);
        
        //顶部的搜索条，扫一扫。。。。。。。。。。
        b0_Input_box_layout = (FrameLayout) mainView.findViewById(R.id.b0_Input_box_layout);
        category_btn = (ImageButton) mainView.findViewById(R.id.category_btn); 
        category_btn.setOnClickListener(category_btn_Listener);
        
        scanning = (ImageButton) mainView.findViewById(R.id.scanning);
        scanning.setOnClickListener(inputListener);
        
        voice_search = (ImageButton) mainView.findViewById(R.id.search_voice);
        voice_search.setOnClickListener(voicesearchlistener);
        
        headUnreadTextView = (TextView)mainView.findViewById(R.id.head_unread_num);
        
        if (null == dataModel)
        {
            dataModel = new HomeModel(getActivity());
            dataModel.checkupdate("" + manager.getVersionCode(getActivity()));
            dataModel.fetchHotSelling();
            dataModel.HomePromoteGoods();
            dataModel.HomeHotGoods();
            dataModel.HomeBostGoods();
            dataModel.HomeNewGoods();
            dataModel.fetchCategoryGoods();
        }

        if (null == ConfigModel.getInstance())
        {
            ConfigModel configModel = new ConfigModel(getActivity());
            configModel.getConfig();
        }

        dataModel.addResponseListener(this);

        bannerView = (FrameLayout)LayoutInflater.from(getActivity()).inflate(R.layout.b0_index_banner, null);

        bannerViewPager = (ViewPager)bannerView.findViewById(R.id.banner_viewpager);
        
        group = (ViewGroup)bannerView.findViewById(R.id.viewGroup);
        
        LayoutParams  params1 = bannerViewPager.getLayoutParams();
		params1.width = getDisplayMetricsWidth();
		params1.height = (int) (params1.width*1.0/ 451 * 200 /*484*/);
		bannerViewPager.setLayoutParams(params1);
		
	

        mListView = (MyListView)mainView.findViewById(R.id.home_listview);
        mListView.addHeaderView(bannerView);
        mListView.bannerView = bannerView;

        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this, 0);
        mListView.setRefreshTime();

        homeSetAdapter();

		ShoppingCartModel shoppingCartModel = new ShoppingCartModel(getActivity());
		shoppingCartModel.addResponseListener(this);
		shoppingCartModel.homeCartList();
		
		exithandler = new Handler() {
			public void handleMessage(final Message msg) {
				switch (msg.what) {
				case 0:
				   	Intent intent = new Intent();
		    		intent.setAction("com.BeeFramework.NetworkStateService");
		    		getActivity().stopService(intent);
		    		getActivity(). finish();
				break;
				}
			}
		};
		
        return mainView;
    }

	public boolean isActive = false;
    @Override
    public void onResume() {
        super.onResume();
       
        if (!isActive) {
            isActive = true;
            EcmobileManager.registerApp(this);
        }
        
        LoginModel loginModel = new LoginModel(getActivity());
		
		ConfigModel configModel = new ConfigModel(getActivity());
        configModel.getConfig();
        MobclickAgent.onPageStart("Home");
    }

    public void homeSetAdapter() {
    	if(dataModel.homeDataCache() != null) {
          if (null == listAdapter)
          {
              listAdapter = new B0_IndexAdapter(getActivity(), dataModel);

          }
          mListView.setAdapter(listAdapter);
          addBannerView();
    	}
    }
    
    android.view.View.OnClickListener voicesearchlistener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			VoiceSearchModel voicesearchmodel = new VoiceSearchModel(B0_IndexFragment.this.getActivity(), B0_IndexFragment.this);
			voicesearchmodel.showRecognizerDialog();//弹出语音搜索框
		}
	};
    
    
    OnClickListener inputListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.search_input:
				try {
					// TODO Auto-generated method stub
					Intent it = new Intent(getActivity(), D2_FilterActivity.class);
					startActivityForResult(it, 1);
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("inputListener =======   "+e);
				}
				break;
			case R.id.scanning:
				Intent openCameraIntent = new Intent(getActivity(), CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
				break;
			default:
				break;
			}
		}
	};

    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
    {
        if (url.endsWith(ApiInterface.HOME_DATA))
        {
            mListView.stopRefresh();
            mListView.setRefreshTime();

            if (null == listAdapter)
            {
                listAdapter = new B0_IndexAdapter(getActivity(), dataModel);
            }
            mListView.setAdapter(listAdapter);
            addBannerView();
        }else if(url.endsWith(ApiInterface.LIST_PROMOTE) 
        		|| url.endsWith(ApiInterface.LIST_HOT)
        		|| url.endsWith(ApiInterface.LIST_BEST) 
        		|| url.endsWith(ApiInterface.LIST_NEW)
        		|| url.endsWith(ApiInterface.CATEGORY)){
            mListView.stopRefresh();
            mListView.setRefreshTime();
            listAdapter = listAdapter != null ? null : null;
            listAdapter = new B0_IndexAdapter(getActivity(), dataModel);
            mListView.setAdapter(listAdapter);
        }
        else if (url.endsWith(ApiInterface.CART_LIST))
        {
        	TabsFragment.setShoppingcartNum();
		}
        else if (url.endsWith(ApiInterface.CHECK_UPDATE))
        {
        	// 检查软件更新
			manager.checkUpdate(dataModel.checkupate, exithandler);
        }
        
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        
    }

    @Override
    public void onDestroy() {    
    	super.onDestroy();
    	dataModel.removeResponseListener(this);
    }

    public void onRefresh(int id)
    {
        dataModel.fetchHotSelling();
        dataModel.HomePromoteGoods();
        dataModel.HomeHotGoods();
        dataModel.HomeBostGoods();
        dataModel.HomeNewGoods();
        dataModel.fetchCategoryGoods();

    }

    @Override
    public void onLoadMore(int id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    
	@Override
	public void onLoadState(int state) {
		// TODO Auto-generated method stub
		switch (state) {
		case XListViewHeader.STATE_NORMAL:
			b0_Input_box_layout.setVisibility(View.GONE);
			break;
		case XListViewHeader.STATE_READY:
			break;
		case XListViewHeader.STATE_REFRESHING:
			b0_Input_box_layout.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
		
	}

    public void addBannerView()
    {
    	if(null != bannerViewPager.getAdapter())
    	{
    		return ; 
    	}
    	
    	WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
    	
    	//将图片装载到数组中
		mImageViews = new ImageView[dataModel.playersList.size()];
		//将点点加入到ViewGroup中
		tips = new ImageView[dataModel.playersList.size()];
        for (int i = 0; i < dataModel.playersList.size(); i++)
        {
            PLAYER player = dataModel.playersList.get(i);
            ImageView  viewOne =  (ImageView)LayoutInflater.from(getActivity()).inflate(R.layout.b0_index_banner_cell,null);
            shared = getActivity().getSharedPreferences("userInfo", 0); 
    		editor = shared.edit();
    		String imageType = shared.getString("imageType", "mind");
    		mImageViews[i] = viewOne;
    		if(imageType.equals("high")) {
                imageLoader.displayImage(player.photo.thumb,viewOne, EcmobileApp.options);
    		} else if(imageType.equals("low")) {
                imageLoader.displayImage(player.photo.small,viewOne, EcmobileApp.options);
    		} else {
    			String netType = shared.getString("netType", "wifi");
    			if(netType.equals("wifi")) {
                    imageLoader.displayImage(player.photo.thumb, viewOne, EcmobileApp.options);
    			} else {
                    imageLoader.displayImage(player.photo.small, viewOne, EcmobileApp.options);
    			}
    		}
    		
    		ImageView number_pages = new ImageView(getActivity());
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			
			layoutParams.height = layoutParams.width = (int) (28f * (float)((float)(wm.getDefaultDisplay().getWidth()) / 720f));
			number_pages.setLayoutParams(layoutParams);
			
	    	tips[i] = number_pages;
	    	if(i == 0){
	    		tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
	    	}else{
	    		tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
	    	}
	    	group.addView(number_pages);
        }
        
		//设置Adapter
        bannerViewPager.setAdapter(new B1_ViewPagerAdapter(getActivity(), mImageViews, dataModel.playersList));
		//设置监听，主要是设置点点的背景
        bannerViewPager.setOnPageChangeListener(this);
		//设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
        bannerViewPager.setCurrentItem((mImageViews.length) * 100);
        handler.sendEmptyMessageDelayed(ImageHandler.MSG_BREAK_SILENT, MSG_DELAY);
  }
    
	public int getDisplayMetricsWidth() {
		int i = getActivity().getWindowManager().getDefaultDisplay().getWidth();
		int j = getActivity().getWindowManager().getDefaultDisplay().getHeight();
		return Math.min(i, j);
	}


	@Override
	public void onRegisterResponse(boolean success) {
		 
	}


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("Home");
    }
    
    @Override
    public void onStop() {
    	 
    	super.onStop();
    	if (!isAppOnForeground()) {
            isActive = false;
        }
    }
    
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getActivity().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getActivity().getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
        	return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    
    OnClickListener category_btn_Listener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			D0_CategoryFragment    searchFragment = new D0_CategoryFragment();
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();

            FragmentTransaction localFragmentTransaction = fragmentManager.beginTransaction();
            localFragmentTransaction.replace(R.id.fragment_container, searchFragment, "tab_two");
            localFragmentTransaction.commit();
     	
            mTabsFragment.getTabImgViewBg();
		}
	};
    
    private static class ImageHandler extends Handler{  
        
        /** 
         * 请求更新显示的View。 
         */  
        protected static final int MSG_UPDATE_IMAGE  = 1;  
        /** 
         * 请求暂停轮播。 
         */  
        protected static final int MSG_KEEP_SILENT   = 2;  
        /** 
         * 请求恢复轮播。 
         */  
        protected static final int MSG_BREAK_SILENT  = 3;  
        /** 
         * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。 
         * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页， 
         * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。 
         */  
        protected static final int MSG_PAGE_CHANGED  = 4;  
           
        //使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等  
        private WeakReference<B0_IndexFragment> weakReference;  
        private int currentItem = 0;  
           
        protected ImageHandler(WeakReference<B0_IndexFragment> wk){  
            weakReference = wk;  
        }  
           
        @Override  
        public void handleMessage(Message msg) {  
            super.handleMessage(msg);  
            B0_IndexFragment activity = weakReference.get();  
            if (activity==null){  
                //Activity已经回收，无需再处理UI了  
                return ;  
            }  
            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。  
            if (activity.handler.hasMessages(MSG_UPDATE_IMAGE)){  
                activity.handler.removeMessages(MSG_UPDATE_IMAGE);  
            }  
            switch (msg.what) {  
            case MSG_UPDATE_IMAGE:  
                activity.bannerViewPager.setCurrentItem((activity.bannerViewPager.getCurrentItem() + 1));  
                //准备下次播放  
                activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);  
                break;  
            case MSG_BREAK_SILENT: 
            case MSG_KEEP_SILENT:  
                //只要不发送消息就暂停了
            	  activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                break;  
/*            case MSG_PAGE_CHANGED:  
               //记录当前的页号，避免播放的时候页面显示不正确。  
                currentItem = msg.arg1;  
                break;  
*/            default:  
                break;  
            }   
        }  
    }  

	@Override
	public void onPageScrollStateChanged(int arg0) {
        switch (arg0) {  
        case ViewPager.SCROLL_STATE_DRAGGING:  
            handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);  
            break;  
        case ViewPager.SCROLL_STATE_IDLE:  
            handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, MSG_DELAY);  
            break;  
        default:  
            break;  
        }  
    
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		setImageBackground(arg0 % mImageViews.length);
	}
	
	/**
	 * 设置选中的tip的背景
	 * @param selectItems
	 */
	private void setImageBackground(int selectItems){
		for(int i = 0; i < tips.length; i++){
			if(i == selectItems){
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			}else{
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
		}
	}

	@Override
	public void VoiceSearchjump(Context context, String resultBuffer) {
		// TODO Auto-generated method stub
		try {
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
