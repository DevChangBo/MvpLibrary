package com.example.dome.mvp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dome.R;
import com.example.dome.di.component.DaggerVoiceComponent;
import com.example.dome.di.module.VoiceModule;
import com.example.dome.mvp.contract.VoiceContract;
import com.example.dome.mvp.presenter.VoicePresenter;
import com.example.dome.mvp.ui.adapter.VoiceAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DialogUtils;
import com.jess.arms.voicerecorder.utils.PathUtil;
import com.jess.arms.voicerecorder.widget.VoiceRecorderView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * ================================================================
 * 创建时间：2018-11-16 11:11:48
 * 创 建 人：Mr.常
 * 文件描述：
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
public class VoiceActivity extends BaseActivity<VoicePresenter> implements VoiceContract.View {


    @BindView(R.id.rv_voice_jilu)
    RecyclerView rvVoiceJilu;
    @BindView(R.id.vrv_voice)
    VoiceRecorderView vrvVoice;
    @BindView(R.id.tv_touch_recorder)
    TextView tvTouchRecorder;

    RxPermissions mPermissions;
    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        mPermissions = new RxPermissions(this);
        DaggerVoiceComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .voiceModule(new VoiceModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_voice; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        setVrvVoice();
        mPresenter.initData();
    }




    @Override
    public void showLoading() {
        loadingDialog = DialogUtils.getInstance().getLoadingDialog(this, mPresenter.getCancleListener());
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }


    /**
     * 显示对话框
     *
     * @param title      标题
     * @param content    内容
     * @param dialogType 对话框类型 成功？失败 SweetAlertDialog.ERROR_TYPE...
     */
    @Override
    public void showDialog(String title, String content, int dialogType) {
        mDialog = DialogUtils.getInstance().getDialog(this, title, content, dialogType, false, sweetAlertDialog -> mDialog.dismiss());
        mDialog.show();
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @Override
    public RxPermissions getPermissions() {
        return mPermissions;
    }

    @Override
    public void setAdapter(VoiceAdapter mAdapter) {
        rvVoiceJilu.setLayoutManager(new GridLayoutManager(this, 1));
        rvVoiceJilu.setAdapter(mAdapter);
    }

    @Override
    public TextView getTvTouchRecorder() {
        return tvTouchRecorder;
    }

    @Override
    public VoiceRecorderView getVrvVoice() {
        return vrvVoice;
    }

    @Override
    public void setVrvVoice() {
        vrvVoice.setShowMoveUpToCancelHint("松开手指，取消发送");
        vrvVoice.setShowReleaseToCancelHint("手指上滑，取消发送");
        /**
         * 设置文件存放目录，存放路径如：全局缓存文件/voicess/
         * 默认不设置，路径存放为：全局缓存文件/voice/
         */
//        PathUtil.getInstance().createDirs("voicess", this);

        /**
         * 自定义语音录制过程中，声音大小的动画，默认使用库文件中的动画，
         * 目前默认需要设置14张图片，以后更新自定义动画帧数
         */
//        vrvVoice.setDrawableAnimation(Drawable[] animationDrawable);

    }
}
