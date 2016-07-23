package com.smd.soma_donation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smd.soma_donation.retrofit.RetrofitService;
import com.smd.soma_donation.retrofit.format.DTOPostResponse;
import com.smd.soma_donation.search.SearchActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by YS on 2016-07-14.
 */
public class ContactActivity extends AppCompatActivity {
    public final String TAG = "ContactActivity";

    public TextView emailBtn;
    public EditText userEmailText;
    public EditText contactText;
    public Button contactBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        emailBtn = (TextView) findViewById(R.id.email_txt);
        Linkify.addLinks(emailBtn, Linkify.EMAIL_ADDRESSES);

        contactText = (EditText) findViewById(R.id.contact_text);
        userEmailText = (EditText) findViewById(R.id.user_email_text);
        contactBtn = (Button) findViewById(R.id.contact_btn);
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contactText.getText().toString().equals("") || userEmailText.getText().toString().equals(""))
                    Toast.makeText(ContactActivity.this, "모든 정보를 기입해 주세요", Toast.LENGTH_LONG).show();
                else {
                    SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                    postQuestion(pref.getString("access_token", ""), contactText.getText().toString(), userEmailText.getText().toString());
                }
            }
        });
    }

    void postQuestion(String access_token, String question, String email) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<DTOPostResponse> call = service.postQuestion(access_token, question, email);
        call.enqueue(new Callback<DTOPostResponse>() {
            @Override
            public void onResponse(Response<DTOPostResponse> response) {
                if (response.isSuccess() && response.body() != null) {

                    DTOPostResponse postResponse = response.body();

                    Toast.makeText(getApplicationContext(), postResponse.getMsg(), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(ContactActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(getApplicationContext(), error.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "작성실패", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onMenuSelected(View v) {

        if (v.getId() == R.id.search_btn) {
            Intent intent = new Intent(ContactActivity.this, SearchActivity.class);
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
