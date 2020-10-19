package com.example.news.base;

public interface BaseCustomView<D extends BaseViewModel> {
    void setData(D data);
}
