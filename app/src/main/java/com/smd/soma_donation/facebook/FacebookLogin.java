package com.smd.soma_donation.facebook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.smd.soma_donation.MainActivity;
import com.smd.soma_donation.R;
import com.smd.soma_donation.SnsName;
import com.smd.soma_donation.retrofit.RetrofitService;
import com.smd.soma_donation.retrofit.format.DTOFacebookLogin;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by YS on 2016-07-15.
 */
public class FacebookLogin extends AppCompatActivity {

    private Button fbButton;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessToken accessToken;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_facebook_login);

        callbackManager = CallbackManager.Factory.create();

        fbButton = (Button) findViewById(R.id.fb_button);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setText("로그인");

    }

    public void onLoginClick(View v) {

        loginButton.setReadPermissions("public_profile", "user_friends");
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getApplicationContext(), "Login cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getApplicationContext(), "Login error", Toast.LENGTH_LONG).show();
            }

        });

        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(
                                accessToken = AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        try {
                                            setUserInfo(accessToken.getToken(), object.getString("name"), "01012341234", SnsName.Facebook);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,link");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // cancel code
                        Toast.makeText(getApplicationContext(), "Login cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // error code
                        Toast.makeText(getApplicationContext(), "Login Error", Toast.LENGTH_LONG).show();
                    }
                });
    }

    // 사용자 정보 저장 프로세스
    void setUserInfo(final String token, final String username, String phone, String sns) {
        String baseUrl = getResources().getString(R.string.baseURL);
        Retrofit client = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = client.create(RetrofitService.class);

        Call<DTOFacebookLogin> call = retrofitService.setUserInfo(
                accessToken.getToken(),
                username,
                phone,
                sns
        );
        call.enqueue(new Callback<DTOFacebookLogin>() {
            @Override
            public void onResponse(Response<DTOFacebookLogin> response) {
                if (response.isSuccess() && response.body() != null) {
                    DTOFacebookLogin postResponse = response.body();

                    String msg = postResponse.getMsg();
                    if (postResponse.isStatus()) {
                        Log.d("저장 결과: ", msg);
                        Toast.makeText(getApplicationContext(), "로그인 되었습니다.", Toast.LENGTH_LONG).show();

                        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        // 사용자 토큰 저장
                        editor.putString("access_token", token);
                        editor.putLong("user_id", postResponse.getData());
                        editor.putString("name", username);
                        editor.putBoolean("push", true);
                        editor.apply();

                        FCMRegistration(String.valueOf(postResponse.getData()));

                        // 정보 저장후 메인으로 이동
                        Intent intent = new Intent(FacebookLogin.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Log.d("저장 실패: ", msg);
                        facebookLogout();
                    }
                    finish();
                } else if (response.isSuccess()) {
                    Log.d("Response Body is NULL", response.message());
                    facebookLogout();
                } else {
                    Log.d("Response Error Body", response.errorBody().toString());
                    Log.d("Response Error Code", ""+response.code());

                    facebookLogout();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Error", t.getMessage());
                facebookLogout();
            }
        });

        //loginComplete();
    }

    protected void FCMRegistration(String user_id) {
        Log.d("REGIT TOPIC", user_id);
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic(user_id);
        // [END subscribe_topics]
    }

    private void facebookLogout() {
        // Facebook logout
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logOut();
    }

    public void onClick(View v) {
        if (v == fbButton) {
            loginButton.performClick();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
