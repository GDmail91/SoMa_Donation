package com.smd.soma_donation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.CallbackManager;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.smd.soma_donation.donate.DonateActivity;
import com.smd.soma_donation.facebook.FacebookShareActivity;
import com.smd.soma_donation.facebook.FacebookShareFlag;
import com.smd.soma_donation.rank.RankListAdpater;
import com.smd.soma_donation.retrofit.RetrofitService;
import com.smd.soma_donation.retrofit.format.DTOMonthly;
import com.smd.soma_donation.retrofit.format.DTOMonthlyItem;
import com.smd.soma_donation.retrofit.format.DTORankContents;
import com.smd.soma_donation.retrofit.format.DTORankItem;
import com.smd.soma_donation.retrofit.format.DTOReview;
import com.smd.soma_donation.retrofit.format.DTOReviewItem;
import com.smd.soma_donation.review.ReviewListAdpater;

import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Youngdo on 2016-02-02.
 */
public class MainFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    public static Context mContext;

    RecyclerView rankContent;
    RecyclerView reviewContent;
    RelativeLayout fragmentMonthly;
    RelativeLayout fragmentMain;
    RelativeLayout fragmentReview;
    private int position;
    SwipeRefreshLayout swipeRefreshLayout;
    DTOMonthlyItem monthlyItem;
    ImageView monthlyImg;

    ShareButton shareButton;
    ShareDialog shareDialog;
    CallbackManager callbackManager;

//    ProgressDialog dialog;

    public static MainFragment newInstance(int position) {
        MainFragment f = new MainFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);

        mContext = this.getActivity();

        //loadPage(access_token);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final SharedPreferences pref = getActivity().getSharedPreferences("pref", getActivity().MODE_PRIVATE);
        switch (position) {
            //*** 이달의 후원 ***//
            case 0:
                fragmentMonthly = (RelativeLayout) inflater.inflate(R.layout.fragment_montly, null, true);

                // TODO 이달의 이미지 - 설명 바꿔야함
                loadMonthly(pref.getString("access_token", ""));


/*              if (monthlyItem == null) {
                    // 정상 로딩
                } else {
                    // 실패 이미지(null 이미지)
                    ImageView month_img = (ImageView) fragmentMonthly.findViewById(R.id.monthly_img);
                    month_img.setImageAlpha(monthlyImg.getImageAlpha());
                }*/
                Button donateShareBtn = (Button) fragmentMonthly.findViewById(R.id.donate_share_btn);
                Button donateBtn = (Button) fragmentMonthly.findViewById(R.id.donate_btn);

                donateShareBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), FacebookShareActivity.class);
                        intent.putExtra("monthly_item", monthlyItem);
                        intent.putExtra("content_type", FacebookShareFlag.MONTHLY_ITEM);

                        startActivity(intent);
                    }
                });
                // TODO 버튼 클릭시 후원하기로
                donateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, DonateActivity.class);
                        intent.putExtra("monthly_item", monthlyItem);
                        startActivity(intent);
                    }
                });

                return fragmentMonthly;


            //*** 랭킹 목록 리스트 ***//
            case 1:
                fragmentMain = (RelativeLayout) inflater.inflate(R.layout.fragment_main, container, false);

                swipeRefreshLayout = (SwipeRefreshLayout) fragmentMain.findViewById(R.id.swipeRefresh);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // TODO 리프레쉬
                        RankListAdpater existADT = (RankListAdpater) rankContent.getAdapter();
                        existADT.clear();
                        existADT.notifyDataSetChanged();
                        loadContents(pref.getString("access_token", ""));
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                rankContent = (RecyclerView) swipeRefreshLayout.findViewById(R.id.ranking_list);
                LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
                rankContent.setHasFixedSize(true);
                rankContent.setLayoutManager(layoutManager1);

                /* fragment 에서 admob이 안나옴
                AdView mAdView = (AdView) this.fragmentMain.findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice("06C91AE386F622D62AE63C8B9A1815DD")
                        .build();
                mAdView.loadAd(adRequest);*/

                // 랭킹 목록 받아옴
                loadContents(pref.getString("access_token", ""));

                fragmentMain.removeAllViews();
                fragmentMain.addView(swipeRefreshLayout);
                return fragmentMain;

            //*** 후원 후기 리스트 ***//
            case 2:
                fragmentReview = (RelativeLayout) inflater.inflate(R.layout.fragment_review, container, false);
                swipeRefreshLayout = (SwipeRefreshLayout) fragmentReview.findViewById(R.id.swipeRefresh);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // TODO 리프레쉬
                        ReviewListAdpater existADT = (ReviewListAdpater) reviewContent.getAdapter();
                        existADT.clear();
                        existADT.notifyDataSetChanged();
                        loadReviews(pref.getString("access_token", ""));
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                reviewContent = (RecyclerView) swipeRefreshLayout.findViewById(R.id.review_list);
                LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
                reviewContent.setHasFixedSize(true);
                reviewContent.setLayoutManager(layoutManager2);

                // 후기 목록 받아옴
                loadReviews(pref.getString("access_token", ""));

                fragmentReview.removeAllViews();
                fragmentReview.addView(swipeRefreshLayout);
                return fragmentReview;
            default:
                return null;

        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    void loadMonthly(String access_token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<DTOMonthly> call = service.getMonthly(access_token);
        call.enqueue(new Callback<DTOMonthly>() {
            @Override
            public void onResponse(Response<DTOMonthly> response) {
                if (response.isSuccess() && response.body() != null) {

                    DTOMonthly monthly = response.body();
                    monthlyItem = monthly.getData();

                    // TODO 이달의 이미지 - 설명 바꿔야함
                    monthlyImg = (ImageView) fragmentMonthly.findViewById(R.id.monthly_img);
                    loadDetailImage(monthlyItem.getContent_img());
                    //monthlyImg.setImageResource(R.drawable.test_child_center);

                    TextView monthlyTitle = (TextView) fragmentMonthly.findViewById(R.id.monthly_title);
                    TextView monthlyIntro = (TextView) fragmentMonthly.findViewById(R.id.monthly_intro);

                    monthlyTitle.setText(monthlyItem.getContent_title());
                    monthlyIntro.setText(monthlyItem.getDescription());
                }
            }

            @Override
            public void onFailure(Throwable t) {

                /*RankListAdpater rankListAdpater = new RankListAdpater(getActivity(), null, R.layout.fragment_main);

                content.setAdapter(rankListAdpater);*/
            }
        });
    }


    void loadContents(String access_token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<DTORankContents> call = service.getRankContents(access_token, 30);
        call.enqueue(new Callback<DTORankContents>() {
            @Override
            public void onResponse(Response<DTORankContents> response) {
                if (response.isSuccess() && response.body() != null) {

                    DTORankContents rankContents = response.body();
                    LinkedList<DTORankItem> rankItems = new LinkedList<>();

                    for (int i = 0; i < rankContents.getDatasize(); i++) {
                        rankItems.addLast(rankContents.getData(i));
                    }

                    RankListAdpater rankListAdpater = new RankListAdpater(getActivity(), rankItems, R.layout.fragment_main);

                    rankContent.setAdapter(rankListAdpater);
                } else {
                    if (rankContent.getAdapter() != null) {
                        RankListAdpater existADT = (RankListAdpater) rankContent.getAdapter();
                        existADT.clear();
                        existADT.notifyDataSetChanged();
                    } else {
                        RankListAdpater rankListAdpater = new RankListAdpater(getActivity(), null, R.layout.fragment_main);

                        rankContent.setAdapter(rankListAdpater);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                RankListAdpater rankListAdpater = new RankListAdpater(getActivity(), null, R.layout.fragment_main);

                rankContent.setAdapter(rankListAdpater);
            }
        });
    }


    void loadReviews(String access_token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<DTOReview> call = service.getReviews(access_token, 30);
        call.enqueue(new Callback<DTOReview>() {
            @Override
            public void onResponse(Response<DTOReview> response) {
                if (response.isSuccess() && response.body() != null) {

                    DTOReview reviewList = response.body();
                    Log.d("TEST", reviewList.getDatasize()+"");
                    LinkedList<DTOReviewItem> reviewItems = new LinkedList<>();
                    for (int i = 0; i < reviewList.getDatasize(); i++) {
                        reviewItems.addLast(reviewList.getData(i));
                    }

                    ReviewListAdpater reviewListAdpater = new ReviewListAdpater(getActivity(), reviewItems, R.layout.fragment_main);

                    reviewContent.setAdapter(reviewListAdpater);
                } else {
                    if (reviewContent.getAdapter() != null) {
                        ReviewListAdpater existADT = (ReviewListAdpater) reviewContent.getAdapter();
                        existADT.clear();
                        existADT.notifyDataSetChanged();
                    } else {
                        ReviewListAdpater reviewListAdpater = new ReviewListAdpater(getActivity(), null, R.layout.fragment_main);

                        reviewContent.setAdapter(reviewListAdpater);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ReviewListAdpater reviewListAdpater = new ReviewListAdpater(getActivity(), null, R.layout.fragment_main);

                reviewContent.setAdapter(reviewListAdpater);
            }
        });
    }

    private void loadDetailImage(String content_img) {
        String baseUrl = getResources().getString(R.string.baseURL);
        String imgUrl = baseUrl + "/ranking/" + monthlyItem.getContent_id() + "/image?content_img=" + content_img;
        Glide.with(this).load(imgUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(monthlyImg) {
            @Override
            protected void setResource(Bitmap resource) {
                if (resource != null) {
                    monthlyImg.setImageBitmap(resource);
                    //onReadyMonthly();
                }
            }
        });
    }
}
