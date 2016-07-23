package com.smd.soma_donation.post;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.smd.soma_donation.R;
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
public class MyPostActivity extends AppCompatActivity {
    public final String TAG = "MyPostActivity";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

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
/*
        // TODO 어떤 어댑터 생성
        ArrayList<DTORankItem> rankItems = new ArrayList<>();
        rankItems.add(new DTORankItem(1,1,"타이틀",10,30));
        rankItems.add(new DTORankItem(2,2,"타이틀",10,30));
        rankItems.add(new DTORankItem(3,3,"타이틀",10,30));
        rankItems.add(new DTORankItem(4,4,"타이틀",10,30));
        for (int i=0; i<rankItems.size(); i++) {
            Log.d("TEST", rankItems.get(i).getContentTitle());
        }

        RankListAdpater rankListAdpater = new RankListAdpater(this, rankItems, R.layout.fragment_main);
        recyclerView.setAdapter(rankListAdpater);*/
        // TODO HTTP Connection

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        getMyPost(pref.getString("access_token", ""), pref.getLong("user_id", 0));
    }

    // Get user post
    public void getMyPost(String token, long user_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<DTORankContents> call = service.getUserPost(token, user_id);
        call.enqueue(new Callback<DTORankContents>() {
            @Override
            public void onResponse(Response<DTORankContents> response) {
                if (response.isSuccess() && response.body() != null) {

                    DTORankContents rankContents = response.body();
                    LinkedList<DTORankItem> rankItems = new LinkedList<>();

                    for (int i = 0; i < rankContents.getDatasize(); i++) {
                        rankItems.addLast(rankContents.getData(i));
                    }

                    PostListAdpater postListAdpater = new PostListAdpater(MyPostActivity.this, rankItems, R.layout.fragment_main);

                    recyclerView.setAdapter(postListAdpater);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "로드실패", Toast.LENGTH_LONG).show();
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
