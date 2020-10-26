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

/**
 * @Author: Jack Ou
 * @CreateDate: 2020/10/20 22:17
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/10/20 22:17
 * @UpdateRemark: 更新说明
 */
public class NewsListModel extends BaseModel<NewsListBean, List<BaseViewModel>> {

    private String mChannelId;
    private String mChannelName;


    public NewsListModel(String channelId, String channelName) {
        super(true, channelId + channelName + "_PREF_KEY", 1);
        this.mChannelId = channelId;
        this.mChannelName = channelName;
    }

    @Override
    public void load() {
        TecentNetworkApi.getService(NewsApiInterface.class)
                .getNewsList(mChannelId, mChannelName, String.valueOf(mPage))
                .compose(TecentNetworkApi.getInstance().applySchedulers(new BaseObserver<NewsListBean>() {
                    @Override
                    public void onSuccess(NewsListBean newsListBean) {
                        List<BaseViewModel> mContentList = new ArrayList<>();
                        if (mPage == 0) {
                            mContentList.clear();
                        }
                        for (NewsListBean.Contentlist contentlist : newsListBean.showapiResBody.pagebean.contentlist) {
                            if (contentlist.imageurls != null && contentlist.imageurls.size() > 0) {
                                PictureTitleViewModel viewModel = new PictureTitleViewModel();
                                viewModel.jumpUri = contentlist.link;
                                viewModel.pictureUrl = contentlist.imageurls.get(0).url;
                                viewModel.title = contentlist.title;
                                mContentList.add(viewModel);
                            } else {
                                TitleViewModel viewModel = new TitleViewModel();
                                viewModel.jumpUri = contentlist.link;
                                viewModel.title = contentlist.title;
                                mContentList.add(viewModel);
                            }
                        }
                        notifyResultListener(newsListBean, mContentList);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        notifyFail(e.getMessage());
                    }
                }));
    }
}
