package com.example.dome.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

/**
 * ================================================================
 * 创建时间：2018-7-13 10:16:43
 * 创 建 人：Mr.常
 * 文件描述：
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
public interface MainContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

    }
}
