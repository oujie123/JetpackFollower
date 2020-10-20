package com.example.common.picturetitleview;

import android.content.Context;
import android.view.View;

import com.example.base.customview.BaseCustomView;
import com.example.common.R;
import com.example.common.databinding.PictureTitleViewBinding;
import com.example.webview.WebviewActivity;


/**
 * @Author: Jack Ou
 * @CreateDate: 2020/10/20 2:14
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/10/20 2:14
 * @UpdateRemark: 更新说明
 */
public class PictureTitleView extends BaseCustomView<PictureTitleViewBinding,PictureTitleViewModel> {

    public PictureTitleView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.picture_title_view;
    }

    @Override
    public void viewOnClicked(View view) {
        WebviewActivity.startCommonWeb(getContext(), "News", viewModel.jumpUri);
    }

    @Override
    public void setViewModelData(PictureTitleViewModel pictureTitleViewModel) {
        binding.setViewModel(pictureTitleViewModel);
    }
}
