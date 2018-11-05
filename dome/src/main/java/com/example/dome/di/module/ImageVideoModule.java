package com.example.dome.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.example.dome.mvp.contract.ImageVideoContract;
import com.example.dome.mvp.model.ImageVideoModel;

/**
 * ================================================================
 * 创建时间：2018-11-1 10:20:52
 * 创 建 人：Mr.常
 * 文件描述：
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
@Module
public class ImageVideoModule {
    private ImageVideoContract.View view;

    /**
     * 构建ImageVideoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ImageVideoModule(ImageVideoContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ImageVideoContract.View provideImageVideoView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ImageVideoContract.Model provideImageVideoModel(ImageVideoModel model) {
        return model;
    }
}