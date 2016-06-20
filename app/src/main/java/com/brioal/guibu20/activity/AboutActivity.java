package com.brioal.guibu20.activity;

import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.brioal.brioallib.swipeback.app.SwipeBackActivity;
import com.brioal.brioallib.util.StatusBarUtils;
import com.brioal.brioallib.util.ToastUtils;
import com.brioal.guibu20.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends SwipeBackActivity implements View.OnClickListener {

    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.about_btn_github)
    TextView mBtnGithub;
    @Bind(R.id.about_btnBlog)
    TextView mBtnBoke;
    @Bind(R.id.about_btn_weibo)
    TextView mBtnWeibo;
    @Bind(R.id.about_btn_qq)
    TextView mBtnQq;
    @Bind(R.id.about_btn_email)
    TextView mBtnEmail;


    @Override
    protected void onResume() {
        super.onResume();
        initTheme();
    }

    @Override
    public void initBar() {
        mToolBar.setTitle("关于我们");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void initTheme() {
        mToolBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        StatusBarUtils.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initData() {

    }

    @Override
    public void loadDataNet() {
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.act_about);
        ButterKnife.bind(this);
    }

    @Override
    public void setView() {
        mBtnGithub.setOnClickListener(this);
        mBtnBoke.setOnClickListener(this);
        mBtnEmail.setOnClickListener(this);
        mBtnQq.setOnClickListener(this);
        mBtnEmail.setOnClickListener(this);
        mBtnWeibo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_btn_github:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Brioal")));
                break;
            case R.id.about_btnBlog:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.csdn.net/qq_26971803")));
                break;
            case R.id.about_btn_weibo:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://weibo.com/3880575750/profile")));
                break;
            case R.id.about_btn_qq:
                ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                c.setText("821329382");//设置Clipboard 的内容
                ToastUtils.showToast(mContext, "成功复制QQ号到剪贴板");
                break;
            case R.id.about_btn_email:
                ClipboardManager d = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                d.setText("brioal@foxmail.com");//设置Clipboard 的内容
                ToastUtils.showToast(mContext, "成功复制邮箱到剪贴板");
                break;
        }
    }
}
