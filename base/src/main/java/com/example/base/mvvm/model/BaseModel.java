package com.example.base.mvvm.model;

import com.example.base.preference.BasicDataPreferenceUtil;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author Jack_Ou  created on 2020/10/22.
 */
public abstract class BaseModel<NETWORK_DATA, RESULT_DATA> {

    protected int mPage = 1;
    protected WeakReference<IBaseModelListener> mListenerWeakReference;
    private boolean mIsPaging;
    private final int INIT_PAGE;
    private boolean mIsLoading;
    private String mCachePreferenceKey;

    public BaseModel(boolean isPaging, String cachePreferenceKey, int... initPageNumber) {
        mIsPaging = isPaging;
        if (isPaging && initPageNumber != null && initPageNumber.length > 0) {
            INIT_PAGE = initPageNumber[0];
        } else {
            INIT_PAGE = -1;
        }
        this.mCachePreferenceKey = cachePreferenceKey;
    }

    public void register(IBaseModelListener listener) {
        if (listener != null) {
            mListenerWeakReference = new WeakReference<>(listener);
        }
    }

    public void refresh() {
        // 此处可以抛一个异常，告知开发者要注册回调
        mPage = 1;
        loadNextPage();
    }

    public void loadNextPage() {
        // 此处可以抛一个异常，告知开发者要注册回调
        if (!mIsLoading) {
            mIsLoading = true;
            load();
        }
    }

    public abstract void load();

    protected void notifyResultListener(NETWORK_DATA networkData, RESULT_DATA data) {
        IBaseModelListener listener = mListenerWeakReference.get();
        if (listener != null) {
            if (mIsPaging) {
                listener.onLoadSuccess(this, data, new PagingResult(mPage == INIT_PAGE, data == null ? null : ((List) data).isEmpty(), ((List) data).size() > 0));
            } else {
                listener.onLoadSuccess(this, data);
            }

            if (mIsPaging) {
                if (mCachePreferenceKey != null && mPage == INIT_PAGE) {
                    saveDataToPreference(networkData);
                }
            } else {
                if (mCachePreferenceKey != null){
                    saveDataToPreference(networkData);
                }
            }

            // 更新页码
            if (mIsPaging) {
                if (data != null && ((List) data).size() > 0) {
                    mPage++;
                }
            }
        }
        mIsLoading = false;
    }

    protected void notifyFail(final String errorMsg) {
        IBaseModelListener listener = mListenerWeakReference.get();
        if (listener != null) {
            if (mIsPaging) {
                listener.onLoadFail(this, errorMsg, new PagingResult(mPage == INIT_PAGE, true, false));
            } else {
                listener.onLoadFail(this, errorMsg);
            }
        }
        mIsLoading = false;
    }

    protected void saveDataToPreference(NETWORK_DATA data) {
        if (data != null) {
            BaseCacheData<NETWORK_DATA> cacheData = new BaseCacheData<>();
            cacheData.data = data;
            cacheData.updateTimeMills = System.currentTimeMillis();
            BasicDataPreferenceUtil.getInstance().setString(mCachePreferenceKey, new Gson().toJson(cacheData));
        }
    }
}
