package com.smd.soma_donation.search;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.smd.soma_donation.R;
import com.smd.soma_donation.rank.RankListAdpater;
import com.smd.soma_donation.retrofit.RetrofitService;
import com.smd.soma_donation.retrofit.format.DTORankContents;
import com.smd.soma_donation.retrofit.format.DTORankItem;

import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by YS on 2016-07-14.
 */
public class SearchActivity extends AppCompatActivity {
    public final String TAG = "SearchActivity";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private EditText searchTextView;
    private ImageButton searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO 리프레쉬
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        recyclerView = (RecyclerView) swipeRefreshLayout.findViewById(R.id.ranking_list);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager1);
        searchTextView = (EditText) findViewById(R.id.search_text);
        searchBtn = (ImageButton) findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO HTTP Connection
                // 검색 목록 가져옴
                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                loadSearch(pref.getString("access_token", ""));
            }
        });
    }


    void loadSearch(String access_token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<DTORankContents> call = service.getSearch(access_token, 30, searchTextView.getText().toString());
        call.enqueue(new Callback<DTORankContents>() {
            @Override
            public void onResponse(Response<DTORankContents> response) {
                if (response.isSuccess() && response.body() != null) {

                    DTORankContents rankContents = response.body();
                    LinkedList<DTORankItem> rankItems = new LinkedList<>();

                    for (int i = 0; i < rankContents.getDatasize(); i++) {
                        rankItems.addLast(rankContents.getData(i));
                    }

                    RankListAdpater rankListAdpater = new RankListAdpater(SearchActivity.this, rankItems, R.layout.fragment_main);

                    recyclerView.setAdapter(rankListAdpater);
                } else {
                    if (recyclerView.getAdapter() != null) {
                        RankListAdpater existADT = (RankListAdpater) recyclerView.getAdapter();
                        existADT.clear();
                        existADT.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "없음");
                RankListAdpater rankListAdpater = new RankListAdpater(SearchActivity.this, null, R.layout.fragment_main);

                recyclerView.setAdapter(rankListAdpater);
            }
        });
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
