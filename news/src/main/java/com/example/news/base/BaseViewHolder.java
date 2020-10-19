package com.example.news.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author: Jack Ou
 * @CreateDate: 2020/10/20 3:17
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/10/20 3:17
 * @UpdateRemark: 更新说明
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private BaseCustomView mBaseCustomView;

    public BaseViewHolder(@NonNull BaseCustomView baseCustomView) {
        super((View) baseCustomView);
        this.mBaseCustomView = baseCustomView;
    }

    public void bind(BaseViewModel viewModel){
        this.mBaseCustomView.setData(viewModel);
    }
}
