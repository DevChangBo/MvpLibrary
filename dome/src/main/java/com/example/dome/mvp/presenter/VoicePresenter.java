package com.example.dome.mvp.presenter;

import android.Manifest;
import android.app.Application;
import android.content.DialogInterface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.dome.dto.VoiceMessageDTO;
import com.example.dome.mvp.ui.activity.VoiceActivity;
import com.example.dome.mvp.ui.adapter.VoiceAdapter;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import timber.log.Timber;

import javax.inject.Inject;

import com.example.dome.mvp.contract.VoiceContract;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.voicerecorder.service.AppCache;
import com.jess.arms.voicerecorder.widget.VoiceRecorderView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;


/**
 * ================================================================
 * 创建时间：2018-11-16 11:11:48
 * 创 建 人：Mr.常
 * 文件描述：
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
@ActivityScope
public class VoicePresenter extends BasePresenter<VoiceContract.Model, VoiceContract.View> implements DialogInterface.OnCancelListener {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    private VoiceAdapter mAdapter;
    private List<VoiceMessageDTO> voices= new ArrayList<>();;
    @Inject
    public VoicePresenter(VoiceContract.Model model, VoiceContract.View rootView
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
        this.mAdapter=null;
    }

    public void initData() {
        getPermissions();
        initAdapter();
        mRootView.getTvTouchRecorder().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (AppCache.getPlayService().isPlaying) {
                        AppCache.getPlayService().stopPlayVoiceAnimation2();
                    }
                }

                return mRootView.getVrvVoice().onPressToSpeakBtnTouch(view, motionEvent, new VoiceRecorderView.EaseVoiceRecorderCallback() {
                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        VoiceMessageDTO bean=new VoiceMessageDTO();
                        bean.setPath(voiceFilePath);
                        bean.setSecond(voiceTimeLength);
                        voices.add(bean);
                        mAdapter.setNewData(voices);
                    }
                });
            }
        });
    }

    private void initAdapter(){
        mAdapter=new VoiceAdapter(null);
        mRootView.setAdapter(mAdapter);
    }


    private void getPermissions() {
        //检测并申请权限、版本更新
        PermissionUtil.requestPermission(
                new PermissionUtil.RequestPermission() {
                    @Override
                    public void onRequestPermissionSuccess() {
                        Timber.d("权限获取成功");

                    }

                    @Override
                    public void onRequestPermissionFailure() {
                        Timber.e("权限获取失败");
                        ArmsUtils.snackbarTextWithLong("请授权后再试!");
                    }
                }, mRootView.getPermissions(), mErrorHandler,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        );
    }

}
