package com.example.dome.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.example.dome.mvp.contract.VoiceContract;
import com.example.dome.mvp.model.VoiceModel;

/**
 * ================================================================
 * 创建时间：2018-11-16 11:11:48
 * 创 建 人：Mr.常
 * 文件描述：
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
@Module
public class VoiceModule {
    private VoiceContract.View view;

    /**
     * 构建VoiceModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public VoiceModule(VoiceContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    VoiceContract.View provideVoiceView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    VoiceContract.Model provideVoiceModel(VoiceModel model) {
        return model;
    }
}