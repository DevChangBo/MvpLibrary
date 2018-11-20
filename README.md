# MVPLibrary 是以JessYanCoding大神的MVP开源框架为主体、进行了一些扩展增删处理、加入了一些项目中常用的及自己开发的组件、是一个整合了大量主流开源项目的 Android MVP 快速搭建框架, 其中包含 Dagger2、Retrofit、RxJava 以及 RxLifecycle、RxCache 等 Rx 系三方库, 并且提供 UI 自适应方案, 本框架将它们结合起来, 并全部使用 Dagger2 管理。

#### MVPLibrary 是一个新的 MVP 架构, 适合中小型项目, 旨在解决传统 MVP 类和接口太多, 并且 Presenter 和 View 通过接口通信过于繁琐, 重用 Presenter 代价太大等问题

1. 改造 Android 官方架构组件 ViewModel 、一行代码监听 App 中所有网络链接的上传以及下载进度, 以及 Glide 加载进度、以最简洁的 Api 让 Retrofit 同时支持多个 BaseUrl 以及动态改变 BaseUrl

2. 特性 通用框架, 适合所有类型的项目, 支持大型项目的开发, 兼容组件化开发, 可作为组件化的 Base 库

3. Base 基类(BaseActivity, BaseFragment, BaseApplication ...)

4. MVP 基类(IModel, IVIew, IPresenter ...)

5. 框架高度可自定义化 (ConfigModule), 可在不修改框架源码的情况下对 Retoift, Okhttp, RxCache, Gson 等框架的特有属性进行自定义化配置, 可在不修改框架源码的情况下向 BaseApplication, BaseActivity, BaseFragment 的对应生命周期中插入任意代码, 并且框架独有的 ConfigModule 配置类, 可在不修改框架源码的情况下为框架轻松扩展任何新增功能

6. 独创的 RxLifeCycle 应用方式, 可在不继承 RxLifeCycle 提供的 Activity 和 Fragment 的情况下, 正常使用 RxLifeCycle 的所有功能, 且使用方式不变

7. 独创的建造者模式 Module (GlobalConfigModule), 可实现使用 Dagger2 向框架任意位置注入自定义参数, 可轻松扩展任意自定义参数

8. 全局使用 Dagger2 管理 (将所有模块使用 Dagger2 连接起来, 绝不是简单的使用)

9. 全局监听整个 App 所有 Activity 以及 Fragment 的生命周期 (包括三方库), 并可向其生命周期内插入任意代码

10. 全局监听 Http Request(请求参数, Headers ...), Response (服务器返回的结果, Headers, 耗时 ...)等信息(包括 Glide 的请求), 可解析 json 后根据状态码做相应的全局操作以及数据加密, Cookie 管理等操作

11. 全局管理所有 Activity (包括三方库的 Activity), 可实现在整个 App 任意位置, 退出所有 Activity, 以及拿到前台 Activity 做相应的操作(如您可以在 App 任何位置做弹出 Dialog 的操作)

12. 全局 Rxjava 错误处理, 错误后自动重试, 捕捉整个应用的所有错误

13. 全局 UI 自适应

14. 屏幕适配方案 今日头条的AndroidAutoSize

15. 图片加载类 ImageLoader 使用策略模式和建造者模式, 轻松切换图片加载框架, 方便功能扩展

16. 网络请求日志打印封装(提供解析后的服务器的请求信息和服务器的响应信息, 按可自定义的任意格式输出打印日志, 内置一个漂亮的打印格式模板)

17. 框架内自有组件的缓存机制封装(框架内可缓存内容的组件都提供有接口供外部开发者自定义缓存机制)

#### 导入框架
	dependencies {
	        implementation 'com.github.DevChangBo:MvpLibrary:v1.1.0'
	}

####  引用 config.gradle 本框架提供一个含有大量第三方库的 config.gradle 文件 (里面的所有第三方库并不会全部被引入到项目中, 只是作为变量方便项目中多个位置进行引用, 特别适用于多 Module 的项目), 用于第三方库的版本管理, 将 config.gradle 复制进根目录, 并在项目的顶级 build.gradle 中引用它
    apply from: "config.gradle" //这里表示引用config.gradle文件
     buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:x.y.z'
    }
    }

    allprojects {
    repositories {
        google() //AndroidStudio v3.0 可以使用 google() 替代 maven { url "https://maven.google.com" }
        jcenter()
        maven { url "https://jitpack.io" }//注意!!! RxCache 是托管于 jitpack 仓库的, 如果没有这一段代码将永远依赖不了 RxCache
    }
    }

     task clean(type: Delete) {
    delete rootProject.buildDir
     }


####  配置 build.gradle 依赖 Dagger2 本框架全部使用 Dagger2 管理, 所以必须依赖 Dagger2, 找到 app 的 build.gradle, 加入如下代码
    dependencies {
           annotationProcessor rootProject.ext.dependencies["butterknife-compiler"] //Butterknife 插件, 很多人因为没加这个而报错, 切记!!!
    annotationProcessor rootProject.ext.dependencies["dagger2-compiler"]//依赖插件
    }

#### 配置 AndroidManifest

1. 添加权限


    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>

2. 指定 Application 本框架想要正常运行需要使用框架提供的 BaseApplication, 当然您也可以自定义一个 Application 继承于它, 也可以不用继承, 直接将 BaseApplication 的代码复制到您自定义的 Application 中 (里面只有几行代码), 但是我并不推荐您使用后面的两种方式, 因为本框架已经向开发者提供了 ConfigModule#injectAppLifecycle 方法, 可以在运行时动态的向 BaseApplication 的任意生命周期中插入任意代码
       
       
        <application
          android:name="com.jess.arms.base.BaseApplication">
     </application>

3. 配置 AndroidAutoSize  的详细介绍请看这里 （https://juejin.im/post/5bce688e6fb9a05cf715d1c2）


      dependencies {
         implementation 'me.jessyan:autosize:x.y.z'
       }
只要依赖 AutoSize 就必须填写设计图尺寸, 否则报错, 不想使用 AutoSize 就不要依赖 AutoSize 只要填写完设计图的尺寸, AutoSize 就会自动启动, 以下 dp 尺寸是根据公式 px / (dpi / 160) 求出, 运算时使用测试机的 dpi 即可
       
        <meta-data
            android:name="design_width_in_dp"
            android:value="360"/>
        <meta-data
            android:name="design_height_in_dp"
            android:value="640"/>

4. 配置框架自定义属性 本框架使用和 Glide v3.0 相同的方式来配置自定义属性, 需要在 AndroidManifest 中声明它, 详情
 arms配置
 
 
        <meta-data
            android:name="me.jessyan.mvparms.demo.app.GlobalConfiguration"
            android:value="ConfigModule"/>

#### 快速开始

1. ConfigModule 用来给框架配置各种自定义属性和功能, 配合 GlobalConfigModule 使用, 功能非常强大
新建一个类实现 ConfigModule 接口, 并在 AndroidManifest 中声明


     public class GlobalConfiguration implements ConfigModule {
    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
     //使用 builder 可以为框架配置一些配置信息
     builder.baseurl(Api.APP_DOMAIN)
            .cacheFile(New File("cache"));
    }

    @Override
    public void injectAppLifecycle(Context context, List<AppLifecycles> lifecycles) {
     //向 Application的 生命周期中注入一些自定义逻辑
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
    //向 Activity 的生命周期中注入一些自定义逻辑
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
    //向 Fragment 的生命周期中注入一些自定义逻辑
    }
    }
      <application>
     <!--arms 配置-->
     <meta-data
         android:name="me.jessyan.mvparms.demo.app.GlobalConfiguration"
         android:value="ConfigModule"/>
      </application>

2. AppComponent
Application 的生命周期和 App 是一致的, 所以适合提供一些单例对象, 本框架使用 Dagger2 管理, 使用 AppComponent 来提供全局所有的单例对象, 现在在 App 的任何地方, 都可通过 BaseApplication (可自定义 Application, 实现 App 接口即可) 的 getAppComponent() (非静态) 方法 (快捷方法 ArmsUtils.obtainAppComponentFromContext(context)), 拿到 AppComponent 里面声明的所有单例对象


    @Singleton
    @Component(modules = {AppModule.class, ClientModule.class, GlobalConfigModule.class})
    public interface AppComponent {
    Application Application();

    //用于管理网络请求层,以及数据缓存层
    IRepositoryManager repositoryManager();

    //Rxjava错误处理管理类
    RxErrorHandler rxErrorHandler();

    OkHttpClient okHttpClient();

    //图片管理器,用于加载图片的管理类,默认使用glide,使用策略模式,可替换框架
    ImageLoader imageLoader();

    //gson
    Gson gson();

    //缓存文件根目录(RxCache和Glide的的缓存都已经作为子文件夹在这个目录里),应该将所有缓存放到这个根目录里,便于管理和清理,可在GlobeConfigModule里配置
    File cacheFile();

    //用于管理所有activity
    AppManager appManager();

    void inject(AppDelegate delegate);
    }

3. RepositoryManager 用来管理网络请求层以及数据缓存层, 以后可能添加数据库储存层, 专门提供给 Model 层做数据处理, 在 v1.5 版本前是使用 ServiceManager 和 CacheManager 来管理, 在 v1.5 版本之后统一使用 RepositoryManager 来管理

自行定义 Retrofit Service, 如下, 熟练 Retrofit 请忽略

    public interface CommonService {

    String HEADER_API_VERSION = "Accept: application/vnd.github.v3+json";

    @Headers({HEADER_API_VERSION})
    @GET("/users")
    Observable<List<User>> getUsers(@Query("since") int lastIdQueried, @Query("per_page") int perPage);
}
自行定义 RxCache Provider, 如下, 熟练 RxCache 请忽略

     public interface CommonCache {

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<List<User>>> getUsers(Observable<List<User>> oUsers, DynamicKey idLastUserQueried, EvictProvider evictProvider);

    }
在 Model 中通过 RepositoryManager#obtainRetrofitService() 或 RepositoryManager#obtainCacheService() 使用这些服务

     public Observable<List<User>> getUsers(int lastIdQueried, boolean update) {
    //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
    return Observable.just(mRepositoryManager
            .obtainRetrofitService(UserService.class)
            .getUsers(lastIdQueried, USERS_PER_PAGE))
            .flatMap(new Function<Observable<List<User>>, ObservableSource<List<User>>>() {
                @Override
                public ObservableSource<List<User>> apply(@NonNull Observable<List<User>> listObservable) throws Exception {
                    return mRepositoryManager.obtainCacheService(CommonCache.class)
                            .getUsers(listObservable
                                    , new DynamicKey(lastIdQueried)
                                    , new EvictDynamicKey(update))
                            .map(listReply -> listReply.getData());
                }
            });
     }

#### MVP 实战 定义业务逻辑 MVP, 继承 MVP 中各自的基类即可, 这里可以稍微粗力度的定义 MVP 类, 即无需每个页面 (Activity 和 Fragment) 都定义不同的 MVP 类, 可以按照相同的业务逻辑使用一组 MVP 类 (即使您使用 MVPArms 全家桶 一键生成这些文件, 也建议将以下基础看完)


1. Contract
这里根据 Google 官方的 MVP 架构,可以在 Contract 中定义 MVP 类的接口, 便于管理, 本框架无需定义 Presenter 接口, 所以在 Contract 中只定义 View 和 Model 的接口


     public interface UserContract {
	//对于经常在日常开发中使用到的关于 UI 的方法可以定义到 IView 中, 如显示隐藏进度条, 和显示文字消息
    interface View extends IView {
        void setAdapter(DefaultAdapter adapter);
        void startLoadMore();
        void endLoadMore();
    }
	//Model 层定义接口, 外部只需关心 Model 返回的数据, 无需关心内部细节, 即是否使用缓存
    interface Model extends IModel{
        Observable<List<User>> getUsers(int lastIdQueried, boolean update);
    }
    }

2. View
一般让 Activity 或 Fragment 实现 Contract 中定义的 View 接口, 供 Presenter 调用对应方法响应 UI, BaseActivity 默认注入 Presenter, 如想使用 Presenter, 必须将范型指定为 Presenter 的实现类 (虽然框架只可以指定一个范型, 但是可以自行生成并持有多个 Presenter, 达到复用的目的, 如何复用 Presenter?), 还需要实现 setupActivityComponent 来提供 Presenter 需要的 Component 和 Module (如这个页面逻辑简单, 并不需要 Presenter, 那就不要指定范型, 也不要实现 setupActivityComponent 方法)


    public class UserActivity extends BaseActivity<UserPresenter> implements UserContract.View {

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerUserComponent
                .builder()
                .appComponent(appComponent)
                .userModule(new UserModule(this))
                .build()
                .inject(this);

    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_user;
    }

    @Override
    protected void initData() {

    }
    }

3. Model
必须实现 Contract 的 Model 接口, 并且继承 BaseModel, 然后通过 IRepositoryManager 拿到需要的 Service 和 Cache, 为 Presenter 提供需要的数据 (是否使用缓存请自行选择)


    @ActivityScope
    public class UserModel extends BaseModel implements UserContract.Model{
    
     @Inject
    public UserModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }
    
    @Override
    public Observable<List<User>> getUsers(int lastIdQueried, boolean update) {
    	return mRepositoryManager.obtainRetrofitService(UserService.class)
    						 .getUsers();
    }
    }

4.Presenter
Presenter 在 MVP 中的大部分作用是实现业务逻辑代码, 从 Model 层获取数据, 在调用 View 层显示数据, 首先必须实现 BasePresenter, 并指定 View 和 Model 的范型, 注意一定要指定 Contract 中定义的接口, Presenter 需要的 View 和 Model, 都使用 Dagger2 来注入, 这样即解藕又方便测试, 怎么注入?

     @ActivityScope
     public class UserPresenter extends BasePresenter<UserContract.Model, UserContract.View> {

    @Inject
    public UserPresenter(UserContract.Model model, UserContract.View rootView) {
        super(model, rootView);
    }
    //这里定义业务方法, 响应用户的交互
    public void requestUsers(final boolean pullToRefresh) {
    ｝
     }

5. MVP Module
这里的 Module 可以提供当前业务逻辑所对应的 View 和 Model 接口 (Contract 中定义的接口) 的实现类, Model 需要 AppComponent 中提供的 RepositoryManager 来实现网络请求和数据缓存, 所以需要通过 Component 依赖 AppComponent 来拿到这个对象


    @Module
    public class UserModule {
    private UserContract.View view;

    //构建UserModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
    public UserModule(UserContract.View view) {
        this.view = view;
    }
   
    @ActivityScope
    @Provides
    UserContract.View provideUserView(){
        return this.view;
    }

    @ActivityScope
    @Provides
    UserContract.Model provideUserModel(UserModel model){
        return model;
    }
    }

6. MVP Component
这里需要注意的是此 Component 必须依赖 AppComponent, 这样才能提供 Model 需要的 RepositoryManager, 提供 inject() 方法就能将 Module 及 AppComponent 中提供的对象注入到对应的类中, inject() 方法中的参数不能是接口, 怎么注入?


    @ActivityScope
    @Component(modules = UserModule.class, dependencies = AppComponent.class)
    public interface UserComponent {
    void inject(UserActivity activity);
    }

7. Dagger Scope
在上面的代码中 ActivityScope 大量的出现在 Module 和 Component 中, Dagger2 使用 Scope 限制每个 Module 中提供的对象的生命周期, Dagger2 默认只提供一个 @Singleton Scope 即单例, 本框架默认只提供 @ActvityScope 和 @FragmentScope, 如有其他需求请自行实现, 在 Module 和 Component 中定义相同的 Scope 后, Module 中提供的对象的生命周期会和 Component 的生命周期进行绑定 (即在 Component 的生命周期内, 如需多次使用到 Moudle 中提供的对象, Dagger 只会调用一次带有 @Provide 注解的方法, 得到此对象)


8. MVP 总结
以后每个业务逻辑都需要重复构造这些类 (如何复用 Presenter?), 只是换个名字而已, 值得注意的是 MVP 刚开始使用时, 确实会觉得平白无故多了很多类, 非常的繁琐麻烦, 但是等业务逻辑代码越来越多时, 您会发现其中的好处, 逻辑清晰、解耦、便于团队协作、易测试、易定位错误, 并且现在本框架也提供了 Template 自动生成代码 来解决这个痛点, 让开发者更加愉快的使用本框架

#### 功能使用

1. App 全局配置信息(使用 Dagger 注入)
GlobalConfigModule 使用建造者模式将 App 的全局配置信息封装进 Module (使用 Dagger 注入到需要配置信息的地方), 可以配置 CacheFile、Interceptor 等, 甚至于 Retrofit、Okhttp、RxCache 都可以自定义配置, 因为使用的是建造者模式, 所以如您有其他配置信息需要使用 Dagger 注入, 直接就可以添加进 Builder, 并且不会影响到其他地方

GlobalConfigModule 需依赖于 ConfigModule 使用


    public class GlobalConfiguration implements ConfigModule {
    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
       //使用 builder 可以为框架配置一些配置信息
       builder.baseurl(Api.APP_DOMAIN)
              .gsonConfiguration((context12, gsonBuilder) -> {//这里可以自己自定义配置Gson的参数
                    gsonBuilder
                            .serializeNulls()//支持序列化null的参数
                            .enableComplexMapKeySerialization();//支持将序列化key为object的map,默认只能序列化key为string的map
                })
                .retrofitConfiguration((context1, retrofitBuilder) -> {//这里可以自己自定义配置Retrofit的参数,甚至您可以替换系统配置好的okhttp对象
     //                    retrofitBuilder.addConverterFactory(FastJsonConverterFactory.create());//比如使用fastjson替代gson
                })
                .okhttpConfiguration((context1, okhttpBuilder) -> {//这里可以自己自定义配置Okhttp的参数
                    okhttpBuilder.writeTimeout(10, TimeUnit.SECONDS);
                }).rxCacheConfiguration((context1, rxCacheBuilder) -> {//这里可以自己自定义配置RxCache的参数
            rxCacheBuilder.useExpiredDataIfLoaderNotAvailable(true);
    }
    }

2. 全局捕捉 Http 请求和响应
在 全局配置类 中通过 GlobalConfigModule.Builder.globalHttpHandler() 方法传入 GlobalHttpHandler


    public class GlobalConfiguration implements ConfigModule {
    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        builder.globalHttpHandler(new GlobalHttpHandler() {

                    /**
                     * 这里可以先客户端一步拿到每一次 Http 请求的结果, 可以先解析成 Json, 再做一些操作, 如检测到 token 过期后
                     * 重新请求 token, 并重新执行请求
                     *
                     * @param httpResult 服务器返回的结果 (已被框架自动转换为字符串)
                     * @param chain {@link okhttp3.Interceptor.Chain}
                     * @param response {@link Response}
                     * @return
                     */
                    @Override
                    public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
                        if (!TextUtils.isEmpty(httpResult) && RequestInterceptor.isJson(response.body().contentType())) {
                            try {
                                List<User> list = ArmsUtils.obtainAppComponentFromContext(context).gson().fromJson(httpResult, new TypeToken<List<User>>() {
                                }.getType());
                                User user = list.get(0);
                                Timber.w("Result ------> " + user.getLogin() + "    ||   Avatar_url------> " + user.getAvatarUrl());
                            } catch (Exception e) {
                                e.printStackTrace();
                                return response;
                            }
                        }
    
                        /* 这里如果发现 token 过期, 可以先请求最新的 token, 然后在拿新的 token 放入 Request 里去重新请求
                        注意在这个回调之前已经调用过 proceed(), 所以这里必须自己去建立网络请求, 如使用 Okhttp 使用新的 Request 去请求
                        create a new request and modify it accordingly using the new token
                        Request newRequest = chain.request().newBuilder().header("token", newToken)
                                             .build();
    
                        retry the request
    
                        response.body().close();
                        如果使用 Okhttp 将新的请求, 请求成功后, 再将 Okhttp 返回的 Response return 出去即可
                        如果不需要返回新的结果, 则直接把参数 response 返回出去即可*/
                        return response;
                    }
    
                    /**
                     * 这里可以在请求服务器之前拿到 {@link Request}, 做一些操作比如给 {@link Request} 统一添加 token 或者 header 以及参数加密等操作
                     *
                     * @param chain {@link okhttp3.Interceptor.Chain}
                     * @param request {@link Request}
                     * @return
                     */
                    @Override
                    public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
                        /* 如果需要再请求服务器之前做一些操作, 则重新返回一个做过操作的的 Request 如增加 Header, 不做操作则直接返回参数 request
                        return chain.request().newBuilder().header("token", tokenId)
                                              .build(); */
                        return request;
                    }
                });
    }
    }

3. 全局错误处理及发生错误时重新执行
如果需要使用 Rxjava 的全局错误处理, 需要在 全局配置类 中通过 GlobalConfigModule.Builder.responseErroListener() 方法传入 ResponseErroListener, 并在 Rxjava 每次订阅调用 subscribe() 方法时, 传入 ErrorHandleSubscriber 实例, ErrorHandleSubscriber 实例的创建需要传入 AppComponent 中提供的 RxErrorHandler, ErrorHandleSubscriber 已经默认实现了 OnError 方法, 如想自定义可以重写 OnError 方法

    
    public class GlobalConfiguration implements ConfigModule {
    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        builder.responseErrorListener((context1, e) -> {
                    /* 用来提供处理所有错误的监听, RxJava 订阅时必须传入 ErrorHandleSubscriber 实例, 此监听才生效 */
                    Timber.w("------------>" + e.getMessage());
                    ArmsUtils.SnackbarText("net error");
                });
    }
    }
    
    
在Rxjava中使用

    Observable
    .just(1)
    .retryWhen(new RetryWithDelay(3,2))//遇到错误时重试, 第一个参数为重试几次, 第二个参数为重试的间隔, 单位为秒
    .subscribe(new ErrorHandleSubscriber<Integer>(mErrorHandler) {
     @Override
     public void onNext(Integer Integer) {
 
     }
    });

4. ImageLoader 如何扩展以及切换图片请求框架
--本框架默认使用 Glide 实现图片加载功能, 使用 ImageLoader 为业务层提供统一的图片请求接口, ImageLoader 使用策略模式和建造者模式, 可以动态切换图片请求框架 (比如说切换成 Picasso), 并且加载图片时传入的参数也可以随意扩展 ( loadImage() 方法在需要扩展参数时, 调用端也不需要改动, 全部通过 Builder 扩展, 比如您想让内部的图片加载框架, 清除缓存您只需要定义个 boolean 字段, 内部的图片加载框架根据这个字段 if|else 做不同的操作, 其他操作同理, 当需要切换图片请求框架或图片请求框架升级后变更了 Api 时, 这里可以将影响范围降到最低, 所以封装 ImageLoader 是为了屏蔽这个风险)

本框架默认提供了 GlideImageLoaderStrategy 和 ImageConfigImpl 简单实现了图片加载逻辑 (v2.5.0 版本后, 需要依赖 arms-imageloader-glide 扩展库, 并自行通过 GlobalConfigModule.Builder#imageLoaderStrategy(new GlideImageLoaderStrategy); 完成注册), 方便快速使用, 但开发中难免会遇到复杂的使用场景, 所以本框架推荐即使不切换图片请求框架继续使用 Glide, 也请按照下面的方法, 自行实现图片加载策略, 因为默认实现的 GlideImageLoaderStrategy 是直接打包进框架的, 如果是远程依赖, 当遇到满足不了需求的情况, 您将不能扩展里面的逻辑

使用 ImageLoader 必须传入一个实现了 BaseImageLoaderStrategy 接口的图片加载实现类从而实现动态切换, 所以首先要实现BaseImageLoaderStrategy, 实现时必须指定一个继承自 ImageConfig 的实现类, 使用建造者模式, 可以储存一些信息, 比如 URL、ImageView、Placeholder 等, 可以不断的扩展, 供图片加载框架使用

    public class PicassoImageLoaderStrategy implements BaseImageLoaderStrategy<PicassoImageConfig> {
	 @Override
    public void loadImage(Context ctx, PicassoImageConfig config) {
                        Picasso.with(ctx)
    			.load(config.getUrl())
    			.into(config.getImageView());
    ｝
    }
实现 ImageConfig, 使用建造者模式 (创建新的 PicassoImageConfig 适用于新项目, 如果想重构之前的项目, 使用其他图片加载框架, 为了避免影响之前的代码, 请继续使用默认提供的 ImageConfigImpl 或者您之前自行实现的 ImageConfig, 继续扩展里面的属性)
   
    public class PicassoImageConfig extends ImageConfig {

    private PicassoImageConfig(Buidler builder) {
        this.url = builder.url;
        this.imageView = builder.imageView;
        this.placeholder = builder.placeholder;
        this.errorPic = builder.errorPic;
    }

    public static Buidler builder() {
        return new Buidler();
    }


    public static final class Buidler {
        private String url;
        private ImageView imageView;
        private int placeholder;
        protected int errorPic;

        private Buidler() {
        }

        public Buidler url(String url) {
            this.url = url;
            return this;
        }

        public Buidler placeholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public Buidler errorPic(int errorPic){
            this.errorPic = errorPic;
            return this;
        }

        public Buidler imagerView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public PicassoImageConfig build() {
            if (url == null) throw new IllegalStateException("url is required");
            if (imageView == null) throw new IllegalStateException("imageview is required");
            return new PicassoImageConfig(this);
        }
    }
    }
在 App 刚刚启动初始化时, 通过 GlobalConfigModule 传入上面扩展的 PicassoImageLoaderStrategy, 也可以在 App 运行期间通过 AppComponent 拿到 ImageLoader 对象后, 调用 setLoadImgStrategy(new PicassoImageLoaderStrategy) 替换之前的实现 (默认使用 Glide)
方法一: 通过GlobalConfigModule传入

     public class GlobalConfiguration implements ConfigModule {
    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        builder.imageLoaderStrategy(new PicassoImageLoaderStrategy);
    }
    }

方法二: 拿到 AppComponent 中的 ImageLoader, 通过方法传入


    ArmsUtils.obtainAppComponentFromContext(context)
	.imageLoader()
	.setLoadImgStrategy(new PicassoImageLoaderStrategy());


使用方法:


    ArmsUtils.obtainAppComponentFromContext(context)
	.imageLoader()
	.loadImage(mApplication, PicassoImageConfig
                .builder()
                .url(data.getAvatarUrl())
                .imagerView(mAvater)
                .build());

5. AndroidEventBus Tag
本框架使用 AndroidEventBus 实现事件总线 (v2.5.0 版本后, 框架内不再包含 AndroidEventBus, 框架使用者可自行在 AndroidEventBus 和 EventBus 两个库中选择, 想选择哪个 EventBus 就依赖哪个 EventBus, 依赖后框架会自动检测您依赖的 EventBus 并自动完成注册), 此框架使用注解标记接受消息的方法, 注解可以指定 Tag, 便于索引, 统一将 Tag 的常量写到 EventBusTag 接口中, 便于管理, 如果想在 Activity、Fragment、Service、Presenter 中使用 AndroidEventBus 请重写 useEventBus() 方法, 返回 true 代表使用, 但框架已经默认返回 true.

6. 自定义 PopupWindow
框架提供一个使用建造者模式的自定义 PopupWindow 组件 CustomPopupWindow, 自己实现布局后就可以直接使用这个类实现 PopupWindow, 因为使用建造者模式, 所以可以随意扩展自定义参数

7. 权限管理(适配 Android 6.0 权限管理)
本框架使用 RxPermissions, 用于权限管理 (适配 Android 6.0), 并提供 PermissionUtil 工具类, 一行代码即可实现权限请求, 适配 Android 6.0 权限管理详解

      
      PermissionUtil.launchCamera(new PermissionUtil.RequestPermission() {

            @Override
            public void onRequestPermissionSuccess() {
                launchCapture();//请求权限成功后做一些操作
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
                mRootView.showMessage("Request permissions failure");
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                mRootView.showMessage("Need to go to the settings");
            }

        }, mRxPermissions, mErrorHandler);

8. Gradle 配置启动 Debug 模式
在主 Module (app) 的 build.gradle 中配置是否开启打印 Log 或者是否使用 LeakCanary 等调试工具

在 build.gradle 中配置


    android {
    buildTypes {
        debug {
            //这两个变量是自定义的, 自己也可以自定义其他字段, 这些字段会默认生成到 **BuildConfig** 类中, 在 **App** 中可以根据这些字段执行一些操作
            buildConfigField "boolean", "LOG_DEBUG", "true"
            buildConfigField "boolean", "USE_CANARY", "true"
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }

        release {
            buildConfigField "boolean", "LOG_DEBUG", "false"
            buildConfigField "boolean", "USE_CANARY", "false"
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }  
    
在代码中使用 (比如在 App 初始化时做一些初始化的设置)

    @Override
    public void injectAppLifecycle(Context context, List<AppLifecycles> lifecycles) {
        lifecycles.add(new AppLifecycles() {
      
            @Override
            public void onCreate(Application application) {
                if (BuildConfig.LOG_DEBUG) {
                    //Timber 日志打印
		    Timber.plant(new Timber.DebugTree());
		}
		if (BuildConfig.USE_CANARY) {
                    //leakCanary 内存泄露检查
		    LeakCanary.install(this);
		}
            }
        });
    }

9. AppManager (管理所有的 Activity)
AppManager 用于管理所有的 Activity, AppManager 内部持有一个含有所有存活的 Activity (未调用 onDestroy) 的 List 集合, 和一个当前在最前端的 Activity (未调用 onStop), AppManager 封装有多种方法, 可以很方便的对它们进行任何操作, AppManager 是单例的, 可以通过静态方法 AppManager.getAppManager() 直接拿到 AppManager 实例, 这样我们可以在整个 App 的任何地方对任何 Activity 进行全局操作, 比如在 App 请求网络超时时让最前端的 Activity 显示连接超时的交互页面 (这个逻辑不用写到当前请求的 Activity 里, 可以在一个单例类里做全局的统一操作, 因为可以随时随地通过 AppManager 拿到当前的任何 Activity)


10. AppDelegate(代理 Application 的生命周期)
AppDelegate 可以代理 Application 的生命周期, 在对应的生命周期, 执行对应的逻辑, 因为 Java 只能单继承, 所以当遇到某些三方库需要继承于它的 Application 的时候, 就只有自定义 Application 并继承于三方库的 Application, 这时就不用再继承 BaseApplication, 只用在自定义 Application 中对应的生命周期调用 AppDelegate 的对应方法 (Application 一定要实现 APP 接口), 框架就能照常运行, 并且 Application 中对应的生命周期可使用以下方式扩展

  
    public class GlobalConfiguration implements ConfigModule {

      @Override
    public void injectAppLifecycle(Context context, List<AppLifecycles> lifecycles) {
        //AppLifecycles 的所有方法都会在基类 Application 对应的生命周期中被调用, 所以可以在对应的方法中扩展一些自己需要的逻辑
        lifecycles.add(new AppLifecycles() {
            private RefWatcher mRefWatcher;//leakCanary观察器

            @Override
            public void onCreate(Application application) {
                if (BuildConfig.LOG_DEBUG) {
                    //Timber日志打印
                    Timber.plant(new Timber.DebugTree());
                }
                //leakCanary内存泄露检查
                this.mRefWatcher = BuildConfig.USE_CANARY ? LeakCanary.install(application) : RefWatcher.DISABLED;
            }

            @Override
            public void onTerminate(Application application) {
                this.mRefWatcher = null;
            }
        });
    }
}

####  多媒体使用 必须先添加权限

     <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
     <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     <uses-permission android:name="android.permission.CAMERA" />
  
1. 功能配置
// 进入相册 以下是例子：用不到的api可以不写


     PictureSelector.create(MainActivity.this)
 	.openGallery()//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
 	.theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
 	.maxSelectNum()// 最大图片选择数量 int
 	.minSelectNum()// 最小选择数量 int
	.imageSpanCount(4)// 每行显示个数 int
 	.selectionMode()// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
 	.previewImage()// 是否可预览图片 true or false
 	.previewVideo()// 是否可预览视频 true or false
	.enablePreviewAudio() // 是否可播放音频 true or false
 	.isCamera()// 是否显示拍照按钮 true or false
	.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
	.isZoomAnim(true)// 图片列表点击 缩放效果 默认true
	.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
	.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
 	.enableCrop()// 是否裁剪 true or false
 	.compress()// 是否压缩 true or false
 	.glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
 	.withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
 	.hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
 	.isGif()// 是否显示gif图片 true or false
	.compressSavePath(getPath())//压缩图片保存地址
 	.freeStyleCropEnabled()// 裁剪框是否可拖拽 true or false
 	.circleDimmedLayer()// 是否圆形裁剪 true or false
 	.showCropFrame()// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
 	.showCropGrid()// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
 	.openClickSound()// 是否开启点击声音 true or false
 	.selectionMedia()// 是否传入已选图片 List<LocalMedia> list
 	.previewEggs()// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
 	.cropCompressQuality()// 裁剪压缩质量 默认90 int
 	.minimumCompressSize(100)// 小于100kb的图片不压缩 
 	.synOrAsy(true)//同步true或异步false 压缩 默认同步
 	.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int 
 	.rotateEnabled() // 裁剪是否可旋转图片 true or false
 	.scaleEnabled()// 裁剪是否可放大缩小图片 true or false
 	.videoQuality()// 视频录制质量 0 or 1 int
	.videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int 
        .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int 
	.recordVideoSecond()//视频秒数录制 默认60s int
	.isDragFrame(false)// 是否可拖动裁剪框(固定)
 	.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code     
2. 缓存清除
 //包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限 
 
 
     PictureFileUtils.deleteCacheDirFile(MainActivity.this);
 
3. 主题配置
默认样式 注意* 样式只可修改，不能删除任何一项 否则报错


    <style name="picture.default.style" parent="Theme.AppCompat.Light.DarkActionBar">
        <!-- Customize your theme here. -->
        <!--标题栏背景色-->
        <item name="colorPrimary">@color/bar_grey</item>
        <!--状态栏背景色-->
        <item name="colorPrimaryDark">@color/bar_grey</item>
        <!--是否改变图片列表界面状态栏字体颜色为黑色-->
        <item name="picture.statusFontColor">false</item>
        <!--返回键图标-->
        <item name="picture.leftBack.icon">@drawable/picture_back</item>
        <!--标题下拉箭头-->
        <item name="picture.arrow_down.icon">@drawable/arrow_down</item>
        <!--标题上拉箭头-->
        <item name="picture.arrow_up.icon">@drawable/arrow_up</item>
        <!--标题文字颜色-->
        <item name="picture.title.textColor">@color/white</item>
        <!--标题栏右边文字-->
        <item name="picture.right.textColor">@color/white</item>
        <!--图片列表勾选样式-->
        <item name="picture.checked.style">@drawable/checkbox_selector</item>
        <!--开启图片列表勾选数字模式-->
        <item name="picture.style.checkNumMode">false</item>
        <!--选择图片样式0/9-->
        <item name="picture.style.numComplete">false</item>
        <!--图片列表底部背景色-->
        <item name="picture.bottom.bg">@color/color_fa</item>
        <!--图片列表预览文字颜色-->
        <item name="picture.preview.textColor">@color/tab_color_true</item>
        <!--图片列表已完成文字颜色-->
        <item name="picture.complete.textColor">@color/tab_color_true</item>
        <!--图片已选数量圆点背景色-->
        <item name="picture.num.style">@drawable/num_oval</item>
        <!--预览界面标题文字颜色-->
        <item name="picture.ac_preview.title.textColor">@color/white</item>
        <!--预览界面已完成文字颜色-->
        <item name="picture.ac_preview.complete.textColor">@color/tab_color_true</item>
        <!--预览界面标题栏背景色-->
        <item name="picture.ac_preview.title.bg">@color/bar_grey</item>
        <!--预览界面底部背景色-->
        <item name="picture.ac_preview.bottom.bg">@color/bar_grey_90</item>
        <!--预览界面返回箭头-->
        <item name="picture.preview.leftBack.icon">@drawable/picture_back</item>
        <!--是否改变预览界面状态栏字体颜色为黑色-->
        <item name="picture.preview.statusFontColor">false</item>
        <!--裁剪页面标题背景色-->
        <item name="picture.crop.toolbar.bg">@color/bar_grey</item>
        <!--裁剪页面状态栏颜色-->
        <item name="picture.crop.status.color">@color/bar_grey</item>
        <!--裁剪页面标题文字颜色-->
        <item name="picture.crop.title.color">@color/white</item>
        <!--相册文件夹列表选中图标-->
        <item name="picture.folder_checked_dot">@drawable/orange_oval</item>
    </style>

4. 常用功能
启动相册并拍照      


       PictureSelector.create(MainActivity.this)
       .openGallery(PictureMimeType.ofImage())
       .forResult(PictureConfig.CHOOSE_REQUEST);
       
4.1 单独启动拍照或视频 根据PictureMimeType自动识别      

  
     PictureSelector.create(MainActivity.this)
       .openCamera(PictureMimeType.ofImage())
       .forResult(PictureConfig.CHOOSE_REQUEST);
4.2 预览图片      

// 预览图片 可自定长按保存路径

     *注意 .themeStyle(themeId)；不可少，否则闪退...

     PictureSelector.create(MainActivity.this).themeStyle(themeId).openExternalPreview(position, "/custom_file", selectList);
     PictureSelector.create(MainActivity.this).themeStyle(themeId).openExternalPreview(position, selectList);

4.3预览视频

     PictureSelector.create(MainActivity.this).externalPictureVideo(video_path);

4.4 结果回调

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
    
4.5 使用多媒体混淆配置 如下  （相关重复的第三幅混淆库请自行合并删除）

     (#PictureSelector 2.0
     -keep class com.luck.picture.lib.** { *; }

     -dontwarn com.yalantis.ucrop**
      -keep class com.yalantis.ucrop** { *; }
     -keep interface com.yalantis.ucrop** { *; }
   
      #rxjava
      -dontwarn sun.misc.**
      -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
     long producerIndex;
     long consumerIndex;
      }
     -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
     rx.internal.util.atomic.LinkedQueueNode producerNode;
    }
    -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
    }

    #rxandroid
    -dontwarn sun.misc.**
    -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
    }
    -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
    }
    -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
    }

    #glide
    -keep public class * implements com.bumptech.glide.module.GlideModule
    -keep public class * extends com.bumptech.glide.AppGlideModule
    -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
    }

    # for DexGuard only
    -keepresourcexmlelements manifest/application/meta-data@value=GlideModule

    5 导航栏FlycoTabLayout
    Android Arsenal

    一个Android TabLayout库,目前有3个TabLayout

    SlidingTabLayout:参照PagerSlidingTabStrip进行大量修改.

 新增部分属性
新增支持多种Indicator显示器
新增支持未读消息显示
新增方法for懒癌患者
    /** 关联ViewPager,用于不想在ViewPager适配器中设置titles数据的情况 */
    
    public void setViewPager(ViewPager vp, String[] titles)
    
    /** 关联ViewPager,用于连适配器都不想自己实例化的情况 */
    public void setViewPager(ViewPager vp, String[] titles, FragmentActivity fa, ArrayList<Fragment> fragments) 
CommonTabLayout:不同于SlidingTabLayout对ViewPager依赖,它是一个不依赖ViewPager可以与其他控件自由搭配使用的TabLayout.

支持多种Indicator显示器,以及Indicator动画
支持未读消息显示
支持Icon以及Icon位置
新增方法for懒癌患者
    /** 关联数据支持同时切换fragments */
    
    public void setTabData(ArrayList<CustomTabEntity> tabEntitys, FragmentManager fm, int containerViewId, ArrayList<Fragment> fragments)
    SegmentTabLayout





