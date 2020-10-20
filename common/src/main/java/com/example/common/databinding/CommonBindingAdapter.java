package com.example.common.databinding;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * @Author: Jack Ou
 * @CreateDate: 2020/10/20 21:18
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/10/20 21:18
 * @UpdateRemark: 更新说明
 */
public class CommonBindingAdapter {

    @BindingAdapter("loadImageUrl")
    public static void loadImageUrl(ImageView imageView, String url){
        if (!TextUtils.isEmpty(url)){
            Glide.with(imageView.getContext()).load(url).transition(withCrossFade()).into(imageView);
        }
    }
}
