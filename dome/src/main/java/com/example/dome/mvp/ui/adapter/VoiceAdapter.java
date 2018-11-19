package com.example.dome.mvp.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.dome.R;
import com.example.dome.dto.VoiceMessageDTO;
import com.jess.arms.voicerecorder.service.AppCache;
import com.jess.arms.voicerecorder.utils.CommonUtils;

import java.util.List;

/**
 * ================================================================
 * 创建时间：2018/11/16 11:30
 * 创 建 人：Mr.常
 * 文件描述：语音聊天适配器
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
public class VoiceAdapter extends BaseQuickAdapter<VoiceMessageDTO, BaseViewHolder> {
    public VoiceAdapter(@Nullable List<VoiceMessageDTO> data) {
        super(R.layout.ease_row_sent_voice);
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    @Override
    protected void convert(BaseViewHolder helper, VoiceMessageDTO item) {
        helper.setText(R.id.tv_length,""+item.getSecond());
        ImageView iv_voice=(ImageView)helper.getView(R.id.iv_voice);
        RelativeLayout bubble=(RelativeLayout)helper.getView(R.id.bubble);

        //更改并显示录音条长度
        RelativeLayout.LayoutParams ps = (RelativeLayout.LayoutParams) bubble.getLayoutParams();
        ps.width = CommonUtils.getVoiceLineWight2(mContext, item.getSecond());
        bubble.setLayoutParams(ps); //更改语音长条长度
        iv_voice.setImageResource(R.mipmap.yy_3);
        bubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppCache.getPlayService() != null) {
                    AppCache.getPlayService().setVoice_to_icon(R.drawable.voice_to_icon1);   //设置播放动画icon
                    AppCache.getPlayService().setVoice_to_noIcon(R.mipmap.yy_3);   //设置停止播放时动画icon
                    AppCache.getPlayService().setImageView(iv_voice);
                    AppCache.getPlayService().stopPlayVoiceAnimation();
                    //  AppCache.getPlayService().play("http://img.layuva.com//b96c4bde124a328d9c6edb5b7d51afb2.amr");  //播放网络音频
                    AppCache.getPlayService().play(item.getPath());
                }
            }
        });
    }
}
