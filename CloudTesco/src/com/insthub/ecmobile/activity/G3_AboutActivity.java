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

public class G3_AboutActivity extends BaseActivity implements OnClickListener{
	private ImageView back;
	private RelativeLayout statement ; //特别声明
	private RelativeLayout use_help ; //使用帮助
	private RelativeLayout cive_conment ; //给我评论
	private TextView top_view_text; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.g3_about);
		back = (ImageView) findViewById(R.id.top_view_back);
		back.setOnClickListener(this);
		top_view_text = (TextView) this.findViewById(R.id.top_view_text);
		statement = (RelativeLayout) this.findViewById(R.id.statement_layout);
		use_help = (RelativeLayout) this.findViewById(R.id.use_help_layout);
		cive_conment = (RelativeLayout) this.findViewById(R.id.cive_conment_layout);
		statement.setOnClickListener(this);
		use_help.setOnClickListener(this);
		cive_conment.setOnClickListener(this);
		top_view_text.setText(getBaseContext().getResources().getString(R.string.myapp_about));
	}
	
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent ; 
		switch (arg0.getId()) {
		case R.id.top_view_back:
			finish();
			break;
		case R.id.statement_layout://特别声明
			intent = new Intent(this, G4_SpecialStatementActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case R.id.use_help_layout://使用帮助
			intent = new Intent(this, G2_InfoActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case R.id.cive_conment_layout://给我评论
			
			break;
		default:
			break;
		}
	}
}
