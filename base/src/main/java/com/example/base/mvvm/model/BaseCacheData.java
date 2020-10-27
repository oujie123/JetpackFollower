package com.example.base.mvvm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @Author: Jack Ou
 * @CreateDate: 2020/10/27 0:37
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/10/27 0:37
 * @UpdateRemark: 更新说明
 */
public class BaseCacheData<DATA> {

    // 不能混淆这个变量名
    @SerializedName("updateTimeMills")
    @Expose
    public long updateTimeMills;

    @SerializedName("data")
    @Expose
    public DATA data;
}
