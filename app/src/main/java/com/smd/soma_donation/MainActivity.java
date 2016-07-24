package com.smd.soma_donation;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.messaging.FirebaseMessaging;
import com.smd.soma_donation.alarm.AlarmActivity;
import com.smd.soma_donation.facebook.FacebookLogin;
import com.smd.soma_donation.post.MyPostActivity;
import com.smd.soma_donation.rank.EditDonation;
import com.smd.soma_donation.search.SearchActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BackPressCloseHandler backPressCloseHandler;
    private CallbackManager callbackManager;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Facebook login check
        FacebookSdk.sdkInitialize(getApplicationContext());
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        if (pref.getString("access_token", "").equals("")) {
            Intent intent = new Intent(MainActivity.this, FacebookLogin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        try {
            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            //toolbar.setNavigationIcon(R.drawable.list_icon);
            int currentVersion = Build.VERSION.SDK_INT;
            if (currentVersion >= Build.VERSION_CODES.LOLLIPOP)
                toolbar.setElevation(0);

            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new MainFragment(), "이달의 후원");
            adapter.addFragment(new MainFragment(), "랭킹");
            adapter.addFragment(new MainFragment(), "후기");
            viewPager.setAdapter(adapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setTabTextColors(Color.GRAY, Color.BLACK);
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.main));

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    public void navClick(View v) {
        int id = v.getId();

        if (id == R.id.setting_btn) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.logout_btn) {
            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
            // firebase unsubscribe
            FirebaseMessaging.getInstance().unsubscribeFromTopic(""+pref.getLong("user_id", 0));

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("access_token", "");
            editor.apply();

            FacebookSdk.sdkInitialize(getApplicationContext());
            callbackManager = CallbackManager.Factory.create();

            LoginManager.getInstance().logOut();

            Intent intent=new Intent(MainActivity.this, FacebookLogin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.my_post_btn) {
            Intent intent = new Intent(MainActivity.this, MyPostActivity.class);
            startActivity(intent);
        } else if (id == R.id.contact_btn) {
            Intent intent = new Intent(MainActivity.this, ContactActivity.class);
            startActivity(intent);
        } else if (id == R.id.alarm_btn) {
            Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
            startActivity(intent);
        }
    }

    public void onMenuSelected(View v) {

        if (v.getId() == R.id.search_btn) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.add_btn) {
            Intent intent = new Intent(MainActivity.this, EditDonation.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

        }
        backPressCloseHandler.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.d("TEST", "id는: "+id);

/*
        if (id == R.id.cart) {//not work
            Intent intent = new Intent(this, ClipList.class);
            startActivity(intent);

        } else if (id == R.id.require) {
            Intent intent = new Intent(this, ApplyList.class);
            startActivity(intent);

        } else if (id == R.id.gonggu) {
            Intent intent = new Intent(this, ContestList.class);
            startActivity(intent);

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
