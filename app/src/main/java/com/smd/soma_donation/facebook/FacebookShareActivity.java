package com.smd.soma_donation.facebook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.smd.soma_donation.R;
import com.smd.soma_donation.retrofit.format.DTOMonthlyItem;
import com.smd.soma_donation.retrofit.format.DTORankItem;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by YS on 2016-07-22.
 */
public class FacebookShareActivity extends AppCompatActivity {
    private LoginManager loginManager;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private DTOMonthlyItem monthlyItem;
    private DTORankItem rankingItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent shareIntent = getIntent();
        final int contentType = shareIntent.getIntExtra("content_type", 0);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        List<String> permissionNeeds = Arrays.asList("publish_actions");

        //this loginManager helps you eliminate adding a LoginButton to your UI
        loginManager = LoginManager.getInstance();

        loginManager.logInWithPublishPermissions(this, permissionNeeds);

        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {

                switch (contentType) {
                    case FacebookShareFlag.MONTHLY_ITEM:
                        shareMonthlyItemToFacebook(shareIntent.getSerializableExtra("monthly_item"));
                        break;
                    case FacebookShareFlag.RANKING_ITEM:
                        shareRankingItemToFacebook(shareIntent.getSerializableExtra("detail_item"));
                        break;
                }
            }

            @Override
            public void onCancel()
            {
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception)
            {
                System.out.println("onError");
            }
        });
    }

    private void shareMonthlyItemToFacebook(Serializable monthlyItem) {

        this.monthlyItem = (DTOMonthlyItem) monthlyItem;

        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://bufy.mooo.com:3000/monthly/"+this.monthlyItem.getContent_id()))
                .setContentTitle(this.monthlyItem.getContent_title())
                .setContentDescription(this.monthlyItem.getDescription())
                .setImageUrl(Uri.parse(getString(R.string.baseURL)+"/ranking/" + this.monthlyItem.getContent_id() + "/image?content_img=" + this.monthlyItem.getContent_img()))
                .build();

        ShareApi.share(linkContent, null);

    }

    private void shareRankingItemToFacebook(Serializable rankingItem) {

        this.rankingItem = (DTORankItem) rankingItem;

        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://bufy.mooo.com:3000/ranking/"+this.rankingItem.getContentId()))
                .setContentTitle(this.rankingItem.getContentTitle())
                .setContentDescription(this.rankingItem.getDescription())
                .setImageUrl(Uri.parse(getString(R.string.baseURL)+"/ranking/" + this.rankingItem.getContentId() + "/image?content_img=" + this.rankingItem.getContentImg()))
                .build();

        ShareApi.share(linkContent, null);

    }

    @Override
    protected void onActivityResult(final int requestCode,
                                    final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Toast.makeText(this, "공유가 완료 되었습니다", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "공유가 취소/실패 되었습니다", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

}
