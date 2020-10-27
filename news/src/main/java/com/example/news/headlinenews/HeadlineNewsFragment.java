package com.example.news.headlinenews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.news.R;
import com.example.news.api.NewsChannelsBean;
import com.example.news.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class HeadlineNewsFragment extends Fragment implements Observer<List<NewsChannelsBean.ChannelList>>{

    public HeadlineNewsFragmentAdapter adapter;
    FragmentHomeBinding viewDataBinding;
    // viewModel还没有创建完成，数据就来了，就会报：The specified child already has a parent. You must call removeView() on the child's parent first.
    private HeadlineNewsViewModel viewModel  = new HeadlineNewsViewModel();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        adapter = new HeadlineNewsFragmentAdapter(getChildFragmentManager());
        viewDataBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewDataBinding.tablayout.setupWithViewPager(viewDataBinding.viewpager);
        viewDataBinding.viewpager.setAdapter(adapter);
        viewDataBinding.viewpager.setOffscreenPageLimit(1);
        viewModel.dataList.observe(this,this);
        return viewDataBinding.getRoot();
    }

    @Override
    public void onChanged(List<NewsChannelsBean.ChannelList> channelLists) {
        adapter.setChannels(channelLists);
    }
}
