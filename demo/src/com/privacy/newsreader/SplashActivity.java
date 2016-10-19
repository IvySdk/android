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
        builder.setSdkResultListener(new SdkResultListener() {
            @Override
            public void onInitialized() {
                asyncToast("sdk initialized");
            }

            @Override
            public void onReceiveServerExtra(String data) {
                asyncToast("server data: " + data);
            }

            @Override
            public void onReceiveNotificationData(String data) {
                asyncToast("noti: " + data);
            }
        }).setUserCenterListener(new UserCenterListener() {
            @Override
            public void onReceiveLoginResult(boolean success) {
                asyncToast("login? " + success);
            }

            @Override
            public void onReceiveInviteResult(boolean success) {
                asyncToast("invite? " + success);
            }

            @Override
            public void onReceiveChallengeResult(int count) {
                asyncToast("challenge? " + " count: " + count);
            }

            @Override
            public void onReceiveLikeResult(boolean success) {
                asyncToast("like? " + success);
            }

        }).setRewardAdListener(new RewardAdListener() {
            @Override
            public void onReceiveReward(boolean success, int id) {
                Log.e("DEMO", "receive reward " + id);
                asyncToast("on receive reward? " + id + ", success = " + success);
            }
        }).setPaymentResultListener(new PaymentResultListener() {
            @Override
            public void onPaymentSuccess(int billId) {
                asyncToast("payment success: " + billId);
            }

            @Override
            public void onPaymentFail(int billId) {
                asyncToast("payment fail: " + billId);
            }

            @Override
            public void onPaymentCanceled(int bill) {
                asyncToast("payment cancel: " + bill);
            }

            @Override
            public void onPaymentSystemValid() {
                Log.e("DEMO", "pay system is valid");
            }
        });

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
                AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_START);
                break;

            case R.id.pause:
                AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                break;

            case R.id.exit:
                AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_EXIT);
                break;

            case R.id.more:
                AndroidSdk.moreGame();
                break;

            case R.id.banner:
                AndroidSdk.showBanner("default", bannerPos[bannerIdx]);
                bannerIdx = (++bannerIdx) % bannerPos.length;
                break;

            case R.id.bill:
                AndroidSdk.pay(1);
                break;

            case R.id.close_banner:
                AndroidSdk.closeBanner();
                break;

            case R.id.custom:
                AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_CUSTOM);
                break;

            case R.id.pass_level:
                AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PASS_LEVEL);
                break;

            case R.id.free:
                if (AndroidSdk.hasRewardAd("default")) {
                    AndroidSdk.showRewardAd("default", 1);
                } else {
                    Toast.makeText(this, "no video ad", Toast.LENGTH_SHORT).show();
                }
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
                        AndroidSdk.share();
                    }
                }).start();
                break;

            case R.id.islogin:
                toast("is login: " + AndroidSdk.isLogin());
                break;

            case R.id.login:
                AndroidSdk.login();
                break;

            case R.id.logout:
                AndroidSdk.logout();
                break;

            case R.id.invite:
                AndroidSdk.invite();
                break;

            case R.id.challenge:
                AndroidSdk.challenge("haha title", "heihei message");
                break;

            case R.id.friends:
                toast(AndroidSdk.friends());
                break;

            case R.id.me: {
                try {
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
