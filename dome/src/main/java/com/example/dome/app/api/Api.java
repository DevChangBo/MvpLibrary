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
package com.example.dome.app.api;

/**
 * ================================================
 * 存放一些与 API 有关的东西,如请求地址,请求码等
 * ================================================
 */
public interface Api {
    String API_HTTP_HEAD = "http://xxx.xxx.xxx.xxx:";  //测试服务器
    String API_HTTP_PORT = "xxxx";                      //测试服务器端口号
    String APP_DOMAIN = API_HTTP_HEAD + API_HTTP_PORT + "/a/";//测试服务地址
    String  DOWNLOAD_URL =API_HTTP_HEAD + API_HTTP_PORT +"/a/appInterface/leaderApp/version/downloadApk";//测试apk更新下载URL

//    String API_HTTP_HEAD = "http://182.150.21.207:";  //正试服务器
//    String API_HTTP_PORT = "8088";                      //正试服务器端口号8089  测试正式服武器端口号8088
//    String APP_DOMAIN = API_HTTP_HEAD + API_HTTP_PORT + "/expressway/a/";//正式服务地址
//    String  DOWNLOAD_URL =API_HTTP_HEAD + API_HTTP_PORT +"/expressway/a/appInterface/leaderApp/version/downloadApk";//正式apk更新下载URL
    String RequestSuccess = "0";


}
