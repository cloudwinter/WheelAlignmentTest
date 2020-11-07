package com.inprintech.wheelalignmenttest.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.inprintech.wheelalignmenttest.Model.UserAccount;
import com.inprintech.wheelalignmenttest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户账号的适配器
 */
public class UserAccountAdapter extends BaseAdapter {

    private Context context;
    private List<UserAccount> accountList = new ArrayList<>();

    public UserAccountAdapter(Context context, List<UserAccount> list) {
        this.context = context;
        this.accountList = list;
    }

    @Override
    public int getCount() {
        return accountList.size();
    }

    @Override
    public Object getItem(int i) {
        return accountList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        UserAccount userAccount = accountList.get(i);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.account_list_item, null);
            viewHolder.tvAccount = view.findViewById(R.id.tv_item_account);
            viewHolder.tvPwd = view.findViewById(R.id.tv_item_pwd);
            viewHolder.btnDel = view.findViewById(R.id.btn_del);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvAccount.setText("账号：" + userAccount.getAccount());
        viewHolder.tvPwd.setText("密码：" + userAccount.getPassword());

        viewHolder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountList.remove(i);
                notifyDataSetChanged();
                SharedPreferences spRegister = context.getSharedPreferences("isRegister", context.MODE_PRIVATE);
                SharedPreferences.Editor editorRegister = spRegister.edit();
                editorRegister.clear();
                editorRegister.commit();
                SharedPreferences sharedList = context.getSharedPreferences("UserList", context.MODE_PRIVATE);
                SharedPreferences.Editor editorList = sharedList.edit();
                editorList.clear();
                editorList.commit();
            }
        });
        return view;
    }

    class ViewHolder {
        TextView tvAccount;
        TextView tvPwd;
        Button btnDel;
    }
}
