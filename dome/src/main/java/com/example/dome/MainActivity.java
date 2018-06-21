package com.example.dome;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.dome.util.BottomBar;
import com.example.dome.util.BottomBarTab;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ================================================================
 * 创建时间：2018-6-8 11:45:16
 * 创 建 人：Mr.常
 * 文件描述：
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.contentContainer)
    FrameLayout contentContainer;
    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    /**
     * 提供 AppComponent(提供所有的单例对象)给实现类,进行 Component 依赖
     *
     * @param appComponent
     */
    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    /**
     * 初始化 View,如果initView返回0,框架则不会调用{@link Activity#setContentView(int)}
     *
     * @param savedInstanceState
     * @return
     */
    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    @Override
    public void initData(Bundle savedInstanceState) {
        bottomBar.addItem(new BottomBarTab(this, R.mipmap.ic_news, "新闻"))
                .addItem(new BottomBarTab(this, R.mipmap.ic_video, "视频"))
                .addItem(new BottomBarTab(this, R.mipmap.ic_jiandan, "煎蛋"))
                .addItem(new BottomBarTab(this, R.mipmap.ic_my, "我的"));
        bottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
//                getSupportDelegate().showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }
}
