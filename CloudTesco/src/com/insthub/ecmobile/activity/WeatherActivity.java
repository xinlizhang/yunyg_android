package com.insthub.ecmobile.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.ecmobile.R;

public class WeatherActivity extends BaseActivity  {
	private ImageView top_view_back; 
	private TextView top_view_text; 
	private WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logistics_query);
		webView = (WebView) findViewById(R.id.webview);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		WebSettings settings = webView.getSettings();
		settings.setUseWideViewPort(false);
		settings.setLoadWithOverviewMode(true);
		settings.setSupportZoom(false);
		settings.setJavaScriptEnabled(true);
		
		
/*		//支持javascript
		webView.getSettings().setJavaScriptEnabled(true); 
		// 设置可以支持缩放 
		webView.getSettings().setSupportZoom(true); 
		// 设置出现缩放工具 
		webView.getSettings().setBuiltInZoomControls(true);
		//扩大比例的缩放
		webView.getSettings().setUseWideViewPort(true);
		//自适应屏幕
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setLoadWithOverviewMode(true);*/
		
//		webView.loadUrl("http://m.kuaidi100.com/index_all.html");
//		webView.loadUrl("http://www.weather.com.cn/weather/101060101.shtml");
		webView.loadUrl("http://i.tianqi.com/index.php?c=code&id=19&py=huludao");
		
		top_view_back = (ImageView) findViewById(R.id.top_view_back);
		top_view_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				WeatherActivity.this.finish();
			}
		});
		
		top_view_text= (TextView) findViewById(R.id.top_view_text);
		top_view_text.setText("天气预报");
	}
	
	
	/*public static String getPinYin(String src) {   
        char[] t1 = null;   
        t1 = src.toCharArray();    
        // System.out.println(t1.length);   
        String[] t2 = new String[t1.length];   
        // System.out.println(t2.length);   
        // 设置汉字拼音输出的格式    
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();   
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);    
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);    
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);    
        String t4 = "";    
        int t0 = t1.length;   
        try {    
            for (int i =0; i < t0; i++) {    
                // 判断能否为汉字字符    
                // System.out.println(t1[i]);   
               if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {   
                   t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// 将汉字的几种全拼都存到t2数组中   
                    t4 += t2[0];// 取出该汉字全拼的第一种读音并连接到字符串t4后   
               } else {   
                   // 如果不是汉字字符，间接取出字符并连接到字符串t4后   
                    t4 += Character.toString(t1[i]);    
                }    
            }    
       } catch (BadHanyuPinyinOutputFormatCombination e) {   
           e.printStackTrace();    
        }    
       return t4;    
    }   
	*/
	
	
}
