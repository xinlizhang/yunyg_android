package com.insthub.ecmobile.activity;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.external.alipay.*;
import com.external.androidquery.callback.AjaxStatus;
import com.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.BeeFramework.model.BusinessResponse;
import com.insthub.BeeFramework.view.MyDialog;
import com.insthub.BeeFramework.view.ToastView;
import com.insthub.ecmobile.ECMobileAppConst;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.model.OrderModel;
import com.insthub.ecmobile.model.ProtocolConst;
import com.insthub.ecmobile.protocol.ORDER_INFO;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * ģ���̻�Ӧ�õ���Ʒ�б����ײ��衣
 * 
 * 1. ���̻�ID���տ��ʺţ��ⲿ�����ţ���Ʒ���ƣ���Ʒ���ܣ��۸�֪ͨ��ַ��װ�ɶ�����Ϣ 2. �Զ�����Ϣ����ǩ�� 3.
 * ��������Ϣ��ǩ����ǩ����ʽ��װ��������� 4. ����pay����
 * 
 * @version v4_0413 2012-03-02
 */
public class AlixPayActivity extends Activity  {
    private  static String TAG = "AppDemo";
    private ProgressDialog mProgress = null;
    private  ORDER_INFO order_info;
    private PartnerConfig partnerConfig;
    public static String ORDER_INFO="ONDER_INFO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        order_info=(ORDER_INFO)getIntent().getSerializableExtra(ORDER_INFO);
        partnerConfig = new PartnerConfig(this);
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");
        registerReceiver(mPackageInstallationListener, filter);
        performPay();


    }

    private BroadcastReceiver mPackageInstallationListener = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String packageName = intent.getDataString();
            if (!TextUtils
                    .equals(packageName, "package:com.alipay.android.app")) {
                return;
            }
            performPay();
        }
    };


    /**
     * get the selected order info for pay. ��ȡ��Ʒ������Ϣ
     *
     * @param
     * @return
     */

    String getOrderInfo() {
        String strOrderInfo = "partner=" + "\"" + partnerConfig.PARTNER + "\"";
        strOrderInfo += "&";
        strOrderInfo += "seller=" + "\"" + partnerConfig.SELLER + "\"";
        strOrderInfo += "&";
        strOrderInfo += "out_trade_no=" + "\"" + order_info.order_sn + "\"";
        strOrderInfo += "&";
        strOrderInfo += "subject=" + "\"" + order_info.subject
                + "\"";
        strOrderInfo += "&";
        strOrderInfo += "body=" + "\"" + order_info.desc + "\"";
        strOrderInfo += "&";
        strOrderInfo += "total_fee=" + "\""
                + order_info.order_amount + "\"";
        strOrderInfo += "&";
        strOrderInfo += "notify_url=" + "\""
                + partnerConfig.ALIPAY_CALLBACK + "\"";

        return strOrderInfo;
    }
    /**
     * sign the order info. �Զ�����Ϣ����ǩ��
     *
     * @param signType ǩ����ʽ
     * @param content  ��ǩ��������Ϣ
     * @return
     */
    String sign(String signType, String content) {
        return Rsa.sign(content, partnerConfig.RSA_PRIVATE);
    }

    /**
     * get the sign type we use. ��ȡǩ����ʽ
     *
     * @return
     */
    String getSignType() {
        String getSignType = "sign_type=" + "\"" + "RSA" + "\"";
        return getSignType;
    }
    protected void performPay() {
        //
        // check to see if the MobileSecurePay is already installed.
        // ��ⰲȫ֧�������Ƿ�װ
        MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this, order_info);
        boolean isMobile_spExist = mspHelper.detectMobile_sp();
        if (!isMobile_spExist) {
            return;
        }

        // check some info.
        // ���������Ϣ
        if (!checkInfo()) {
            BaseHelper
                    .showDialog(
                            AlixPayActivity.this,
                            getResources().getString(R.string.prompt),
                            "ȱ��partner����seller������src/com/alipay/android/appDemo4/PartnerConfig.java�����ӡ�",
                            R.drawable.infoicon);
            return;
        }

        // start pay for this order.
        // ���ݶ�����Ϣ��ʼ����֧��
        try {
            // prepare the order info.
            // ׼��������Ϣ
            String orderInfo = getOrderInfo();
            // �������ǩ����ʽ�Զ�����Ϣ����ǩ��
            String signType = getSignType();
            String strsign = sign(signType, orderInfo);
            // ��ǩ�����б���
            strsign = URLEncoder.encode(strsign, "UTF-8");
            // ��װ�ò���
            //getOrderInfo
            String info = orderInfo + "&sign=" + "\"" + strsign + "\"" + "&"
                    + getSignType();
            // start the pay.
            // ����pay��������֧��
            MobileSecurePayer msp = new MobileSecurePayer();
            boolean bRet = msp.pay(info, mHandler, AlixId.RQF_PAY, this);

            if (bRet) {
                // show the progress bar to indicate that we have started
                // paying.
                // ��ʾ������֧����������
                closeProgress();
                mProgress = BaseHelper.showProgress(this, null, "����֧��", false,
                        true);
            } else
                ;
        } catch (Exception ex) {
            Toast.makeText(AlixPayActivity.this, R.string.remote_call_failed,
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * check some info.the partner,seller etc. ���������Ϣ
     * partnerid�̻�id��seller�տ��ʺŲ���Ϊ��
     *
     * @return
     */
    private boolean checkInfo() {
        String partner = partnerConfig.PARTNER;
        String seller = partnerConfig.SELLER;
        if (partner == null || partner.length() <= 0 || seller == null
                || seller.length() <= 0)
            return false;

        return true;
    }

    //
    // the handler use to receive the pay result.
    // �������֧�������֧�����ֻ���ͬ��֪ͨ
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                String ret = (String) msg.obj;
                switch (msg.what) {
                    case AlixId.RQF_PAY: {
                        Resources resource = (Resources) getBaseContext().getResources();
                        //
                        closeProgress();

                        BaseHelper.log(TAG, ret);

                        // �����׽��
                        try {
                            // ��ȡ����״̬�룬����״̬������ο��ĵ�
                            String tradeStatus = "resultStatus={";
                            int imemoStart = ret.indexOf("resultStatus=");
                            imemoStart += tradeStatus.length();
                            int imemoEnd = ret.indexOf("};memo=");
                            tradeStatus = ret.substring(imemoStart, imemoEnd);

                            // ����ǩ֪ͨ
                            ResultChecker resultChecker = new ResultChecker(ret);
                            int retVal = resultChecker.checkSign(AlixPayActivity.this);
                            // ��ǩʧ��
                            if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {
                                BaseHelper.showDialog(
                                        AlixPayActivity.this,
                                        getResources().getString(R.string.prompt),
                                        getResources().getString(
                                                R.string.check_sign_failed),
                                        android.R.drawable.ic_dialog_alert
                                );
                            } else {// ��ǩ�ɹ�����ǩ�ɹ������жϽ���״̬��
                                if (tradeStatus.equals("9000"))// �жϽ���״̬�룬ֻ��9000��ʾ���׳ɹ�
                                {
                                    Intent data=new Intent();
                                    data.putExtra("pay_result", "success");
                                    setResult(Activity.RESULT_OK, data);
                                    finish();
                                } else {
                                    Intent data=new Intent();
                                    data.putExtra("pay_result", "fail");
                                    setResult(Activity.RESULT_OK, data);
                                    finish();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            BaseHelper.showDialog(AlixPayActivity.this, getResources().getString(R.string.prompt), ret,
                                    R.drawable.infoicon);
                        }
                    }
                    break;
                }

                super.handleMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

	/**
	 * the OnCancelListener for lephone platform. lephoneϵͳʹ�õ���ȡ��dialog����
	 */
	public static class AlixOnCancelListener implements
			DialogInterface.OnCancelListener {
		Activity mcontext;

		public AlixOnCancelListener(Activity context) {
			mcontext = context;
		}

		public void onCancel(DialogInterface dialog) {
			mcontext.onKeyDown(KeyEvent.KEYCODE_BACK, null);
		}
	}
	// close the progress bar
	// �رս��ȿ�
	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//
	@Override
	public void onDestroy() {
		super.onDestroy();

		unregisterReceiver(mPackageInstallationListener);

		Log.v(TAG, "onDestroy");

		try {
			mProgress.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
    }
    public void startActivityForResult(Intent intent, int requestCode)
    {
        super.startActivityForResult(intent,requestCode);
        overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
    }
}
