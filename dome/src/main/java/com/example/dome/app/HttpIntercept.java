package com.example.dome.app;

import com.jess.arms.http.GlobalHttpHandler;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ================================================================
 * 创建时间：2017/9/5 上午8:52
 * 创建人：Mr.常
 * 文件描述：网络请求的拦截器 请求前和请求后 总会调用(response在客户端收到之前调用  request在客户端发出后(还未送往后台时)调用)
 * 看淡身边的虚伪，静心宁神做好自己。路那么长，无愧走好每一步。
 * ================================================================
 */
public class HttpIntercept implements GlobalHttpHandler {
    // 这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
    @Override
    public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
        return response;
    }

    // 这里可以在请求服务器之前可以拿到request,做一些操作比如给request统一添加token或者header以及参数加密等操作
    @Override
    public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
                        /* 如果需要再请求服务器之前做一些操作,则重新返回一个做过操作的的requeat如增加header,不做操作则直接返回request参数
                           return chain.request().newBuilder().header("token", tokenId)
                                  .build(); */
//        if (request.method().equals("POST")) {
//            Map<Object, Object> m = new HashMap<>();
//            Set<String> stringSet = request.url().queryParameterNames();
//            Iterator<String> iterator = stringSet.iterator();
//            for (int i = 0; i < stringSet.size(); i++) {
//                String p0 = iterator.next();
//                String p1 = request.url().queryParameterValue(i);
//                m.put(p0, p1);
//            }
//            String json = new Gson().toJson(m);
//            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
//            return chain.request().newBuilder().post(requestBody).build();
//        }
        return request;
    }
}
