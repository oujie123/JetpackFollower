package com.example.news.newslist.picturetitleview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.news.R;
import com.example.news.base.BaseCustomView;
import com.example.news.databinding.PictureTitleViewBinding;
import com.example.webview.WebviewActivity;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


/**
 * @Author: Jack Ou
 * @CreateDate: 2020/10/20 2:14
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/10/20 2:14
 * @UpdateRemark: 更新说明
 */
public class PictureTitleView extends LinearLayout implements BaseCustomView<PictureTitleViewModel> {

    private PictureTitleViewBinding mBinding;
    private PictureTitleViewModel mViewModel;

    public PictureTitleView(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.picture_title_view, this, false);
        mBinding.getRoot().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                WebviewActivity.startCommonWeb(getContext(), "News", mViewModel.jumpUri);
            }
        });
        addView(mBinding.getRoot());
    }

    @Override
    public void setData(PictureTitleViewModel data) {
        mBinding.setViewModel(data);
        mBinding.executePendingBindings();
        this.mViewModel = data;
    }

    @BindingAdapter("loadImageUrl")
    public static void loadImageUrl(ImageView imageView, String url){
        if (!TextUtils.isEmpty(url)){
            Glide.with(imageView.getContext()).load(url).transition(withCrossFade()).into(imageView);
        }
    }
}
