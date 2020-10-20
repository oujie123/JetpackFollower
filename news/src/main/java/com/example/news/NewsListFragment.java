package com.example.news;

import androidx.annotation.NonNull;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.network.TecentNetworkApi;
import com.example.network.observer.BaseObserver;
import com.example.news.api.NewsApiInterface;
import com.example.news.api.NewsListBean;
import com.example.base.customview.BaseViewModel;
import com.example.news.databinding.FragmentNewsBinding;
import com.example.common.picturetitleview.PictureTitleViewModel;
import com.example.common.titleview.TitleViewModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class NewsListFragment extends Fragment {
    private NewsListRecyclerViewAdapter mAdapter;
    private FragmentNewsBinding viewDataBinding;

    protected final static String BUNDLE_KEY_PARAM_CHANNEL_ID = "bundle_key_param_channel_id";
    protected final static String BUNDLE_KEY_PARAM_CHANNEL_NAME = "bundle_key_param_channel_name";
    private int mPage = 1;

    public static NewsListFragment newInstance(String channelId, String channelName) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_PARAM_CHANNEL_ID, channelId);
        bundle.putString(BUNDLE_KEY_PARAM_CHANNEL_NAME, channelName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);
        mAdapter = new NewsListRecyclerViewAdapter();
        viewDataBinding.listview.setHasFixedSize(true);
        viewDataBinding.listview.setLayoutManager(new LinearLayoutManager(getContext()));
        viewDataBinding.listview.setAdapter(mAdapter);
        load();
        viewDataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 0;
                load();
            }
        });
        viewDataBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                load();
            }
        });
        return viewDataBinding.getRoot();
    }
    List<BaseViewModel> mContentList = new ArrayList<>();
    protected void load() {
        TecentNetworkApi.getService(NewsApiInterface.class)
                .getNewsList(getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_ID),
                        getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_NAME), String.valueOf(mPage))
                .compose(TecentNetworkApi.getInstance().applySchedulers(new BaseObserver<NewsListBean>() {
                    @Override
                    public void onSuccess(NewsListBean newsChannelsBean) {
                        if (mPage == 0) {
                            mContentList.clear();
                        }
                        for (NewsListBean.Contentlist contentlist: newsChannelsBean.showapiResBody.pagebean.contentlist){
                            if (contentlist.imageurls != null && contentlist.imageurls.size() > 0){
                                PictureTitleViewModel viewModel = new PictureTitleViewModel();
                                viewModel.jumpUri = contentlist.link;
                                viewModel.pictureUrl = contentlist.imageurls.get(0).url;
                                viewModel.title = contentlist.title;
                                mContentList.add(viewModel);
                            }else {
                                TitleViewModel viewModel = new TitleViewModel();
                                viewModel.jumpUri = contentlist.link;
                                viewModel.title = contentlist.title;
                                mContentList.add(viewModel);
                            }
                        }
                        mAdapter.setData(mContentList);
                        mPage ++;
                        viewDataBinding.refreshLayout.finishRefresh();
                        viewDataBinding.refreshLayout.finishLoadMore();
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        e.printStackTrace();
                    }
                }));
    }
}
