package com.insthub.ecmobile.activity;



import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.BeeFramework.adapter.BeeBaseAdapter;
import com.insthub.BeeFramework.model.BusinessResponse;
import com.insthub.BeeFramework.view.ToastView;
import com.insthub.ecmobile.EcmobileManager;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.adapter.B1_ProductListAdapter;
import com.insthub.ecmobile.adapter.GoodListLargeListActivityAdapter;
import com.insthub.ecmobile.model.GoodsListModel;
import com.insthub.ecmobile.model.ShoppingCartModel;
import com.insthub.ecmobile.model.VoiceSearchModel;
import com.insthub.ecmobile.model.VoiceSearchModel.VoiceSearch;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.insthub.ecmobile.protocol.FILTER;
import com.insthub.ecmobile.protocol.PAGINATED;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi") 
public class B1_ProductListActivity extends BaseActivity implements BusinessResponse, IXListViewListener,OnClickListener, VoiceSearch
{
	private ImageView backImageButton;

	private ImageView bg;
    private ImageView item_grid_button;
    private ImageView shopping_cart;
    private ImageView vice_search; 
    private TextView good_list_shopping_cart_num;
    private FrameLayout b1_search_layout ;
    private LinearLayout good_list_shopping_cart_num_bg;
    private LinearLayout goodslist_toolbar ; 
	
	private XListView goodlistView;
	private GoodsListModel dataModel;
	private B1_ProductListAdapter listAdapter;
    private GoodListLargeListActivityAdapter largeListActivityAdapter;
    
    
    private int flag = IS_HOT;
    private boolean isSetAdapter = true;
    private SharedPreferences shared;
	private SharedPreferences.Editor editor;
	private View null_pager;
	private String Product_Types = "";

    public static final String KEYWORD = "keyword";
    public static final String CATEGORY_ID = "category_id";
    public static final String TITLE = "title";
    public static final String FILTER = "filter";
    public static final String GENRE = "genre";

    public static final int IS_HOT = 0;
    public static final int PRICE_DESC_INT = 1;
    public static final int PRICE_ASC_INT = 2;

    public String predefine_category_id;

	
	protected class TitleCellHolder
	{
		public ImageView 	triangleImageView;
		public TextView 	titleTextView;
		public ImageView	orderIconImageView;
        public RelativeLayout tabRelative;
	}
	
	TitleCellHolder tabOneCellHolder;
	TitleCellHolder tabTwoCellHolder;
	TitleCellHolder tabThreeCellHolder;

    private BeeBaseAdapter currentAdapter;
    
    FILTER filter = new FILTER();

//    private ImageView search;
    private EditText input;
    private TextView promote;
//    private Button searchFilter;
    private View bottom_line;
	private View top_view;
	public B1_ProductListActivity() 
	{
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.b1_product_list);

        input = (EditText) findViewById(R.id.search_input);
        bottom_line = (View)findViewById(R.id.bottom_line);
        top_view = findViewById(R.id.top_view);
        promote = (TextView) findViewById(R.id.b1_promote_text);
        top_view.setOnClickListener(this);
        input.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        input.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        input.setOnClickListener(this);

        final LinearLayout mainView = (LinearLayout) findViewById(R.id.keyboardLayout1);
        ViewTreeObserver mainViewObserver =  mainView.getViewTreeObserver();
        if (null != mainViewObserver)
        {
            mainViewObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    int heightDiff = mainView.getRootView().getHeight() - mainView.getHeight();
                    if (heightDiff > 100)
                    { // if more than 100 pixels, its probably a keyboard...
                        input.setCursorVisible(true);
                        top_view.setVisibility(View.VISIBLE);
//                        searchFilter.setVisibility(View.GONE);
                    }
                    else
                    {
                        input.setCursorVisible(false);
                        top_view.setVisibility(View.INVISIBLE);
//                        searchFilter.setVisibility(View.VISIBLE);
                    }
                }
            });
        }


        shared = getSharedPreferences("userInfo", 0);
		editor = shared.edit();
        vice_search = (ImageView) findViewById(R.id.b1_vice_search);
        vice_search.setOnClickListener(this);
        
        backImageButton = (ImageView)findViewById(R.id.nav_back_button);
        backImageButton.setOnClickListener(new View.OnClickListener()
		{				
			public void onClick(View v) {				
				finish();
                CloseKeyBoard();
				overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
			}
		});
		
		shopping_cart = (ImageView) findViewById(R.id.good_list_shopping_cart);
		shopping_cart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			   String uid = shared.getString("uid", "");
               if(uid.equals(""))
                {
                    Intent intent = new Intent(B1_ProductListActivity.this, A0_SigninActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_buttom_in,R.anim.push_buttom_out);
                    Resources resource = (Resources) getBaseContext().getResources();
                    String nol=resource.getString(R.string.no_login);                    
                    ToastView toast = new ToastView(B1_ProductListActivity.this, nol);
			        toast.setGravity(Gravity.CENTER, 0, 0);
			        toast.show();
                } else {
                	Intent it = new Intent(B1_ProductListActivity.this,C0_ShoppingCartActivity.class);
                	startActivity(it);
                }
			}
		});
		
		good_list_shopping_cart_num = (TextView) findViewById(R.id.good_list_shopping_cart_num);
		good_list_shopping_cart_num_bg = (LinearLayout) findViewById(R.id.good_list_shopping_cart_num_bg);
		
        if(ShoppingCartModel.getInstance().goods_num == 0) {
        	good_list_shopping_cart_num_bg.setVisibility(View.GONE);
        } else {
        	good_list_shopping_cart_num_bg.setVisibility(View.VISIBLE);
            if(ShoppingCartModel.getInstance()!=null){
                good_list_shopping_cart_num.setText(ShoppingCartModel.getInstance().goods_num+"");
            }
        }
        bg = (ImageView) findViewById(R.id.goodslist_bg);
        null_pager = findViewById(R.id.null_pager);
		
		goodlistView = (XListView)findViewById(R.id.goods_listview);
		
		 goodlistView.setPullLoadEnable(true);
	     goodlistView.setRefreshTime();
	     goodlistView.setXListViewListener(this,1);

		dataModel = new GoodsListModel(this);
	 /*   String listindex =  getIntent().getStringExtra("index");
	    if(listindex != null){
	    	goodlistView.setSelection(Integer.parseInt(listindex));
	    }*/
//		goodlistView.setSelection(3);
		
        String filter_string =  getIntent().getStringExtra(FILTER);
        if( getIntent().getStringExtra(GENRE) != null && !getIntent().getStringExtra(GENRE).isEmpty() )
        {
        	Product_Types = getIntent().getStringExtra(GENRE);
        }
        
        b1_search_layout = (FrameLayout) findViewById(R.id.b1_search_layout);
        goodslist_toolbar = (LinearLayout) findViewById(R.id.goodslist_toolbar);
        if (null != filter_string)
        {
            try
            {
                JSONObject filterJSONObject = new JSONObject(filter_string);
                filter = new com.insthub.ecmobile.protocol.FILTER();
                filter.fromJson(filterJSONObject);
                filter.sort_by = dataModel.PRICE_DESC;

                if(null != filter.category_id)
                {
                    predefine_category_id = filter.category_id;
                }

                if (null != filter.keywords)
                {
                    input.setText(filter.keywords);
                    input.setSelection(input.getText().length());
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        dataModel.addResponseListener(this);

        largeListActivityAdapter = new GoodListLargeListActivityAdapter(this,dataModel.simplegoodsList);

        tabOneCellHolder = new TitleCellHolder();
        tabTwoCellHolder = new TitleCellHolder();
        tabThreeCellHolder = new TitleCellHolder();

        tabOneCellHolder.titleTextView = (TextView)findViewById(R.id.filter_title_tabone);
        tabOneCellHolder.orderIconImageView = (ImageView)findViewById(R.id.filter_order_tabone);
        tabOneCellHolder.triangleImageView = (ImageView)findViewById(R.id.filter_triangle_tabone);
        tabOneCellHolder.tabRelative        = (RelativeLayout)findViewById(R.id.tabOne);
        tabOneCellHolder.tabRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTab(IS_HOT);//
            }
        });

        tabTwoCellHolder.titleTextView = (TextView)findViewById(R.id.filter_title_tabtwo);
        tabTwoCellHolder.orderIconImageView = (ImageView)findViewById(R.id.filter_order_tabtwo);
        tabTwoCellHolder.triangleImageView = (ImageView)findViewById(R.id.filter_triangle_tabtwo);
        tabTwoCellHolder.tabRelative        = (RelativeLayout)findViewById(R.id.tabTwo);
        tabTwoCellHolder.tabRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = PRICE_DESC_INT;
                selectedTab(PRICE_DESC_INT);

            }
        });

        tabThreeCellHolder.titleTextView = (TextView)findViewById(R.id.filter_title_tabthree);
        tabThreeCellHolder.titleTextView.setTextColor(Color.RED);
        tabThreeCellHolder.orderIconImageView = (ImageView)findViewById(R.id.filter_order_tabthree);
        tabThreeCellHolder.triangleImageView = (ImageView)findViewById(R.id.filter_triangle_tabthree);
        tabThreeCellHolder.tabRelative        = (RelativeLayout)findViewById(R.id.tabThree);
        tabThreeCellHolder.tabRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = PRICE_ASC_INT;
                selectedTab(PRICE_ASC_INT);

            }
        });
        selectedTab(PRICE_ASC_INT);
        flag = PRICE_ASC_INT;
        
        if((Product_Types != null && !Product_Types.isEmpty()) && Product_Types.equals(ApiInterface.LIST_PROMOTE))
        {	promote.setVisibility(View.VISIBLE);
        	b1_search_layout.setVisibility(View.GONE);
        	goodslist_toolbar.setVisibility(View.GONE);
        }
        else 
        {
        	promote.setVisibility(View.GONE);
        	b1_search_layout.setVisibility(View.VISIBLE);
        	goodslist_toolbar.setVisibility(View.VISIBLE);
        }
		
	}

    void selectedTab(int index)
    {
    	isSetAdapter = true;
        Resources resource = (Resources) getBaseContext().getResources();
        ColorStateList selectedTextColor = (ColorStateList) resource.getColorStateList(R.color.filter_text_color);

        if (index == IS_HOT)
        {
//            tabOneCellHolder.orderIconImageView.setImageResource(R.drawable.item_grid_filter_down_active_arrow);
//            tabOneCellHolder.orderIconImageView.setWillNotCacheDrawing(true);
//            tabOneCellHolder.triangleImageView.setVisibility(View.VISIBLE);
            tabOneCellHolder.titleTextView.setTextColor(Color.RED);

//            tabTwoCellHolder.orderIconImageView.setImageResource(R.drawable.item_grid_filter_down_arrow);
//            tabTwoCellHolder.triangleImageView.setVisibility(View.INVISIBLE);
            tabTwoCellHolder.titleTextView.setTextColor(selectedTextColor);

//            tabThreeCellHolder.triangleImageView.setVisibility(View.INVISIBLE);
//            tabThreeCellHolder.orderIconImageView.setImageResource(R.drawable.item_grid_filter_up_arrow);
            tabThreeCellHolder.titleTextView.setTextColor(selectedTextColor);

            filter.sort_by = dataModel.IS_HOT;
//            dataModel.fetchPreSearch(filter);
            setRequestGenre(false);


        }
        else if (index == PRICE_DESC_INT)
        {
//            tabTwoCellHolder.orderIconImageView.setImageResource(R.drawable.item_grid_filter_down_active_arrow);
//            tabTwoCellHolder.triangleImageView.setVisibility(View.VISIBLE);
            tabTwoCellHolder.titleTextView.setTextColor(Color.RED);

//            tabOneCellHolder.orderIconImageView.setImageResource(R.drawable.item_grid_filter_down_arrow);
//            tabOneCellHolder.triangleImageView.setVisibility(View.INVISIBLE);
            tabOneCellHolder.titleTextView.setTextColor(selectedTextColor);

//            tabThreeCellHolder.triangleImageView.setVisibility(View.INVISIBLE);
//            tabThreeCellHolder.orderIconImageView.setImageResource(R.drawable.item_grid_filter_up_arrow);
            tabThreeCellHolder.titleTextView.setTextColor(selectedTextColor);

            filter.sort_by = dataModel.PRICE_DESC;
//            dataModel.fetchPreSearch(filter);;
            setRequestGenre(false);
        }
        else
        {

//            tabThreeCellHolder.triangleImageView.setVisibility(View.VISIBLE);
//            tabThreeCellHolder.orderIconImageView.setImageResource(R.drawable.item_grid_filter_up_active_arrow);
            tabThreeCellHolder.titleTextView.setTextColor(Color.RED);

//            tabOneCellHolder.orderIconImageView.setImageResource(R.drawable.item_grid_filter_down_arrow);
//            tabOneCellHolder.triangleImageView.setVisibility(View.INVISIBLE);
            tabOneCellHolder.titleTextView.setTextColor(selectedTextColor);

//            tabTwoCellHolder.orderIconImageView.setImageResource(R.drawable.item_grid_filter_down_arrow);
//            tabTwoCellHolder.triangleImageView.setVisibility(View.INVISIBLE);
            tabTwoCellHolder.titleTextView.setTextColor(selectedTextColor);

            filter.sort_by = dataModel.PRICE_ASC;
//            dataModel.fetchPreSearch(filter);
            setRequestGenre(false);
            
        }
    }
    
    
    private void setRequestGenre (boolean more){
    	if(null == dataModel)
    	{
    		return ;
    	}
    	if(null == filter)
    	{
    		return ; 
    	}
    	if(Product_Types == null || Product_Types.isEmpty())
    	{
      	    dataModel.fetchPreSearch(filter);
      	    return ; 
    	}
    	
    	if(!more)
    	{
    		if(Product_Types.equals(ApiInterface.LIST_PROMOTE))
        	{
        		dataModel.ListGeneralSearch(filter, ApiInterface.LIST_PROMOTE);
        	}
        	else if(Product_Types.equals(ApiInterface.LIST_BEST))
        	{
        		dataModel.ListGeneralSearch(filter, ApiInterface.LIST_BEST);
        	}
        	else if(Product_Types.equals(ApiInterface.LIST_HOT))
        	{
        		dataModel.ListGeneralSearch(filter, ApiInterface.LIST_HOT);
        	}
        	else if(Product_Types.equals(ApiInterface.LIST_NEW))
        	{
        		dataModel.ListGeneralSearch(filter, ApiInterface.LIST_NEW);
        	}
        	else {
        	    dataModel.fetchPreSearch(filter);
        	}
    	}
    	else 
    	{
    		if(Product_Types.equals(ApiInterface.LIST_PROMOTE))
        	{
        	    dataModel.ListGeneralMore(filter, ApiInterface.LIST_PROMOTE);
        	}
        	else if(Product_Types.equals(ApiInterface.LIST_BEST))
        	{
        		dataModel.ListGeneralMore(filter, ApiInterface.LIST_BEST);
        	}
        	else if(Product_Types.equals(ApiInterface.LIST_HOT))
        	{
        		dataModel.ListGeneralMore(filter, ApiInterface.LIST_HOT);
        	}
        	else if(Product_Types.equals(ApiInterface.LIST_NEW))
        	{
        		dataModel.ListGeneralMore(filter, ApiInterface.LIST_NEW);
        	}
        	else {
        	    dataModel.fetchPreSearch(filter);
        	}
    	}
    
    }
    
    
    public void setContent() {
    	
    	if(listAdapter == null) {
    		
    		if(dataModel.simplegoodsList.size() == 0) 
    		{
    			bg.setVisibility(View.GONE);
    			null_pager.setVisibility(View.VISIBLE);
                bottom_line.setBackgroundColor(Color.parseColor("#FFFFFF"));

    		}
    		else 
    		{
    			bg.setVisibility(View.GONE);
    			null_pager.setVisibility(View.GONE);
                bottom_line.setBackgroundColor(Color.parseColor("#000000"));
                if(Product_Types != null && !Product_Types.isEmpty())
                {
                	listAdapter = new B1_ProductListAdapter(this, dataModel.simplegoodsList, this.Product_Types);
                }else 
                {
                	listAdapter = new B1_ProductListAdapter(this, dataModel.simplegoodsList);
                }
                goodlistView.setAdapter(listAdapter);
                if(getIntent().getStringExtra("index") != null){
                	goodlistView.setSelection(Integer.parseInt(getIntent().getStringExtra("index")));
                }
                
    		}
    		
    	} 
    	else 
    	{
    		if(isSetAdapter == true) {
    			if (currentAdapter == largeListActivityAdapter)
    			{
                    goodlistView.setAdapter(largeListActivityAdapter);
                } 
    			else 
    			{
                   if(Product_Types != null && !Product_Types.isEmpty())
                   {
                	   listAdapter = new B1_ProductListAdapter(this, dataModel.simplegoodsList, this.Product_Types);
                   }
                   else 
                   {
                       	listAdapter = new B1_ProductListAdapter(this, dataModel.simplegoodsList);
                   }
                   goodlistView.setAdapter(listAdapter);
                }
    		} else {
    			listAdapter.dataList = dataModel.simplegoodsList;
        		listAdapter.notifyDataSetChanged();	
    		}
    	}

        if (ShoppingCartModel.getInstance().goods_num == 0) {
            good_list_shopping_cart_num_bg.setVisibility(View.GONE);
        } else {
            good_list_shopping_cart_num_bg.setVisibility(View.VISIBLE);
            if (ShoppingCartModel .getInstance()!= null) {
                good_list_shopping_cart_num.setText(ShoppingCartModel.getInstance().goods_num + "");
            }
        }

    }
	
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException
    {    
    	
    	if(Product_Types.equals(ApiInterface.LIST_PROMOTE) || 
    	   Product_Types.equals(ApiInterface.LIST_BEST) ||
    	   Product_Types.equals(ApiInterface.LIST_HOT) ||
    	   Product_Types.equals(ApiInterface.LIST_NEW) )
    	{
      		goodlistView.stopRefresh();
    		goodlistView.stopLoadMore();
    		goodlistView.setRefreshTime();
    		setContent();
            PAGINATED paginated = new PAGINATED();
            paginated.fromJson(jo.optJSONObject("paginated"));
            if (0 == paginated.more)
            {
                goodlistView.setPullLoadEnable(false);
            }
            else
            {
                goodlistView.setPullLoadEnable(true);
            }
            
    	}
    	else if(url.endsWith(ApiInterface.SEARCH))
        {
    		goodlistView.stopRefresh();
    		goodlistView.stopLoadMore();
    		goodlistView.setRefreshTime();

    		setContent();
            PAGINATED paginated = new PAGINATED();
            paginated.fromJson(jo.optJSONObject("paginated"));
            if (0 == paginated.more)
            {
                goodlistView.setPullLoadEnable(false);
            }
            else
            {
                goodlistView.setPullLoadEnable(true);
            }
    	}
    }
    
    

    @Override
    public void onClick(View v) {        
        String tag;
        Intent intent;
        switch(v.getId())
        {
//            case R.id.search_search:
//                break;
            case R.id.top_view:
                CloseKeyBoard();
                input.setText("");
                break;
            case R.id.search_voice:
//            {
//                showRecognizerDialog();
                break;
//            }
            case R.id.search_input:
                try
                {
                Intent it = new Intent(this, D2_FilterActivity.class);
            
                it.putExtra("filter",filter.toJson().toString());
                if (null != predefine_category_id)
                {
                	it.putExtra("predefine_category_id",predefine_category_id);
                }
                startActivityForResult(it, 1);
                }
                catch (JSONException e)
                {
                	System.out.println("search_input ======  "+e);
                }
            	break;
            case R.id.b1_vice_search:
    			VoiceSearchModel voicesearchmodel = new VoiceSearchModel(this, this);
    			voicesearchmodel.showRecognizerDialog();
            	break;
                
   /*         case R.id.search_filter:
            {
                Intent it = new Intent(this, D2_FilterActivity.class);
                try
                {
                    it.putExtra("filter",filter.toJson().toString());
                    if (null != predefine_category_id)
                    {
                        it.putExtra("predefine_category_id",predefine_category_id);
                    }

                }
                catch (JSONException e)
                {

                }
                startActivityForResult(it, 1);
                break;*/
//            }
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            return false;
        }
        return true;
    }
    
	@Override
	public void onRefresh(int id) {			
		isSetAdapter = true;
//        dataModel.fetchPreSearch(filter);
		setRequestGenre(false);
	}
	@Override
	public void onLoadMore(int id) {		
		isSetAdapter = false;
//        dataModel.fetchPreSearchMore(filter);
		setRequestGenre(true);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case 1:
            {
                if(null != data)
                {
                    String filter_string =  data.getStringExtra("filter");
                    Product_Types =  data.getStringExtra("genre");
                    if (null != filter_string)
                    {
                        try
                        {
                            JSONObject filterJSONObject = new JSONObject(filter_string);
                            FILTER filter = new com.insthub.ecmobile.protocol.FILTER();
                            filter.fromJson(filterJSONObject);
                            this.filter = filter;
//                            this.filter.category_id = filter.category_id;
//                            this.filter.price_range = filter.price_range;
//                            this.filter.brand_id = filter.brand_id;
                            
//                            dataModel.fetchPreSearch(this.filter);
                        	setRequestGenre(false);
                            input.clearFocus();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }

	@Override
	protected void onResume() {		
		super.onResume();
        if(EcmobileManager.getUmengKey(this)!=null){
            MobclickAgent.onResume(this, EcmobileManager.getUmengKey(this), "");
            MobclickAgent.onPageStart("FilterPage");
        }
		if(ShoppingCartModel.getInstance().goods_num == 0) {
			good_list_shopping_cart_num_bg.setVisibility(View.GONE);
        } else {
        	good_list_shopping_cart_num_bg.setVisibility(View.VISIBLE);
        	good_list_shopping_cart_num.setText(ShoppingCartModel.getInstance().goods_num+"");
        }
	}

    public void CloseKeyBoard() {
        input.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(EcmobileManager.getUmengKey(this)!=null){
            MobclickAgent.onPageEnd("FilterPage");
            MobclickAgent.onPause(this);
        }
    }
    
	@Override
	public void onLoadState(int state) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void VoiceSearchjump(Context context, String resultBuffer) {
		// TODO Auto-generated method stub
		try {
			// TODO Auto-generated method stub
			FILTER filter = new FILTER();
			filter.brand_id = "0";
			filter.category_id = "0";
			filter.keywords = resultBuffer;
			Product_Types = "";
			this.filter = filter;
		   	setRequestGenre(false);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("VoiceSearchjump =========   "+e);
		}
		
	}
}
