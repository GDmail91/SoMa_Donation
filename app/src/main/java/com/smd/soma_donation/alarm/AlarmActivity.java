package com.smd.soma_donation.alarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.smd.soma_donation.R;
import com.smd.soma_donation.retrofit.RetrofitService;
import com.smd.soma_donation.retrofit.format.DTOAlarmItem;
import com.smd.soma_donation.retrofit.format.DTOAlarmList;
import com.smd.soma_donation.search.SearchActivity;

import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by YS on 2016-07-23.
 */
public class AlarmActivity extends AppCompatActivity {
    private static final String TAG = "AlarmActivity";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

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
        recyclerView = (RecyclerView) swipeRefreshLayout.findViewById(R.id.alarm_list);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager1);

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        loadContents(pref.getString("access_token", ""));
    }

    void loadContents(String access_token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<DTOAlarmList> call = service.getAlarm(access_token, 30);
        call.enqueue(new Callback<DTOAlarmList>() {
            @Override
            public void onResponse(Response<DTOAlarmList> response) {
                if (response.isSuccess() && response.body() != null) {
                    DTOAlarmList alarmList = response.body();
                    Log.d(TAG,alarmList.getDatasize()+"");
                    LinkedList<DTOAlarmItem> alarmItems = new LinkedList<>();

                    for (int i = 0; i < alarmList.getDatasize(); i++) {
                        alarmItems.addLast(alarmList.getData(i));
                    }

                    AlarmListAdpater alarmListAdpater = new AlarmListAdpater(AlarmActivity.this, alarmItems, R.layout.activity_alarm);

                    recyclerView.setAdapter(alarmListAdpater);
                } else {
                    if (recyclerView.getAdapter() != null) {
                        AlarmListAdpater existADT = (AlarmListAdpater) recyclerView.getAdapter();
                        existADT.clear();
                        existADT.notifyDataSetChanged();
                    } else {
                        AlarmListAdpater alarmListAdpater = new AlarmListAdpater(AlarmActivity.this, null, R.layout.activity_alarm);

                        recyclerView.setAdapter(alarmListAdpater);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                AlarmListAdpater rankListAdpater = new AlarmListAdpater(AlarmActivity.this, null, R.layout.activity_alarm);

                recyclerView.setAdapter(rankListAdpater);
            }
        });
    }

    public void onMenuSelected(View v) {

        if (v.getId() == R.id.search_btn) {
            Intent intent = new Intent(AlarmActivity.this, SearchActivity.class);
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
