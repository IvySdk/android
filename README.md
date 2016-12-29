#  android sdk 文档
## 1，添加risesdk.jar文件依赖，将demo中的libs目录下的risesdk.jar文件拷贝到您项目中进行引用
![Copy](/capture/android_pic1.bmp)
## 2，如果您有使用proguard来混淆Java代码，需要添加以下规则：
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
## 3,在Activity中初始化Android SDK,构造AndroidSDK builder对象，提供初始化完成回调
```java
  @Override
    protected void onCreate() {
       AndroidSdk.Builder builder = new AndroidSdk.Builder();
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
        });
       AndroidSdk.onCreate(this, builder); 
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
## 4，以下类型广告的api以及回调:
* 全屏广告，需配置不同时机弹出的广告，以便于后台统计,我们预定义了以下几种时机弹出的广告：
```java
AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_START); //游戏开始时
AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE); //游戏暂停
AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PASS_LEVEL);//游戏过关
AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_EXIT); //游戏退出
AndroidSdk.showFullAd("xxx"); //您还可以自定义类型广告
``` 
 注意：不要在activity的生命周期onResume()和onPause()中调用弹出全屏广告
  
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
* 广告相关操作回调
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
## 5, faceook相关操作的接口以及回调
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
 //返回的json格式如下：
 [
 {
 "id":"0000000000000001",//我的facebook好友1的账户id
 "name":"Friend 1",//我的facebook好友1的账户名称
 "picture":"/data/empty_not_exists1"//我的facebook好友1个人头像本地保存的绝对路径
 },
 {
  "id":"0000000000000002",//我的facebook好友2的账户id
 "name":"Friend 2",//我的facebook好友2的账户名称
 "picture":"/data/empty_not_exists2"//我的facebook好友2个人头像本地保存的绝对路径
 },
 {
 "id":"0000000000000003",//我的facebook好友3的账户id
 "name":"Friend 3",//我的facebook好友3的账户名称
 "picture":"/data/empty_not_exists3"//我的facebook好友3个人头像本地保存的绝对路径
 }
 ]
 ```
* 获取我的faceook个人信息

```java
String meJson = AndroidSdk.me();
//返回的json格式如下：
 {
 "id":"0000000000000000",//我的facebook账户id
 "name":"Me is me",//我的facebook账户名称
 "picture":"/data/empty_not_exists"//我的facebook账户个人图片本地保存的绝对路径
 }
  ```
* 退出facebook账户
```java
 AndroidSdk.logout();
 ```
* facebook接口回调：
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

## 6,应用内支付的接口以及回调，后台配置计费点 ：
```java
int billId = 1; //计费点
AndroidSdk.pay(billId);//支付接口，对计费点进行支付
AndroidSdk.query(billId);//查询支付结果
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
## 7，友盟统计相关接口
* 统计玩家等级
```java
int level = 1; //玩家等级
AndroidSdk.UM_setPlayerLevel(level);//统计玩家等级
```
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
AndroidSdk.UM_onEvent(eventId, eventLabel);
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
AndroidSdk.UM_pay(money,itemName,number,price);
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
int count = 10;//使用道具数量
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
## 8，我们额外还提供以下接口：
* 判断网络是否连接
```java
boolean isNetworkConnected = AndroidSdk.isNetworkConnected();
```
* 弹出android原生toast提示
```java
String messageContent="我是toast消息内容";
AndroidSdk.toast(messageContent);
```
* 弹出android原生alert dialog
```java
String title = "我是标题";
String message = "我是内容";
AndroidSdk.alert(title,message);
```
* 游戏，应用程序退出
```java
AndroidSdk.onQuit();
```
* 缓存文件
```java
String url = "http://xxxx.png";//文件下载连接
String path = AndroidSdk.cacheUrl(url); //返回保存文件的绝对路径（/sdcard/0/.cache/383292918283483291）
```
* 给应用，游戏等五星好评
```java
AndroidSdk.rateUs();
```
* 给应用，游戏等加Google Analytics统计
```java
AndroidSdk.track("shop"); //统计商店页面
AndroidSdk.track("shop","buy"); //商店页面购买装备统计
AndroidSdk.track("shop","buy","血瓶"); //商店页面购买血瓶装备统计
```
## 9，demo中有对接口的详细注释，如果您看完demo后还有不明白之处，您可以发送邮件至appdev@ivymobile.com进行咨询！谢谢！



  
