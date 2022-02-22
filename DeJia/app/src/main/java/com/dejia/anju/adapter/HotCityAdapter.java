package com.dejia.anju.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.base.Constants;
import com.dejia.anju.model.CityInfo;
import com.dejia.anju.utils.KVUtils;

import java.util.List;

/**
 * @author ych
 */
public class HotCityAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<CityInfo.HotCity> cityList;
    private ViewHolder viewHolder;

    public HotCityAdapter(Context mContext, List<CityInfo.HotCity> cityList) {
        this.mContext = mContext;
        this.cityList = cityList;
        inflater = LayoutInflater.from(mContext);
    }

    static class ViewHolder {
        public TextView groupNameTV;
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public Object getItem(int position) {
        return cityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_pop_home_xiala2, null);
            viewHolder = new ViewHolder();
            viewHolder.groupNameTV = convertView.findViewById(R.id.home_pop_part_name_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(cityList.get(position).getIs_start_using() == 1){
            //可用
            if(cityList.get(position).getName().equals(KVUtils.getInstance().decodeString(Constants.DWCITY,"北京"))){
                //蓝色
                viewHolder.groupNameTV.setTextColor(Color.parseColor("#0095FF"));
                viewHolder.groupNameTV.setBackgroundResource(R.drawable.shape_city2);
            }else{
                //黑色
                viewHolder.groupNameTV.setTextColor(Color.parseColor("#1C2125"));
                viewHolder.groupNameTV.setBackgroundResource(R.drawable.shape_city);
            }
        }else{
            //不可用
            viewHolder.groupNameTV.setTextColor(Color.parseColor("#D7D8D9"));
            viewHolder.groupNameTV.setBackgroundResource(R.drawable.shape_city3);
        }
        viewHolder.groupNameTV.setText(cityList.get(position).getName());
        return convertView;
    }

}
