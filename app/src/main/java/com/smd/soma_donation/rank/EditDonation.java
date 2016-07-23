package com.smd.soma_donation.rank;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.smd.soma_donation.MainActivity;
import com.smd.soma_donation.R;
import com.smd.soma_donation.retrofit.FileUtils;
import com.smd.soma_donation.retrofit.RetrofitService;
import com.smd.soma_donation.retrofit.ServiceGenerator;
import com.smd.soma_donation.retrofit.format.DTOPostResponse;
import com.smd.soma_donation.retrofit.format.DTORankItem;
import com.smd.soma_donation.search.SearchActivity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by YS on 2016-07-14.
 */
public class EditDonation extends AppCompatActivity {
    public final String TAG = "EditDonation";
    EditText detailTitle;
    EditText detailDescription;
    ImageView detailImg;
    ImageView getImg;
    Uri detailImgUri;
    RelativeLayout progressLayout;
    private int PICK_IMAGE_REQUEST = 1;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    //String detailImg = "http://test.test.test.url.com";
    DTORankItem detailDonation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_edit);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        detailDonation = (DTORankItem) getIntent().getSerializableExtra("detailDonation");

        detailTitle = (EditText) findViewById(R.id.detail_title);
        detailDescription = (EditText) findViewById(R.id.detail_intro);
        detailImg = (ImageView) findViewById(R.id.detail_img);
        getImg = (ImageView) findViewById(R.id.get_img);

        progressLayout = (RelativeLayout) findViewById(R.id.progress_layout);

        // Permission Check (Because of API 23)
        if (ContextCompat.checkSelfPermission(EditDonation.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditDonation.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }
            ActivityCompat.requestPermissions(EditDonation.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant

        }

        if (detailDonation != null) {
            // TODO dounload detail img

            detailTitle.setText(detailDonation.getContentTitle());
            detailDescription.setText(detailDonation.getDescription());
        }
    }
/*
    void postContent(String access_token, String title, String description, String imgUrl) {
        Log.d(TAG, "함수 실행");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<DTOPostResponse> call;
        if (detailDonation != null) {
            call = service.postRankContent(access_token, detailDonation.getContentId(), title, description, imgUrl);
        } else {
            call = service.postRankContent(access_token, title, description, imgUrl);
        }
        call.enqueue(new Callback<DTOPostResponse>() {
            @Override
            public void onResponse(Response<DTOPostResponse> response) {
                if (response.isSuccess() && response.body() != null) {

                    DTOPostResponse postResponse = response.body();

                    Toast.makeText(getApplicationContext(), postResponse.getMsg(), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(EditDonation.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "작성실패", Toast.LENGTH_LONG).show();
            }
        });
    }*/

    // edit button click
    public void editPost(View v) {
        // TODO editPost
        if (v.getId() == R.id.edit_complete_btn) {
            if (detailTitle.getText().toString().equals("")
            || detailDescription.getText().toString().equals("")
            || detailImgUri.toString().equals("")) {
                Toast.makeText(EditDonation.this, "모두 작성해주세요", Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "클릭");
                // 키보드 숨기기
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(detailTitle.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(detailDescription.getWindowToken(), 0);

                detailImg.setEnabled(false);
                detailTitle.setEnabled(false);
                detailDescription.setEnabled(false);

                // 프로그래스 액티비티 보여주기
                progressLayout.setVisibility(View.VISIBLE);
                progressLayout.bringToFront();

                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                uploadFile(pref.getString("access_token", ""),
                        detailTitle.getText().toString(),
                        detailDescription.getText().toString(),
                        detailImgUri);
            }
        }
    }

    // image upload
    private void uploadFile(String access_token, String title, String description, Uri fileUri) {
        // create upload service client
        RetrofitService service =
                ServiceGenerator.createService(RetrofitService.class);

        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, fileUri);

        Log.d(TAG, file.toString());
        // create RequestBody instance from file
        RequestBody contentTitle =
                RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody contentDescription =
                RequestBody.create(MediaType.parse("text/plain"), description);

        Map<String, RequestBody> map = new HashMap<>();
        RequestBody fileBody = RequestBody.create(MediaType.parse(getMimeType(file.toString())), file);
        map.put("content_img\"; filename=\"" + file.getName(), fileBody);

        Call<DTOPostResponse> call;
        if (detailDonation != null) {
            call = service.postRankContent(access_token, detailDonation.getContentId(), contentTitle, contentDescription, map);
        } else {
            call = service.postRankContent(access_token, contentTitle, contentDescription, map);
        }
        call.enqueue(new Callback<DTOPostResponse>() {
            @Override
            public void onResponse(Response<DTOPostResponse> response) {
                if (response.isSuccess()) {
                    Intent intent = new Intent(EditDonation.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    Log.v("Upload", "success");
                } else {
                    try {
                        progressLayout.setVisibility(View.GONE);
                        Toast.makeText(EditDonation.this, "Please try again", Toast.LENGTH_LONG)
                                .show();

                        detailImg.setEnabled(true);
                        detailTitle.setEnabled(true);
                        detailDescription.setEnabled(true);
                        Log.e("Upload error:", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressLayout.setVisibility(View.GONE);
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public void onCheckPermission(View v) {
        if (v.getId() == R.id.select_img) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                Log.d(TAG, "실행");
                //dialog.dismiss();
                return;
            } else {
                imageSelect();
            }

        }
    }

    protected void imageSelect() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        setImageBySelected(data);
        getImg.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "requestCode: "+requestCode);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    imageSelect();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void setImageBySelected(Intent data) {
        String ImageDecode;
        try {
            if (data != null) {
                ContentResolver contentResolver = getContentResolver();
                detailImgUri = data.getData();
                grantUriPermission("com.smd.soma_donation.rank", detailImgUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                String[] FILE = { MediaStore.Images.Media.DATA };


                Cursor cursor = contentResolver.query(detailImgUri,
                        FILE, null, null, null);

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(FILE[0]);
                ImageDecode = cursor.getString(columnIndex);
                cursor.close();

                detailImg.setImageBitmap(BitmapFactory
                        .decodeFile(ImageDecode));

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG)
                    .show();
        }
    }


    public void onMenuSelected(View v) {

        if (v.getId() == R.id.search_btn) {
            Intent intent = new Intent(EditDonation.this, SearchActivity.class);
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