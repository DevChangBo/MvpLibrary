package com.example.dome.mvp.presenter;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.dome.mvp.ui.activity.ImageVideoActivity;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.example.dome.mvp.contract.MainContract;


/**
 * ================================================================
 * 创建时间：2018-7-13 10:16:43
 * 创 建 人：Mr.常
 * 文件描述：
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
@ActivityScope
public class MainPresenter extends BasePresenter<MainContract.Model, MainContract.View> implements DialogInterface.OnCancelListener {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public MainPresenter(MainContract.Model model, MainContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }


    public DialogInterface.OnCancelListener getCancleListener() {
        return this;
    }


    @Override
    public void onCancel(DialogInterface mDialogInterface) {
        unDispose();
        mRootView.showMessage("操作被取消");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void strImageVideoAty() {
        mRootView.launchActivity(new Intent(mApplication, ImageVideoActivity.class));
    }
}
