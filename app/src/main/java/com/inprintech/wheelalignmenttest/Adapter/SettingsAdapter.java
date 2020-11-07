package com.inprintech.wheelalignmenttest.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inprintech.wheelalignmenttest.R;

import java.util.List;
import java.util.Map;

/**
 * 系统设置中ListView的适配器
 */
public class SettingsAdapter extends BaseAdapter {

    private Context mContext;
    private List<Map<String, ?>> listItems;

    public SettingsAdapter(Context context, List<Map<String, ?>> list) {
        this.mContext = context;
        this.listItems = list;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int i) {
        return listItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.settings_item, null);
            viewHolder.imgIcon = view.findViewById(R.id.img_settings_icon);
            viewHolder.tvTitle = view.findViewById(R.id.tv_settings_title);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.imgIcon.setImageResource((Integer) this.listItems.get(i).get("imgIcon"));
        viewHolder.tvTitle.setText(this.listItems.get(i).get("tvTitle").toString());
        return view;
    }

    private class ViewHolder {
        ImageView imgIcon;
        TextView tvTitle;
    }
}
