package com.insthub.ecmobile.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.insthub.BeeFramework.adapter.BeeBaseAdapter;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.component.MoreGoodsListCell;
import com.insthub.ecmobile.component.PromoteGoodsListCell;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.insthub.ecmobile.protocol.SIMPLEGOODS;

/**
 * @author
 *
 */
@SuppressLint("NewApi") 
public class B1_ProductListAdapter extends BeeBaseAdapter {
	private String Product_Types ; 

	public B1_ProductListAdapter(Context c, ArrayList dataList) 
	{
		super(c, dataList);
	}
	
	public B1_ProductListAdapter(Context c, ArrayList dataList, String product_types) 
	{
		super(c, dataList);
		this.Product_Types = product_types ;
	}

	
	@Override
	public int getCount() 
	{	
		return dataList.size();
	}
    public int getItemViewType(int position)
    {
        return  1;
    }


    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
	public Object getItem(int position) {		
		return null;
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	protected View bindData(int position, View cellView, ViewGroup parent, BeeCellHolder h) 
	{
		if( (Product_Types != null && !Product_Types.isEmpty()) && Product_Types.equals(ApiInterface.LIST_PROMOTE))
		{
			((PromoteGoodsListCell)cellView).bindData((SIMPLEGOODS)dataList.get(position));
		}
		else 
		{
			((MoreGoodsListCell)cellView).bindData((SIMPLEGOODS)dataList.get(position));
		}
		return null;
	}

	@Override
	public View createCellView() 
	{		
		if((Product_Types != null && !Product_Types.isEmpty()) && Product_Types.equals(ApiInterface.LIST_PROMOTE))
		{
			return mInflater.inflate(R.layout.b1_promote_goods_list_cell, null);
		}
		else 
		{
			return mInflater.inflate(R.layout.b1_more_goods_list_cell, null);
		}
	}

	@Override
	protected BeeCellHolder createCellHolder(View cellView) 
	{
        BeeCellHolder holder = new BeeCellHolder();

		return holder;
	}

    public View getView(int position, View cellView, ViewGroup parent) 
    {
        BeeCellHolder holder = null;
        if (cellView == null )
        {
            cellView = createCellView();
            holder = createCellHolder(cellView);
            if (null != holder)
            {
                cellView.setTag(holder);
            }

        }
        else
        {
            holder = (BeeCellHolder)cellView.getTag();
            if (holder == null)
            {
                Log.v("lottery", "error");
            }
            else
            {
                Log.d("ecmobile", "last position" + holder.position +"    new position" + position+"\n");
            }
        }

        if(null != holder)
        {
            holder.position = position;
        }

        bindData(position, cellView, parent, holder);
        return cellView;
    }

}
