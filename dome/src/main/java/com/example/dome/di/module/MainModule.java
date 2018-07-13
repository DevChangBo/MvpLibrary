package com.example.dome.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.example.dome.mvp.contract.MainContract;
import com.example.dome.mvp.model.MainModel;

/**
 * ================================================================
 * 创建时间：2018-7-13 10:16:43
 * 创 建 人：Mr.常
 * 文件描述：
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
@Module
public class MainModule {
    private MainContract.View view;

    /**
     * 构建MainModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MainModule(MainContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MainContract.View provideMainView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MainContract.Model provideMainModel(MainModel model) {
        return model;
    }
}