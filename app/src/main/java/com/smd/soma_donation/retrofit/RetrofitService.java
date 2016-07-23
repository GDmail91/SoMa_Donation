package com.smd.soma_donation.retrofit;

import com.smd.soma_donation.retrofit.format.DTOAlarmList;
import com.smd.soma_donation.retrofit.format.DTOFacebookLogin;
import com.smd.soma_donation.retrofit.format.DTOLikeResponse;
import com.smd.soma_donation.retrofit.format.DTOMonthly;
import com.smd.soma_donation.retrofit.format.DTOPostResponse;
import com.smd.soma_donation.retrofit.format.DTORankContents;
import com.smd.soma_donation.retrofit.format.DTORankDetail;
import com.smd.soma_donation.retrofit.format.DTOReview;
import com.smd.soma_donation.retrofit.format.DTOReviewDetail;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by YS on 2016-07-15.
 */
public interface RetrofitService {
    @FormUrlEncoded
    @POST("/users")
    Call<DTOFacebookLogin> setUserInfo(
            @Header("access-token") String access_token,
            @Field("username") String username,
            @Field("phone") String phone,
            @Field("sns") String sns);

    @GET("/ranking")
    Call<DTORankContents> getRankContents(
            @Header("access-token") String access_token,
            @Query("amount") int amount
    );

    @GET("/ranking")
    Call<DTORankContents> getRankContents(
            @Header("access-token") String access_token,
            @Query("amount") int amount,
            @Query("start_id") int start_id
    );

    @Multipart
    @POST("/ranking")
    Call<DTOPostResponse> postRankContent(
            @Header("access-token") String access_token,
            @Part("content_title") RequestBody contentTitle,
            @Part("description") RequestBody description,
            @PartMap Map<String, RequestBody> params
    );

    @FormUrlEncoded
    @POST("/ranking")
    Call<DTOPostResponse> postRankContent(
            @Header("access-token") String access_token,
            @Field("content_title") String contentTitle,
            @Field("description") String description,
            @Field("content_img") String  content_img
    );

    @FormUrlEncoded
    @PUT("/ranking/{content_id}")
    Call<DTOPostResponse> postRankContent(
            @Header("access-token") String access_token,
            @Path("content_id") int content_id,
            @Part("content_title") RequestBody contentTitle,
            @Part("description") RequestBody description,
            @PartMap Map<String, RequestBody> params
    );

    @GET("/ranking/{content_id}")
    Call<DTORankDetail> getDetailDonation(
            @Header("access-token") String access_token,
            @Path("content_id") int content_id
    );

    @GET("/ranking/{content_id}")
    Call<DTORankDetail> getDetailDonation(
            @Header("access-token") String access_token,
            @Path("content_id") int content_id,
            @Query("is_check") int isCheck
    );

    @POST("/ranking/{content_id}/likes")
    Call<DTOLikeResponse> changeLike(
            @Header("access-token") String access_token,
            @Path("content_id") int content_id
    );

    @GET("/ranking/user/{user_id}")
    Call<DTORankContents> getUserPost(
            @Header("access-token") String access_token,
            @Path("user_id") long user_id
    );

    @GET("/monthly")
    Call<DTOMonthly> getMonthly(
            @Header("access-token") String access_token
    );

    @FormUrlEncoded
    @POST("/contact")
    Call<DTOPostResponse> postQuestion(
            @Header("access-token") String access_token,
            @Field("question") String question,
            @Field("email") String email
    );

    @GET("/reviews")
    Call<DTOReview> getReviews(
            @Header("access-token") String access_token,
            @Query("amount") int amount
    );

    @GET("/reviews")
    Call<DTOReview> getReviews(
            @Header("access-token") String access_token,
            @Query("amount") int amount,
            @Query("start_id") int start_id
    );

    @GET("/reviews/{review_id}")
    Call<DTOReviewDetail> getDetailReview(
            @Header("access-token") String access_token,
            @Path("review_id") int review_id
    );

    @Multipart
    @POST("/ranking")
    Call<DTOPostResponse> postReview(
            @Header("access-token") String access_token,
            @Part("review_support_id") RequestBody contentTitle,
            @Part("review_content") RequestBody description,
            @PartMap Map<String, RequestBody> params
    );

    @GET("/search")
    Call<DTORankContents> getSearch(
            @Header("access-token") String access_token,
            @Query("amount") int amount,
            @Query("search") String search
    );

    @GET("/search")
    Call<DTORankContents> getSearch(
            @Header("access-token") String access_token,
            @Query("amount") int amount,
            @Query("start_id") int start_id,
            @Query("search") String search
    );

    @GET("/alarm")
    Call<DTOAlarmList> getAlarm(
            @Header("access-token") String access_token,
            @Query("amount") int amount
    );

    @GET("/alarm")
    Call<DTOAlarmList> getAlarm(
            @Header("access-token") String access_token,
            @Query("amount") int amount,
            @Query("start_id") int start_id
    );
}
