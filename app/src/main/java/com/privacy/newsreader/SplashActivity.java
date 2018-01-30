package com.privacy.newsreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.client.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;

/**
 * Created by song on 16/5/26.
 */
public class SplashActivity extends Activity implements View.OnClickListener {

    /*private boolean nativeShowing;
    private boolean nativeBannerShowing;*/

    /**
     * sdk初始化相关接口的监听回调
     * {@link com.android.client.AndroidSdk.Builder#setSdkResultListener(SdkResultListener)}
     */
    private SdkResultListener sdkResultListener = new SdkResultListener() {
        /**
         * sdk初始化成功接口
         */
        @Override
        public void onInitialized() {
            asyncToast("sdk完成初始化");
        }

        /**
         * 初始化成功后收到服务器回传数据
         * @param data 服务器返回的数据
         */
        @Override
        public void onReceiveServerExtra(String data) {
            asyncToast("收到服务器数据:" + data);
        }

        /**
         * 初始化成功后收到通知数据
         * @param data 收到服务器通知数据
         */
        @Override
        public void onReceiveNotificationData(String data) {
            asyncToast("收到服务器通知数据: " + data);
        }
    };

    /**
     * {@link com.android.client.AndroidSdk.Builder#setUserCenterListener(UserCenterListener)}
     * 对facebook用户操作相关接口的监听回调
     */
    private UserCenterListener userCenterListener = new UserCenterListener() {
        /**
         * {@link AndroidSdk#login()} SDK登陆方法的回调
         * 登陆facebook账户成功与否回调
         * @param success 登陆是否成功
         */
        @Override
        public void onReceiveLoginResult(boolean success) {
            asyncToast(success ? "登陆成功" : "登陆失败");
        }

        @Override
        public void onReceiveFriends(String s) {
            asyncToast("friends: " + s);
        }

        /**
         * {@link AndroidSdk#invite()} SDK邀请好友接口的回调
         * 是否成功邀请好友安装程序，游戏等
         * @param success 是否邀请成功
         */
        @Override
        public void onReceiveInviteResult(boolean success) {
            asyncToast(success ? "邀请成功" : "邀请失败");
        }

        /**
         * {@link AndroidSdk#challenge(String, String)}  SDK挑战好友接口的回调
         * @param count 成功挑战的好友个数
         */
        @Override
        public void onReceiveChallengeResult(int count) {
            //收到挑战结果的回调，count代表成功挑战了几位好友
            asyncToast("成功挑战" + count + "位好友");
        }

        /**
         * {@link AndroidSdk#like()} SDK点赞接口的回调
         * @param success 是否点赞成功
         */
        @Override
        public void onReceiveLikeResult(boolean success) {
            //点赞回调，是否点赞成功
            asyncToast(success ? "点赞成功" : "点赞失败");
        }

    };

    /**
     * 对广告操作的相关接口的监听回调
     */
    private AdListener adListener = new AdListener() {
        @Override
        public void onAdShow() {
            super.onAdShow();
        }

        @Override
        public void onAdClosed() {
            super.onAdClosed();
        }

        @Override
        public void onAdClicked() {
            super.onAdClicked();
        }

        @Override
        public void onAdShowFails() {
            super.onAdShowFails();
        }

        /*
        * {@link AndroidSdk#showRewardAd(String, AdListener)}
        */
        @Override
        public void onAdReward() {
            super.onAdReward();
            asyncToast("视频广告显示成功");
        }

        @Override
        public void onAdLoadSuccess() {
            super.onAdLoadSuccess();
        }

        @Override
        public void onAdLoadFails() {
            super.onAdLoadFails();
        }

    };

    /**
     * 使用应用内支付接口的回调
     * /**
     * {@link AndroidSdk#pay(int)};//支付接口，对计费点进行支付
     * {@link AndroidSdk#query(int)};//查询支付结果
     * PaymentResultListener是以上两个接口的回调类
     */
    PaymentSystemListener paymentResultListener = new PaymentSystemListener() {
        /**
         * 支付成功
         * @param billId 计费点
         */
        @Override
        public void onPaymentSuccess(int billId) {
            asyncToast("支付成功，计费点： " + billId);
        }

        /**
         * 支付失败
         * @param billId 计费点
         */
        @Override
        public void onPaymentFail(int billId) {
            //支付失败
            asyncToast("支付失败，计费点是： " + billId);
        }

        /**
         * 取消支付
         * @param bill 计费点
         */
        @Override
        public void onPaymentCanceled(int bill) {
            asyncToast("取消支付，计费点是 " + bill);
        }

        /**
         * 手机，平板等系统 支付环境有效，可以进行支付
         */
        @Override
        public void onPaymentSystemValid() {
            asyncToast("支付环境有效");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        AndroidSdk.Builder builder = new AndroidSdk.Builder();

        builder.setSdkResultListener(sdkResultListener)
                .setUserCenterListener(userCenterListener)
                .setPaymentListener(paymentResultListener);

        AndroidSdk.onCreate(this, builder);

        setContentView(R.layout.main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                /**
                 * 全屏广告，需与后台配置tag ,不同时机弹出不同的广告
                 * 我们已经预定义以下几种时机弹出的广告：
                 * {@link AndroidSdk#FULL_TAG_START} 游戏开始时弹出
                 * {@link AndroidSdk#FULL_TAG_PAUSE} 游戏暂停时弹出
                 * {@link AndroidSdk#FULL_TAG_PASS_LEVEL} 游戏过关时弹出
                 * {@link AndroidSdk#FULL_TAG_EXIT} 游戏退出时弹出
                 * {@link AndroidSdk#FULL_TAG_CUSTOM} 自定义时机弹出
                 */
                AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_START);
                break;

            case R.id.more:
                /**
                 * 跳到推广的游戏列表界面
                 */
                AndroidSdk.moreGame();
                break;

            case R.id.banner:
                /**
                 * 显示banner广告 ，
                 * 显示位置已经预定义以下几个：
                 * {@link AndroidSdk#POS_CENTER} 居中显示
                 * {@link AndroidSdk#POS_CENTER_BOTTOM} 居中底部显示
                 * {@link AndroidSdk#POS_CENTER_TOP} 居中顶部显示
                 * {@link AndroidSdk#POS_LEFT_BOTTOM} 左下角显示
                 * {@link AndroidSdk#POS_LEFT_TOP} 左上角显示
                 * {@link AndroidSdk#POS_RIGHT_BOTTOM} 右下角显示
                 * {@link AndroidSdk#POS_RIGHT_TOP} 右上角显示
                 */
                AndroidSdk.showBanner("main", AndroidSdk.POS_CENTER);
                break;

            case R.id.bill:
                /**
                 * 对计费点进行支付
                 * {@link PaymentResultListener}
                 */
                int billId = 1; //计费点
                AndroidSdk.pay(billId);//支付接口，对计费点进行支付
                break;

            case R.id.close_banner:
                /**
                 * 关闭banner广告
                 */
                AndroidSdk.closeBanner("main");
                break;

            case R.id.video_ad:
                /**
                 * 显示视频广告
                 */
                int rewardId = 1; //客户端配置的视频广告调用时机
                if (AndroidSdk.hasRewardAd("shop")) { //检查后台是否有配置视频广告
                    AndroidSdk.showRewardAd("shop", new AdListener(){
                        @Override
                        public void onAdReward() {
                            asyncToast("shop reward video is played");
                        }
                    });
                }
               /* if (AndroidSdk.hasRewardAd("default")) {
                    AndroidSdk.showRewardAd("default", 1);
                } else {
                    Toast.makeText(this, "no video ad", Toast.LENGTH_SHORT).show();
                }*/
                break;

           /* case R.id.native_1:
                */
            /**
             * 显示native广告，暂时不对外开放
             */
              /*
                if (nativeShowing) {
                    AndroidSdk.hideNativeAdScrollView("unlock_pre");
                } else {
                    AndroidSdk.showNativeAdScrollView("unlock_pre", AndroidSdk.HIDE_BEHAVIOR_NO_HIDE, 50);
                }
                nativeShowing = !nativeShowing;
                break;*/

            case R.id.share:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * facebook分享,注意此方法会阻塞主线程，需启线程调用
                         */
                        AndroidSdk.share();
                    }
                }).start();
                break;

            case R.id.islogin:
                /**
                 * 判断是否登陆facebook账户
                 */
                toast(AndroidSdk.isLogin() ? "facebook账户已登陆" : "facebook账户未登陆");
                break;

            case R.id.login:
                /**
                 * 登陆facebook账户
                 * {@link UserCenterListener#onReceiveLoginResult(boolean)} 是此登陆接口的回调方法
                 */
                AndroidSdk.login();
                break;

            case R.id.logout:
                /**
                 * 退出facebook账户
                 */
                AndroidSdk.logout();
                break;

            case R.id.invite:
                /**
                 * 邀请facebook好友安装应用，游戏等
                 * {@link UserCenterListener#onReceiveInviteResult(boolean)} 是此邀请接口的回调方法
                 */
                AndroidSdk.invite();
                break;

            case R.id.challenge:
                /**
                 * 向你的facebook好友发起挑战
                 * {@link UserCenterListener#onReceiveChallengeResult(int)} 是此挑战接口的回调方法
                 */
                String title = "haha title"; //挑战标题
                String msg = "heihei message";//挑战内容
                AndroidSdk.challenge(title, msg);
                break;

            case R.id.friends:
                /**
                 * 获取faceook朋友信息列表
                 * 返回的json格式如下：
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
                 */
                toast(AndroidSdk.friends());
                break;

            case R.id.me: {
                try {
                    /**
                     * 获取我的faceook个人信息
                     * 返回的json格式如下：
                     {
                     "id":"0000000000000000",//我的facebook账户id
                     "name":"Me is me",//我的facebook账户名称
                     "picture":"/data/empty_not_exists"//我的facebook账户个人图片本地保存的绝对路径
                     }
                     */
                    String me1 = AndroidSdk.me();
                    JSONObject me = new JSONObject(me1);
                    if (me.has("picture")) {
                        ImageView vv = new ImageView(this);
                        vv.setImageURI(Uri.parse(me.getString("picture")));
                        AlertDialog dialog = new AlertDialog.Builder(this).setMessage(me.getString("name")).create();
                        dialog.show();
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(256, 256);
                        ((FrameLayout) dialog.getWindow().getDecorView()).addView(vv, lp);
                    } else {
                        toast(me1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;
            case R.id.like:
                /**
                 * facebook点赞
                 * {@link UserCenterListener#onReceiveChallengeResult(int)}是此点赞接口的回调方法
                 */
                AndroidSdk.like();
                break;
            case R.id.test_umeng:
                testUMeng();
            /*case R.id.show_native_banner:
                if (nativeBannerShowing) {
                    AndroidSdk.hideNativeBanner("unlock_pre");
                } else {
                    AndroidSdk.showNativeBanner("unlock_pre", 50, 50);
                }
                nativeBannerShowing = !nativeBannerShowing;
                break;*/
            default:
                break;
        }
    }

    private void testUMeng() {
        /**
         * 统计玩家等级
         */
        int level = 1; //玩家等级
        AndroidSdk.UM_setPlayerLevel(level);//统计玩家等级

        /**
         * 统计时间名称
         */
        AndroidSdk.UM_onEvent("EnterGame");

        /**
         *统计事件标签操作
         */
        String eventId = "EnterGame"; //事件名称
        String eventLabel = "eventLable";//事件的某个操作标签
        AndroidSdk.UM_onEvent(eventId, eventLabel);
        /**
         *统计事件详细分组内容
         */
        HashMap<String, String> map = new HashMap<>(); //事件详细分组内容
        map.put("openGift", "roll");
        int value = 1;//计数统计值，比如持续时间，每次付款金额
        AndroidSdk.UM_onEventValue("EnterGame", map, value);

        /**
         * 统计进入某页面
         */
        AndroidSdk.UM_onPageStart("Shop");
        /**
         * 统计离开某页面
         */
        AndroidSdk.UM_onPageEnd("Shop");
        /**
         * 统计关卡开始
         */
        AndroidSdk.UM_startLevel("Level1");

        /**
         * 统计关卡结束
         */
        AndroidSdk.UM_finishLevel("Level1");

        /**
         *统计关卡失败
         */
        AndroidSdk.UM_failLevel("leve12");

        /**
         * 游戏内支付统计
         */
        double money = 5.0; //内付的金额
        String itemName = "钻石"; //内付购买的商品名称
        int number = 10;//内付购买的商品数量
        double price = 99.0;//内付购买的商品价格
        AndroidSdk.UM_pay(money, itemName, number, price);

        /**
         * 购买道具统计
         */
        String itemName1 = "血瓶"; //购买游戏中道具名称
        int count = 10;//购买道具数量
        double price1 = 99.0;//购买道具价格
        AndroidSdk.UM_buy(itemName1, count, price1);

        /**
         * 使用道具统计
         */
        String itemName2 = "血瓶"; //使用道具名称
        int count2 = 10;//使用道具数量
        double price2 = 99.0;//使用道具价格
        AndroidSdk.UM_use(itemName2, count2, price2);

        /**
         * 额外奖励统计
         */
        String itemName3 = "血瓶"; //奖励道具名称
        int number3 = 5;//奖励道具数量
        double price3 = 99.0;//奖励道具价格
        int trigger3 = 1;//触发奖励的事件, 取值在 1~10 之间，“1”已经被预先定义为“系统奖励”， 2~10 需要在网站设置含义
        AndroidSdk.UM_bonus(itemName3, number3, price3, trigger3);
    }

    private void toast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public void onBackPressed() {
        /**
         * 应用，游戏等退出
         */
        AndroidSdk.onQuit();
    }

    void asyncToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
