package com.example.news.headlinenews;

import android.os.Parcelable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.news.api.NewsChannelsBean;
import com.example.news.newslist.NewsListFragment;

import java.util.List;

public class HeadlineNewsFragmentAdapter extends FragmentPagerAdapter {
    private List<NewsChannelsBean.ChannelList> mChannels;
    private int itemCount = 0;

    public HeadlineNewsFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setChannels(List<NewsChannelsBean.ChannelList> channels) {
        this.mChannels = channels;
        itemCount = channels.size();
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int pos) {
        return NewsListFragment.newInstance(mChannels.get(pos).channelId, mChannels.get(pos).name);
    }

    @Override
    public int getCount() {
        return itemCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels.get(position).name;
    }

    @Override
    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
    }
}