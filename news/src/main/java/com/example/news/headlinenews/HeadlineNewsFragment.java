package com.example.news.headlinenews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.base.mvvm.model.IBaseModelListener;
import com.example.base.mvvm.model.PagingResult;
import com.example.news.R;
import com.example.news.api.NewsChannelsBean;
import com.example.news.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class HeadlineNewsFragment extends Fragment implements IBaseModelListener<List<NewsChannelsBean.ChannelList>> {

    public HeadlineNewsFragmentAdapter adapter;
    FragmentHomeBinding viewDataBinding;
    private NewsChannelModel mNewsChanelModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        adapter = new HeadlineNewsFragmentAdapter(getChildFragmentManager());
        viewDataBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewDataBinding.viewpager.setAdapter(adapter);
        viewDataBinding.tablayout.setupWithViewPager(viewDataBinding.viewpager);
        viewDataBinding.viewpager.setOffscreenPageLimit(1);
        mNewsChanelModel = new NewsChannelModel();
        mNewsChanelModel.register(this);
        mNewsChanelModel.load();
        return viewDataBinding.getRoot();
    }

    @Override
    public void onLoadSuccess(List<NewsChannelsBean.ChannelList> channelLists, PagingResult... results) {
        if (adapter != null) {
            adapter.setChannels(channelLists);
        }
    }

    @Override
    public void onLoadFail(String e) {
        Toast.makeText(getContext(), e, Toast.LENGTH_LONG).show();
    }
}
