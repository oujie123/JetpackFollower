package com.example.news.newslist;

import com.example.base.customview.BaseViewModel;
import com.example.base.mvvm.model.BaseModel;
import com.example.common.picturetitleview.PictureTitleViewModel;
import com.example.common.titleview.TitleViewModel;
import com.example.network.TecentNetworkApi;
import com.example.network.observer.BaseObserver;
import com.example.news.api.NewsApiInterface;
import com.example.news.api.NewsListBean;

import java.util.ArrayList;
import java.util.List;

public class NewsListModel extends BaseModel<NewsListBean, List<BaseViewModel>> {
    private String mChannelId;
    private String mChannelName;

    public NewsListModel(String channelId, String channelName) {
        super(true, channelId + channelName + "_preference_key", null, 1);
        mChannelId = channelId;
        mChannelName = channelName;
    }

    @Override
    public void load() {
        TecentNetworkApi.getService(NewsApiInterface.class)
                .getNewsList(mChannelId,
                        mChannelName, String.valueOf(mPage))
                .compose(TecentNetworkApi.getInstance().applySchedulers(new BaseObserver<NewsListBean>(this, this)));
    }

    @Override
    public void onSuccess(NewsListBean newsListBean, boolean isFromCache) {
        List<BaseViewModel> viewModels = new ArrayList<>();
        for (NewsListBean.Contentlist contentlist : newsListBean.showapiResBody.pagebean.contentlist) {
            if (contentlist.imageurls != null && contentlist.imageurls.size() > 0) {
                PictureTitleViewModel pictureTitleViewModel = new PictureTitleViewModel();
                pictureTitleViewModel.pictureUrl = contentlist.imageurls.get(0).url;
                pictureTitleViewModel.jumpUri = contentlist.link;
                pictureTitleViewModel.title = contentlist.title;
                viewModels.add(pictureTitleViewModel);
            } else {
                TitleViewModel titleViewModel = new TitleViewModel();
                titleViewModel.jumpUri = contentlist.link;
                titleViewModel.title = contentlist.title;
                viewModels.add(titleViewModel);
            }
        }
        notifyResultListener(newsListBean, viewModels, isFromCache);
    }

    @Override
    public void onFailure(Throwable e) {
        notifyFail(e.getMessage());
    }
}
