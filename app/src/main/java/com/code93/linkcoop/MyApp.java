package com.code93.linkcoop;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.code93.linkcoop.persistence.cache.SP2;
import com.zcs.sdk.DriverManager;
import com.zcs.sdk.card.CardInfoEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
            String net_direccionip = sp2.getString(SP2.Companion.getNet_direccionip(), null);
            if (net_direccionip == null) {
                sp2.putString(SP2.Companion.getNet_direccionip(), getString(R.string.net_direccionip));
            }

            String net_puerto = sp2.getString(SP2.Companion.getNet_puerto(), null);
            if (net_puerto == null) {
                sp2.putString(SP2.Companion.getNet_puerto(), getString(R.string.net_puerto));
            }

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

            //verificarFechaUltimoLogin();
        }

    }

    private void verificarFechaUltimoLogin() {
        long day = 0;
        long diff = 0;

        String fechaUltimoLogin = SP2.Companion.getInstance(this).getString(SP2.Companion.getFechaUltimoLogin(), "NO");
        if (!fechaUltimoLogin.equals("NO")) {
            String fechaLogin = fechaUltimoLogin.substring(0, fechaUltimoLogin.indexOf(" "));
            Log.d("FECHA ULTIMA", fechaLogin);
            String outputPattern = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
            try {
                Date date1 = outputFormat.parse(fechaUltimoLogin);
                Date date2 = outputFormat.parse(Tools.getLocalDateTime());
                diff = date2.getTime() - date1.getTime();
                day = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                Log.d("DIAS DE ULTIMO LOGIN", ""+day);

                if (day >= 1) {
                    sp2.putBoolean(SP2.Companion.getSP_LOGIN(), false);
                }
            }catch (Exception e) {
                sp2.putBoolean(SP2.Companion.getSP_LOGIN(), false);
            }
        } else {
            sp2.putBoolean(SP2.Companion.getSP_LOGIN(), false);
        }
    }
}
