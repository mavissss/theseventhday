package com.bytedance.videoplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;


import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView videoView;
    private Button restartBtn;
    private Button startOrPauseBtn;
    private MediaController mediaController;
    private WindowManager wm = this.getWindowManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        final ImageView imageView = findViewById(R.id.imageView);
//        String url = "https://s3.pstatp.com/toutiao/static/img/logo.271e845.png";
//        Glide.with(this).load(url).into(imageView);
        videoView = findViewById(R.id.videoView);
        restartBtn = findViewById(R.id.restart_btn);
        startOrPauseBtn = findViewById(R.id.startOrPasuse_btn);
        restartBtn.setOnClickListener(this);
        startOrPauseBtn.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            initView();
        }

    }




    private boolean portrait;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        portrait = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT;
        tryFullScreen(!portrait);
    }

    private void tryFullScreen(boolean fullScreen) {
        if (MainActivity.this instanceof AppCompatActivity) {
            ActionBar supportActionBar = ((AppCompatActivity) MainActivity.this).getSupportActionBar();
            if (supportActionBar != null) {
                if (fullScreen) {
                    supportActionBar.hide();

                } else {
                    supportActionBar.show();
                }
            }
        }
        setFullScreen(fullScreen);
    }


    private void setFullScreen(boolean fullScreen) {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        if (fullScreen) {
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            startOrPauseBtn.setVisibility(View.GONE);
            restartBtn.setVisibility(View.GONE);




        } else {
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            startOrPauseBtn.setVisibility(View.VISIBLE);
            restartBtn.setVisibility(View.VISIBLE);
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initView();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:

        }
    }

    private void initView() {
        videoView = findViewById(R.id.videoView);
        mediaController = new MediaController(this);
        videoView.setVideoPath(getVideoPath(R.raw.bytedance));
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startOrPasuse_btn:
                if(videoView.isPlaying())
                {
                    videoView.pause();//暂停播放
                    startOrPauseBtn.setText("播放");

                }
                else
                {
                    videoView.start();//开始播放
                    startOrPauseBtn.setText("暂停");

                }


                break;

            case R.id.restart_btn:
                    videoView.start();
                    videoView.resume();//重新播放


                break;
        }
        videoView.requestFocus();

    }

    private String getVideoPath(int resId) {
        return "android.resource://" + this.getPackageName() + "/" + resId;
    }


}



