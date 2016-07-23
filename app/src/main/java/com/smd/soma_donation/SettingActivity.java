package com.smd.soma_donation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.smd.soma_donation.search.SearchActivity;

/**
 * Created by YS on 2016-07-14.
 */
public class SettingActivity extends AppCompatActivity {
    public final String TAG = "SettingActivity";

    TextView usernameTxt;
    ToggleButton pushOnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        usernameTxt = (TextView) findViewById(R.id.username_txt);
        pushOnOff = (ToggleButton) findViewById(R.id.push_onoff);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        pushOnOff.setChecked(pref.getBoolean("push", true));
        usernameTxt.setText(pref.getString("name", ""));
        // TODO HTTP Connection

        pushOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("push", isChecked);
                editor.apply();
            }
        });
    }

    public void onMenuSelected(View v) {

        if (v.getId() == R.id.search_btn) {
            Intent intent = new Intent(SettingActivity.this, SearchActivity.class);
            startActivity(intent);
        }
    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
