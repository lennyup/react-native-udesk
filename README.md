# react-native-udesk
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
include ':UdeskSDKUI'
project(':UdeskSDKUI').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-udesk/UdeskSDKUI')
```
在 `app/build.gradle`里添加如下代码：
```
compile project(':react-native-udesk')
```
在`android/app/build.gradle`里，defaultConfig栏目下添加如下代码：
```
        manifestPlaceholders = [
                UDESK_DOMAIN: "xxx",
                UDESK_APPKEY: "xxx",
                UDESK_APPID: "xxx"
        ]
```
在 application里添加如下代码：
 ```
 new UdeskPackage()
 ```

包：

```
import com.lenny.module.udesk.UdeskPackage;
```
