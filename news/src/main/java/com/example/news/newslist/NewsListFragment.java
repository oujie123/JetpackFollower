package com.example.news.newslist;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.base.customview.BaseViewModel;
import com.example.base.loadsir.EmptyCallback;
import com.example.base.mvvm.viewmodel.ViewStatus;
import com.example.base.utils.ToastUtil;
import com.example.news.R;
import com.example.news.databinding.FragmentNewsBinding;
import com.example.webview.utils.LoadingCallback;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

public class NewsListFragment extends Fragment {
    private NewsListRecyclerViewAdapter mAdapter;
    private FragmentNewsBinding viewDataBinding;
    private NewsListViewModel mNewsListViewModel;
    private LoadService mLoadService;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);
        mAdapter = new NewsListRecyclerViewAdapter();
        viewDataBinding.listview.setHasFixedSize(true);
        viewDataBinding.listview.setLayoutManager(new LinearLayoutManager(getContext()));
        viewDataBinding.listview.setAdapter(mAdapter);
        mNewsListViewModel = new NewsListViewModel(getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_ID),
                getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_NAME));
        viewDataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mNewsListViewModel.refresh();
            }
        });
        viewDataBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mNewsListViewModel.loadNextPage();
            }
        });
        mNewsListViewModel.dataList.observe(this, new Observer<List<BaseViewModel>>() {
            @Override
            public void onChanged(List<BaseViewModel> baseViewModels) {
                viewDataBinding.refreshLayout.finishRefresh();
                viewDataBinding.refreshLayout.finishLoadMore();
                mAdapter.setData(baseViewModels);
            }
        });

        // 更新页面
        mLoadService = LoadSir.getDefault().register(viewDataBinding.refreshLayout, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mNewsListViewModel.refresh();
            }
        });
        mNewsListViewModel.viewStatusMutableLiveData.observe(this, new Observer<ViewStatus>() {
            @Override
            public void onChanged(ViewStatus viewStatus) {
                if (viewStatus instanceof ViewStatus && mLoadService != null) {
                    switch ((ViewStatus) viewStatus) {
                        case LOADING:
                            mLoadService.showCallback(LoadingCallback.class);
                            break;
                        case EMPTY:
                            mLoadService.showCallback(EmptyCallback.class);
                            break;
                        case SHOW_CONTENT:
                            mLoadService.showSuccess();
                            break;
                        case NO_MORE_DATA:
                            ToastUtil.show("没有更多了");
                            break;
                        case REFRESH_ERROR:
                            if (mNewsListViewModel.dataList.getValue().size() == 0) {
                                mLoadService.showCallback(Camera.ErrorCallback.class);
                            } else {
                                ToastUtil.show(mNewsListViewModel.errorMsg.getValue().toString());
                            }
                            break;
                        case LOAD_MORE_FAILED:
                            ToastUtil.show(mNewsListViewModel.errorMsg.getValue().toString());
                            break;
                    }
                }
            }
        });
        return viewDataBinding.getRoot();
    }
}
