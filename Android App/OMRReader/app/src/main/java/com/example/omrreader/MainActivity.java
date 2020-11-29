package com.example.omrreader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.omrreader.Student.StudentActivity;
import com.example.omrreader.database.AddDatabase;
import com.example.omrreader.Details.DetailsModel;
import com.example.omrreader.Details.DetailsResponseModel;
import com.example.omrreader.Retrofit.NetworkClient;
import com.example.omrreader.Retrofit.UploadApi;
import com.google.gson.JsonObject;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    ImageButton btBrowse,btReset,btNext;
    ImageView imageView;
    Uri uri;
    File file;
    DetailsModel detailsModel;
    private String TAG=this.getClass().getSimpleName();
    String choose;
    String setId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar=findViewById(R.id.mainProgressBar);
        choose=getIntent().getStringExtra("choose");
        btBrowse=findViewById(R.id.bt_browse);
        btReset=findViewById(R.id.bt_reset);
        btNext=findViewById(R.id.bt_next);
        imageView=findViewById(R.id.imageView);
        if(choose.equals("student")){
            imageView.setImageResource(R.drawable.image1);
        }else{
            imageView.setImageResource(R.drawable.image);

        }

        btBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(MainActivity.this);

            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(choose.equals("student")){
                    imageView.setImageResource(R.drawable.image1);

                }else {
                    imageView.setImageResource(R.drawable.image);

                }
                file=null;
            }
        });
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(choose.equals("student")){
                    setId=getIntent().getStringExtra("setId");
                    uploadAnswerSheet(setId);
                }else {
                    detailsModel = (DetailsModel) getIntent().getSerializableExtra("detailsModel");

                    uploadFile();
                }
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE
            && resultCode== Activity.RESULT_OK){
            Uri imageUri=CropImage.getPickImageResultUri(this,data);
            if(CropImage.isReadExternalStoragePermissionsRequired(this,imageUri)){
                uri=imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
            }else{
                startCrop(imageUri);
            }
        }

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                file=new File(result.getUri().getPath());
                imageView.setImageURI(result.getUri());
                Toast.makeText(this, "Image Updated Successfully!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCrop(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);


    }

    private void uploadFile() {
        if(file!=null){
            progressBar.setVisibility(View.VISIBLE);

            Retrofit retrofit= NetworkClient.getRetrofit(this);

            UploadApi uploadApi=retrofit.create(UploadApi.class);

            RequestBody requestBody=RequestBody.create(MediaType.parse("image/*"),file );

            RequestBody set=RequestBody.create(MediaType.parse("text/plain"),detailsModel.getSetId());
            RequestBody cor=RequestBody.create(MediaType.parse("text/plain"),detailsModel.getCorrect());
            RequestBody wrong=RequestBody.create(MediaType.parse("text/plain"),detailsModel.getWrong());

            //create a MultipartBody.Part using file name request body, file name and part name
            MultipartBody.Part part=MultipartBody.Part.createFormData("file",file.getName(),requestBody);

            Call< DetailsResponseModel> call=uploadApi.uploadAnswerSheet(part,set,cor,wrong);
            call.enqueue(new Callback<DetailsResponseModel>() {
                @Override
                public void onResponse(Call<DetailsResponseModel> call, Response<DetailsResponseModel> response) {
                        Log.e(TAG,response+"");
                        DetailsResponseModel model=response.body();
                        Log.e(TAG,model+"");
                        if(model!=null){
                            AddDatabase addDatabase=new AddDatabase(MainActivity.this);
                            boolean res=addDatabase.insert(detailsModel);
                            if(res){
                                Toast.makeText(MainActivity.this, "Data inserted", Toast.LENGTH_SHORT).show();
                                Intent homeintent=new Intent(MainActivity.this, HomeActivity.class);
                                homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(homeintent);

                            }
                        }
                        if(model==null){
                            Toast.makeText(MainActivity.this, "Image is not in OMR format", Toast.LENGTH_SHORT).show();
                        }
                    progressBar.setVisibility(View.INVISIBLE);

                }

                @Override
                public void onFailure(Call<DetailsResponseModel> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);

                }
            });
        }else {
            Toast.makeText(this, "Select file first", Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadAnswerSheet(String setId) {
        if(file!=null){
            progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit= NetworkClient.getRetrofit(this);

            UploadApi uploadApi=retrofit.create(UploadApi.class);

            RequestBody requestBody=RequestBody.create(MediaType.parse("image/*"),file );

            RequestBody set=RequestBody.create(MediaType.parse("text/plain"),setId);

            //create a MultipartBody.Part using file name request body, file name and part name
            MultipartBody.Part part=MultipartBody.Part.createFormData("file",file.getName(),requestBody);

            Call<JsonObject> call=uploadApi.uploadAnswerSheet(part,set);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()){
                        Log.e(TAG,response+"");
                        Log.e(TAG,response.body()+"");
                        JsonObject jsonObject=response.body();

                        Log.e(TAG,jsonObject+"");
                        if(response.body()!=null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Final score is")
                                    .setMessage(jsonObject.getAsJsonObject("data").get("score") + "")
                                    .setIcon(R.drawable.success)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            imageView.setImageResource(R.drawable.image1);
                                            file = null;
                                        }
                                    });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Image is not in OMR format", Toast.LENGTH_SHORT).show();

                        }
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG,t.getMessage());
                    progressBar.setVisibility(View.INVISIBLE);

                }
            });
        }else {
            Toast.makeText(this, "Select file first", Toast.LENGTH_SHORT).show();
        }
    }

}