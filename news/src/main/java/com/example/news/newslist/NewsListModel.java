package com.example.news.newslist;

import com.example.base.customview.BaseViewModel;
import com.example.base.mvvm.model.IBaseModelListener;
import com.example.base.mvvm.model.PagingResult;
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
public class NewsListModel {

    private IBaseModelListener<List<BaseViewModel>> mListener;
    private String mChannelId;
    private String mChannelName;
    private int mPage = 1;

    public NewsListModel(IBaseModelListener<List<BaseViewModel>> mListener, String channelId, String channelName) {
        this.mListener = mListener;
        this.mChannelId = channelId;
        this.mChannelName = channelName;
    }

    public void refresh(){
        mPage = 1;
        loadNextPage();
    }

    public void loadNextPage() {
        TecentNetworkApi.getService(NewsApiInterface.class)
                .getNewsList(mChannelId, mChannelName, String.valueOf(mPage))
                .compose(TecentNetworkApi.getInstance().applySchedulers(new BaseObserver<NewsListBean>() {
                    @Override
                    public void onSuccess(NewsListBean newsChannelsBean) {
                        List<BaseViewModel> mContentList = new ArrayList<>();
                        if (mPage == 0) {
                            mContentList.clear();
                        }
                        for (NewsListBean.Contentlist contentlist : newsChannelsBean.showapiResBody.pagebean.contentlist) {
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
                        mListener.onLoadSuccess(mContentList,new PagingResult(mPage==1,mContentList.isEmpty(),mContentList.size() > 10));
                        mPage++;
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        mListener.onLoadFail(e.getMessage());
                    }
                }));
    }
}
