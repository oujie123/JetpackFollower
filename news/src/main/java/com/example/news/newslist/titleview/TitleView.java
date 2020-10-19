package com.example.news.newslist.titleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import com.example.news.R;
import com.example.news.base.BaseCustomView;
import com.example.news.base.BaseViewModel;
import com.example.news.databinding.TitleViewBinding;
import com.example.webview.WebviewActivity;

/**
 * @Author: Jack Ou
 * @CreateDate: 2020/10/20 3:45
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/10/20 3:45
 * @UpdateRemark: 更新说明
 */
public class TitleView extends LinearLayout implements BaseCustomView<TitleViewModel> {

    private TitleViewBinding mBinding;

    public TitleView(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.title_view,this,false);
        mBinding.getRoot().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                WebviewActivity.startCommonWeb(getContext(), "News", view.getTag() + "");
            }
        });
        addView(mBinding.getRoot());
    }

    @Override
    public void setData(TitleViewModel data) {
        mBinding.setViewModel(data);
        mBinding.executePendingBindings();
    }
}
