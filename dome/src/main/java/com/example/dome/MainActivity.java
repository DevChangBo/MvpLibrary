package com.example.dome;

import android.Manifest;
import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;

import com.jess.arms.utils.DialogUtils;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.widget.dialog.SweetAlertDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import timber.log.Timber;

/**
 * ================================================================
 * 创建时间：2018-6-8 11:45:16
 * 创 建 人：Mr.常
 * 文件描述：
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
public class MainActivity extends AppCompatActivity {
    public SweetAlertDialog mDialog;//对话框
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        PermissionUtil.requestPermission(new PermissionUtil.RequestPermission() {
                                             @Override
                                             public void onRequestPermissionSuccess() {
                                                 showDialog("提示", "授权成功", SweetAlertDialog.SUCCESS_TYPE);
                                             }

                                             @Override
                                             public void onRequestPermissionFailure() {
                                                 showDialog("提示", "您有未授予的权限，可能导致部分功能闪退，请点击\"设置\"授权相关权限", SweetAlertDialog.ERROR_TYPE);
                                             }
                                         }, new RxPermissions(this), getRxErrorHandler(getApplication()),
                /**
                 * 注释后面有 1 的为显示确认访问、无 1 的为隐藏默认访问
                 */
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.WRITE_SETTINGS);
    }



    public void showDialog(String title, String content, int dialogType) {
    mDialog=DialogUtils.getInstance().getDialog(this,title,content,dialogType,false, new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {
            mDialog.dismiss();
        }
    });
        mDialog.show();
    }

    public RxErrorHandler getRxErrorHandler(Application mApplication){
        return   RxErrorHandler
                .builder()
                .with(mApplication)
                .responseErrorListener((context, t) -> Timber.e("异常"))
                .build();
    }
}
