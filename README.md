# react-native-udesk
[![npm version](https://badge.fury.io/js/react-native-udesk.svg)](https://badge.fury.io/js/react-native-image-picker)
![MIT](https://img.shields.io/dub/l/vibe-d.svg) ![Platform - Android](https://img.shields.io/badge/platform-Android-yellow.svg)

React Native 的udesk插件, 目前只支持android
## 如何安装
### 安装包
```
npm install react-native-udesk --save
```
### 手动link
在 `android/setting.gradle`里添加如下代码：

```
include ':react-native-udesk'
project(':react-native-udesk').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-udesk/android')
include ':udeskSDKUI'
project(':udeskSDKUI').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-udesk/UdeskSDKUI')
```
在 `app/build.gradle`里添加如下代码：
```
compile project(':react-native-udesk')
```
在 application里添加如下代码：
 ```
 new UdeskPackage()
 ```

包：

```
import com.lenny.module.udesk.UdeskPackage;
```
## 使用
### 初始化
```
...
import UdeskAPI from 'react-native-udesk';
...
UdeskAPI.initSDK(UDESK_DOMAIN, UDESK_APPKEY, UDESK_APPID);
```
### 设置用户信息
```
const user = {
        nick_name: ***,
        cellphone: ***,
        // email: ***,
        sdk_token: ***,
        customer_field: { // 自定义的字段
          TextField_10075: ***,
        },
        // description: ***,
      }；
      UdeskAPI.setUserInfo(user, (response) => {

        });
```
### 普通的在线客服
```
UdeskAPI.entryChat();
```
### 带商品信息的 客服聊天界面
```
 const params = {
   productImageUrl: 'https://qn-im.udesk.cn/570D6DB3-113D-4310-921A-F5E66D158CC2-2017-04-05-03-42-33.jpg',
   productTitle: '测试测试测试测你测试测试测你测试测试测你测试测试测你测试测试测你测试测试测你！',
   productDetail: '¥9988888.088888.088888.0',
   productURL: 'http://www.xebest.com',
 };
UdeskAPI.createCommodity(params, (response) => {

  });
```

### android 代码混淆
如果需要混淆，请加上以下规则
```
//udesk
-keep class udesk.** {*;}
-keep class cn.udesk.**{*; }
//七牛
-keep class okhttp3.** {*;}
-keep class okio.** {*;}
-keep class com.qiniu.**{*;}
-keep class com.qiniu.**{public <init>();}
-ignorewarnings
//smack
-keep class org.jxmpp.** {*;}
-keep class de.measite.** {*;}
-keep class org.jivesoftware.** {*;}
-keep class org.xmlpull.** {*;}
-dontwarn org.xbill.**
-keep class org.xbill.** {*;}

//freso
-keep class com.facebook.** {*; }  
-keep class com.facebook.imagepipeline.** {*; }
-keep class com.facebook.animated.gif.** {*; }  
-keep class com.facebook.drawee.** {*; }  
-keep class com.facebook.drawee.backends.pipeline.** {*; }  
-keep class com.facebook.imagepipeline.** {*; }  
-keep class bolts.** {*; }  
-keep class me.relex.photodraweeview.** {*; }  



 //其它
-keep class com.tencent.bugly.** {*; }
```
