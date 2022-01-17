package com.dejia.anju.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.model.CityInfo;

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
        viewHolder.groupNameTV.setText(cityList.get(position).getName());
        return convertView;
    }

}
