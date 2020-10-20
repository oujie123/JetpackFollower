package com.example.base.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.base.customview.IBaseCustomView;
import com.example.base.customview.BaseViewModel;

/**
 * @Author: Jack Ou
 * @CreateDate: 2020/10/20 3:17
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/10/20 3:17
 * @UpdateRemark: 更新说明
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private IBaseCustomView mBaseCustomView;

    public BaseViewHolder(@NonNull IBaseCustomView baseCustomView) {
        super((View) baseCustomView);
        this.mBaseCustomView = baseCustomView;
    }

    public void bind(BaseViewModel viewModel){
        this.mBaseCustomView.setData(viewModel);
    }
}
