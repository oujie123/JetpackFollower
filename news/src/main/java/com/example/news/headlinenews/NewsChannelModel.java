package com.example.news.headlinenews;

import com.example.base.mvvm.model.IBaseModelListener;
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
public class NewsChannelModel {

    private IBaseModelListener<List<NewsChannelsBean.ChannelList>> mListener;

    public NewsChannelModel(IBaseModelListener listener) {
        this.mListener = listener;
    }

    public void load(){
        TecentNetworkApi.getService(NewsApiInterface.class)
                .getNewsChannels()
                .compose(TecentNetworkApi.getInstance().applySchedulers(new BaseObserver<NewsChannelsBean>() {
                    @Override
                    public void onSuccess(NewsChannelsBean newsChannelsBean) {
                        mListener.onLoadSuccess(newsChannelsBean.showapiResBody.channelList);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        mListener.onLoadFail(e.getMessage());
                    }
                }));
    }
}
