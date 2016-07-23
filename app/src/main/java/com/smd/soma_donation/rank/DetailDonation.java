package com.smd.soma_donation.rank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.smd.soma_donation.R;
import com.smd.soma_donation.facebook.FacebookShareActivity;
import com.smd.soma_donation.facebook.FacebookShareFlag;
import com.smd.soma_donation.retrofit.RetrofitService;
import com.smd.soma_donation.retrofit.format.DTOLikeResponse;
import com.smd.soma_donation.retrofit.format.DTORankDetail;
import com.smd.soma_donation.retrofit.format.DTORankItem;
import com.smd.soma_donation.search.SearchActivity;

import bolts.AppLinks;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by YS on 2016-07-14.
 */
public class DetailDonation extends AppCompatActivity {
    public final String TAG = "DetailDonation";

    private ImageView isLikeBtn;
    private Button editBtn;
    private int detail_id;
    private ImageView detailImg;
    private TextView detailTitle;
    private TextView detailIntro;
    private boolean isLike = false;
    private DTORankItem detailDonation;
    private int alarm_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_donation);

        Intent mIntent = getIntent();
        Bundle bundle = mIntent.getExtras();
        Log.d(TAG, "넘어온 ID: "+bundle.getInt("detail_id"));
        detail_id = bundle.getInt("detail_id");
        if (bundle.getInt("alarm_id") != 0)
            alarm_id = bundle.getInt("alarm_id");

        // Facebook app link
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, mIntent);
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        isLikeBtn = (ImageView) findViewById(R.id.isLike_btn);
        editBtn= (Button) findViewById(R.id.edit_btn);

        detailImg = (ImageView) findViewById(R.id.detail_img);
        detailTitle = (TextView) findViewById(R.id.detail_title);
        detailIntro = (TextView) findViewById(R.id.detail_intro);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("06C91AE386F622D62AE63C8B9A1815DD")
                .build();
        mAdView.loadAd(adRequest);

        // TODO HTTP Connection
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        loadDetailDonation(pref.getString("access_token", ""), detail_id);
    }

    public void shareToSNS(View v) {
        Intent intent = new Intent(DetailDonation.this, FacebookShareActivity.class);
        intent.putExtra("detail_item", detailDonation);
        intent.putExtra("content_type", FacebookShareFlag.RANKING_ITEM);

        startActivity(intent);

    }

    private void loadDetailDonation(String access_token, int content_id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<DTORankDetail> call;
        if (alarm_id != 0)
            call = service.getDetailDonation(access_token, content_id, alarm_id);
        else
            call = service.getDetailDonation(access_token, content_id);
        call.enqueue(new Callback<DTORankDetail>() {
            @Override
            public void onResponse(Response<DTORankDetail> response) {
                if (response.isSuccess() && response.body() != null) {

                    DTORankDetail rankDetail= response.body();
                    detailDonation = rankDetail.getData();

                    // TODO detailImg.setImage (with glide)
                    detailTitle.setText(detailDonation.getContentTitle());
                    detailIntro.setText(detailDonation.getDescription());
                    loadDetailImage(detailDonation.getContentImg());

                    if (detailDonation.isLike() == 1) isLike = true;
                    else isLike = false;

                    if (isLike) {
                        isLikeBtn.setImageResource(R.drawable.hand_black);
                    } else {
                        isLikeBtn.setImageResource(R.drawable.hand_empty);
                    }

                    SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                    if (detailDonation.getContentUserId() == pref.getLong("user_id", 0)) {
                        editBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "로드실패", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadDetailImage(String content_img) {
        String baseUrl = getResources().getString(R.string.baseURL);
        String imgUrl = baseUrl + "/ranking/" + detail_id + "/image?content_img=" + content_img;
        Glide.with(this).load(imgUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(detailImg) {
            @Override
            protected void setResource(Bitmap resource) {
                if (resource != null)
                    detailImg.setImageBitmap(resource);
            }
        });
    }

    public void setLike(View v) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String access_token = pref.getString("access_token", "");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<DTOLikeResponse> call = service.changeLike(access_token, detail_id);
        call.enqueue(new Callback<DTOLikeResponse>() {
            @Override
            public void onResponse(Response<DTOLikeResponse> response) {
                if (response.isSuccess() && response.body() != null) {

                    DTOLikeResponse postResponse = response.body();

                        isLike = postResponse.getData();
                        if (isLike) {
                            isLikeBtn.setImageResource(R.drawable.hand_black);
                        } else {
                            isLikeBtn.setImageResource(R.drawable.hand_empty);
                        }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "로드실패", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void editDetail(View v) {
        Intent intent = new Intent(DetailDonation.this, EditDonation.class);
        intent.putExtra("detailDonation", detailDonation);
        startActivity(intent);
    }

    public void onMenuSelected(View v) {

        if (v.getId() == R.id.search_btn) {
            Intent intent = new Intent(DetailDonation.this, SearchActivity.class);
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
