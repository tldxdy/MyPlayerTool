package com.hjb1993.myplayertool;


import android.util.Log;
import android.view.View;

import com.aliyun.player.alivcplayerexpand.constants.GlobalPlayerConfig;
import com.aliyun.player.alivcplayerexpand.util.VidStsUtil;
import com.aliyun.player.aliyunplayerbase.bean.AliyunPlayAuth;
import com.aliyun.player.aliyunplayerbase.net.GetAuthInformation;
import com.aliyun.player.bean.ErrorCode;
import com.aliyun.player.bean.ErrorInfo;
import com.aliyun.player.source.VidAuth;
import com.aliyun.svideo.common.utils.ToastUtils;
import com.aliyun.vodplayerview.activity.AliyunPlayerSkinActivity;

public class MyPlayerActivity extends AliyunPlayerSkinActivity {

    /**
     * 视频准备完成
     */
    @Override
    public void onPrepared() {

        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.setAutoPlay(true);
            mAliyunVodPlayerView.onResume();
            mAliyunVodPlayerView.start();
        }

    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onTimExpiredError() {
        //网络请求获取最新的鉴权
        Log.e("==============","=========================================2");
        checkedIsNeedNormalData();
    }

    @Override
    public void onErrorListenter(ErrorInfo errorInfo) {
        //鉴权过期
        if (errorInfo.getCode().getValue() == ErrorCode.ERROR_SERVER_POP_UNKNOWN.getValue()) {
            Log.e("==============","=========================================1");
            checkedIsNeedNormalData();
        }
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
                    VidAuth vidAuth = getVidAuth(GlobalPlayerConfig.mVid);
                    mCurrentVideoId = GlobalPlayerConfig.mVid;
                    mAliyunVodPlayerView.setAuthInfo(vidAuth);
                }
            }
        });
    }
}
