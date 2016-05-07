package com.example.administrator.gobang;

/**
 * User: Administrator
 * Time: 2016/5/7 15:52 52
 * Annotation:
 */
public interface OnGameOverListener {

    //win true 白赢，否则黑赢
    void onGameWin(boolean win);

    //是否平局
    void onGameTie();
}
