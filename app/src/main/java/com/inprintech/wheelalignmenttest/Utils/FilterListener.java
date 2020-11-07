package com.inprintech.wheelalignmenttest.Utils;

import com.inprintech.wheelalignmenttest.Model.UserRecord;

import java.util.List;

public interface FilterListener {
    void getFilterData(List<UserRecord> list);// 获取过滤后的数据
}
