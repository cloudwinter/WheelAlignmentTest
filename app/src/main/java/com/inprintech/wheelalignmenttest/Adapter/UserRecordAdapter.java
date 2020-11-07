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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.inprintech.wheelalignmenttest.Activity.UserRecordActivity;
import com.inprintech.wheelalignmenttest.Activity.UserReportActivity;
import com.inprintech.wheelalignmenttest.DataBase.SQLiteHelpter;
import com.inprintech.wheelalignmenttest.DataBase.SQLiteSelMeasureHelpter;
import com.inprintech.wheelalignmenttest.Model.UserRecord;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.ActivityCollector;
import com.inprintech.wheelalignmenttest.Utils.FilterListener;
import com.inprintech.wheelalignmenttest.Utils.UITools;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户账号的适配器
 */
public class UserRecordAdapter extends BaseAdapter implements Filterable {
    private static final String TAG = "UserRecordAdapter";

    private Context context;
    private List<UserRecord> recordList = new ArrayList<>();
    private RecordFilter filter = null;
    private FilterListener listener = null;
    public boolean seeOrdel = false;
    SQLiteHelpter dbHelper;
    SQLiteSelMeasureHelpter selMeasureHelpter;
    SQLiteDatabase db;
    SQLiteDatabase dbsel;
    private Cursor cursor;
    private Cursor cursorSel;

    public UserRecordAdapter(Context context, List<UserRecord> list, FilterListener filterListener) {
        this.context = context;
        this.recordList = list;
        this.listener = filterListener;

        selMeasureHelpter = new SQLiteSelMeasureHelpter(context, selMeasureHelpter.DB_NAME, null, 1);
        dbHelper = new SQLiteHelpter(context, dbHelper.DB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();// 打开数据库
        db = dbHelper.getReadableDatabase();// 打开数据库
        dbsel = selMeasureHelpter.getWritableDatabase();// 打开数据库
        dbsel = selMeasureHelpter.getReadableDatabase();// 打开数据库
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int i) {
        return recordList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        final UserRecord userRecord = recordList.get(i);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.user_record_item, null);
            viewHolder.tvTime = view.findViewById(R.id.tv_user_time);
            viewHolder.tvPhone = view.findViewById(R.id.tv_user_phone);
            viewHolder.imgName = view.findViewById(R.id.tv_user_name);
            viewHolder.btnSee = view.findViewById(R.id.btn_see);
            viewHolder.cbSel = view.findViewById(R.id.cb_sel);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvTime.setText(userRecord.getTime());
        viewHolder.tvPhone.setText(userRecord.getRegisterNum());
        viewHolder.imgName.setImageBitmap(UITools.byteTobitmap(userRecord.getAutograph()));
        if (seeOrdel) {
            viewHolder.btnSee.setVisibility(View.GONE);
            viewHolder.cbSel.setVisibility(View.VISIBLE);
        } else {
            viewHolder.btnSee.setVisibility(View.VISIBLE);
            viewHolder.cbSel.setVisibility(View.GONE);
        }
        viewHolder.btnSee.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                byte[] autograph = null;
                String sel1 = null, sel2 = null, sel3 = null, sel4 = null;
                String registerNum = null, date = null, customerInfo = null, manufacturer = null, vehiclTeype = null, mileage = null;
                String where = "register_num=" + userRecord.getRegisterNum();
                cursor = db.query(dbHelper.TB_NAME, null, where, null, null, null, "_id ASC");
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    registerNum = cursor.getString(1);
                    date = cursor.getString(2);
                    customerInfo = cursor.getString(3);
                    manufacturer = cursor.getString(4);
                    vehiclTeype = cursor.getString(5);
                    Log.i(TAG, "onClick: --" + vehiclTeype);
                    mileage = cursor.getString(6);
                    autograph = cursor.getBlob(7);
                    cursor.moveToNext();
                }
                cursorSel = dbsel.query(selMeasureHelpter.TB_NAME, null, where, null, null, null, "_id ASC");
                Log.i(TAG, "onClick: " + cursorSel.getCount());
                cursorSel.moveToFirst();
                while (!cursorSel.isAfterLast()) {
                    sel1 = cursorSel.getString(2);
                    sel2 = cursorSel.getString(3);
                    sel3 = cursorSel.getString(4);
                    sel4 = cursorSel.getString(5);
                    cursorSel.moveToNext();
                }
                Intent intent = new Intent(context, UserReportActivity.class);
                intent.putExtra("registerNum", registerNum);
                intent.putExtra("date", date);
                intent.putExtra("customerInfo", customerInfo);
                intent.putExtra("manufacturer", manufacturer);
                intent.putExtra("vehiclTeype", vehiclTeype);
                intent.putExtra("mileage", mileage);
                intent.putExtra("autograph", autograph);
                intent.putExtra("sel1", sel1);
                intent.putExtra("sel2", sel2);
                intent.putExtra("sel3", sel3);
                intent.putExtra("sel4", sel4);
                context.startActivity(intent);
                ActivityCollector.removeActivity(context);
            }
        });

        viewHolder.cbSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( viewHolder.cbSel.isChecked()) {
                    userRecord.setCheck(true);
                    viewHolder.cbSel.setChecked(userRecord.isCheck());
                } else {
                    userRecord.setCheck(false);
                    viewHolder.cbSel.setChecked(userRecord.isCheck());
                }
            }
        });
        viewHolder.cbSel.setChecked(userRecord.isCheck());
//        if (seeOrdel.equals(context.getResources().getString(R.string.str_examine))) {
//
//        } else if (seeOrdel.equals(context.getResources().getString(R.string.str_del))) {
//            viewHolder.btnSee.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.i(TAG, "del: " + userRecord.getRegisterNum());
//                    db.execSQL("delete from " + dbHelper.TB_NAME + " where register_num=?", new String[]{userRecord.getRegisterNum()});
//                    recordList.remove(i);
//                    notifyDataSetChanged();
//                }
//            });
//        }
        return view;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new RecordFilter(recordList);
        }
        return filter;
    }

    class ViewHolder {
        TextView tvTime;
        TextView tvPhone;
        ImageView imgName;
        Button btnSee;
        CheckBox cbSel;
    }

    class RecordFilter extends Filter {
        // 创建集合保存原始数据
        private List<UserRecord> original = new ArrayList<>();

        public RecordFilter(List<UserRecord> list) {
            this.original = list;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (TextUtils.isEmpty(constraint)) {
                results.values = original;
                results.count = original.size();
            } else {
                List<UserRecord> mList = new ArrayList<>();
                for (UserRecord userRecord : original) {
                    if (userRecord.getRegisterNum().trim().toLowerCase().contains(constraint.toString().trim().toLowerCase())) {
                        mList.add(userRecord);
                    }
                }
                results.values = mList;
                results.count = mList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            recordList = (List<UserRecord>) results.values;
            if (listener != null) {
                listener.getFilterData(recordList);
            }
            notifyDataSetChanged();
        }
    }
}
