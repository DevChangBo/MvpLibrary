/**
 * Copyright 2017 JessYan
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.dome.app;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.widget.ImageView;

import com.blankj.utilcode.util.Utils;
import com.example.dome.MVPConfig;
import com.example.dome.R;
import com.example.dome.app.greendao.DaoMaster;
import com.jess.arms.base.App;
import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DialogUtils;
import com.jess.arms.utils.logger.AndroidLogAdapter;
import com.jess.arms.utils.logger.Logger;
import com.jess.arms.utils.logger.PrettyFormatStrategy;
import com.jess.arms.widget.dialog.SweetAlertDialog;
import com.lzy.ninegrid.NineGridView;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

//import io.vov.vitamio.Vitamio;

/**
 * ================================================
 * 展示 {@link AppLifecycles} 的用法
 * <p>
 * Created by JessYan on 04/09/2017 17:12
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class AppLifecyclesImpl implements AppLifecycles {

    @Override
    public void attachBaseContext(Context base) {
        MultiDex.install(base);  //这里比 onCreate 先执行,常用于 MultiDex 初始化,插件化框架的初始化
    }

    @Override
    public void onCreate(Application application) {
        initReleaseData(application);
    }

    @Override
    public void onTerminate(Application application) {

    }

    private void initReleaseData(Application application) {
        /*初始化日志打印*/
        initLog();
//        //必须写这个，初始化加载库文件
//        Vitamio.isInitialized(application);

        /*工具类初始化*/
        Utils.init(application);

        //leakCanary内存泄露检查
        ArmsUtils.obtainAppComponentFromContext(application).extras().put(RefWatcher.class.getName(), MVPConfig.USE_CANARY ? LeakCanary.install(application) : RefWatcher.DISABLED);
        //扩展 AppManager ZFConfig
        ArmsUtils.obtainAppComponentFromContext(application).appManager().setHandleListener((appManager, message) -> {
            switch (message.what) {
                //case 0:
                //do something ...
                //   break;
            }
        });
        /*数据库初始化*/
        initDB(application);
        /*初始化九图加载器*/
        NineGridView.setImageLoader(new NineGridView.ImageLoader() {
            @Override
            public void onDisplayImage(Context context, ImageView imageView, String url) {
                Picasso.with(context).load(url)//
                        .placeholder(R.drawable.ic_default_image)//
                        .error(R.drawable.ic_default_image)//
                        .into(imageView);
            }

            @Override
            public Bitmap getCacheImage(String url) {
                return null;
            }
        });
        Timber.d("初始化完成");
    }






    private void initLog() {
        if (MVPConfig.LOG_DEBUG) {
            /*初始化日志打印工具,Timber内部可以动态的切换成任何日志框架(打印策略)进行日志打印,内部可以做到同时使用多个策略,比如添加三个策略,一个打印日志,一个将日志保存本地,一个将日志上传服务器*/
            Logger.addLogAdapter(new AndroidLogAdapter(PrettyFormatStrategy.newBuilder()
                    .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                    .methodCount(1)         // (Optional) How many method line to show. Default 2
                    .tag(MVPConfig.LOG_TAG)
                    .methodOffset(5).build()));
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected void log(int priority, String tag, String message, Throwable t) {
                    com.jess.arms.utils.logger.Logger.log(priority, tag, message, t);
                }
            });
            ButterKnife.setDebug(true);
        }
    }

    private void initDB(Application application) {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(application.getApplicationContext(), MVPConfig.DBNAME);

        //将数据库操作对象写入全局，得到app实例后可使用数据库名称获取对应自定义参数
        ArmsUtils.obtainAppComponentFromContext(application.getApplicationContext()).extras().put(MVPConfig.DBNAME, new DaoMaster(openHelper.getWritableDb()).newSession());
    }

}
