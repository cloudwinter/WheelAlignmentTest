package com.inprintech.wheelalignmenttest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.inprintech.wheelalignmenttest.Activity.DeviceDetailsActivity;
import com.inprintech.wheelalignmenttest.Activity.UserReportActivity;
import com.inprintech.wheelalignmenttest.DataBase.SQLiteDeviceHelpter;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.FilterIdListener;

import java.util.ArrayList;
import java.util.List;

public class DeviceAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private List<String> listItems = new ArrayList<>();
    private RecordFilter filter = null;
    private FilterIdListener listener = null;
    private boolean seeOrdel = false;
    SQLiteDeviceHelpter dbHelper;
    SQLiteDatabase db;
    private Cursor cursor;

    public DeviceAdapter(Context context, List<String> list, FilterIdListener filterListener, boolean seeOrdel) {
        this.mContext = context;
        this.listItems = list;
        this.listener = filterListener;
        this.seeOrdel = seeOrdel;

        dbHelper = new SQLiteDeviceHelpter(context, dbHelper.DB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();// 打开数据库
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.search_device_item, null);
            viewHolder.tvTitle = view.findViewById(R.id.tv_device_id);
            viewHolder.tvSee = view.findViewById(R.id.tv_see);
            viewHolder.imgDel = view.findViewById(R.id.img_del);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvTitle.setText(listItems.get(i));
        viewHolder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItems.remove(i);
                notifyDataSetChanged();
            }
        });

        viewHolder.tvSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double reada1 = 0, reada2 = 0, reada3 = 0, reada4 = 0, reada5 = 0;
                double readb1 = 0, readb2 = 0, readb3 = 0, readb4 = 0, readb5 = 0;
                String where = "device_id=" + listItems.get(i);
                cursor = db.query(dbHelper.TB_NAME, null, where, null, null, null, "_id ASC");
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    reada1 = cursor.getDouble(1);
                    reada2 = cursor.getDouble(2);
                    reada3 = cursor.getDouble(3);
                    reada4 = cursor.getDouble(4);
                    reada5 = cursor.getDouble(5);
                    readb1 = cursor.getDouble(6);
                    readb2 = cursor.getDouble(7);
                    readb3 = cursor.getDouble(8);
                    readb4 = cursor.getDouble(9);
                    readb5 = cursor.getDouble(10);
                    cursor.moveToNext();
                }
                Log.i("CalibrationActivity", "onClick: " + reada1);
                Intent intent = new Intent(mContext, DeviceDetailsActivity.class);
                intent.putExtra("reada1", reada1);
                intent.putExtra("reada2", reada2);
                intent.putExtra("reada3", reada3);
                intent.putExtra("reada4", reada4);
                intent.putExtra("reada5", reada5);
                intent.putExtra("readb1", readb1);
                intent.putExtra("readb2", readb2);
                intent.putExtra("readb3", readb3);
                intent.putExtra("readb4", readb4);
                intent.putExtra("readb5", readb5);
                mContext.startActivity(intent);
            }
        });

        viewHolder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.execSQL("delete from " + dbHelper.TB_NAME + " where register_num=?", new String[]{listItems.get(i)});
                listItems.remove(i);
                notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new RecordFilter(listItems);
        }
        return filter;
    }

    private class ViewHolder {
        TextView tvSee;
        TextView tvTitle;
        ImageView imgDel;
    }

    class RecordFilter extends Filter {
        // 创建集合保存原始数据
        private List<String> original = new ArrayList<>();

        public RecordFilter(List<String> list) {
            this.original = list;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (TextUtils.isEmpty(constraint)) {
                results.values = original;
                results.count = original.size();
            } else {
                List<String> mList = new ArrayList<>();
                for (String deviceId : original) {
                    if (deviceId.trim().toLowerCase().contains(constraint.toString().trim().toLowerCase())) {
                        mList.add(deviceId);
                    }
                }
                results.values = mList;
                results.count = mList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listItems = (List<String>) results.values;
            if (listener != null) {
                listener.getFilterData(listItems);
            }
            notifyDataSetChanged();
        }
    }
}
