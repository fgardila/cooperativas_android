package com.code93.linkcoop;

import android.annotation.SuppressLint;
import android.app.Application;

import java.lang.reflect.InvocationTargetException;

public class Config {

    public static final String BASE_URL = "http://tms.szzcs.com:7099/pay/";
    public static final String CHECK_FIRMWARE_UPDATE = BASE_URL + "tms/getUpgradeInf.json";// 检查更新
    public static final String CHECK_UPDATE = BASE_URL + "tms/getAppUpgradeInf.json";// 检查APP更新
    /* 服务端返回的结果 */
    public static final String RES_OK = "000000";
    public static final String RES_ERR = "999999";

    public static final String NET_CONN_ERROR = "netException";
    public static final String SERVER_CONN_ERROR = "webException";
    public static final String NET_EXCEPTION = "exception";
    public static final String REQUEST_YES = "yes";
    public static final String REQUEST_NO = "no";
    public static final String REQUEST_NULL = "null";
    public static final String ERROR = "ERROR";
    public static final String SUCCESS = "SUCCESS";
    public static final String EXIST = "EXIST";// 用户已存在
    /* inten跳转状态码 */
    public static final String REQUEST_CODE = "requstCode";
    public static final int SEARCH_TO_RESULT = 1;
    public static final int LIST_TO_RESULT = 2;
    public static final int COLLECT_TO_RESULT = 3;
    public static final int PUBLISH_TO_RESULT = 7;

    // update
    public static final String APP_NAME = "appName";
    public static final String APP_VERSION = "appVersion";
    public static final String SYS_TYPE = "sysType";
    public static final String SYS_TYPE_VALUE = "Android";
    public static final String CHECK_STATE = "checkState";
    public static final String FILE_URL = "fileUrl";
    public static final String FILE_DESC = "fileDesc";

    public static final String FIRMWARE_NAME = "firmWareName";
    public static final String FIRMV_ERSION = "firmVersion";
    public static final String PID = "pid";

    private static Config sUtils;
    private static Application sApplication;

    public static Config getInstance() {
        if (sUtils == null)
            sUtils = new Config();
        return sUtils;
    }

    public static Config init(final Application app) {
        getInstance();
        if (sApplication == null) {
            if (app == null) {
                sApplication = getApplicationByReflect();
            } else {
                sApplication = app;
            }
        } else {
            if (app != null && app.getClass() != sApplication.getClass()) {
                sApplication = app;
            }
        }
        return sUtils;
    }

    public static Application getApp() {
        if (sApplication != null)
            return sApplication;
        Application app = getApplicationByReflect();
        init(app);
        return app;
    }

    private static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            return (Application) app;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }



}
