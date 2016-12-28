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
-dontwarn com.unity3d.**
-keep class com.android.client.** {
    <methods>;
}

-keep class android.support.** {
    *;
}

-keep class com.android.async.** {
    public *;
}

-keep class com.android.common.** {
    public *;
}

-keep class com.android.network.** {
    public *;
}

-keep class com.android.view.** {
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
AndroidSdk.pay(billId);//支付接口，对计费点进行支付
AndroidSdk.query(billId);//查询支付结果
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
/**
* AndroidSdk.pay(billId);//支付接口，对计费点进行支付
  AndroidSdk.query(billId);//查询支付结果
  PaymentResultListener是以上两个接口的回调类
*/
builder.setPaymentResultListener(new PaymentResultListener() {
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
                //手机，平板等支持支付功能,支付环境有效的回调
                Log.d("DEMO", "pay system is valid");
            }
  });
  ```
## 10，提供友盟统计相关接口
* 统计进入某页面
```java
String pageName = "Shop"; 
AndroidSdk.UM_onPageStart(pageName);//统计进入商店页面
```
* 统计离开某页面
```java
String pageName = "Shop";
AndroidSdk.UM_onPageEnd(pageName);//统计离开商店页面
```  
* 统计事件名称
```java
String eventId = "EnterGame"; //事件名称
AndroidSdk.UM_onEvent(eventId);
```
* 统计事件标签操作
```java
String eventId = "EnterGame"; //事件名称
String eventLabel = "eventLable";//事件的某个操作标签
AndroidSdk.UM_onEvent("EnterGame", "openGift");
```
* 统计事件详细分组内容
```java
HashMap<String, String> map = new HashMap<>(); //事件详细分组内容
map.put("openGift", "roll");
int value = 1;//计数统计值，比如持续时间，每次付款金额
AndroidSdk.UM_onEventValue("EnterGame", map, value);
```
* 统计关卡开始
```java
String level = "Level" + 5;//level ,开始哪个关卡
AndroidSdk.UM_startLevel(level);
```
* 统计关卡失败
```java
String level = "Level" + 5); //level ,哪个关卡失败
AndroidSdk.UM_failLevel(level);
```
* 关卡结束
```java
String level = "Level" + (new Random().nextInt(30) + 10); //level,完成哪个关卡
AndroidSdk.UM_finishLevel(level);
```
* 游戏内付统计
```java
double money = 5.0; //内付的金额
String itemName = "钻石"; //内付购买的商品名称
int number = 10;//内付购买的商品数量
double price = 99.0;//内付购买的商品价格
AndroidSdk.UM_pay(level); 
```
* 购买道具统计
```java
String itemName = "血瓶"; //购买游戏中道具名称
int number = 10;//购买道具数量
double price = 99.0;//购买道具价格
AndroidSdk.UM_buy(itemName,count,price); 
```
* 使用道具统计
```java
String itemName = "血瓶"; //使用道具名称
int number = 10;//使用道具数量
double price = 99.0;//使用道具价格
AndroidSdk.UM_use(itemName,count,price); 
```
* 额外奖励统计
```java
String itemName = "血瓶"; //奖励道具名称
int number = 5;//奖励道具数量
double price = 99.0;//奖励道具价格
int trigger = 1;//触发奖励的事件, 取值在 1~10 之间，“1”已经被预先定义为“系统奖励”， 2~10 需要在网站设置含义
AndroidSdk.UM_bonus(itemName,number,price,trigger); 
```

  
