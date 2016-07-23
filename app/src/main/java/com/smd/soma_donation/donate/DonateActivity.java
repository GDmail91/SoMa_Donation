package com.smd.soma_donation.donate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.smd.soma_donation.R;
import com.smd.soma_donation.retrofit.format.DTOMonthlyItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YS on 2016-07-21.
 */
public class DonateActivity extends AppCompatActivity {
    private WebView mainWebView;
    private final String APP_SCHEME = "iamporttest://";
    private DTOMonthlyItem monthlyItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        mainWebView = (WebView) findViewById(R.id.mainWebView);
        mainWebView.setWebViewClient(new InicisWebViewClient(this));
        WebSettings settings = mainWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        Intent intent = getIntent();
        monthlyItem = (DTOMonthlyItem) intent.getExtras().getSerializable("monthly_item");
        Uri intentData = intent.getData();

        if ( intentData == null ) {
            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
            String access_token = pref.getString("access_token", "");

            // add header
            Map<String, String> extraHeaders = new HashMap<String, String>();
            extraHeaders.put("access-token", access_token);

            // load WebView
            mainWebView.loadUrl(getString(R.string.baseURL) + "/support/"+monthlyItem.getContent_id(), extraHeaders);
        } else {
            //isp 인증 후 복귀했을 때 결제 후속조치
            String url = intentData.toString();
            if ( url.startsWith(APP_SCHEME) ) {
                String redirectURL = url.substring(APP_SCHEME.length()+3);
                mainWebView.loadUrl(redirectURL);
            }
        }
    }
}
