package com.insthub.ecmobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.ecmobile.R;

public class G4_SpecialStatementActivity extends BaseActivity{
	private TextView top_view_text; 
	private ImageView back;
	private TextView text ; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.g4_specialstatement);
		back = (ImageView) findViewById(R.id.top_view_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();				
			}
		});
		top_view_text = (TextView) this.findViewById(R.id.top_view_text);
		top_view_text.setText(getBaseContext().getResources().getString(R.string.statement));
		
		text = (TextView) this.findViewById(R.id.text);
		text.setText("��׹��ڴ���������ͨ��������μӵ���ҵ����뱾app�޹ء�");
		
	}
	
}
