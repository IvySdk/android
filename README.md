#  android sdk 文档
## 1,在Activity中初始化Android SDK,构造AndroidSDK builder对象
```java
  @Override
    protected void onCreate() {
       AndroidSdk.Builder builder = new AndroidSdk.Builder();
       AndroidSdk.onCreate(this, builder); //onCreate方法中调用
    }
  @Override
    protected void onStart() {
        super.onStart();
        AndroidSdk.onStart();
    }

    @Override
    protected void onStop() {
        AndroidSdk.onStop();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AndroidSdk.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        AndroidSdk.onResume(this);
    }

    @Override
    protected void onDestroy() {
        AndroidSdk.onDestroy();
        super.onDestroy();
    }
```
## 2，如果你使用proguard来混淆Java代码，需要添加以下规则：
```java
-keep class com.android.client.** {
    <methods>;
}
-keep class android.support.** {
    *;
}
-keep class com.core.async.** {
   public *;
}

-keep class com.core.common.** {
   public *;
}

-keep class com.core.network.** {
   public *;
}

-keep class com.core.view.** {
   public *;
}
```
## 3，提供以下样式广告的api:
* 全屏广告，需配置不同时机弹出的广告，以便于后台统计,我们预定义了以下几种时机弹出的广告：
```java
AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_START); //游戏开始时
AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE); //游戏暂停
AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PASS_LEVEL);//游戏过关
AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_EXIT); //游戏退出
AndroidSdk.showFullAd("xxx"); //您还可以自定义类型广告
``` 
 注意：以上类型广告的弹出不要在activity的生命周期onResume()和onPause()中调用
  
* banner广告

```java
int[] bannerPos = {
            AndroidSdk.POS_CENTER,      //居中显示banner广告
            AndroidSdk.POS_CENTER_BOTTOM, //中间底部显示banner广告
            AndroidSdk.POS_CENTER_TOP, //中间底部显示banner广告
            AndroidSdk.POS_LEFT_BOTTOM, //左下角显示banner广告
            AndroidSdk.POS_LEFT_TOP, //左上角显示banner广告
            AndroidSdk.POS_RIGHT_BOTTOM, //右下角显示banner广告
            AndroidSdk.POS_RIGHT_TOP //右上角显示banner广告
 };
 AndroidSdk.showBanner("default", bannerPos[0]); //居中显示banner广告
 AndroidSdk.closeBanner(); //关闭banner广告
 ```
* 视频广告
```java
int rewardId = 1; //客户端配置的视频广告调用时机
if(AndroidSdk.hasRewardAd()){ //检查后台是否有配置视频广告
    AndroidSdk.showRewardAd(rewardId);
}
```
 
## 4, 提供对faceook相关操作的api	
* 登陆facebook账户
```java
AndroidSdk.login();
 ```
* 是否登陆facebook账户
```java
boolean isLogin = AndroidSdk.isLogin()
 ```
* facebook分享
```java
 AndroidSdk.share(); //注意此方法会阻塞主线程，需启线程调用
 ```
* facebook点赞
```java
AndroidSdk.like();
 ```
* facebook邀请好友安装应用，游戏等
```java
AndroidSdk.invite();
 ```
* 向你的facebook朋友发出挑战
```java
 AndroidSdk.challenge("haha title", "heihei message");
 ```
* 获取faceook朋友信息列表
```java
 String friendsJson = AndroidSdk.friends()；
  ```
* 获取我的faceook个人信息
```java
String meJson = AndroidSdk.me();
 ```
* 退出facebook账户
```java
 AndroidSdk.logout();
 ```
## 5,提供使用应用内支付的api，后台配置计费点 ：
```java
int billId = 1; //计费点
AndroidSdk.pay(billId);
```
## 6,提供对sdk初始化相关接口的监听回调：
* sdk初始化成功接口
* 初始化成功后收到服务器回传数据
* 初始化成功后收到通知数据
```java
builder.setSdkResultListener(new SdkResultListener() {
            @Override
            public void onInitialized() {
                 //sdk初始化成功接口
                 Log.e("DEMO","sdk initialized");
            }

            @Override
            public void onReceiveServerExtra(String data) {
                 // 初始化成功后收到服务器回传数据
                 Log.e("DEMO","server data: " + data);
            }

            @Override
            public void onReceiveNotificationData(String data) {
                 //初始化成功后收到通知数据
                 Log.e("DEMO","noti: " + data);
            }
 })
```
## 7,提供对facebook用户相关接口的监听回调:

```java
builder.setUserCenterListener(new UserCenterListener() {
            @Override
            public void onReceiveLoginResult(boolean success) {
                 //登陆facebook账户成功与否回调
                 Log.e("DEMO","login? " + success);
            }

            @Override
            public void onReceiveInviteResult(boolean success) {
                 //邀请好友安装程序，游戏等回调
                 Log.e("DEMO","invite? " + success);
            }

            @Override
            public void onReceiveChallengeResult(int count) {
                 //收到挑战结果的回调，count代表成功挑战了几位好友
                 Log.e("DEMO","challenge? " + " count: " + count);
            }

            @Override
            public void onReceiveLikeResult(boolean success) {
                 //点赞回调，是否点赞成功
                  Log.e("DEMO","like? " + success);
            }

 });
 ```
## 8,提供对广告操作的相关接口的监听回调
```java
builder.setRewardAdListener(new AdListener() {
            @Override
            public void onReceiveReward(boolean success, int id) {
                //success:是否成功显示视频广告 ,id:广告id
                Log.e("DeMO","on receive reward? " + id + ", success = " + success);
            }

            @Override
            public void onFullAdClosed() {
                //全屏广告被关闭
            }

            @Override
            public void onFullAdClicked() 
                //全屏广告被点击
            }

            @Override
            public void onVideoAdClosed() {
                //视频广告被关闭
            }

            @Override
            public void onBannerAdClicked() {
                //Banner广告被点击
            }

            @Override
            public void onCrossAdClicked() {
                //交叉推广广告被点击
            }
});
```
## 9,提供使用应用内支付接口的回调 
```java
builder..setPaymentResultListener(new PaymentResultListener() {
            @Override
            public void onPaymentSuccess(int billId) {
                //支付成功
                Log.d("DEMO","payment success: " + billId);
            }

            @Override
            public void onPaymentFail(int billId) {
                 //支付失败
                 Log.d("DEMO","payment fail: " + billId);
            }

            @Override
            public void onPaymentCanceled(int bill) {
                //取消支付
                Log.d("DEMO","payment cancel: " + bill);
            }

            @Override
            public void onPaymentSystemValid() {
                //手机，平板等不支持支付功能
                Log.d("DEMO", "pay system is valid");
            }
  });
  ```

