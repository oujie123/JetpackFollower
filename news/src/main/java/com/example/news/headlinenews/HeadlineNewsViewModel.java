package com.example.news.headlinenews;

import com.example.base.mvvm.viewmodel.BaseMvvmViewModel;
import com.example.news.api.NewsChannelsBean;

/**
 * @Author: Jack Ou
 * @CreateDate: 2020/10/27 22:05
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/10/27 22:05
 * @UpdateRemark: 更新说明
 */
public class HeadlineNewsViewModel extends BaseMvvmViewModel<NewsChannelModel, NewsChannelsBean.ChannelList> {

    @Override
    public NewsChannelModel createModel() {
        return new NewsChannelModel();
    }
}
