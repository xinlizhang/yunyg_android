package com.insthub.ecmobile.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.external.androidquery.callback.AjaxStatus;
import com.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.BeeFramework.model.BusinessResponse;
import com.insthub.BeeFramework.view.MyDialog;
import com.insthub.ecmobile.EcmobileApp;
import com.insthub.ecmobile.EcmobileManager;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.model.OrderModel;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.insthub.ecmobile.protocol.GOODORDER;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

public class E7_OrderDetailsActivity extends BaseActivity implements BusinessResponse
{
	
	private GOODORDER order;
	private TextView top_view_text ;
	private TextView order_merk;//订单号
	private TextView balance_name;//收货地址的收货人名
	private TextView balance_phoneNum;//收货地址的收货电话号
	private TextView balance_address;//收货地址的详细地址
	private LinearLayout goods_list ; //商品的布局
	private TextView balance_pay;//支付方式
	private TextView send_information;//配送的信息
	private TextView goods_totai_money;//商品总额
	private TextView order_freight; //运费
	private TextView formated_integral_money;//积分
	private TextView formated_bonus;//红包
	private TextView accounts_payable;//实付款
	private TextView order_time; //下单时间
	private ImageView top_view_back ;//返回键 
	private Button contact_service;//联系客服 
	private Button order_cancel ;//取消订单 
	private Button order_pay;//去支付
//	private OrderInfoModel orderinfomodel;
	private String topviewtext;
	private OrderModel orderModel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e7_order_details);
		try {
			Intent intent = getIntent();
			String orderstr = intent.getStringExtra("order");
			if(intent.getStringExtra("topviewtext") != null)
			{
				topviewtext = intent.getStringExtra("topviewtext").toString();
			}
			JSONObject myJsonObject = new JSONObject(orderstr);
			order = new GOODORDER();
			order.fromJson(myJsonObject);			
			orderModel = new OrderModel(this);
			orderModel.addResponseListener(this);
//			orderinfomodel = new OrderInfoModel(this);
//			orderinfomodel.addResponseListener(this);
//			orderinfomodel.OrderInfoRequest(""+ order.order_id);
			initView();
			setView(order);
	
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("openjson ===========   "+e);
		}
	}
	
	
	private void initView (){
		Resources resource = (Resources) getBaseContext().getResources();
		top_view_back = (ImageView) this.findViewById(R.id.top_view_back);
		top_view_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				E7_OrderDetailsActivity.this.finish();
			}
		});
		
		top_view_text = (TextView) this.findViewById(R.id.top_view_text);
		order_merk = (TextView) this.findViewById(R.id.order_merk);
		balance_name  = (TextView) this.findViewById(R.id.balance_name);
		balance_phoneNum = (TextView) this.findViewById(R.id.balance_phoneNum);
		balance_address = (TextView) this.findViewById(R.id.balance_address);
		goods_list = (LinearLayout) this.findViewById(R.id.goods_list);
		balance_pay = (TextView) this.findViewById(R.id.balance_pay);
		send_information = (TextView) this.findViewById(R.id.send_information);
		goods_totai_money = (TextView) this.findViewById(R.id.goods_totai_money);
		order_freight = (TextView) this.findViewById(R.id.order_freight);
		formated_integral_money = (TextView) this.findViewById(R.id.formated_integral_money);
		formated_bonus = (TextView) this.findViewById(R.id.formated_bonus);
		accounts_payable = (TextView) this.findViewById(R.id.accounts_payable);
		order_time = (TextView) this.findViewById(R.id.order_time);
		contact_service = (Button) this.findViewById(R.id.contact_service);
		contact_service.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(E7_OrderDetailsActivity.this, J0_ServiceChatActivity.class);
				E7_OrderDetailsActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.push_buttom_in,R.anim.push_buttom_out);
			}
		});
		
		if(topviewtext != null)
		{
			top_view_text.setText(topviewtext);
		}
		
		if(!topviewtext.equals("等待付款")){
			order_cancel.setVisibility(View.GONE);
			order_pay.setVisibility(View.GONE);
		}else {
			order_cancel = (Button) this.findViewById(R.id.order_cancel);
			order_cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Resources resource = (Resources) getBaseContext().getResources();
					String exit = resource.getString(R.string.balance_cancel_or_not);
					String exiten = resource.getString(R.string.prompt);
					final MyDialog cancelOrders = new MyDialog(E7_OrderDetailsActivity.this, exiten, exit);
					cancelOrders.show();
					cancelOrders.positive.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							cancelOrders.dismiss();
							orderModel.orderCancle(Integer.parseInt(order.order_id));
						}
					});
					cancelOrders.negative.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							cancelOrders.dismiss();
						}
					});

				}
			});
			order_pay = (Button) this.findViewById(R.id.order_pay);
			order_pay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
                    if (EcmobileManager.getAlipayCallback(getApplicationContext()) != null
                            && EcmobileManager.getAlipayParterId(getApplicationContext()) != null
                            && EcmobileManager.getAlipaySellerId(getApplicationContext()) != null
                            && EcmobileManager.getRsaAlipayPublic(getApplicationContext()) != null
                            && EcmobileManager.getRsaPrivate(getApplicationContext()) != null)
                    {
                        if (0 == order.order_info.pay_code.compareTo("alipay"))
                        {
//                            showAlipayDialog();
                        	orderModel.orderPay(Integer.parseInt(order.order_info.order_id));
                        }else if(0 == order.order_info.pay_code.compareTo("upop")){
                            orderModel.orderPay(  Integer.parseInt(order.order_info.order_id));
                        }else if(0 == order.order_info.pay_code.compareTo("tenpay")){
                            orderModel.orderPay(  Integer.parseInt(order.order_info.order_id));
                        }
                        else
                        {
                            orderModel.orderPay(  Integer.parseInt(order.order_info.order_id));
                        }
                    }
                    else
                    {
                        orderModel.orderPay(  Integer.parseInt(order.order_info.order_id));
                    }
				}
			});
		}
	}
	
    private void showAlipayDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.alipay_dialog,null);
        final Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        LinearLayout alipayLayout = (LinearLayout) view.findViewById(R.id.alipay);
        LinearLayout alipayWapLayout = (LinearLayout) view.findViewById(R.id.alipay_wap);

        alipayLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                Intent intent =new Intent(E7_OrderDetailsActivity.this, AlixPayActivity.class);
                intent.putExtra(AlixPayActivity.ORDER_INFO, order.order_info);
                startActivityForResult(intent, E4_HistoryActivity.REQUEST_ALIPAY);
            }
        });

        alipayWapLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                orderModel.orderPay(Integer.parseInt(order.order_info.order_id));
            }
        });
    }

	
	
	
	private void setView (GOODORDER order){
		order_merk.setText("订单号 : "+ order.order_sn);
		balance_name.setText(order.addr_info.consignee);
		balance_phoneNum.setText(order.addr_info.mobile);
		balance_address.setText(order.addr_info.country_name + ", " +
								order.addr_info.province_name + ", " + 
								order.addr_info.city_name + ", " +
								order.addr_info.district_name + ", " +
								order.addr_info.address + ".");
		SharedPreferences shared = this.getSharedPreferences("userInfo", 0);
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		for (int i = 0; i < order.goods_list.size(); i++) {
			View view = LayoutInflater.from(this).inflate(R.layout.e7_order_goods_item, null);
			ImageView goods_img = (ImageView)  view.findViewById(R.id.goods_img);
			TextView goods_name = (TextView)  view.findViewById(R.id.goods_name);
			TextView goods_num = (TextView)  view.findViewById(R.id.goods_num);
			TextView goods_price = (TextView)  view.findViewById(R.id.goods_price);
			goods_name.setText(order.goods_list.get(i).name);
			goods_num.setText(order.goods_list.get(i).goods_number);
			goods_price.setText(order.goods_list.get(i).subtotal);
			
			String imageType = shared.getString("imageType", "mind");

			if (imageType.equals("high")) {
				imageLoader.displayImage(order.goods_list.get(i).img.thumb, goods_img, EcmobileApp.options);
			} else if (imageType.equals("low")) {
				imageLoader.displayImage(order.goods_list.get(i).img.small, goods_img, EcmobileApp.options);
			} else {
				String netType = shared.getString("netType", "wifi");
				if (netType.equals("wifi")) {
					imageLoader.displayImage(order.goods_list.get(i).img.thumb, goods_img, EcmobileApp.options);
				} else {
					imageLoader.displayImage(order.goods_list.get(i).img.small, goods_img, EcmobileApp.options);
				}
			}
			goods_list.addView(view);
		}
		
		balance_pay.setText(order.order_info.pay_name);
		send_information.setText("送货日期：节假日均可派送！");
		goods_totai_money.setText("" + order.goods_list.size());
		order_freight.setText(order.formated_shipping_fee);
		formated_integral_money.setText(order.formated_integral_money);
		formated_bonus.setText(order.formated_bonus);
		accounts_payable.setText(order.total_fee);
		order_time.setText(order.order_time.toString().subSequence(0, order.order_time.toString().length() - 5));
		
	}
	
	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
	//	 TODO Auto-generated method stub
        if (url.endsWith(ApiInterface.ORDER_CANCLE))
        {
        	Intent intent = new Intent(this, E4_HistoryActivity.class);
			intent.putExtra("flag", "await_pay");
			startActivityForResult(intent, 2);
            this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        	this.finish();
        } else if(url.endsWith(ApiInterface.ORDER_PAY))
        {
            String pay_wap= orderModel.pay_wap;
            String pay_online=orderModel.pay_online;
            String upop_tn=orderModel.upop_tn;
            if (upop_tn != null && !"".equals(upop_tn)) {
                //银联sdk支付
                UPPayAssistEx.startPayByJAR(E7_OrderDetailsActivity.this, PayActivity.class, null, null, upop_tn, "00");
            } else if (pay_wap != null && !"".equals(pay_wap)) {
                //wap支付
                Intent intent = new Intent(this, PayWebActivity.class);
                intent.putExtra(PayWebActivity.PAY_URL, pay_wap);
                startActivityForResult(intent, E4_HistoryActivity.REQUEST_Pay_Web);
            } else if (pay_online != null && !"".equals(pay_online)) {
                //其他方式
                Intent intent = new Intent(this, OtherPayWebActivity.class);
                intent.putExtra("html", pay_online);
                startActivity(intent);
            }
		} 
	}
	
	
}
