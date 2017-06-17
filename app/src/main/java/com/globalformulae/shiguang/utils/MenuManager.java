package com.globalformulae.shiguang.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * Created by ZXG on 2017/2/27.
 */

public class MenuManager implements ScreenShotable {
    public static final String CLOSE = "Close";
    public static final String USERICON="UserIcon";
    public static final String STATISTIC = "Statistic";  //
    public static final String FUTURETASK = "FutureTask";
    public static final String WEATHER = "Werther";
    public static final String NBA="Nba";
    public static final String SETTING = "Setting";
    public static final String SHOP = "Shop";
    public static final String PARTY = "Party";
    public static final String MOVIE = "Movie";
    public static final String SCHOOL="School";
    public static final String DRUM="drum";

    public static MenuManager menuManager=new MenuManager();
    private MenuManager(){}

    /**
     * 单例模式
     * @return menuManager
     */
    public static MenuManager getInstance(){
        return menuManager;
    }

    private View containerView;
    protected ImageView mImageView;
    protected int res;
    private Bitmap bitmap;


    @Override
    public void takeScreenShot() {

    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }
}
