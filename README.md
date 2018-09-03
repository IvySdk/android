#  android sdk 文档
# 当前版本：5.2.21
English Doc Link ：https://github.com/IvySdk/android/wiki/English-Doc
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
       AndroidSdk.registerAdEventListener(new AdEventListener());
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        AndroidSdk.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
```
## 4，广告相关接口以及回调:
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
            AndroidSdk.POS_CENTER_BOTTOM, //底部居中显示banner广告
            AndroidSdk.POS_CENTER_TOP, //顶部居中显示banner广告
            AndroidSdk.POS_LEFT_BOTTOM, //左下角显示banner广告
            AndroidSdk.POS_LEFT_TOP, //左上角显示banner广告
            AndroidSdk.POS_RIGHT_BOTTOM, //右下角显示banner广告
            AndroidSdk.POS_RIGHT_TOP //右上角显示banner广告
 };
 AndroidSdk.showBanner("default", bannerPos[0]); //居中显示banner广告
 AndroidSdk.closeBanner("default"); //关闭banner广告
 ```
* 视频广告
```java
int rewardId = 1; //客户端配置的视频广告奖励id，自定义即可，依据这个id决定给玩家奖励什么物品，以及数量等
if(AndroidSdk.hasRewardAd("shop")){ //检查是否有视频广告
    AndroidSdk.showRewardAd("shop", new AdListener(){
        @Override
        public void onAdReward() {
            asyncToast("shop reward video is played");
        }
    }); //展示视频广告
}
```

## 5, faceook接口以及回调
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
 AndroidSdk.share(); 
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

            @Override
            public void onReceiveFriends(String s) {
                asyncToast("friends: " + s);
            }

 });
 ```

## 6,应用内支付的接口以及回调，后台配置计费点 ：
```java
int billId = 1; //计费点，和运营人员确定即可
AndroidSdk.pay(billId);//支付接口，对计费点进行支付
AndroidSdk.query(billId);//查询支付结果
/**
* AndroidSdk.pay(billId); //支付接口，对计费点进行支付
  AndroidSdk.query(billId); //查询支付结果
  PaymentSystemListener 是以上两个接口的回调类
*/
builder.setPaymentListener(new PaymentSystemListener() {
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
* 提供给欧盟区的设置: 在应用的设置页面提供一个设置项，让用户可以设置广告相关的策略
```java
// 判断该用户是否是欧盟区的
AndroidSdk.hasGDPR()

// 显示广告设置页面
AndroidSdk.resetGDPR();

// 使用场景
如果是欧盟区的用户，你需要提供一个设置项
if (AndroidSdk.hasGDPR()){
    // 这里展示你们自己的设置项 UI
    // 如果用户点击了你们的设置项，
    // 则你们需要调用 AndroidSdk.resetGDPR() 来展示广告设置
    // 广告设置我们已经做好了，你们不需要自己另做界面
}
```

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
## 9，demo中有对接口的详细注释，如您看完demo后还有不明白之处，可发送邮件到appdev@ivymobile.com，我们会尽快给您回复！谢谢！
  
