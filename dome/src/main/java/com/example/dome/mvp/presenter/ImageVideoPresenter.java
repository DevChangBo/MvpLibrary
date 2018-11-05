package com.example.dome.mvp.presenter;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.example.dome.mvp.contract.ImageVideoContract;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.tools.imagepicker.adapter.GridImageAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


/**
 * ================================================================
 * 创建时间：2018-11-1 10:20:51
 * 创 建 人：Mr.常
 * 文件描述：图片视频页面
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
@ActivityScope
public class ImageVideoPresenter extends BasePresenter<ImageVideoContract.Model, ImageVideoContract.View> implements DialogInterface.OnCancelListener {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private GridImageAdapter imageAdapter,videoAdapter;
    @Inject
    public ImageVideoPresenter(ImageVideoContract.Model model, ImageVideoContract.View rootView
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
        this.imageAdapter=null;
        this.videoAdapter=null;
    }



    public void initData() {
        mModel.setActivity(mRootView.getActivity());
        initImageAdapter();
        initVideoAdapter();
    }

    private void initImageAdapter(){
        imageAdapter=new GridImageAdapter(mApplication,mModel.setOnImageAddPicClickListener(true));
        imageAdapter.setList(mModel.getImageSelectList());
        imageAdapter.setSelectMax(9);
        mRootView.setImageAdapter(imageAdapter);
        mModel.setOnItemClickListener(imageAdapter,1);
    }

    private void initVideoAdapter(){
        videoAdapter=new GridImageAdapter(mApplication,mModel.setOnImageAddPicClickListener(false));
        videoAdapter.setList(mModel.getVideoSelectList());
        videoAdapter.setSelectMax(9);
        mRootView.setVideoAdapter(videoAdapter);
        mModel.setOnItemClickListener(videoAdapter,2);
    }

    /**
     * 选择结果回调,给视频列表赋值
     * @param data
     */
    public void setVideoList(Intent data) {
        mModel.setVideoSelectList(PictureSelector.obtainMultipleResult(data));
        // 例如 LocalMedia 里面返回三种path
        // 1.media.getPath(); 为原图path
        // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
        // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
        // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
        for (LocalMedia media : mModel.getVideoSelectList()) {
            Log.i("图片-----》", media.getPath());
        }
        videoAdapter.setList(mModel.getVideoSelectList());
        videoAdapter.notifyDataSetChanged();
    }

    /**
     * 选择结果回调,给图片列表赋值
     * @param data
     */
    public void setimageList(Intent data) {
        mModel.setImageSelectList(PictureSelector.obtainMultipleResult(data));
        // 例如 LocalMedia 里面返回三种path
        // 1.media.getPath(); 为原图path
        // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
        // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
        // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
        for (LocalMedia media : mModel.getImageSelectList()) {
            Log.i("图片-----》", media.getPath());
        }
        imageAdapter.setList(mModel.getImageSelectList());
        imageAdapter.notifyDataSetChanged();
    }
}
