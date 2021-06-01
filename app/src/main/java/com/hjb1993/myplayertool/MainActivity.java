package com.hjb1993.myplayertool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aliyun.player.alivcplayerexpand.constants.GlobalPlayerConfig;
import com.aliyun.player.aliyunplayerbase.bean.AliyunPlayAuth;
import com.aliyun.player.aliyunplayerbase.net.GetAuthInformation;
import com.aliyun.svideo.common.utils.PermissionUtils;
import com.aliyun.svideo.common.utils.ToastUtils;

public class MainActivity extends AppCompatActivity {


    String[] permission = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int PERMISSION_REQUEST_CODE = 1001;

    private ProgressBar mLoadingProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingProgressBar = findViewById(R.id.loading_progress);
        initGlobalConfig();


        boolean checkResult = PermissionUtils.checkPermissionsGroup(MainActivity.this, permission);
        if (!checkResult) {
            PermissionUtils.requestPermissions(MainActivity.this, permission, PERMISSION_REQUEST_CODE);
        }else{
            checkedIsNeedNormalData();
        }
    }

    /**
     * 检查是否需要默认源
     */
    private void checkedIsNeedNormalData() {
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        GetAuthInformation getAuthInformation = new GetAuthInformation();
        getAuthInformation.getVideoPlayAuthInfo(new GetAuthInformation.OnGetPlayAuthInfoListener() {

            @Override
            public void onGetPlayAuthError(String msg) {
                mLoadingProgressBar.setVisibility(View.GONE);
                ToastUtils.show(MainActivity.this, msg);
            }

            @Override
            public void onGetPlayAuthSuccess(AliyunPlayAuth.PlayAuthBean dataBean) {
                mLoadingProgressBar.setVisibility(View.GONE);
                if (dataBean != null) {
                    GlobalPlayerConfig.mVid = dataBean.getVideoMeta().getVideoId();
                    GlobalPlayerConfig.mPlayAuth = dataBean.getPlayAuth();
                    startPlay();
                }
            }
        });
    }

    private void startPlay() {
        // 视频播放
        Intent playerIntent = new Intent(MainActivity.this, MyPlayerActivity.class);
        startActivity(playerIntent);
    }

    /**
     * 初始化配置
     */
    private void initGlobalConfig(){
        GlobalPlayerConfig.mEnableHardDecodeType = true;
        /**
         * 设置是否自动播放
         */
        GlobalPlayerConfig.PlayConfig.mAutoSwitchOpen = true;
        GlobalPlayerConfig.PlayConfig.mEnablePlayBackground = false;
        GlobalPlayerConfig.PlayConfig.mEnableAccurateSeekModule = true;
        GlobalPlayerConfig.mCurrentPlayType = GlobalPlayerConfig.PLAYTYPE.AUTH;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 视频播放
                checkedIsNeedNormalData();
                // 如果所有的权限都授予了

            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                Toast.makeText(this, "请打开权限", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
