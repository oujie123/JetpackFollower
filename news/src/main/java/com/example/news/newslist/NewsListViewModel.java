package com.example.news.newslist;

import androidx.lifecycle.MutableLiveData;

import com.example.base.customview.BaseViewModel;
import com.example.base.mvvm.model.BaseModel;
import com.example.base.mvvm.model.IBaseModelListener;
import com.example.base.mvvm.model.PagingResult;
import com.example.base.mvvm.viewmodel.ViewStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jack Ou
 * @CreateDate: 2020/10/27 22:11
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/10/27 22:11
 * @UpdateRemark: 更新说明
 */
public class NewsListViewModel implements IBaseModelListener<List<BaseViewModel>> {

    public MutableLiveData<List<BaseViewModel>> dataList = new MutableLiveData<>();
    public MutableLiveData<ViewStatus> viewStatusMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> errorMsg = new MutableLiveData<>();
    private NewsListModel model;

    public NewsListViewModel(String channelId,String channelName){
        dataList.setValue(new ArrayList<BaseViewModel>());
        model = new NewsListModel(channelId, channelName);
        model.register(this);
        model.getDataFromCacheAndLoad();
    }

    public void refresh(){
        model.refresh();
    }

    public void loadNextPage(){
        model.loadNextPage();
    }

    @Override
    public void onLoadSuccess(BaseModel model, List<BaseViewModel> baseViewModels, PagingResult... results) {
        if (model instanceof  NewsListModel){
            if (results[0].isFirst) {
                dataList.getValue().clear();
            }
            if (results[0].isEmpty) {
                if (results[0].isFirst) {
                    viewStatusMutableLiveData.postValue(ViewStatus.EMPTY);
                } else {
                    viewStatusMutableLiveData.postValue(ViewStatus.NO_MORE_DATA);
                }
            } else {
                dataList.getValue().addAll(baseViewModels);
                dataList.postValue(dataList.getValue());
                viewStatusMutableLiveData.postValue(ViewStatus.SHOW_CONTENT);
            }
        }
    }

    @Override
    public void onLoadFail(BaseModel model, String e, PagingResult... results) {
        errorMsg.postValue(e);
        if (results[0].isFirst){
            viewStatusMutableLiveData.postValue(ViewStatus.REFRESH_ERROR);
        } else {
            viewStatusMutableLiveData.postValue(ViewStatus.LOAD_MORE_FAILED);
        }
    }
}
