package com.example.dome.app.dto;

/**
 * ================================================================
 * 创建时间：2018/8/11 10:08
 * 创 建 人：Mr.常
 * 文件描述：
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
public class MyData {
    private String url;
    private boolean isSeleted;

    public MyData(String url, boolean isSeleted) {
        this.url = url;
        this.isSeleted = isSeleted;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSeleted() {
        return isSeleted;
    }

    public void setSeleted(boolean seleted) {
        isSeleted = seleted;
    }
}
