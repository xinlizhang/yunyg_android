package com.insthub.ecmobile;

public class ErrorCodeConst
{
    public final static int ResponseSucceed = 1;//����ɹ�
    public final static int InvalidUsernameOrPassword = 6;//�û������������
    public final static int ProcessFailed = 8;//����ʧ��
    public final static int UserOrEmailExist = 11; //�û�����email��ʹ��
    public final static int UnexistInformation = 13;//�����ڵ���Ϣ
    public final static int BuyFailed = 14;//����ʧ��

    public final static int InvalidSession = 100;//session ������
    public final static int InvalidParameter = 101;//����Ĳ����ύ

    public final static int InvalidPagination = 501;//û��pagination�ṹ
    public final static int InvalidCode = 502;//code����
    public final static int CodeExpire  = 503;//��ͬ����ֹ

    public final static int SelectedDeliverMethod = 10001;//������ѡ��һ�����ͷ�ʽ
    public final static int NoGoodInCart          = 10002;//���ﳵ��û����Ʒ
    public final static int InsufficientBalance   = 10003;//����������֧��������������ѡ������֧����ʽ
    public final static int InsufficientNumberOfPackage = 10005;//��ѡ��ĳ�ֵ��������Ѿ�������档�������ٹ���������ϵ�̼�
    public final static int CashOnDeliveryDisable       = 10006;//������Ź����ұ�֤�����0������ʹ�û�������
    public final static int AlreadyCollected       = 10007;//�����ղع�����Ʒ
    public final static int InventoryShortage       = 10008;//��治��
    public final static int NoShippingInformation       = 10009;//�����޷�����Ϣ


}
