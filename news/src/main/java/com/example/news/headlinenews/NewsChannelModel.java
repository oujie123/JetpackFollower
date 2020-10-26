package com.example.news.headlinenews;

import com.example.base.mvvm.model.BaseModel;
import com.example.network.TecentNetworkApi;
import com.example.network.observer.BaseObserver;
import com.example.news.api.NewsApiInterface;
import com.example.news.api.NewsChannelsBean;

import java.util.List;

/**
 * 不需要分页的
 *
 * @Author: Jack Ou
 * @CreateDate: 2020/10/20 21:57
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/10/20 21:57
 * @UpdateRemark: 更新说明
 */
public class NewsChannelModel extends BaseModel<NewsChannelsBean,List<NewsChannelsBean.ChannelList>> {

    public NewsChannelModel() {
        super(false,"NEWS_CHANNEL_PREF_KEY");
    }

    @Override
    public void load() {
        TecentNetworkApi.getService(NewsApiInterface.class)
                .getNewsChannels()
                .compose(TecentNetworkApi.getInstance().applySchedulers(new BaseObserver<NewsChannelsBean>() {
                    @Override
                    public void onSuccess(NewsChannelsBean newsChannelsBean) {
                        notifyResultListener(newsChannelsBean, newsChannelsBean.showapiResBody.channelList);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        notifyFail(e.getMessage());
                    }
                }));
    }
}
