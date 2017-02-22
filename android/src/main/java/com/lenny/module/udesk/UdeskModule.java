package com.lenny.module.udesk;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

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
    public void getMsgCount(Callback callback) {
        int unreadMsg = UdeskSDKManager.getInstance().getCurrentConnectUnReadMsgCount();
        callback.invoke(unreadMsg);
    }

}
