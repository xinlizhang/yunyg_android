package com.insthub.ecmobile.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.insthub.BeeFramework.adapter.BeeBaseAdapter;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.component.CategorySellingCell;
import com.insthub.ecmobile.component.HomeBestGoodsCell;
import com.insthub.ecmobile.component.HomeHotGoodsCell;
import com.insthub.ecmobile.component.HomeNewGoodsCell;
import com.insthub.ecmobile.component.LimitTimeDiscountCell;
import com.insthub.ecmobile.component.PayAreaCell;
import com.insthub.ecmobile.model.HomeModel;
import com.insthub.ecmobile.protocol.CATEGORY;
import com.insthub.ecmobile.protocol.SIMPLEGOODS;

public class B0_IndexAdapter extends BeeBaseAdapter 
{
	//却分布局显示
	protected static final int TYPE_RECHARGE = 0;//充值区 
	protected static final int TYPE_LIMITTIME = 1;//限时打折
	protected static final int TYPE_HOMEHOT = 2;//热门商品
	protected static final int TYPE_CATEGORYSELL = 3;//普通商品
	protected static final int TYPE_HOMEBEST = 4;//推荐商品
	protected static final int TYPE_HOMENEW = 5;//新品上架
	protected static final int TYPE_HELPSELL = 6;
	
	
	private HomeModel dataModel;
	private final int LINE_NUMBER = 1 ; //行数	
	private int RECHARGE;//C充值区        （充值区暂定位）
	private int LIMIT_TIME_GOODS;//限时打折
	private int HOME_HOT_GOODS;//热门商品
	private int HOME_BEST_GOODS;//推荐商品
	private int HOME_NEW_GOODS;//新品上架
	private int COMMODITY_TYPE;//商品类型 
	
	
	public B0_IndexAdapter(Context c, ArrayList dataList) {
		super(c, dataList);
	}
	
	public B0_IndexAdapter(Context c, HomeModel dataModel) {
		super(c, dataModel.playersList);
		this.dataModel = dataModel;
		RECHARGE = 1;
		LIMIT_TIME_GOODS = RECHARGE + (int)Math.ceil(dataModel.simplegoodsList.size() > 0 ? dataModel.simplegoodsList.size() / dataModel.simplegoodsList.size() : 0);/*(int)Math.ceil(dataModel.simplegoodsList.size()*1.0/2)*/;
		HOME_HOT_GOODS = LIMIT_TIME_GOODS + (int)Math.ceil(dataModel.homehotgoodsList.size() > 0 ? dataModel.homehotgoodsList.size() / dataModel.homehotgoodsList.size() : 0); 
		HOME_BEST_GOODS = HOME_HOT_GOODS + (int)Math.ceil(dataModel.homebestgoodsList.size() > 0 ? dataModel.homebestgoodsList.size() / dataModel.homebestgoodsList.size() : 0);
		HOME_NEW_GOODS = HOME_BEST_GOODS + (int)Math.ceil(dataModel.homenewgoodsList.size() > 0 ? dataModel.homenewgoodsList.size() / dataModel.homenewgoodsList.size() : 0) ;
		COMMODITY_TYPE = HOME_NEW_GOODS + (int)Math.ceil(dataModel.categorygoodsList.size() > 0 ? dataModel.categorygoodsList.size() / dataModel.categorygoodsList.size() : 0);
		
//		ROWS_NUMBER_GOODS = (int)Math.ceil(dataModel.simplegoodsList.size()*1.0/2);
//		COMMODITY_TYPE = COMMODITY_TYPE;
//		LIMIT_TIME_GOODS = RECHARGE + ROWS_NUMBER_GOODS + COMMODITY_TYPE + RECHARGE ;
	}

	@Override
	protected BeeCellHolder createCellHolder(View cellView) {		
		return null;
	}
	
    @Override
    public int getViewTypeCount() {
        
        return 1000;
    }
    
    @Override
    public int getCount() 
    {
    	return COMMODITY_TYPE/*RECHARGE + ROWS_NUMBER_GOODS  + dataModel.categorygoodsList.size()*/;
    }
    
    public int getItemViewType(int position)
    {    	
        return position;
    }


    public int getItemViewRealType(int position)
    {
    	if(position < RECHARGE)
    	{
    		return TYPE_RECHARGE; 
    	}
    	else if(position < LIMIT_TIME_GOODS)
    	{
    	     return TYPE_LIMITTIME;
    	}
    	else if(position < HOME_HOT_GOODS)
    	{
    		return TYPE_HOMEHOT;
    	}
    	else if(position < HOME_BEST_GOODS)
    	{
    		return TYPE_HOMEBEST ; 
    	}
    	else if(position < HOME_NEW_GOODS)
    	{
    		return TYPE_HOMENEW ; 
    	}
    	else if(position < COMMODITY_TYPE)
    	{
            return TYPE_CATEGORYSELL;
    	}
    	else
    	{
    		return TYPE_HELPSELL;
    	}

    }

    @Override
    public long getItemId(int position) {
        if (position < LIMIT_TIME_GOODS)
        {
            return position;
        }
        else  if (position < LIMIT_TIME_GOODS + COMMODITY_TYPE)
        {
            return position - LIMIT_TIME_GOODS;
        }
        else
        {
            return position - LIMIT_TIME_GOODS- COMMODITY_TYPE;
        }
    }

    @Override
	protected View bindData(int position, View cellView, ViewGroup parent,
			BeeCellHolder h) {		
		return null;
	}

	@Override
	public View createCellView() {		
		return null;
	}
    @Override
    public View getView(final int position, View cellView, ViewGroup parent) {

        BeeCellHolder holder = null;
        int type = getItemViewRealType(position) ; 
        if(TYPE_RECHARGE== type)
        {
        	if (null == cellView || cellView.getClass()!= PayAreaCell.class)
            {
                cellView = (PayAreaCell)LayoutInflater.from(mContext).inflate(R.layout.b0_pay_area_cell, null);
                cellView.setBackgroundResource(R.drawable.body_cont_bg);
            }
            else
            {
                return cellView;
            }
            ((PayAreaCell)cellView).bindData(dataModel.simplegoodsList);
        }
        else if (TYPE_LIMITTIME== type)
        {
            if (null == cellView || cellView.getClass()!= LimitTimeDiscountCell.class)
            {
                cellView = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.b0_limittime_discount_cell, null);
            }
            else
            {
                return cellView;
            }
            ((LimitTimeDiscountCell)cellView).bindData(dataModel.simplegoodsList);
		}    
        else if(TYPE_HOMEHOT == type)
        {
        	if (null == cellView || cellView.getClass() != HomeHotGoodsCell.class)
        	{
        		cellView = (HomeHotGoodsCell)LayoutInflater.from(mContext).inflate(R.layout.b0_home_hot_cell, null);
        	}
        	else
        	{
        		return cellView;
        	}
        	int realPosition = position - TYPE_HOMEHOT;
        	((HomeHotGoodsCell)cellView).bindData(dataModel.homehotgoodsList);
        }
        else if(TYPE_HOMEBEST == type)
        {
            if (null == cellView || cellView.getClass()!= HomeBestGoodsCell.class)
            {
                cellView = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.b0_home_best_goods_cell, null);
            }
            else
            {
                return cellView;
            }
            ((HomeBestGoodsCell)cellView).bindData(dataModel.homebestgoodsList);
        	
        }
        else if(TYPE_HOMENEW == type)
        {
         	if (null == cellView || cellView.getClass() != HomeNewGoodsCell.class)
        	{
        		cellView = (HomeNewGoodsCell)LayoutInflater.from(mContext).inflate(R.layout.b0_home_new_cell, null);
        	}
        	else
        	{
        		return cellView;
        	}
        	int realPosition = position - TYPE_HOMEHOT;
        	((HomeNewGoodsCell)cellView).bindData(dataModel.homenewgoodsList);
        }
        else if (TYPE_CATEGORYSELL == type)
        {
            if (null == cellView || cellView.getClass()!= CategorySellingCell.class)
            {
                cellView = (CategorySellingCell)LayoutInflater.from(mContext).inflate(R.layout.b0_index_category_cell, null);
            }
            else
            {
                return cellView;
            }
            List<SIMPLEGOODS> itemList = null;
            int realPosition = position - HOME_BEST_GOODS;
        	((CategorySellingCell)cellView).bindData( dataModel.categorygoodsList);
		}
       
        return cellView;
    }

}
