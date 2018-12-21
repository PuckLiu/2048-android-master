package com.uberspot.a2048;

/**
 * Created by liu on 2018/12/4.
 */

public interface JavaScriptFunction {
    public int getLevel();

    public void gameOver(String over, String score);

    public void getScore(int score);

    public void needShowAd();

    public void shareClick();

    public void log(String string);
}
