# android sdk 文档

# 前言

**default.json: sdk配置文件, 广告、计费、打点等功能均需要通过此文件配置；对各部分配置详细说明见文档最底部**

## 1, 添加引用

1. 添加default.json到native/engine/android/app/src/main/assets目录
2. 修改native/engine/android/app/build.gradle,添加一下配置

```js
defaultConfig {
    //...
        manifestPlaceholders = [
                ivy_debug: true,
                din: false,
                gms_games_app_id  : "933591439403",
                google_admob_application_id: 'ca-app-pub-1914768831611213~5856809174',
                facebook_appId             : '419434508669986',
                facebook_clientToken: '003925c73e83fe389581f4700b43f16c'
        ]
    }
```

**Notice**

* ivy_debug                   : 是否是debug模式
* din                          : 是否主动时配刘海屏
* gms_games_app_id*            : google play games id
* google_admob_application_id  : admob app id
* facebook_appId              : facebook app id
* facebook_clientToken         : facebook 客户端token
* 如果您有使用proguard来混淆Java代码，需要添加以下规则：
* if you use proguard to obfuscate your java source code, you should add these rules to your proguard rules file:

**重要！！！**
在打包时务必将`AndroidManifest.xml`中的`UnityPlayerActivity`指向`com.android.client.UnityPlayerActivity `

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

## 2, 添加firebase

1. 在Firebase控制台创建项目
2. 在Firebase项目中点击Android机器人图标，开始设置Android应用，添加正确的包名及签名文件的SHA-1
3. 点击注册应用
4. 在对应Android应用目录下下载配置文件(google-services.json),并放置在应用的模块(应用级)根目录中
5. 在根级build.gradle中添加

```js
dependencies {
       //...
        classpath 'com.google.gms:google-services:4.3.0'
    }
```

6. 在模块(应用级)的build.gradle文件末尾添加插件

```js
apply plugin: 'com.google.gms.google-services'
```

## 3, 配置 Firebase crashlytics

1. 引入 Firebase crashlytics 插件
   在项目Build.gradle中增加以下配置:

```js
dependencies {
           //...
            classpath 'com.google.firebase:firebase-crashlytics-gradle:2.5.1'
        }
```

2. 引用Firebase crashlytics 插件
   在项目主module的Build.gradle文件末尾增加以下配置

```js
apply plugin: 'com.google.firebase.crashlytics'
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
    protected void onResume() {
        super.onResume();
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

* 全屏广告，
  1. 插屏状态

```
AndroidSdk.hasFull(String tag);
```

2. 展示插屏

```
AndroidSdk.showFullAd(tag, new AdListener() {
            @Override
            public void onAdClosed() {
                sendMessage("onFullAdClosed", tag);
            }

            @Override
            public void onAdShow() {
                sendMessage("onFullAdShown", tag);
            }

            @Override
            public void onAdClicked() {
                sendMessage("onFullAdClicked", tag);
            }
        });
```
 

* banner广告

java
int[] bannerPos = {
           AndroidSdk.POS_CENTER,      //居中显示banner广告
           AndroidSdk.POS_CENTER_BOTTOM, //底部居中显示banner广告
           AndroidSdk.POS_CENTER_TOP, //顶部居中显示banner广告
           AndroidSdk.POS_LEFT_BOTTOM, //左下角显示banner广告
           AndroidSdk.POS_LEFT_TOP, //左上角显示banner广告
           AndroidSdk.POS_RIGHT_BOTTOM, //右下角显示banner广告
           AndroidSdk.POS_RIGHT_TOP //右上角显示banner广告
};

AndroidSdk.hasBanner(String tag); //是否已准备好banner
AndroidSdk.showBanner("default", bannerPos[0]); //居中显示banner广告
AndroidSdk.closeBanner("default"); //关闭banner广告

* 视频广告

```java
int rewardId = 1; //客户端配置的视频广告奖励id，自定义即可，依据这个id决定给玩家奖励什么物品，以及数量等
if(AndroidSdk.hasRewardAd("shop")){ //检查是否有视频广告
    AndroidSdk.showRewardAd("shop", new AdListener(){
        @Override
        public void onAdReward(boolean skip) {
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

## 7，gp 相关操作

* 登录google

```
AndroidSdk.loginGoogle(new GoogleListener() {
            @Override
            public void onSuccess(String googleId, String googleEmail) {
                Logger.debug(TAG, "Google onSuccess: " + googleId);
                sendMessage("onLoginGoogleSuccess", googleId);
            }

            @Override
            public void onFails() {
                Logger.debug(TAG, "Google onFails: ");
                sendMessage("onLoginGoogleFailure", FALSE);
            }
        });
```

* google登录状态

```
AndroidSdk.isGoogleLogin();
```

* 退出google登录

```
AndroidSdk.logoutGoogle(new GoogleListener() {
            @Override
            public void onSuccess(String googleId, String googleEmail) {
                sendMessage("onLogoutGoogle", TRUE);
            }

            @Override
            public void onFails() {
                sendMessage("onLogoutGoogle", FALSE);
            }
        });
```

* 更新排行榜

```
AndroidSdk.updateGoogleLeaderBoard(String leaderBoardId, long value, new GoogleListener() {
            @Override
            public void onSuccess(String googleId, String googleEmail) {
                sendMessage("onUpdateLeaderBoard", id + "|" + TRUE);
            }

            @Override
            public void onFails() {
                sendMessage("onUpdateLeaderBoard", id + "|" + FALSE);
            }
        });
```

* 展示排行榜

```
AndroidSdk.showGoogleLeaderBoards(String leaderBoardId);
```


* 展示所有排行榜

```
AndroidSdk.showGoogleLeaderBoards();
```

* 更新成就

```
AndroidSdk.updateGoogleAchievement(String achievementId, int step, new GoogleListener() {
            @Override
            public void onSuccess(String googleId, String googleEmail) {
                sendMessage("onUpdateAchievement", id + "|" + TRUE);
            }

            @Override
            public void onFails() {
                sendMessage("onUpdateAchievement", id + "|" + FALSE);
            }
        });
```

* 展示成就

```
AndroidSdk.showGoogleAchievements();
```



## 6,应用内支付的接口以及回调，后台配置计费点 ：

```java
int billId = 1; //计费点，和运营人员确定即可
AndroidSdk.pay(billId);//支付接口，对计费点进行支付
AndroidSdk.pay(billId, payload)
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

V2以后支付成功以后，会同步回调配置的游戏服务器接口(可选），可以在此接口中实现游戏发货，进一步校验的逻辑。会上传如下参数

```
// 所在的国家
.add("country", countryCode)
// 用户购买的Google SKU
.add("sku", purchase.getSku())
// billId
.add("payId", String.valueOf(billId))
// 订单号
.add("orderId", purchase.getOrderId())
.add("purchaseTime", String.valueOf(purchase.getPurchaseTime()))
// purchaseToken可以用来自行向google服务器监听购买的通知，比如退款等
.add("purchaseToken", purchase.getPurchaseToken())
.add("purchaseState", String.valueOf(purchase.getPurchaseState()))
.add("uuid", IvySdk.getUUID())
.add("packageName", purchase.getPackageName())
.add("jsonData", purchase.getOriginalJson())
.add("signature", purchase.getSignature())
.add("sku_json", skuDetail != null ? skuDetail.toString() : "{}")
.add("appid", appid != null ? appid : "")
// 可选，如果使用pay(billId, payload)接口，将提交
.add("payload", payload)
```

游戏服务器接口，需要使用返回一个加密的json字符串（请联系），再强校验的情况下，SDK会检查status字段，明确为1才会回调onPaymentSuccess的客户端回调。
payload: 客户端通过pay(billId, payload)接口调用支付接口，对接的服务器接口将上传payload, payload可以加密保存一些支付的用户信息，用于后续的校验或发货。

服务器可以对jsonData进行验签，防止数据被修改 参考：
https://gist.github.com/mkorszunsands/3cbc0016ac7d0bca318228f96ffd0ab3

https://stackoverflow.com/questions/30420847/verification-google-play-purchase-from-server-side

支付校验PHP Sample

```php
public function get_openssl_key($publicKey){
  $key = "-----BEGIN PUBLIC KEY-----\n".chunk_split($publicKey, 64, "\n")."-----END PUBLIC KEY-----";
  $key = openssl_get_publickey($key);
  return $key;
}

$public_keys = 'google - public -key'; //计费平台公钥
$key = $this->get_openssl_key($public_keys);
// signature: 是客户端post过来的signature数据
// jsonData 是客户端Post过来的json数据
$result = openssl_verify($jsonData, base64_decode($signature), $key, OPENSSL_ALGO_SHA1);
```

接口返回

```json
{"status":1}
```

的JSON字符串，将认为发货成功，将调用支付成功，否则将调用支付失败回调。
可选的可以回传payload

```json
{"status" : 1, "payload": payload}
```

这样客户端收到的回调就会包含此payload, 服务器可以将个性化的数据包含在此payload中

AndroidSdk.getPrices函数返回以billId为key的本地化json字符串
注意: 此函数返回的数据依赖于Google Play的连接，所以游戏刚打开的时候可能没有。

```java
/// 返回json数据，格式为：
    /// 22 23 等是计费点
    /// price_amount 一般用于计算折扣，当前的价格是打折后的价格，通过折扣来计算原价，展示给用户
    /// {
    ///     "22": {
    ///         "id": "com.ivy.galaxyshooting.sky22",
    ///         "type": "inapp",
    ///         "price": "HK$15.00",
    ///         "price_amount": 15.0,
    ///         "currency": "HKD",
    ///         "title": " remove Ads +2000 coins (Galaxy sky shooting)",
    ///         "desc": " remove Ads +2000 coins"
    ///     },
    ///     "23": {
    ///         "id": "com.ivy.galaxyshooting.sky23",
    ///         "type": "subs",
    ///         "price": "HK$15.00",
    ///         "price_amount": 15.0,
    ///         "currency": "HKD",
    ///         "title": "VIP Privilege (Galaxy sky shooting)",
    ///         "desc": "VIP Privilege"
    ///     }
    /// }
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

## 9, 远程配置

RemoteConfig接口首先会检查Firebase RemoteConfig的值，如果是默认值将fallback到运营本地配置的值。

注意: Firebase Remote Config不是数据库，在后台更新后不会立马生效，Firebase会根据自己的策略发布到用户设备上。

```java
AndroidSdk.getRemoteConfigInt(String key);
AndroidSdk.getRemoteConfigLong(String key);
AndroidSdk.getRemoteConfigDouble(String key);
AndroidSdk.getRemoteConfigBoolean(String key);
AndroidSdk.getRemoteConfigString(String key);
```

## default.json 各部分说明

sdk配置文件,放置在`native/engine/android/app/src/main/assets`目录下，广告、计费、打点等功能均需要通过此文件配置

* 完整default.json 示例

```js
{
  "appflyers.devkey": "J6ejjnUP9fMkv29PqBuYzR",
  "requireFriends": true,
  "debug": false,
  "enableAfAdPing": false,
  "mixAdEvents": false,
  "api.top_user_advalue": "https://hda2k62cp0.execute-api.us-west-1.amazonaws.com/top_user_advalue",
  "ad.ecpm.url": "https://k3kp5yrim4.execute-api.us-west-1.amazonaws.com/default/ivy-ecpm-api",
  "providers": [],
  "data": {
    "push": [
      {
        "api": "firebase",
        "manual": 0,
        "topic": "",
        "push-server-url": "http://push.papermobi.com:7778/fcmpush"
      }
    ]
  },
  "sns": {
    "api": "facebook",
    "invite_url": "",
    "invite_preview_url": "",
    "like_url": "",
    "friends": true,
    "leader_board_url": "http://match3games1.iibingo.com/api"
  },
  "share": "https://play.google.com/store/apps/details?id=com.bubbleshooter.popbubbles.shootbubblesgame",
  "remoteconfig": {
    "PAM_ad_unit_android_banner": "ca-app-pub-1914768831611213/4106575741",
    "PAM_ad_unit_android_interstitial": "ca-app-pub-1914768831611213/2402557932",
    "PAM_ad_unit_android_rewarded": "ca-app-pub-1914768831611213/5419657410",
    "is_pam_banner": false,
    "is_pam_inter": false,
    "is_pam_video": false
  },
  "banner": [
    {
      "provider": "admob",
      "p": {
        "placement": "ca-app-pub-1914768831611213/4106575741"
      }
    }
  ],
  "full": [
    {
      "provider": "admob",
      "p": {
        "placement": "ca-app-pub-1914768831611213/2402557932"
      }
    }
  ],
  "video": [
    {
      "provider": "admob",
      "p": {
        "placement": "ca-app-pub-1914768831611213/5419657410"
      }
    }
  ],
  "adLoadTimeout": 10,
  "adRefreshInterval": 1800,
  "bannerLoadTimeoutSeconds": 5,
  "gen_events": {
    "interstitial_shown_2_in1day": [
      {
        "e1": "interstitial_shown",
        "v": 2,
        "op": ">=",
        "d": 1,
        "r": false
      }
    ],
    "S3_1D": [
      {
        "e1": "interstitial_shown",
        "v": 3,
        "op": ">=",
        "d": 1,
        "r": false
      }
    ],
    "S4_1D": [
      {
        "e1": "interstitial_shown",
        "v": 4,
        "op": ">=",
        "d": 1,
        "r": false
      }
    ],
    "S5_1D": [
      {
        "e1": "interstitial_shown",
        "v": 5,
        "op": ">=",
        "d": 1,
        "r": false
      }
    ],
    "interstitial_shown_2_in3day": [
      {
        "e1": "interstitial_shown",
        "v": 2,
        "op": ">=",
        "d": 3,
        "r": false
      }
    ],
    "video_shown_2_in1day": [
      {
        "e1": "video_shown",
        "v": 2,
        "op": ">=",
        "d": 1,
        "r": false
      }
    ],
    "video_shown_2_in3day": [
      {
        "e1": "video_shown",
        "v": 2,
        "op": ">=",
        "d": 3,
        "r": false
      }
    ]
  },
  "summary_events": {
    "op": [
      2,
      3
    ],
    "retention": [
      1,
      2,
      3,
      4,
      5,
      6,
      7
    ],
    "cv": {
      "count": [
        1,
        2,
        3,
        5,
        10,
        20
      ],
      "e": "video_completed"
    }
  },
  "adParallelRequests": 1,
  "adParallelWaitTime": 5,
  "dontShowFullPageAdsOnSlowConnection": false,
  "adFullScreenTimespan": 120,
  "ad": {
    "adNextLoadInterval": 15,
    "timeToWaitForAdToShowSeconds": 5,
    "updateBanner": [],
    "adDelayFirstInterstitialCallSec": 30,
    "adProvidersRefreshInMinutes": 60,
    "useBannerFingerPrinting": true,
    "useVideoClipPreloading": true,
    "rewardedClipsCaps": {
      "*": {
        "intervalHours": 24,
        "maxImpressions": 4
      }
    },
    "iLTS": 10,
    "bLTS": 10,
    "aC": {
      "iTs": [
        20,
        120
      ],
      "iPTs": [
        0,
        60
      ],
      "fIPT": 5,
      "fIPSS": 2,
      "iSTs": [
        {
          "f": "*",
          "t": "*"
        }
      ]
    }
  },
  "payment": {
    "checkout": {
      "1": {
        "feename": "25coins",
        "repeat": 1,
        "usd": 1.99
      },
      "2": {
        "feename": "70coins",
        "repeat": 1,
        "usd": 4.99
      },
      "key": "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtpVDxkfuv2T6jkErdllxnHeMr4JqFucU+gontr0yYlvM4qt3PHlg1VY/1CQeLskBs2K420cQDaAIfCvAqil3VB6NWSci0SbMGkCidPQmlQRn90MCFAX3t+dRwgAlFwvlj4yVP53giV9GKfWVQ4EehWTg9h8fgRtILEEGgNFJMFz3MUS3erBBSOpuc9UDDtnvEo4NRaH6yUI4zUn46nrgskchYlUTeYEQo1y4lS3uPHpgriBk8XLxFIXSyKjHnnNwSeRSqeX6gEKUrzVpxzp122qv4ebdqPfyzxLUySqhkKkr3dkt45OP+/7xn3jYT5V8ClUA9GgoMwZv80bASfh9wwIDAQAB"
    }
  },
  "gts": 1692666353000,
  "gv": 1,
  "ver": 2,
  "appid": 2712,
  "v_api": 0,
  "token": "174f3dc7c2a0d2f56c9dba7946965fb6"
}
```

### 普通属性

* appid: 应用id
* v_api: 此配置版本号
* appflyers.devkey: af app id
* requireFriends:是否在facebook登陆是拉取朋友列表
* enableAfAdPing:是否开启自定义af广告收入统计事件
* mixAdEvents：交叉推广事件是否与正式广告事件合并
* api.top_user_advalue：用户分层价值事件，不需要修改
* push: 自定义应用内消息推送
* remoteconfig：key-value格式的预配置字段，在firebase remote config 获取失败时可使用

### 广告

广告类型包括 banner、full(大屏广告)、video(激励视频广告)
内部可配置属性：

- provider：广告平台，此sdk仅包含Admob
- p: 广告单元
- placement: 广告id

**tip:每种广告类型可配置多个广告单元:**
注：provider值中的 1x、2x 作为广告单元区分，在调用广告时区分广告对象

```js
"video": [
    {
      "provider": "admob_1x",
      "p": {
        "placement": "ca-app-pub-1914768831611213/5419657410"
      }
    },
    {
      "provider": "admob_2x",
      "p": {
        "placement": "ca-app-pub-1914768831611213/5419657410"
      }
    }
  ]
```

* adLoadTimeout：广告加载自定义超时时间
* adRefreshInterval：banner广告自动刷新间隔，单位ms
* bannerLoadTimeoutSeconds： banner自定义加载超时时间

### 计费

所有计费点信息配置在 payment 字段结构中

* key：用于在计费点购买后的校验支付结果，若留空，则购买后不会校验购买结果，以google billing 返回状态为最终购买结果
* checkout：计费点信息列表，示例：

```js
//说明：
// billId：即每个计费点对应的序号，如 1，2；在程序中传递此序号用以支付对应计费点
// feename：google billing 后台创建的计费点名称
// repeat： 若值为 1 则为消耗型计费点；0 则为订阅型计费点
// usd：计费点价格
     "1": {
        "feename": "25coins",
        "repeat": 1,
        "usd": 1.99
      },
      "2": {
        "feename": "70coins",
        "repeat": 1,
        "usd": 4.99
      }
```

### 转化事件

gen_events、summery_events 内配置用作app转化标记事件，可不修改

### 通过firebase remote config 下发配置

1. 整体下发
   * 将default.json内容作为字段 config_grid_data_android 的值配置
2. 仅下发Banner配置
   * 将banner配置作为字段 ad_config_banner 的值配置
     示例：

```js
{
    "ads":[
        {
          "provider": "admob",
          "p": {
            "placement": "ca-app-pub-1914768831611213/4106575741"
          }
        }
    ],
    "adRefreshInterval": 1800
}
```

3. 仅下发大屏广告配置
   * 将大屏广告配置作为字段 ad_config_full 的值配置
     示例：

```js
{
    "ads":[
        {
          "provider": "admob",
          "p": {
            "placement": "ca-app-pub-1914768831611213/2402557932"
          }
        }
    ]
}
```

4. 仅下发激励视频广告配置
   * 将激励视频广告配置作为字段 ad_config_video 的值配置

```js
{
    "ads":[
         {
          "provider": "admob",
          "p": {
            "placement": "ca-app-pub-1914768831611213/5419657410"
          }
        }
    ]
}
```



## 10，demo中有对接口的详细注释，如您看完demo后还有不明白之处，可发送邮件到appdev@ivymobile.com，我们会尽快给您回复！谢谢！

 
