package com.smd.soma_donation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.smd.soma_donation.rank.DetailDonation;

import java.util.List;

/**
 * Created by YS on 2016-07-23.
 */
public class LinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAppLink();
    }

    protected void onAppLink() {
        // Facebook app link
        Intent linkIntent = getIntent();
        Uri data = linkIntent.getData();
        if (data != null) {
            Log.d("appLink", "App Link Target URL: " + data.toString());
            //somadonation://bufy.mooo.com:3000//monthly/15
            List<String> path = data.getPathSegments();
            Intent intent;
            switch (path.get(0)) {
                case LinkCategory.RANKING:
                    Log.d("appLink", path.toString());
                    intent = new Intent(LinkActivity.this, DetailDonation.class);
                    intent.putExtra("detail_id", path.get(1));
                    startActivity(intent);
                    break;
                case LinkCategory.MONTHLY:
                    intent = new Intent(LinkActivity.this, MainActivity.class);
                    //intent.putExtra("detail_id", path.get(1));
                    startActivity(intent);
                    break;
            }
            //Intent intent = new Intent(MainActivity.this, FacebookLogin.class);
            //startActivity(intent);
        }
    }
}
