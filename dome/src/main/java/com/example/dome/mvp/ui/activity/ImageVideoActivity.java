package com.example.dome.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.dome.R;
import com.example.dome.di.component.DaggerImageVideoComponent;
import com.example.dome.di.module.ImageVideoModule;
import com.example.dome.mvp.contract.ImageVideoContract;
import com.example.dome.mvp.presenter.ImageVideoPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.tools.imagepicker.adapter.GridImageAdapter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DialogUtils;
import com.luck.picture.lib.config.PictureConfig;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * ================================================================
 * 创建时间：2018-11-1 10:20:51
 * 创 建 人：Mr.常
 * 文件描述：图片视频页面
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
public class ImageVideoActivity extends BaseActivity<ImageVideoPresenter> implements ImageVideoContract.View {


    @BindView(R.id.rv_new_image)
    RecyclerView rvNewImage;
    @BindView(R.id.rv_new_video)
    RecyclerView rvNewVideo;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerImageVideoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .imageVideoModule(new ImageVideoModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_image_video; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
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
    public Activity getActivity() {
        return ImageVideoActivity.this;
    }


    @Override
    public void setImageAdapter(GridImageAdapter mAdapter) {
        rvNewImage.setLayoutManager(new GridLayoutManager(this, 3));
        rvNewImage.setAdapter(mAdapter);
    }

    @Override
    public void setVideoAdapter(GridImageAdapter mAdapter) {
        rvNewVideo.setLayoutManager(new GridLayoutManager(this, 3));
        rvNewVideo.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    // 图片选择结果回调
                    mPresenter.setimageList(data);
                    break;
                case PictureConfig.CHOOSE_REQUEST:
                    // 视频选择结果回调
                    mPresenter.setVideoList(data);
                    break;
            }
        }
    }
}
