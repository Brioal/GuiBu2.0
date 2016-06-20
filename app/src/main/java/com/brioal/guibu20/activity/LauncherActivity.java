package com.brioal.guibu20.activity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.brioal.brioallib.base.BaseActivity;
import com.brioal.guibu20.MainActivity;
import com.brioal.guibu20.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LauncherActivity extends BaseActivity {

    static {
        TAG = "LauncherActInfo";
    }

    @Bind(R.id.act_launcher_logo)
    ImageView mLogo;
    @Bind(R.id.act_launcher_bg)
    ImageView mBg;

    @Override
    public void initData() {
        setTheme(R.style.AppTheme_NoActionBar);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.initData();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.act_launcher);
        ButterKnife.bind(this);
        Glide.with(getApplicationContext()).load(R.mipmap.ic_launcher_kuibu).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                mBg.setImageDrawable(glideDrawable);
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.alpha_in);
                animation.setDuration(2000);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        MainActivity.enterMainAct(LauncherActivity.this);
                        finish();
                        overridePendingTransition(R.anim.trans_in_down, R.anim.trans_out_down);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mLogo.startAnimation(animation);
            }
        });

    }

    @Override
    public void initBar() {
        super.initBar();
    }

    @Override
    public void initTheme() {
        super.initTheme();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_launcher);
        ButterKnife.bind(this);
    }
}
