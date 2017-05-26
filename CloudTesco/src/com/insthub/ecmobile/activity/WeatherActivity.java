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
		
		
/*		//֧��javascript
		webView.getSettings().setJavaScriptEnabled(true); 
		// ���ÿ���֧������ 
		webView.getSettings().setSupportZoom(true); 
		// ���ó������Ź��� 
		webView.getSettings().setBuiltInZoomControls(true);
		//�������������
		webView.getSettings().setUseWideViewPort(true);
		//����Ӧ��Ļ
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
		top_view_text.setText("����Ԥ��");
	}
	
	
	/*public static String getPinYin(String src) {   
        char[] t1 = null;   
        t1 = src.toCharArray();    
        // System.out.println(t1.length);   
        String[] t2 = new String[t1.length];   
        // System.out.println(t2.length);   
        // ���ú���ƴ������ĸ�ʽ    
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();   
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);    
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);    
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);    
        String t4 = "";    
        int t0 = t1.length;   
        try {    
            for (int i =0; i < t0; i++) {    
                // �ж��ܷ�Ϊ�����ַ�    
                // System.out.println(t1[i]);   
               if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {   
                   t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// �����ֵļ���ȫƴ���浽t2������   
                    t4 += t2[0];// ȡ���ú���ȫƴ�ĵ�һ�ֶ��������ӵ��ַ���t4��   
               } else {   
                   // ������Ǻ����ַ������ȡ���ַ������ӵ��ַ���t4��   
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
