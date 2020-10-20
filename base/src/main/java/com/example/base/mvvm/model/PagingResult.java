package com.example.base.mvvm.model;

/**
 * @Author: Jack Ou
 * @CreateDate: 2020/10/20 22:43
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/10/20 22:43
 * @UpdateRemark: 更新说明
 */
public class PagingResult {

    public PagingResult(boolean isFirst, boolean isEmpty, boolean hasNextPage) {
        this.isFirst = isFirst;
        this.isEmpty = isEmpty;
        this.hasNextPage = hasNextPage;
    }

    public boolean isFirst;
    public boolean isEmpty;
    public boolean hasNextPage;
}
