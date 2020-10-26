package com.example.base.mvvm.model;

import java.lang.ref.WeakReference;

/**
 * @author Jack_Ou  created on 2020/10/22.
 */
public abstract class BaseModel {

    protected int mPage = 1;
    protected WeakReference<IBaseModelListener> mListenerWeakReference;
    private boolean mIsPaging;
    private final int INIT_PAGE;
    private boolean mIsLoading;

    public BaseModel(boolean isPaging,int... initPageNumber){
        mIsPaging = isPaging;
        if (isPaging && initPageNumber != null && initPageNumber.length > 0){
            INIT_PAGE = initPageNumber[0];
        } else {
            INIT_PAGE = -1;
        }
    }

    public void register(IBaseModelListener listener){
        if (listener != null){
            mListenerWeakReference = new WeakReference<>(listener);
        }
    }

    public void refresh(){
        mPage = 1;
        loadNextPage();
    }

    public void loadNextPage(){
        if (!mIsLoading){
            mIsLoading = true;
            load();
        }
    }

    public abstract void load();
}
