package com.code93.linkcoop;

import android.app.Application;
import android.os.Build;

import com.zcs.sdk.DriverManager;
import com.zcs.sdk.card.CardInfoEntity;

public class MyApp extends Application {

    public static DriverManager sDriverManager;
    public static CardInfoEntity cardInfoEntity;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.MODEL.equals("Z90")) {
            sDriverManager = DriverManager.getInstance();
            cardInfoEntity = new CardInfoEntity();
            Config.init(this);
        }
    }
}
