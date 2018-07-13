/**
  * Copyright 2017 JessYan
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *      http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.example.dome.app;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.webkit.WebView;

import com.blankj.utilcode.util.SDCardUtils;
import com.example.dome.MVPConfig;
import com.example.dome.app.api.Api;
import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.di.module.GlobalConfigModule;
import com.jess.arms.http.RequestInterceptor;
import com.jess.arms.http.logging.LoggingInterceptor;
import com.jess.arms.integration.ConfigModule;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DeviceUtils;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;

/**
 * ================================================
 * app 的全局配置信息在此配置,需要将此实现类声明到 AndroidManifest 中
 * <p>
 * Created by JessYan on 12/04/2017 17:25
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public final class GlobalConfiguration implements ConfigModule {
//    public static String sDomain = Api.APP_DOMAIN;

    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
         /*关闭框架的日志打印，使用自己的打印方式*/
        builder.printHttpLogLevel(RequestInterceptor.Level.NONE);
         /*配置全局缓存文件夹*/
         String filePath ;
        if (SDCardUtils.isSDCardEnable()) {
            filePath = SDCardUtils.getSDCardPath() + MVPConfig.CACHE_FILE_NAME;
        } else {
            filePath = context.getFilesDir().getPath() + MVPConfig.CACHE_FILE_NAME;
        }
        Log.d("Config", "项目存储文件的全局目录：" + filePath);
        builder.cacheFile(new File(filePath ))
                .baseurl(Api.APP_DOMAIN)
                .addInterceptor(new LoggingInterceptor.Builder()
                        .loggable(true)
                        .request("==Request==")
                        .response("==Response==")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Content-Encoding", "gzip")
                        .addHeader("Authorization", "xxxxxxxxxxxxxxxxxxxxx")
                        .addHeader("Version", DeviceUtils.getVersionName(context))
                        .addHeader("Accept", "application/json;charset=utf-8;encoding=gzip;language=zh")/*指定客户端能够接收的内容类型*/
                        .addHeader("Cache-Control", "no-cache")
//                        .addHeader("Connection", "close")
//                        .addHeader("Cookie", "xxxxxxxxxxxxxxxxxxx")
                        .addHeader("From", "dev_zwy@aliyun.com")
                        .addHeader("Date", new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US).format(new Date()))
                        .addHeader("User-Agent", new WeakReference<WebView>(new WebView(context)).get().getSettings().getUserAgentString())
                        .build())
//                .baseurl(() -> HttpUrl.parse(mDomainURL))/*如果url在请求前需要从网络获取时打开该链接*/
                .globalHttpHandler(new HttpIntercept())
                .globalHttpHandler(new GlobalHttpHandlerImpl(context))
                // 用来处理 rxjava 中发生的所有错误,rxjava 中发生的每个错误都会回调此接口
                // rxjava必要要使用ErrorHandleSubscriber(默认实现Subscriber的onError方法),此监听才生效
                .responseErrorListener(new ResponseErrorListenerImpl())
                .gsonConfiguration((context1, gsonBuilder) -> {//这里可以自己自定义配置Gson的参数
                    gsonBuilder
                            .serializeNulls()//支持序列化null的参数
                            .enableComplexMapKeySerialization();//支持将序列化key为object的map,默认只能序列化key为string的map
                })
                .retrofitConfiguration((context1, retrofitBuilder) -> {//这里可以自己自定义配置Retrofit的参数,甚至你可以替换系统配置好的okhttp对象
//                    retrofitBuilder.addConverterFactory(FastJsonConverterFactory.create());//比如使用fastjson替代gson
                })
                .okhttpConfiguration((context1, okhttpBuilder) -> {//这里可以自己自定义配置Okhttp的参数
//                    okhttpBuilder.sslSocketFactory(); //支持 Https,详情请百度
                    okhttpBuilder.writeTimeout(MVPConfig.NETWORK_TIME_OUT, TimeUnit.SECONDS);
                    okhttpBuilder.readTimeout(MVPConfig.NETWORK_TIME_OUT,TimeUnit.SECONDS);// 读取超时时间设置
                    //使用一行代码监听 Retrofit／Okhttp 上传下载进度监听,以及 Glide 加载进度监听 详细使用方法查看 https://github.com/JessYanCoding/ProgressManager
                    ProgressManager.getInstance().with(okhttpBuilder);
                    //让 Retrofit 同时支持多个 BaseUrl 以及动态改变 BaseUrl. 详细使用请方法查看 https://github.com/JessYanCoding/RetrofitUrlManager
                    RetrofitUrlManager.getInstance().with(okhttpBuilder);
                })
                .rxCacheConfiguration((context1, rxCacheBuilder) -> {//这里可以自己自定义配置RxCache的参数
                    rxCacheBuilder.useExpiredDataIfLoaderNotAvailable(true);
                });
    }

    @Override
    public void injectAppLifecycle(Context context, List<AppLifecycles> lifecycles) {
        // AppLifecycles 的所有方法都会在基类 Application 的对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        // 可以根据不同的逻辑添加多个实现类
        lifecycles.add(new AppLifecyclesImpl());
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
        // ActivityLifecycleCallbacks 的所有方法都会在 Activity (包括三方库) 的对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        // 可以根据不同的逻辑添加多个实现类
        lifecycles.add(new ActivityLifecycleCallbacksImpl());
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        lifecycles.add(new FragmentManager.FragmentLifecycleCallbacks() {

            @Override
            public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                // 在配置变化的时候将这个 Fragment 保存下来,在 Activity 由于配置变化重建是重复利用已经创建的Fragment。
                // https://developer.android.com/reference/android/app/Fragment.html?hl=zh-cn#setRetainInstance(boolean)
                // 如果在 XML 中使用 <Fragment/> 标签,的方式创建 Fragment 请务必在标签中加上 android:id 或者 android:tag 属性,否则 setRetainInstance(true) 无效
                // 在 Activity 中绑定少量的 Fragment 建议这样做,如果需要绑定较多的 Fragment 不建议设置此参数,如 ViewPager 需要展示较多 Fragment
                f.setRetainInstance(true);
            }

            @Override
            public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                ((RefWatcher) ArmsUtils
                        .obtainAppComponentFromContext(f.getActivity())
                        .extras()
                        .get(RefWatcher.class.getName()))
                        .watch(f);
            }
        });
    }

}
