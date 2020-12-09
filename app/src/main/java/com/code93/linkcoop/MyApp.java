package com.code93.linkcoop;

import android.app.Application;
import android.os.Build;

import androidx.lifecycle.ViewModelProvider;

import com.code93.linkcoop.cache.SP;
import com.code93.linkcoop.cache.SP2;
import com.code93.linkcoop.viewmodel.CooperativaViewModel;
import com.zcs.sdk.DriverManager;
import com.zcs.sdk.card.CardInfoEntity;

public class MyApp extends Application {

    public static DriverManager sDriverManager;
    public static CardInfoEntity cardInfoEntity;
    public static SP2 sp2;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.MODEL.equals("Z90")) {
            sDriverManager = DriverManager.getInstance();
            cardInfoEntity = new CardInfoEntity();
            Config.init(this);
        }
        sp2 = SP2.Companion.getInstance(this);
        if (sp2 != null) {
            String aes_iv = sp2.getString(SP2.Companion.getAes_iv(), null);
            if (aes_iv == null) {
                sp2.putString(SP2.Companion.getAes_iv(), getString(R.string.aes_iv));
            }

            String aes_password = sp2.getString(SP2.Companion.getAes_password(), null);
            if (aes_password == null) {
                sp2.putString(SP2.Companion.getAes_password(), getString(R.string.aes_password));
            }

            String aes_salt = sp2.getString(SP2.Companion.getAes_salt(), null);
            if (aes_salt == null) {
                sp2.putString(SP2.Companion.getAes_salt(), getString(R.string.aes_salt));
            }
        }

    }
}
