package com.lenny.module.udesk;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.udesk.UdeskConst;
import cn.udesk.UdeskSDKManager;
import cn.udesk.model.UdeskCommodityItem;

/**
 * Created by lenny on 2017/2/22.
 */

class UdeskModule extends ReactContextBaseJavaModule {
    private WritableMap response = Arguments.createMap();
    private String appId, appKey, appDomain;
    private ReactApplicationContext mReactContext;
    public UdeskModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
        ApplicationInfo info = null;
        try {
            info = reactContext.getPackageManager().getApplicationInfo(reactContext.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (!info.metaData.containsKey("UDESK_DOMAIN") || !info.metaData.containsKey("UDESK_APPKEY") || !info.metaData.containsKey("UDESK_APPID")) {
            throw new Error("meta-data not found in AndroidManifest.xml");
        }
        this.appId = info.metaData.getString("UDESK_APPID");
        this.appKey = info.metaData.getString("UDESK_APPKEY");
        this.appDomain = info.metaData.getString("UDESK_DOMAIN");
    }

    @Override
    public String getName() {
        return "UdeskAPI";
    }

    @ReactMethod
    public void initSDK() {
        UdeskSDKManager.getInstance().initApiKey(mReactContext.getApplicationContext(), this.appDomain, this.appKey, this.appId);
    }

    @ReactMethod
    public void setUserInfo(String token, String name, String email, String phone, String description) {
//        initSDK();

        Map<String, String> info = new HashMap<>();
        if (token == null) {
            token = UUID.randomUUID().toString();
        }
        //token 必填
        info.put(UdeskConst.UdeskUserInfo.USER_SDK_TOKEN, token);
        //以下信息是可选
        if (name != null) {
            info.put(UdeskConst.UdeskUserInfo.NICK_NAME, name);
        }
        if (email != null) {
            info.put(UdeskConst.UdeskUserInfo.EMAIL, email);
        }
        if (phone != null) {
            info.put(UdeskConst.UdeskUserInfo.CELLPHONE, phone);
        }
        if (description != null) {
            info.put(UdeskConst.UdeskUserInfo.DESCRIPTION, description);
        }
        UdeskSDKManager.getInstance().setUserInfo(mReactContext.getApplicationContext(), token, info);
    }

    @ReactMethod
    public void setUserInfo(final ReadableMap options, final Callback callback) {
        Map<String, String> info = new HashMap<>();
        String token = options.getString("token");
        if (token == null) {
            token = UUID.randomUUID().toString();
        }
        //token 必填
        info.put(UdeskConst.UdeskUserInfo.USER_SDK_TOKEN, token);
        //以下信息是可选
        if (hasAndNotEmpty(options, "name")) {
            info.put(UdeskConst.UdeskUserInfo.NICK_NAME, options.getString("name"));
        }
        if (hasAndNotEmpty(options, "email")) {
            info.put(UdeskConst.UdeskUserInfo.EMAIL, options.getString("email"));
        }
        if (hasAndNotEmpty(options, "phone")) {
            info.put(UdeskConst.UdeskUserInfo.EMAIL, options.getString("phone"));
        }
        if (hasAndNotEmpty(options, "description")) {
            info.put(UdeskConst.UdeskUserInfo.EMAIL, options.getString("description"));
        }
        Map field = (Map) options.getMap("custom_field");
        if (field!= null) {
            UdeskSDKManager.getInstance().setUserInfo(mReactContext.getApplicationContext(), token, info, field);
        } else {
            UdeskSDKManager.getInstance().setUserInfo(mReactContext.getApplicationContext(), token, info);
        }
    }

    @ReactMethod
    public void setUpdateUserinfo(String name, String email, String phone, String description) {
        Map<String, String> info = new HashMap<>();
        if (name != null) {
            info.put(UdeskConst.UdeskUserInfo.NICK_NAME, name);
        }
        if (email != null) {
            info.put(UdeskConst.UdeskUserInfo.EMAIL, email);
        }
        if (phone != null) {
            info.put(UdeskConst.UdeskUserInfo.CELLPHONE, phone);
        }
        if (description != null) {
            info.put(UdeskConst.UdeskUserInfo.DESCRIPTION, description);
        }
        UdeskSDKManager.getInstance().setUpdateUserinfo(info);
    }

    @ReactMethod
    public void entryChat() {
        UdeskSDKManager.getInstance().entryChat(mReactContext.getApplicationContext());
    }

    @ReactMethod
    public void createCommodity (String title, String description, String imageUrl, String productUrl) { // 都是必传的
        UdeskCommodityItem item = new UdeskCommodityItem();
        item.setTitle(title);// 商品主标题
        item.setSubTitle(description);//商品副标题
        item.setThumbHttpUrl(imageUrl);// 左侧图片
        item.setCommodityUrl(productUrl);// 商品网络链接
        UdeskSDKManager.getInstance().setCommodity(item);
        UdeskSDKManager.getInstance().toLanuchChatAcitvity(mReactContext.getApplicationContext());
    }

    @ReactMethod
    public void createCommodity (final ReadableMap options, final Callback callback) { // 都是必传的
        cleanResponse();
        if (!hasAndNotEmpty(options, "title")) {
            invokeError(callback, "title is empty");
            return;
        }
        if (!hasAndNotEmpty(options, "description")) {
            invokeError(callback, "description is empty");
            return;
        }
        if (!hasAndNotEmpty(options, "imageUrl")) {
            invokeError(callback, "imageUrl is empty");
            return;
        }
        if (!hasAndNotEmpty(options, "productUrl")) {
            invokeError(callback, "productUrl is empty");
            return;
        }
        UdeskCommodityItem item = new UdeskCommodityItem();
        item.setTitle(options.getString("title"));// 商品主标题
        item.setSubTitle(options.getString("description"));//商品副标题
        item.setThumbHttpUrl(options.getString("imageUrl"));// 左侧图片
        item.setCommodityUrl(options.getString("productUrl"));// 商品网络链接
        UdeskSDKManager.getInstance().setCommodity(item);
        UdeskSDKManager.getInstance().toLanuchChatAcitvity(mReactContext.getApplicationContext());
    }

    @ReactMethod
    public void getMsgCount(Callback callback) {
        int unreadMsg = UdeskSDKManager.getInstance().getCurrentConnectUnReadMsgCount();
        callback.invoke(unreadMsg);
    }

    public static @NonNull boolean hasAndNotEmpty(@NonNull final ReadableMap target,
                                                  @NonNull final String key)
    {
        if (!target.hasKey(key))
        {
            return false;
        }

        final String value = target.getString(key);

        return !TextUtils.isEmpty(value);
    }

    public void cleanResponse() {
        response = Arguments.createMap();
    }

    public void invokeError(@NonNull final Callback callback,
                            @NonNull final String error) {
        cleanResponse();
        response.putString("error", error);
        invokeResponse(callback);
    }

    public void invokeResponse(@NonNull final Callback callback) {
        callback.invoke(response);
    }

}
