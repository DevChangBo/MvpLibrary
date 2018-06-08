package com.jess.arms.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;

import com.jess.arms.R;
import com.jess.arms.widget.dialog.SweetAlertDialog;
import com.jess.arms.widget.loading.ACProgressConstant;
import com.jess.arms.widget.loading.ACProgressFlower;

/**
 * ================================================================
 * 创建时间：2017/10/31 17:14
 * 创建人：赵文贇
 * 文件描述：
 * 看淡身边的虚伪，静心宁神做好自己。路那么长，无愧走好每一步。
 * ================================================================
 */
public class DialogUtils {
    private static DialogUtils mDialogUtils;

    private DialogUtils() {
    }

    public static DialogUtils getInstance() {
        mDialogUtils = mDialogUtils == null ? new DialogUtils() : mDialogUtils;
        return mDialogUtils;
    }

    public Dialog getLoadingDialog(Activity mActivity, DialogInterface.OnCancelListener mCancelListener) {
        ACProgressFlower dialog = new ACProgressFlower.Builder(mActivity, R.style.NonDimACProgressDialog)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();
        dialog.setCanceledOnTouchOutside(false);
        if (mCancelListener != null) dialog.setOnCancelListener(mCancelListener);
        dialog.show();
        return dialog;
    }


    /**
     * 获取一个对话框
     *
     * @param mActivity              上下午
     * @param title                 标题
     * @param content               内容
     * @param dialogType            对话框类型  SweetAlertDialog.ERROR_TYPE...
     * @param isCancelable          对话框是否可以取消
     * @param mOnSweetClickListener 确定按钮点击事件
     * @return
     */
    public SweetAlertDialog getDialog(Activity mActivity, String title, String content, int dialogType, boolean isCancelable, SweetAlertDialog.OnSweetClickListener mOnSweetClickListener) {
        SweetAlertDialog mDialog = new SweetAlertDialog(mActivity, dialogType)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmClickListener(mOnSweetClickListener);
        mDialog.setCancelable(isCancelable);
        mDialog.setConfirmText("确定");
        return mDialog;
    }


    /**
     * 展示一个从下方弹出图库选择提示框
     * 更换图像是弹出的选择框
     * @param activity
     * @param onImagesListListenr 图库按钮监听
     */
    public static void showBottomSheetDialog_SeletorImages(Activity activity,
                                                           ActionSheetDialog.OnSheetItemClickListener onImagesListListenr) {
        new ActionSheetDialog(activity)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .setTitle("请选择图片来源")
                .addSheetItem("图库", ActionSheetDialog.SheetItemColor.Blue, onImagesListListenr)
//                .addSheetItem("相机", ActionSheetDialog.SheetItemColor.Blue, onCameraListener)
                .show();
    }


    /**
     * 展示一个从下方弹出图库选择提示框
     * H5页面文件选择框点击时弹出时图库选择
     * @param activity
     * @param onImagesListListenr 图库按钮监听
     */
    public static void showH5_SeletorImages(Activity activity,
                                                           ActionSheetDialog.OnSheetItemClickListener onImagesListListenr,ActionSheetDialog.OnSheetItemClickListener onVideoListListenr,ActionSheetDialog.OnCanceledOnTouchListener onCanceledOnTouchListener) {
        new ActionSheetDialog(activity)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(false)
                .addOnCanceledOnTouchListener(onCanceledOnTouchListener)   //取消按钮事件
                .setTitle("请选择图片来源")
                .addSheetItem("图库", ActionSheetDialog.SheetItemColor.Blue, onImagesListListenr)
                .addSheetItem("视频", ActionSheetDialog.SheetItemColor.Blue, onVideoListListenr)
                .show();
    }



    /**
     * 展示一个从下方弹出图库选择提示框
     * 事件选择事件点击时弹出的选择框
     * @param activity
     */
    public static void showBottomSheetDialog_XingZhi(Activity activity,
                                                           ActionSheetDialog.OnSheetItemClickListener onYibanListListenr,ActionSheetDialog.
                                                             OnSheetItemClickListener onZhongDaListListenr,
                                                     ActionSheetDialog.OnSheetItemClickListener onJinJiListListenr) {
        new ActionSheetDialog(activity)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .setTitle("性质选择")
                .addSheetItem("一般", ActionSheetDialog.SheetItemColor.text_dialog_button, onYibanListListenr)
                .addSheetItem("重大", ActionSheetDialog.SheetItemColor.text_dialog_button, onZhongDaListListenr)
                .addSheetItem("紧急", ActionSheetDialog.SheetItemColor.text_dialog_button, onJinJiListListenr)
                .show();
    }
}
