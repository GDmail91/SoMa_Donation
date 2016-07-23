package com.smd.soma_donation.review;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.smd.soma_donation.R;
import com.smd.soma_donation.retrofit.RetrofitService;
import com.smd.soma_donation.retrofit.format.DTOReviewDetail;
import com.smd.soma_donation.retrofit.format.DTOReviewItem;
import com.smd.soma_donation.search.SearchActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by YS on 2016-07-14.
 */
public class ReviewDonation extends AppCompatActivity {
    public final String TAG = "ReviewDonation";

    private int review_id;
    private ImageView reviewBanner;
    private TextView reviewTitle;
    private TextView reviewContent;
    private DTOReviewItem reviewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Intent mIntent = getIntent();
        Bundle bundle = mIntent.getExtras();
        Log.d(TAG, "넘어온 ID: "+bundle.getInt("review_id"));
        review_id = bundle.getInt("review_id");

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        reviewBanner = (ImageView) findViewById(R.id.banner);
        reviewTitle = (TextView) findViewById(R.id.review_title);
        reviewContent = (TextView) findViewById(R.id.review_detail);

        // TODO HTTP Connection
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        loadDetailReview(pref.getString("access_token", ""), review_id);
    }


    private void loadDetailReview(String access_token, int content_id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<DTOReviewDetail> call = service.getDetailReview(access_token, content_id);
        call.enqueue(new Callback<DTOReviewDetail>() {
            @Override
            public void onResponse(Response<DTOReviewDetail> response) {
                if (response.isSuccess() && response.body() != null) {

                    DTOReviewDetail rankDetail= response.body();
                    reviewItem = rankDetail.getData();

                    // TODO detailImg.setImage (with glide)
                    reviewTitle.setText(reviewItem.getReview_title());
                    reviewContent.setText(reviewItem.getReview_content());
                    loadReviewImage(reviewItem.getBanner_img());

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "로드실패", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadReviewImage(String banner_img) {
        String baseUrl = getResources().getString(R.string.baseURL);
        String imgUrl = baseUrl + "/reviews/" + review_id + "/image?banner_img=" + banner_img;
        Glide.with(this).load(imgUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(reviewBanner) {
            @Override
            protected void setResource(Bitmap resource) {
                if (resource != null)
                    reviewBanner.setImageBitmap(resource);
            }
        });
    }

    public void onMenuSelected(View v) {

        if (v.getId() == R.id.search_btn) {
            Intent intent = new Intent(ReviewDonation.this, SearchActivity.class);
            startActivity(intent);
        }
    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.search_btn:
                Intent intent = new Intent(ReviewDonation.this, SearchActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
