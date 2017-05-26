package com.insthub.ecmobile.model;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.insthub.ecmobile.EcmobileManager;
import com.insthub.ecmobile.R;

public class VoiceSearchModel {
	private Context mContext;
	// 语音听写对象
	private SpeechRecognizer mIat;
	// 语音听写UI
	private RecognizerDialog mIatDialog;
	// 用HashMap存储听写结果
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
	private Toast mToast;
    private VoiceSearch voicesearch;
    
    public VoiceSearchModel(Context context, VoiceSearch v){
    	this.mContext = context;
    	this.voicesearch = v ; 
    }
    
    
	public void showRecognizerDialog() {
		SpeechUtility.createUtility(mContext, SpeechConstant.APPID + 
				EcmobileManager.getXunFeiCode(mContext));
		
		// 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
		// 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
		mIatDialog = new RecognizerDialog(mContext, mInitListener);
		
		mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
		
		// 初始化识别无UI识别对象
		// 使用SpeechRecognizer对象，可根据回调消息自定义界面；
		mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);
		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE,  SpeechConstant.TYPE_CLOUD);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
		mIat.setParameter(SpeechConstant.DOMAIN, "iat");
		mIat.setParameter(SpeechConstant.ASR_PTT, "0");
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
		mIat.startListening(new RecognizerListener() {
			//音量值0-30
			@Override
			public void onVolumeChanged(int volume) {
				// TODO Auto-generated method stub
				showTip("当前正在说话，音量大小：" + volume);
			}
			
			//一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
			//关于解析Json的代码可参见MscDemo中JsonParser类；
			//isLast等于true时会话结束。
			@Override
			public void onResult(RecognizerResult results, boolean isLast) {
				// TODO Auto-generated method stub
				printResult(results);
				if (isLast) {
					// TODO 最后的结果
				}
				
			}
			//扩展用借口
			@Override
			public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
				// TODO Auto-generated method stub
				
			}
			
			// 会话发生错误回调接口
			@Override
			public void onError(SpeechError arg0) {
				// TODO Auto-generated method stub
				// Tips：
				// 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
				// 如果使用本地功能（语音+）需要提示用户开启语音+的录音权限。
				arg0.getPlainDescription(true);//或得错误码描述
			}
			//结束录音
			@Override
			public void onEndOfSpeech() {
				// TODO Auto-generated method stub
				showTip("结束说话");
			}
			//开始录音
			@Override
			public void onBeginOfSpeech() {
				// TODO Auto-generated method stub
				showTip("开始说话");
			}
		});
		
		// 显示听写对话框
		mIatDialog.setListener(recognizerDialogListener);
		mIatDialog.show();
		showTip(mContext.getString(R.string.text_begin));
	}
	
	
	private void printResult(RecognizerResult results) {
		try {
			String text = JsonParser.parseIatResult(results.getResultString());
			String sn = null;
			
			// 读取json结果中的sn字段
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");

			mIatResults.put(sn, text);

			StringBuffer resultBuffer = new StringBuffer();
			for (String key : mIatResults.keySet()) {
				resultBuffer.append(mIatResults.get(key));
			}
			voicesearch.VoiceSearchjump(mContext, resultBuffer.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * 听写UI监听器
	 */
	private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			printResult(results);
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}
	};
	
	
	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
				showTip("初始化失败，错误码：" + code);
			}
		}
	};
	
	
	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}
	
	public interface VoiceSearch {
		public abstract void VoiceSearchjump (Context context, String resultBuffer);
	}
	
}
