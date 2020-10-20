package com.example.base.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * @Author: Jack Ou
 * @CreateDate: 2020/10/20 20:38
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/10/20 20:38
 * @UpdateRemark: 更新说明
 */
public abstract class BaseCustomView<VIEW extends ViewDataBinding, DATA extends BaseViewModel> extends LinearLayout implements IBaseCustomView<DATA> {

    protected VIEW binding;
    protected DATA viewModel;

    public BaseCustomView(Context context) {
        super(context);
        init();
    }

    public BaseCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BaseCustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), this, false);
        binding.getRoot().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                viewOnClicked(view);
            }
        });
        addView(binding.getRoot());
    }

    // 我不知道layoutId是多少，就让子View传给我
    public abstract int getLayoutId();

    // 我也不知道用户点击了要干什么，我就透出接口就行
    public abstract void viewOnClicked(View view);

    //帮用户实现setData
    @Override
    public void setData(DATA data){
        //我不知道用户的viewmodel变量名字，所有用模板设计模式让用户自己设置
        setViewModelData(data);
        binding.executePendingBindings();
        this.viewModel = data;
    }

    public abstract void setViewModelData(DATA data);
}
