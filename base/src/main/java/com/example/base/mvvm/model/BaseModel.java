package com.example.base.mvvm.model;

import android.text.TextUtils;

import com.example.base.preference.BasicDataPreferenceUtil;
import com.example.base.utils.GenericUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author Jack_Ou  created on 2020/10/22.
 */
public abstract class BaseModel<NETWORK_DATA, RESULT_DATA> implements MvvmDataObserver<NETWORK_DATA> {

    protected int mPage = 1;
    protected WeakReference<IBaseModelListener> mListenerWeakReference;
    private boolean mIsPaging;
    private final int INIT_PAGE;
    private boolean mIsLoading;
    private String mCachePreferenceKey;
    private CompositeDisposable compositeDisposable;
    // 第一次安装，预加载数据
    private String mApkPreDefined;

    public BaseModel(boolean isPaging, String cachePreferenceKey, String apkPreDefined,int... initPageNumber) {
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

    // 默认每次都更新，如果不是每次更新，需要覆盖一下这个方法
    public boolean isNeedToUpdate(long cachedTimeslot){
        return true;
    }

    public void getDataFromCacheAndLoad() {
        if (!mIsLoading) {
            mIsLoading = true;
            if (mCachePreferenceKey != null) {
                String savedDataString = BasicDataPreferenceUtil.getInstance().getString(mCachePreferenceKey);
                if (!TextUtils.isEmpty(savedDataString)) {
                    try {
                        NETWORK_DATA savedData = new Gson().fromJson(new JSONObject(savedDataString).getString("data"),
                                (Class<NETWORK_DATA>) GenericUtils.getGenericType(this, 0));
                        if (savedData != null){
                            onSuccess(savedData,true);
                        }
                        long timeSlot = new JSONObject(savedDataString).getLong("updateTimeMills");
                        if (isNeedToUpdate(timeSlot)){
                            load();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // 把预置数据加载到界面
                if (mApkPreDefined != null) {
                    NETWORK_DATA savedData = new Gson().fromJson(mApkPreDefined,
                            (Class<NETWORK_DATA>) GenericUtils.getGenericType(this, 0));
                    if (savedData != null){
                        onSuccess(savedData,true);
                    }
                }
            }
            load();
        }
    }

    protected void notifyResultListener(NETWORK_DATA networkData, RESULT_DATA data, boolean isFromCache) {
        IBaseModelListener listener = mListenerWeakReference.get();
        if (listener != null) {
            if (mIsPaging) {
                listener.onLoadSuccess(this, data, new PagingResult(mPage == INIT_PAGE, data == null ? null : ((List) data).isEmpty(), ((List) data).size() > 0));
            } else {
                listener.onLoadSuccess(this, data);
            }

            if (mIsPaging) {
                if (mCachePreferenceKey != null && mPage == INIT_PAGE && !isFromCache) {
                    saveDataToPreference(networkData);
                }
            } else {
                if (mCachePreferenceKey != null && !isFromCache) {
                    saveDataToPreference(networkData);
                }
            }

            // 更新页码,并且不是来自缓存
            if (mIsPaging && !isFromCache) {
                if (data != null && ((List) data).size() > 0) {
                    mPage++;
                }
            }
        }
        if (!isFromCache) {
            mIsLoading = false;
        }
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

    public void cancel() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }


    public void addDisposable(Disposable d) {
        if (d == null) {
            return;
        }

        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }

        compositeDisposable.add(d);
    }

    public boolean isPaging() {
        return mIsPaging;
    }
}
