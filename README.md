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
* 视频广告
* native广告
## 2,提供对sdk初始化相关的监听回调：
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
## 3,提供对facebook用户相关事件的监听回调:
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
## 4,用户对广告操作的相关事件的监听回调
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
## 5,使用google checkout 支付事件的回调 
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
     
