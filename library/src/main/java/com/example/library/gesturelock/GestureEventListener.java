package com.example.library.gesturelock;

/**
 * 项目名称：MeshLed_dxy
 * 类描述：
 * 创建人：oden
 * 创建时间：2016/7/25 21:22
 */
public interface GestureEventListener {

    void onDown();
    void onGestureEvent(boolean matched);
}
