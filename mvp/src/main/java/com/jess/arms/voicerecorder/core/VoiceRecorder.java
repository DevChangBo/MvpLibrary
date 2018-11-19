package com.jess.arms.voicerecorder.core;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;

import com.jess.arms.voicerecorder.utils.EMError;
import com.jess.arms.voicerecorder.utils.PathUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * ================================================================
 * 创建时间：2018/11/16 10:22
 * 创 建 人：Mr.常
 * 文件描述：声音录制类
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
public class VoiceRecorder {
    MediaRecorder recorder;
    static final String PREFIX = "voice";
    static final String EXTENSION = ".amr";

    private boolean isRecording = false;
    private long startTime;
    private String voiceFilePath = null;
    private String voiceFileName = null;
    private File file;
    private Handler handler;

    private boolean isCustomNamingFile =false;

    public VoiceRecorder(Handler handler) {
        this.handler = handler;
    }

    /**
     * start recording to the file
     *
     * @return string
     */
    public String startRecording(Context appContext) {
        file = null;
        try {
            // need to create recorder every time, otherwise, will got exception
            // from setOutputFile when try to reuse
            if (recorder != null) {
                recorder.release();
                recorder = null;
            }
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setAudioChannels(1); // MONO
            recorder.setAudioSamplingRate(8000); // 8000Hz
            recorder.setAudioEncodingBitRate(64); // seems if change this to
            // 128, still got same file
            // size.
            // one easy way is to use temp file
            // file = File.createTempFile(PREFIX + userId, EXTENSION,
            // User.getVoicePath());

            if(!isCustomNamingFile){
                //默认命名是用时间戳0, 15位
                voiceFileName = getVoiceFileName(System.currentTimeMillis() + "");
            }

            if (!isDirExist()) {
                /**
                 * not exist ,so create default folder(全局缓存文件/voice)
                 */
                PathUtil.getInstance().createDirs("voice", appContext);
            }

            voiceFilePath = PathUtil.getInstance().getVoicePath() + "/" + voiceFileName;
            file = new File(voiceFilePath);
            recorder.setOutputFile(file.getAbsolutePath());
            recorder.prepare();
            isRecording = true;
            recorder.start();
        } catch (IOException e) {
            Log.e("voice", "prepare() failed");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isRecording) {
                        /**
                         * 监听音量，改变录音显示icon
                         */
                        android.os.Message msg = new android.os.Message();
                        msg.what = recorder.getMaxAmplitude() * 13 / 0x7FFF;
                        handler.sendMessage(msg);
                        SystemClock.sleep(100);
                    }
                } catch (Exception e) {
                    // from the crash report website, found one NPE crash from
                    // one android 4.0.4 htc phone
                    // maybe handler is null for some reason
                    Log.e("voice", e.toString());
                }
            }
        }).start();
        startTime = new Date().getTime();
        Log.d("voice", "start voice recording to file:" + file.getAbsolutePath());
        return file == null ? null : file.getAbsolutePath();
    }


    /**
     * stop the recoding
     * seconds of the voice recorded
     */
    public void discardRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
                recorder = null;
                if (file != null && file.exists() && !file.isDirectory()) {
                    file.delete();
                }
            } catch (IllegalStateException e) {
            } catch (RuntimeException e) {
            }
            isRecording = false;
        }
    }

    public int stopRecoding() {
        if (recorder != null) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;

            if (file == null || !file.exists() || !file.isFile()) {
                return EMError.FILE_INVALID;
            }
            if (file.length() == 0) {
                file.delete();
                return EMError.FILE_INVALID;
            }
            int seconds = (int) (new Date().getTime() - startTime) / 1000;
            Log.d("voice", "voice recording finished. seconds:" + seconds + " file length:" + file.length());
            return seconds;
        }
        return 0;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (recorder != null) {
            recorder.release();
        }
    }




    private String getVoiceFileName(String uid) {
        Time now = new Time();
        now.setToNow();
        return uid + now.toString().substring(0, 15) + EXTENSION;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public String getVoiceFilePath() {
        return voiceFilePath;
    }

    public String getVoiceFileName() {
        return voiceFileName;
    }




    /**
     * 判断是否存在缓存文件夹
     *
     * @return
     */
    public boolean isDirExist() {
        if (TextUtils.isEmpty(PathUtil.pathPrefix)) {
            return false;
        } else {
            return true;
        }
    }


    public void isCustomNamingFile(boolean isTrue, String name) {
        if (isTrue) {
            isCustomNamingFile = isTrue;
            setVoiceFileName(name);
        }
    }

    /**
     * 自定义音频文件命名
     *
     * @param voiceFileName
     */
    public void setVoiceFileName(String voiceFileName) {
        this.voiceFileName = voiceFileName+EXTENSION;
    }
}
