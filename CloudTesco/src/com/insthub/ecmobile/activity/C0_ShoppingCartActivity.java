package com.insthub.ecmobile.activity;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListViewCart;
import com.external.maxwin.view.XListViewCart.IXListViewListenerCart;
import com.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.BeeFramework.model.BusinessResponse;
import com.insthub.BeeFramework.view.MyDialog;
import com.insthub.BeeFramework.view.ToastView;
import com.insthub.ecmobile.EcmobileManager;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.adapter.C0_ShoppingCartAdapter;
import com.insthub.ecmobile.model.AddressModel;
import com.insthub.ecmobile.model.OrderModel;
import com.insthub.ecmobile.model.ShoppingCartModel;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.umeng.analytics.MobclickAgent;

public class C0_ShoppingCartActivity extends BaseActivity  implements BusinessResponse, IXListViewListenerCart
{
    private View footerView;

    private TextView footer_total;
//    private FrameLayout footer_balance;
    private FrameLayout shop_car_null;
    private FrameLayout shop_car_isnot;
    private TextView top_view_text; 
	private Button footer_balance;
//    private ImageView cart_icon;


    private XListViewCart xlistView;
    private C0_ShoppingCartAdapter shopCarAdapter;
    private ArrayList<String> items = new ArrayList<String>();
	private BigDecimal TotalPrices = new BigDecimal(0.00);//总价
    private ShoppingCartModel shoppingCartModel;
    public Handler messageHandler;
    private ImageView back;
    private MyDialog mDialog;
    private OrderModel orderModel;
    private AddressModel addressModel;
    private ProgressDialog pd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c0_shopping_cart);
        shop_car_null = (FrameLayout) findViewById(R.id.shop_car_null);
        shop_car_isnot = (FrameLayout) findViewById(R.id.shop_car_isnot);
        final Resources resource = (Resources) getBaseContext().getResources();
        
        xlistView = (XListViewCart) findViewById(R.id.shop_car_list);
        xlistView.setPullLoadEnable(false);
        xlistView.setRefreshTime();
        xlistView.setXListViewListener(this,1);

        footerView = LayoutInflater.from(this).inflate(R.layout.c0_shopping_car_footer, null);
        footer_total = (TextView) footerView.findViewById(R.id.shop_car_footer_total);
        
        top_view_text = (TextView) findViewById(R.id.top_view_text);
        top_view_text.setText(getBaseContext().getResources().getString(R.string.shopcar_shopcar));
        
//        footer_balance = (FrameLayout) footerView.findViewById(R.id.shop_car_footer_balance);
//        cart_icon = (ImageView) footerView.findViewById(R.id.shop_car_footer_balance_cart_icon);
        
        xlistView.addFooterView(footerView);

        addressModel = new AddressModel(this);
		addressModel.addResponseListener(this);
		
		footer_balance = (Button) footerView.findViewById(R.id.hop_car_footer_balance);
        footer_balance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
           /*     ToastView toast = new ToastView(C0_ShoppingCartActivity.this, "功能暂未开启！");
       	        toast.setGravity(Gravity.CENTER, 0, 0);
       	        toast.show();*/
            	addressModel.getAddressList();
            }
        });

        shoppingCartModel = new ShoppingCartModel(this);
        shoppingCartModel.addResponseListener(this);
        shoppingCartModel.cartList();

        messageHandler = new Handler() {

            public void handleMessage(Message msg) {

                if (msg.what == C0_ShoppingCartAdapter.CART_CHANGE_REMOVE) {
                    Integer rec_id = (Integer) msg.arg1;
                    shoppingCartModel.deleteGoods(rec_id);
                }
                if (msg.what == C0_ShoppingCartAdapter.CART_CHANGE_MODIFY) {
                    int rec_id =  msg.arg1;
                    int new_number = msg.arg2;
                    shoppingCartModel.updateGoods(rec_id, new_number);
                }
//                if (msg.what == C0_ShoppingCartAdapter.CART_CHANGE_CHANGE1) {
//                	footer_balance.setEnabled(false);
//                	footer_balance.setBackgroundResource(R.drawable.item_info_add_cart_desabled_btn_red_b);
//                	cart_icon.setImageResource(R.drawable.shopping_cart_acc_cart_icon);
//                }
//                if (msg.what == C0_ShoppingCartAdapter.CART_CHANGE_CHANGE2) {
//                	footer_balance.setEnabled(true);
//                	footer_balance.setBackgroundResource(R.drawable.button_red);
//                	cart_icon.setImageResource(R.drawable.shopping_cart_acc_cart_icon);
//                }
            } 
        };

        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

            }
        });
        
        orderModel = new OrderModel(this);
		orderModel.addResponseListener(this);

    }

    public void setShopCart() {

        if(shoppingCartModel.goods_list.size() == 0) {
            shop_car_null.setVisibility(View.VISIBLE);
            shop_car_isnot.setVisibility(View.GONE);
        } else {
//            footer_total.setText(shoppingCartModel.total.goods_price);
            shop_car_isnot.setVisibility(View.VISIBLE);
            shop_car_null.setVisibility(View.GONE);
        	if (null == shopCarAdapter)
            {
        		shopCarAdapter = new C0_ShoppingCartAdapter(this, shoppingCartModel.goods_list);
            }
        	
        	updateSumAndNumber();
//        	footer_balance.setText("去结算" + "(" + number/*shoppingCartModel.goods_list.size()*/ + ")");
            xlistView.setAdapter(shopCarAdapter);

//            footer_balance.setEnabled(true);
//        	footer_balance.setBackgroundResource(R.drawable.button_red);
//        	cart_icon.setImageResource(R.drawable.shopping_cart_acc_cart_icon);
            
            shopCarAdapter.parentHandler = messageHandler;
        }

    }
    
	/**
	 * 统计总价
	 * @param skuprives  
	 * @param addandsubtract  
	 * @return
	 */
	private BigDecimal StatisticsTotalPrices (String skuprives, String addandsubtract){
		if(skuprives != null)
		{
			if(addandsubtract.equals("add"))//加
			{
				TotalPrices = TotalPrices.add(new BigDecimal(skuprives.substring(1, skuprives.length() - 1)));  
				
			}
			else if (addandsubtract.equals("subtract"))//减
			{
				TotalPrices = TotalPrices.subtract(new BigDecimal(skuprives.substring(1, skuprives.length() - 1)));  
			}
		}
		return TotalPrices;
	}
	

    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) {
        if (url.endsWith(ApiInterface.CART_LIST)) {
            xlistView.stopRefresh();
        	xlistView.setRefreshTime();
            setShopCart();
        } else if(url.endsWith(ApiInterface.CART_DELETE)) {
            updataCar();
        } else if(url.endsWith(ApiInterface.CART_UPDATE)) {
        	updateSumAndNumber();
//            updataCar();
        } else if(url.endsWith(ApiInterface.FLOW_CHECKORDER)) {
        	
        }  else if(url.endsWith(ApiInterface.ADDRESS_LIST)) {
			if(addressModel.addressList.size() == 0) {
				Intent intent = new Intent(C0_ShoppingCartActivity.this, F1_NewAddressActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(C0_ShoppingCartActivity.this, C1_CheckOutActivity.class);
				startActivityForResult(intent, 1);
			}
			
		}
        else if(url.endsWith(ApiInterface.ORDER_PAY))
        {
			Intent intent = new Intent(C0_ShoppingCartActivity.this, PayWebActivity.class);
            String data = null;
            try
            {
                data = jo.getString("data").toString();
                intent.putExtra("html", data);
            } catch (JSONException e) {
                e.printStackTrace();  
            }

			startActivity(intent);
		}
    }

    public void updataCar() {
        shoppingCartModel.goods_list.clear();
        shoppingCartModel.cartList();
    }
    
	
	/**
	 * 更新商品的总钱数和商品的数量
	 */
	private void updateSumAndNumber (){
		int number = 0 ;
		TotalPrices = new BigDecimal(0.00f);
		for (int i = 0; i < shoppingCartModel.goods_list.size(); i++) {
			//统计总钱数
//			if(shoppingCartModel.goods_list.get(i).selected == null){
//				shoppingCartModel.goods_list.get(i).selected = "0";
//			}else if (shoppingCartModel.goods_list.get(i).selected.equals("1")){
				number ++ ; 
//			}
			
			//统计商品的个数，几个类型的商品。
			for (int j = 0; j < Integer.parseInt(shoppingCartModel.goods_list.get(i).goods_number); j++) {
//				if(shoppingCartModel.goods_list.get(i).selected.equals("1")){
					StatisticsTotalPrices(shoppingCartModel.goods_list.get(i).goods_price, "add");
//				}
			}
		}
		
		footer_balance.setText("去结算" + "(" + number + ")");
		footer_total.setText("￥" + StatisticsTotalPrices(null, null) + "元");
	}

    @Override
    public void onRefresh(int id) {        
        shoppingCartModel.cartList();
    }

    @Override
    public void onLoadMore(int id) {        

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {        
        Resources resource = (Resources) getBaseContext().getResources();
        String suc=resource.getString(R.string.successful_operation);
        String pay=resource.getString(R.string.pay_or_not);
        final String per=resource.getString(R.string.personal_center);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            shoppingCartModel.cartList();
        }
    }
    @Override
    public void onResume() {        
        super.onResume();

        if(EcmobileManager.getUmengKey(this)!=null){
            MobclickAgent.onPageStart("ShopCart");
            MobclickAgent.onResume(this, EcmobileManager.getUmengKey(this),"");
        }
    }

    @Override
    public void onPause() {        
        super.onPause();
        if(EcmobileManager.getUmengKey(this)!=null){
            MobclickAgent.onPageEnd("ShopCart");
            MobclickAgent.onPause(this);
        }
    }
}
