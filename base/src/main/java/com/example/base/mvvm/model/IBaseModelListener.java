package com.example.base.mvvm.model;

/**
 * @Author: Jack Ou
 * @CreateDate: 2020/10/20 21:55
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/10/20 21:55
 * @UpdateRemark: 更新说明
 */
public interface IBaseModelListener<DATA> {

    // 接口可变长度
    void onLoadSuccess(DATA data, PagingResult... results);
    void onLoadFail(String e);
}
