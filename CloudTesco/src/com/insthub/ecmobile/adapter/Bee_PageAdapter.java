package com.insthub.ecmobile.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class Bee_PageAdapter extends PagerAdapter
{
    public List<View> mListViews;

    public Bee_PageAdapter(List<View> mListViews)
    {
        this.mListViews = mListViews;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) 	{
        container.removeView(mListViews.get(position));//ɾ��ҳ��
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {	//�����������ʵ����ҳ��
        container.addView(mListViews.get(position), 0);//���ҳ��
        return mListViews.get(position);
    }

    @Override
    public int getCount() {
        return  mListViews.size();//����ҳ��������
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;//�ٷ���ʾ����д
    }

}
