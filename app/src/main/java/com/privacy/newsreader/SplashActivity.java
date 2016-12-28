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

/**
 * Created by song on 16/5/26.
 */
public class SplashActivity extends Activity implements View.OnClickListener {

    /**
     * sdk初始化相关接口的监听回调
     */
    SdkResultListener sdkResultListener = new SdkResultListener() {
        @Override
        public void onInitialized() {
            //sdk初始化成功接口
            asyncToast("sdk initialized");
        }

        @Override
        public void onReceiveServerExtra(String data) {
            // 初始化成功后收到服务器回传数据
            asyncToast("server data: " + data);
        }

        @Override
        public void onReceiveNotificationData(String data) {
            //初始化成功后收到通知数据
            asyncToast("noti: " + data);
        }
    };

    /**
     * 对facebook用户操作相关接口的监听回调
     */
    UserCenterListener userCenterListener = new UserCenterListener() {
        //登陆facebook账户成功与否回调
        @Override
        public void onReceiveLoginResult(boolean success) {
            asyncToast("login? " + success);
        }

        @Override
        public void onReceiveInviteResult(boolean success) {
            //邀请好友安装程序，游戏等回调
            asyncToast("invite? " + success);
        }

        @Override
        public void onReceiveChallengeResult(int count) {
            //收到挑战结果的回调，count代表成功挑战了几位好友
            asyncToast("challenge? " + " count: " + count);
        }

        @Override
        public void onReceiveLikeResult(boolean success) {
            //点赞回调，是否点赞成功
            asyncToast("like? " + success);
        }

    };

    /**
     * 对广告操作的相关接口的监听回调
     */
    AdListener adListener = new AdListener() {
        @Override
        public void onReceiveReward(boolean success, int id) {
            //success:是否成功显示视频广告 ,id:广告id
            Log.e("DEMO", "receive reward " + id);
            asyncToast("on receive reward? " + id + ", success = " + success);
        }

        @Override
        public void onFullAdClosed() {
            //全屏广告被关闭
        }

        @Override
        public void onFullAdClicked() {
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
    };

    /**
     * 使用应用内支付接口的回调
     */
    PaymentResultListener paymentResultListener = new PaymentResultListener() {
        @Override
        public void onPaymentSuccess(int billId) {
            //支付成功 billID:计费点
            asyncToast("payment success: " + billId);
        }

        @Override
        public void onPaymentFail(int billId) {
            //支付失败
            asyncToast("payment fail: " + billId);
        }

        @Override
        public void onPaymentCanceled(int bill) {
            //支付取消
            asyncToast("payment cancel: " + bill);
        }

        @Override
        public void onPaymentSystemValid() {
            //手机，平板等支持支付功能,支付环境有效的回调
            Log.e("DEMO", "pay system is valid");
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
                .setRewardAdListener(adListener)
                .setPaymentResultListener(paymentResultListener);

        AndroidSdk.onCreate(this, builder);

        setContentView(R.layout.main);
    }

    void asyncToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    boolean nativeShowing;
    boolean nativeBannerShowing;

    int bannerIdx;
    int[] bannerPos = {
            AndroidSdk.POS_CENTER,
            AndroidSdk.POS_CENTER_BOTTOM,
            AndroidSdk.POS_CENTER_TOP,
            AndroidSdk.POS_LEFT_BOTTOM,
            AndroidSdk.POS_LEFT_TOP,
            AndroidSdk.POS_RIGHT_BOTTOM,
            AndroidSdk.POS_RIGHT_TOP
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                //全屏广告，与后台配置tag AndroidSdk.FULL_TAG_START,游戏开始时弹出
                AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_START);
                break;

            case R.id.pause:
                //全屏广告，与后台配置tag AndroidSdk.FULL_TAG_START,游戏暂停时弹出
                AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                break;

            case R.id.exit:
                //全屏广告，与后台配置tag AndroidSdk.FULL_TAG_START,游戏退出时弹出
                AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_EXIT);
                break;

            case R.id.more:
                //跳到推广的游戏列表界面
                AndroidSdk.moreGame();
                break;

            case R.id.banner:
                //显示banner广告
                AndroidSdk.showBanner("default", bannerPos[bannerIdx]);
                bannerIdx = (++bannerIdx) % bannerPos.length;
                break;

            case R.id.bill:
                int billId = 1; //计费点
                AndroidSdk.pay(billId);//支付接口，对计费点进行支付
                break;

            case R.id.close_banner:
                //关闭banner广告
                AndroidSdk.closeBanner();
                break;

            case R.id.custom:
                //全屏广告，你可以与后台协商，自定义时机弹出广告
                AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_CUSTOM);
                break;

            case R.id.pass_level:
                //全屏广告，与后台配置tag AndroidSdk.FULL_TAG_START,游戏过关时弹出
                AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PASS_LEVEL);
                break;

            case R.id.free:
                int rewardId = 1; //客户端配置的视频广告调用时机
                if (AndroidSdk.hasRewardAd()) { //检查后台是否有配置视频广告
                    AndroidSdk.showRewardAd(rewardId);
                }
               /* if (AndroidSdk.hasRewardAd("default")) {
                    AndroidSdk.showRewardAd("default", 1);
                } else {
                    Toast.makeText(this, "no video ad", Toast.LENGTH_SHORT).show();
                }*/
                break;

            case R.id.native_1:
                if (nativeShowing) {
                    AndroidSdk.hideNativeAdScrollView("unlock_pre");
                } else {
                    AndroidSdk.showNativeAdScrollView("unlock_pre", AndroidSdk.HIDE_BEHAVIOR_NO_HIDE, 50);
                }
                nativeShowing = !nativeShowing;
                break;

            case R.id.share:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //facebook分享，注意此方法会阻塞主线程，需启线程调用
                        AndroidSdk.share();
                    }
                }).start();
                break;

            case R.id.islogin:
                //是否登陆facebook账户
                toast("is login: " + AndroidSdk.isLogin());
                break;

            case R.id.login:
                //登陆facebook账户
                AndroidSdk.login();
                break;

            case R.id.logout:
                //退出facebook账户
                AndroidSdk.logout();
                break;

            case R.id.invite:
                //facebook邀请好友安装应用，游戏等
                AndroidSdk.invite();
                break;

            case R.id.challenge:
                //向你的facebook朋友发出挑战
                AndroidSdk.challenge("haha title", "heihei message");
                break;

            case R.id.friends:
                //获取faceook朋友信息列表
                toast(AndroidSdk.friends());
                break;

            case R.id.me: {
                try {
                    //获取我的faceook个人信息
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
                //facebook点赞
                AndroidSdk.like();
                break;
//
//            case R.id.submit_score:
//                AndroidSdk.submitScore("endless", 232, "");
//                break;
//
//            case R.id.load_friend:
//                AndroidSdk.loadLeaderBoard("endless", 1, 20, "");
//                break;
//
//            case R.id.load_global:
//                AndroidSdk.loadGlobalLeaderBoard("endless", 1, 20);
//                break;
//
//            case R.id.show_sales:
//                AndroidSdk.showSales(2);
//                break;

            case R.id.show_native_banner:
                if (nativeBannerShowing) {
                    AndroidSdk.hideNativeBanner("unlock_pre");
                } else {
                    AndroidSdk.showNativeBanner("unlock_pre", 50, 50);
                }
                nativeBannerShowing = !nativeBannerShowing;
                break;
            default:
                break;
        }
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
        AndroidSdk.onQuit();
    }
}
