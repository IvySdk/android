#  android sdk 文档
## 1,在Activity的onCreate方法中初始化Android SDK,构造AndroidSDK builder对象
```java
AndroidSdk.Builder builder = new AndroidSdk.Builder();
AndroidSdk.onCreate(this, builder);
```
## 2，提供以下样式广告的api:
* 全屏广告
	 * 页面开始进入弹出的广告，即在onCreate()或onStart中调用：<br>
     ```java
     AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_START);
     ```
	 </br>
	 * 页面暂停时弹出的广告，即在onPause()中调用：<br>
	 ```java
     AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
      ```</br>
	 * 页面退出的广告，即在onStop()中调用<br>
	 ```java
     AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_EXIT);
     ``` 
	 </br>
	 * 游戏过关弹出的广告
	 <br>
	 ```java
     AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PASS_LEVEL);
     ``` 
	 </br>
	 * 自定义类型广告
	  <br>
	 ```java
     AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_CUSTOM);
     ``` 
	 </br>
* banner广告
	* 普通banner
    ```java
    int[] bannerPos = {
            AndroidSdk.POS_CENTER,
            AndroidSdk.POS_CENTER_BOTTOM,
            AndroidSdk.POS_CENTER_TOP,
            AndroidSdk.POS_LEFT_BOTTOM,
            AndroidSdk.POS_LEFT_TOP,
            AndroidSdk.POS_RIGHT_BOTTOM,
            AndroidSdk.POS_RIGHT_TOP
     };
     AndroidSdk.showBanner("default", bannerPos[1]); //开启
     AndroidSdk.closeBanner(); //关闭
     ```
	 * native banner 
	 
	 ```java
	 AndroidSdk.showNativeBanner("unlock_pre", 50, 50);
	 AndroidSdk.hideNativeBanner("unlock_pre");
     ```         
                    
* 视频广告
```java
 AndroidSdk.showRewardAd("default", 1);
 ```
* native广告
```java
 AndroidSdk.showNativeAdScrollView("unlock_pre", AndroidSdk.HIDE_BEHAVIOR_NO_HIDE, 50); //显示native广告
 AndroidSdk.hideNativeAdScrollView("unlock_pre"); //隐藏native广告
 ```
## 3, 提供对faceook相关操作的api	
* 登陆
```java
AndroidSdk.login();
 ```
* 是否登陆
```java
boolean isLogin = AndroidSdk.isLogin()
 ```
* 分享
```java
 AndroidSdk.share(); //注意此方法会阻塞主线程，需启线程调用
 ```
* 喜欢
```java
AndroidSdk.like();
 ```
* 邀请
```java
AndroidSdk.invite();
 ```
* 挑战
```java
 AndroidSdk.challenge("haha title", "heihei message");
 ```
* 朋友
```java
 String friendsJson = AndroidSdk.friends()；
  ```
* 我
```java
String meJson = AndroidSdk.me();
 ```
* 退出
```java
 AndroidSdk.logout();
 ```
## 5,提供使用google chekout进行支付的api ：
```java
int money = 1;
AndroidSdk.pay(money);
```
## 6,提供对sdk初始化相关的监听回调：
* sdk初始化成功事件
* 初始化成功后收到服务器回传数据
* 初始化成功后收到通知数据
```java
builder.setSdkResultListener(new SdkResultListener() {
            @Override
            public void onInitialized() {
                 Log.e("DEMO","sdk initialized");
            }

            @Override
            public void onReceiveServerExtra(String data) {
                 Log.e("DEMO","server data: " + data);
            }

            @Override
            public void onReceiveNotificationData(String data) {
                 Log.e("DEMO","noti: " + data);
            }
 })
```
## 7,提供对facebook用户相关事件的监听回调:
* 登录是否成功
* 邀请
* 挑战
* 喜欢
```java
builder.setUserCenterListener(new UserCenterListener() {
            @Override
            public void onReceiveLoginResult(boolean success) {
                 Log.e("DEMO","login? " + success);
            }

            @Override
            public void onReceiveInviteResult(boolean success) {
                 Log.e("DEMO","invite? " + success);
            }

            @Override
            public void onReceiveChallengeResult(int count) {
                 Log.e("DEMO","challenge? " + " count: " + count);
            }

            @Override
            public void onReceiveLikeResult(boolean success) {
                 Log.e("DEMO","like? " + success);
            }

 });
 ```
## 8,用户对广告操作的相关事件的监听回调
```java
builder.setRewardAdListener(new AdListener() {
            @Override
            public void onReceiveReward(boolean success, int id) {
                Log.e("DeMO","on receive reward? " + id + ", success = " + success);
            }

            @Override
            public void onFullAdClosed() {

            }

            @Override
            public void onFullAdClicked() 

            }

            @Override
            public void onVideoAdClosed() {

            }

            @Override
            public void onBannerAdClicked() {

            }

            @Override
            public void onCrossAdClicked() {

            }
});
```
## 9,使用google checkout 支付事件的回调 
```java
builder..setPaymentResultListener(new PaymentResultListener() {
            @Override
            public void onPaymentSuccess(int billId) {
                Log.d("DEMO","payment success: " + billId);
            }

            @Override
            public void onPaymentFail(int billId) {
                 Log.d("DEMO","payment fail: " + billId);
            }

            @Override
            public void onPaymentCanceled(int bill) {
                Log.d("DEMO","payment cancel: " + bill);
            }

            @Override
            public void onPaymentSystemValid() {
                Log.d("DEMO", "pay system is valid");
            }
  });
  ```
     
