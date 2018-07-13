package com.example.dome.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.example.dome.di.module.MainModule;

import com.example.dome.mvp.ui.activity.MainActivity;

/**
 * ================================================================
 * 创建时间：2018-7-13 10:16:43
 * 创 建 人：Mr.常
 * 文件描述：
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
@ActivityScope
@Component(modules = MainModule.class, dependencies = AppComponent.class)
public interface MainComponent {
    void inject(MainActivity activity);
}