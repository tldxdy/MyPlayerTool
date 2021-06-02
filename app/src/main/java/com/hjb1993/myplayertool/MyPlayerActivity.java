package com.hjb1993.myplayertool;

import com.aliyun.player.alivcplayerexpand.constants.GlobalPlayerConfig;
import com.aliyun.player.aliyunplayerbase.bean.AliyunPlayAuth;
import com.aliyun.player.aliyunplayerbase.net.GetAuthInformation;
import com.aliyun.vodplayerview.activity.AliyunPlayerSkinActivity;

public class MyPlayerActivity extends AliyunPlayerSkinActivity {

    /**
     * 视频准备完成
     */
    @Override
    public void onPrepared() {

        if (mAliyunVodPlayerView != null) {
            if(GlobalPlayerConfig.PlayConfig.mAutoSwitchOpen){
                mAliyunVodPlayerView.setAutoPlay(true);
                mAliyunVodPlayerView.start();
            }else {
                mAliyunVodPlayerView.setAutoPlay(false);
                mAliyunVodPlayerView.pause();
            }

        }

    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onTimExpiredError() {
        //网络请求获取最新的鉴权
        checkedIsNeedNormalData();
    }

    @Override
    public void onErrorListenter() {
        //鉴权过期
        checkedIsNeedNormalData();
    }


    /**
     * 获取新的鉴权
     */
    private void checkedIsNeedNormalData() {
        GetAuthInformation getAuthInformation = new GetAuthInformation();
        getAuthInformation.getVideoPlayAuthInfo(new GetAuthInformation.OnGetPlayAuthInfoListener() {

            @Override
            public void onGetPlayAuthError(String msg) {

            }

            @Override
            public void onGetPlayAuthSuccess(AliyunPlayAuth.PlayAuthBean dataBean) {
                if (dataBean != null) {
                    GlobalPlayerConfig.mVid = dataBean.getVideoMeta().getVideoId();
                    GlobalPlayerConfig.mPlayAuth = dataBean.getPlayAuth();
                    setAuthInfo();
                }
            }
        });
    }
}
