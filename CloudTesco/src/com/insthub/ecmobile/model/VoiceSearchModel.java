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
	// ������д����
	private SpeechRecognizer mIat;
	// ������дUI
	private RecognizerDialog mIatDialog;
	// ��HashMap�洢��д���
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
		
		// ��ʼ����дDialog�����ֻʹ����UI��д���ܣ����贴��SpeechRecognizer
		// ʹ��UI��д���ܣ������sdk�ļ�Ŀ¼�µ�notice.txt,���ò����ļ���ͼƬ��Դ
		mIatDialog = new RecognizerDialog(mContext, mInitListener);
		
		mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
		
		// ��ʼ��ʶ����UIʶ�����
		// ʹ��SpeechRecognizer���󣬿ɸ��ݻص���Ϣ�Զ�����棻
		mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);
		// ������д����
		mIat.setParameter(SpeechConstant.ENGINE_TYPE,  SpeechConstant.TYPE_CLOUD);
		// ���÷��ؽ����ʽ
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
		mIat.setParameter(SpeechConstant.DOMAIN, "iat");
		mIat.setParameter(SpeechConstant.ASR_PTT, "0");
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
		mIat.startListening(new RecognizerListener() {
			//����ֵ0-30
			@Override
			public void onVolumeChanged(int volume) {
				// TODO Auto-generated method stub
				showTip("��ǰ����˵����������С��" + volume);
			}
			
			//һ������»�ͨ��onResults�ӿڶ�η��ؽ����������ʶ�������Ƕ�ν�����ۼӣ�
			//���ڽ���Json�Ĵ���ɲμ�MscDemo��JsonParser�ࣻ
			//isLast����trueʱ�Ự������
			@Override
			public void onResult(RecognizerResult results, boolean isLast) {
				// TODO Auto-generated method stub
				printResult(results);
				if (isLast) {
					// TODO ���Ľ��
				}
				
			}
			//��չ�ý��
			@Override
			public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
				// TODO Auto-generated method stub
				
			}
			
			// �Ự��������ص��ӿ�
			@Override
			public void onError(SpeechError arg0) {
				// TODO Auto-generated method stub
				// Tips��
				// �����룺10118(��û��˵��)��������¼����Ȩ�ޱ�������Ҫ��ʾ�û���Ӧ�õ�¼��Ȩ�ޡ�
				// ���ʹ�ñ��ع��ܣ�����+����Ҫ��ʾ�û���������+��¼��Ȩ�ޡ�
				arg0.getPlainDescription(true);//��ô���������
			}
			//����¼��
			@Override
			public void onEndOfSpeech() {
				// TODO Auto-generated method stub
				showTip("����˵��");
			}
			//��ʼ¼��
			@Override
			public void onBeginOfSpeech() {
				// TODO Auto-generated method stub
				showTip("��ʼ˵��");
			}
		});
		
		// ��ʾ��д�Ի���
		mIatDialog.setListener(recognizerDialogListener);
		mIatDialog.show();
		showTip(mContext.getString(R.string.text_begin));
	}
	
	
	private void printResult(RecognizerResult results) {
		try {
			String text = JsonParser.parseIatResult(results.getResultString());
			String sn = null;
			
			// ��ȡjson����е�sn�ֶ�
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
	 * ��дUI������
	 */
	private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			printResult(results);
		}

		/**
		 * ʶ��ص�����.
		 */
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}
	};
	
	
	/**
	 * ��ʼ����������
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
				showTip("��ʼ��ʧ�ܣ������룺" + code);
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
