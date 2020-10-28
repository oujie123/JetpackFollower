package com.example.news.newslist;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.base.customview.BaseViewModel;
import com.example.base.mvvm.view.BaseMvvmFragment;
import com.example.news.R;
import com.example.news.databinding.FragmentNewsBinding;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

public class NewsListFragment extends BaseMvvmFragment<FragmentNewsBinding, NewsListViewModel, BaseViewModel> {
    private NewsListRecyclerViewAdapter mAdapter;

    protected final static String BUNDLE_KEY_PARAM_CHANNEL_ID = "bundle_key_param_channel_id";
    protected final static String BUNDLE_KEY_PARAM_CHANNEL_NAME = "bundle_key_param_channel_name";

    public static NewsListFragment newInstance(String channelId, String channelName) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_PARAM_CHANNEL_ID, channelId);
        bundle.putString(BUNDLE_KEY_PARAM_CHANNEL_NAME, channelName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getFragmentTag() {
        return getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_NAME);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    public NewsListViewModel getViewModel() {
        return new ViewModelProvider(getActivity(), new NewsListViewModel.NewsListViewModelFactory(getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_ID), getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_NAME)))
                .get(getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_ID), NewsListViewModel.class);
    }

    @Override
    protected View getLoadSirView() {
        return viewDataBinding.refreshLayout;
    }

    @Override
    public void onNetworkResponded(List<BaseViewModel> baseCustomViewModels, boolean isDataUpdated) {
        viewDataBinding.refreshLayout.finishRefresh();
        viewDataBinding.refreshLayout.finishLoadMore();
        if (isDataUpdated) {
            mAdapter.setData(baseCustomViewModels);
        }
    }

    @Override
    protected void onViewCreated() {
        mAdapter = new NewsListRecyclerViewAdapter();
        viewDataBinding.listview.setHasFixedSize(true);
        viewDataBinding.listview.setLayoutManager(new LinearLayoutManager(getContext()));
        viewDataBinding.listview.setAdapter(mAdapter);
        viewDataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.refresh();
            }
        });
        viewDataBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.loadNextPage();
            }
        });
        showLoading();
    }
}
