package com.example.dome.mvp.contract;

import android.app.Activity;
import android.content.Intent;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.jess.arms.tools.imagepicker.adapter.GridImageAdapter;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * ================================================================
 * 创建时间：2018-11-1 10:20:51
 * 创 建 人：Mr.常
 * 文件描述：
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
public interface ImageVideoContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        Activity getActivity();
        void setImageAdapter(GridImageAdapter mAdapter);
        void setVideoAdapter(GridImageAdapter mAdapter);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        void setActivity(Activity mActivity);

        /**
         * 给图片列表赋值
         * @param imageSelectList
         */
        void setImageSelectList(List<LocalMedia> imageSelectList);
        List<LocalMedia> getImageSelectList();
        /**
         * 给视频列表赋值
         * @param videoSelectList
         */
        void setVideoSelectList(List<LocalMedia> videoSelectList);
        List<LocalMedia> getVideoSelectList();
        /**
         * 图片列表选择点击事件回调
         * @return
         */
        GridImageAdapter.onAddPicClickListener setOnImageAddPicClickListener(boolean mode);

        /**
         * 设置图片/视频适配的的点击事件
         * @param mAdapter
         */
        void setOnItemClickListener(GridImageAdapter mAdapter,int type);
    }
}
