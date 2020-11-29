package com.example.omrreader.Retrofit;

import com.example.omrreader.Details.DetailsResponseModel;
import com.example.omrreader.Student.Result.ResultModel;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UploadApi {
    @Multipart
    @POST("upload/")
    Call< DetailsResponseModel> uploadAnswerSheet(@Part MultipartBody.Part file, @Part("set-id") RequestBody setid, @Part("correct-marks") RequestBody correct, @Part("negative") RequestBody negative);


    @Multipart
    @POST("eval/")
    Call<JsonObject> uploadAnswerSheet(@Part MultipartBody.Part file, @Part("set-id") RequestBody setid);

    @GET("students/")
    Call<ResultModel> getAnswers(@Query("set-id") String setId);
}
