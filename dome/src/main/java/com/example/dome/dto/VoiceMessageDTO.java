package com.example.dome.dto;

import java.io.Serializable;

/**
 * ================================================================
 * 创建时间：2018/11/16 11:33
 * 创 建 人：Mr.常
 * 文件描述：语音消息model类
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
public class VoiceMessageDTO implements Serializable {
    public String path;  //聊天语音路径
    public String msg;   //聊天文字内容
    public int second;   //语音框长度
    public long time;    //聊天时间

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

