package com.code93.linkcoop;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.code93.linkcoop.persistence.cache.SP2;
import com.code93.linkcoop.network.DownloadCallback;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AesBase64Wrapper {

    private static String IV = "1234567890123456";
    private static String PASSWORD = "ABCDEF0123456789ABCDEF0123456789";
    private static String SALT = "1234567890123456";

    DownloadCallback callback;
    Context context;

    public void encryptAndEncode(String raw, Context context, DownloadCallback callback) {
        this.context = context;
        try {
            Cipher c = getCipher(Cipher.ENCRYPT_MODE);
            byte[] encryptedVal = c.doFinal(getBytes(raw));
            callback.onDownloadCallback(Base64.encodeToString(encryptedVal, Base64.DEFAULT));
        } catch (Exception t) {
            callback.onDownloadCallback("Error " + t);
        }
    }

    public String decodeAndDecrypt(String encrypted) throws Exception {
        //Decode
        //byte[] decodedValue = Base64.decodeBase64(getBytes(encrypted));
        byte[] decodedValue = Base64.decode(encrypted, Base64.DEFAULT);
        Cipher c = getCipher(Cipher.DECRYPT_MODE);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    private String getString(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes, "UTF-8");
    }

    private byte[] getBytes(String str) throws UnsupportedEncodingException {
        return str.getBytes("UTF-8");
    }

    private Cipher getCipher(int mode) throws Exception {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SP2 sp2 = SP2.Companion.getInstance(context);
        String ivKey = sp2.getString(SP2.Companion.getAes_iv(), IV);
        Log.d("ivKey", ivKey);
        byte[] iv = getBytes(ivKey);
        c.init(mode, generateKey(), new IvParameterSpec(iv));
        return c;
    }

    private Key generateKey() throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SP2 sp2 = SP2.Companion.getInstance(context);
        String sPassword = sp2.getString(SP2.Companion.getAes_password(), PASSWORD);
        String sSalt = sp2.getString(SP2.Companion.getAes_salt(), SALT);
        char[] password = sPassword.toCharArray();
        byte[] salt = getBytes(sSalt);

        KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        byte[] encoded = tmp.getEncoded();
        return new SecretKeySpec(encoded, "AES");
    }
}
