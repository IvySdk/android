# android sdk 文档
## 1.在Activity的onCreate方法中初始化Android SDK,构造AndroidSDK builder对象
   ```java
    AndroidSdk.Builder builder = new AndroidSdk.Builder();
    AndroidSdk.onCreate(this, builder);
   ```
## 2.提供对sdk初始化相关的监听回调：
  * sdk初始化成功事件
  * 初始化成功后收到服务器回传数据
  * 初始化成功后收到通知数据
  ```java
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
        })
   ```
   ## 3.提供对facebook用户相关事件的监听回调:
   * 登录是否成功
   * 邀请
   * 挑战
   * 喜欢
   ```java
   builder.setUserCenterListener(new UserCenterListener() {
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

        });
    ```
## 4.用户对广告操作的相关事件的监听回调
     ```java
     builder.setRewardAdListener(new AdListener() {
            @Override
            public void onReceiveReward(boolean success, int id) {
                Log.e("DEMO", "receive reward " + id);
                asyncToast("on receive reward? " + id + ", success = " + success);
            }

            @Override
            public void onFullAdClosed() {

            }

            @Override
            public void onFullAdClicked() {

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
   
