package com.destiner.social_reader.presenter;

public interface Presenter<T> {

    void attachView(T view);

    void detachView();
}
