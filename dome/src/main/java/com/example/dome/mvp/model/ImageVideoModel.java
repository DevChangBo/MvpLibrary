package com.example.dome.mvp.model;

import android.app.Activity;
import android.app.Application;
import android.view.View;

import com.example.dome.R;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.example.dome.mvp.contract.ImageVideoContract;
import com.jess.arms.tools.imagepicker.adapter.GridImageAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================================
 * 创建时间：2018-11-1 10:20:51
 * 创 建 人：Mr.常
 * 文件描述：move
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
@ActivityScope
public class ImageVideoModel extends BaseModel implements ImageVideoContract.Model {
    private Gson mGson;
    private Application mApplication;

    private Activity mActivity;
    private List<LocalMedia> imageSelectList = new ArrayList<>();
    private List<LocalMedia> videoSelectList = new ArrayList<>();
    @Inject
    public ImageVideoModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
        this.mActivity=null;
    }

    @Override
    public void setActivity(Activity mActivity) {
        this.mActivity=mActivity;
    }

    /**
     * 给图片列表赋值
     *
     * @param imageSelectList
     */
    @Override
    public void setImageSelectList(List<LocalMedia> imageSelectList) {
        this.imageSelectList=imageSelectList;
    }

    @Override
    public List<LocalMedia> getImageSelectList() {
        return imageSelectList;
    }

    /**
     * 给视频列表赋值
     *
     * @param videoSelectList
     */
    @Override
    public void setVideoSelectList(List<LocalMedia> videoSelectList) {
        this.videoSelectList=videoSelectList;
    }

    @Override
    public List<LocalMedia> getVideoSelectList() {
        return videoSelectList;
    }

    /**
     * 图片选择列表点击回调
     * @return
     */
    @Override
    public GridImageAdapter.onAddPicClickListener setOnImageAddPicClickListener(boolean mode) {
        return new GridImageAdapter.onAddPicClickListener() {
            @Override
            public void onAddPicClick() {
                if (mode) {
                    // 进入相册 以下是例子：不需要的api可以不写
                    PictureSelector.create(mActivity)
                            .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                            .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                            .maxSelectNum(9)// 最大图片选择数量
                            .minSelectNum(1)// 最小选择数量
                            .imageSpanCount(4)// 每行显示个数
                            .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                            .previewImage(true)// 是否可预览图片
                            .previewVideo(false)// 是否可预览视频
                            .enablePreviewAudio(false) // 是否可播放音频
                            .isCamera(true)// 是否显示拍照按钮
                            .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                            //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                            //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                            .compress(true)// 是否压缩
                            .synOrAsy(true)//同步true或异步false 压缩 默认同步
                            //.compressSavePath(getPath())//压缩图片保存地址
                            //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                            .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                            .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
                            .isGif(false)// 是否显示gif图片
                            .enableCrop(true)// 是否裁剪
                            .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                            .isDragFrame(true)// 是否可拖动裁剪框(固定)
                            .circleDimmedLayer(true)// 是否圆形裁剪
                            .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                            .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                            .rotateEnabled(true) // 裁剪是否可旋转图片
                            .scaleEnabled(true)// 裁剪是否可放大缩小图片
//                            .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                            //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                            .withAspectRatio(4, 3)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                            .openClickSound(false)// 是否开启点击声音
                            .selectionMedia(imageSelectList)// 是否传入已选图片
                            //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                            .minimumCompressSize(100)// 小于100kb的图片不压缩
                            //.videoQuality()// 视频录制质量 0 or 1
                            //.videoSecond()//显示多少秒以内的视频or音频也可适用
//                            .recordVideoSecond()//录制视频秒数 默认60s
                            .forResult(1);//结果回调onActivityResult code
                } else {
                    // 视频
                    PictureSelector.create(mActivity)
                            .openGallery(PictureMimeType.ofVideo())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                            .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                            .maxSelectNum(9)// 最大图片选择数量
                            .minSelectNum(1)// 最小选择数量
                            .imageSpanCount(4)// 每行显示个数
                            .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                            .previewImage(false)// 是否可预览图片
                            .previewVideo(true)// 是否可预览视频
                            .enablePreviewAudio(false) // 是否可播放音频
                            .isCamera(true)// 是否显示拍照按钮
                            .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                            //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                            //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                            .enableCrop(false)// 是否裁剪
                            .compress(true)// 是否压缩
                            .synOrAsy(true)//同步true或异步false 压缩 默认同步
                            //.compressSavePath(getPath())//压缩图片保存地址
                            //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                            .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                            .withAspectRatio(0, 0)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                            .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
                            .isGif(false)// 是否显示gif图片
                            .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                            .circleDimmedLayer(false)// 是否圆形裁剪
                            .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                            .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                            .openClickSound(false)// 是否开启点击声音
                            .selectionMedia(videoSelectList)// 是否传入已选图片
                            //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                            //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                            //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                            .minimumCompressSize(100)// 小于100kb的图片不压缩
                            //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                            //.rotateEnabled(true) // 裁剪是否可旋转图片
                            //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                            //.videoQuality()// 视频录制质量 0 or 1
                            //.videoSecond()//显示多少秒以内的视频or音频也可适用
                            .recordVideoSecond(10)//录制视频秒数 默认60s
                            .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                }
            }

        };
    }

    /**
     * 设置图片/视频适配的的点击事件
     *
     * @param mAdapter
     * @param type
     */
    @Override
    public void setOnItemClickListener(GridImageAdapter mAdapter, int type) {
        mAdapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                switch (type){
                    case 1:
                        if (imageSelectList.size()>0){
                            LocalMedia media = imageSelectList.get(position);
                            String pictureType = media.getPictureType();
                            int mediaType = PictureMimeType.pictureToVideo(pictureType);
                            if (mediaType==1)
                                // 预览图片 可自定长按保存路径 *注意 .themeStyle(themeId)；不可少，否则闪退...
                                PictureSelector.create(mActivity).themeStyle(R.style.picture_default_style).openExternalPreview(position, imageSelectList);
                        }
                        break;
                    case 2:
                        if (videoSelectList.size()>0){
                            LocalMedia media = videoSelectList.get(position);
                            String pictureType = media.getPictureType();
                            int mediaType = PictureMimeType.pictureToVideo(pictureType);
                            if (mediaType==2)
                                // 预览视频
                                PictureSelector.create(mActivity).externalPictureVideo(media.getPath());
                        }
                        break;
                }
            }
        });

    }
}