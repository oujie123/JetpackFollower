package com.example.news.headlinenews;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.base.BaseApplication;
import com.example.base.mvvm.model.BaseModel;
import com.example.base.mvvm.model.IBaseModelListener;
import com.example.base.mvvm.model.PagingResult;
import com.example.news.api.NewsChannelsBean;

import java.util.List;

/**
 * @Author: Jack Ou
 * @CreateDate: 2020/10/27 22:05
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/10/27 22:05
 * @UpdateRemark: 更新说明
 */
public class HeadlineNewsViewModel implements IBaseModelListener<List<NewsChannelsBean.ChannelList>> {

    private NewsChannelModel mNewsChanelModel;
    public MutableLiveData<List<NewsChannelsBean.ChannelList>> dataList = new MutableLiveData<>();

    public HeadlineNewsViewModel(){
        mNewsChanelModel = new NewsChannelModel();
        mNewsChanelModel.register(this);
        mNewsChanelModel.getDataFromCacheAndLoad();
    }

    @Override
    public void onLoadSuccess(BaseModel model, List<NewsChannelsBean.ChannelList> channelLists, PagingResult... results) {
        if (model instanceof NewsChannelModel) {
            Log.e("JackOu", "==================");
            dataList.postValue(channelLists);
        }
    }

    @Override
    public void onLoadFail(BaseModel model, String e, PagingResult... results) {
        Toast.makeText(BaseApplication.sApplication, e, Toast.LENGTH_LONG).show();
    }
}
