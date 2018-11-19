package com.jess.arms.voicerecorder.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * ================================================================
 * 创建时间：2018/11/16 15:19
 * 创 建 人：Mr.常
 * 文件描述：
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
public class PlayServiceConnection implements ServiceConnection {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        final PlayService playService = ((PlayService.PlayBinder) service).getService();
        Log.e("onServiceConnected----", "onServiceConnected");
        AppCache.setPlayService(playService);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }
}
