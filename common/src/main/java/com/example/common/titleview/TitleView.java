package com.example.common.titleview;

import android.content.Context;
import android.view.View;

import com.example.base.customview.BaseCustomView;
import com.example.common.R;
import com.example.common.databinding.TitleViewBinding;
import com.example.webview.WebviewActivity;

/**
 * @Author: Jack Ou
 * @CreateDate: 2020/10/20 3:45
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/10/20 3:45
 * @UpdateRemark: 更新说明
 */
public class TitleView extends BaseCustomView<TitleViewBinding,TitleViewModel> {

    public TitleView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.title_view;
    }

    @Override
    public void viewOnClicked(View view) {
        WebviewActivity.startCommonWeb(getContext(), "News", viewModel.jumpUri);
    }

    @Override
    public void setViewModelData(TitleViewModel titleViewModel) {
        binding.setViewModel(titleViewModel);
    }

}
