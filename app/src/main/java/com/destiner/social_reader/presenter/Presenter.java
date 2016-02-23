package com.destiner.social_reader.presenter;

import com.destiner.social_reader.view.View;

public interface Presenter<T extends View> {

    void attachView(T view);

    void detachView();
}
