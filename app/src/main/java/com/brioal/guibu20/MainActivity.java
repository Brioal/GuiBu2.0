package com.brioal.guibu20;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.brioal.brioallib.util.StatusBarUtils;
import com.brioal.brioallib.util.ToastUtils;
import com.brioal.guibu20.activity.AboutActivity;
import com.brioal.guibu20.fragment.RunningFragment;
import com.brioal.guibu20.fragment.RunningRecordFragment;
import com.brioal.guibu20.fragment.StepFragment;
import com.brioal.guibu20.fragment.StepRecordFragment;
import com.brioal.guibu20.service.StepService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getSupportFragmentManager();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.main_container, RunningFragment.newInstance()).commit();
        StatusBarUtils.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    private long mTimeLastClick = 0;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (System.currentTimeMillis() - mTimeLastClick < 2000) {

            super.onBackPressed();
        } else {
            ToastUtils.showToast(MainActivity.this,"再按一次退出");
            mTimeLastClick = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_step: //计步器
                changeFragment(StepFragment.newInstance());
                getSupportActionBar().setTitle("计步器");
                break;
            case R.id.nav_running: //跑步界面
                changeFragment(RunningFragment.newInstance());
                getSupportActionBar().setTitle("跑步");
                break;
            case R.id.nav_step_record://步数记录界面
                changeFragment(StepRecordFragment.newInstance());
                getSupportActionBar().setTitle("步数记录");
                break;
            case R.id.nav_running_record: //跑步记录界面
                changeFragment(RunningRecordFragment.newInstance());
                getSupportActionBar().setTitle("跑步记录");
                break;
            case R.id.nav_about: //关于界面
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeFragment(Fragment fragment) {
        if (fragment.isAdded()) {
            manager.beginTransaction().replace(R.id.main_container, fragment).commit();
        } else {
            manager.beginTransaction().add(R.id.main_container, fragment).commit();
        }
    }

    public static void enterMainAct(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        Intent serviceIntent = new Intent(activity, StepService.class);
        activity.startService(serviceIntent);
    }
}
