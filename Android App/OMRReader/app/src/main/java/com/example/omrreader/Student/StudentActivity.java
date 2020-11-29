package com.example.omrreader.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.omrreader.MainActivity;
import com.example.omrreader.R;
import com.example.omrreader.Retrofit.NetworkClient;
import com.example.omrreader.Retrofit.UploadApi;
import com.example.omrreader.Student.Result.ResultDataModel;
import com.example.omrreader.Student.Result.ResultModel;
import com.example.omrreader.Student.Table.TableActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StudentActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    Button resultButton;

    String setId;
    private String TAG=this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        setId=getIntent().getStringExtra("setId");
        floatingActionButton=findViewById(R.id.studentFloatingBtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StudentActivity.this, MainActivity.class);
                intent.putExtra("choose","student");
                intent.putExtra("setId",setId);
                startActivity(intent);
            }
        });

        resultButton=findViewById(R.id.resBtn);
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAnswerSheet();
            }
        });
    }

    private void getAnswerSheet() {
        Retrofit retrofit= NetworkClient.getRetrofit(this);

        UploadApi uploadApi=retrofit.create(UploadApi.class);

        Call<ResultModel> call=uploadApi.getAnswers(setId);
        call.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                if(response.isSuccessful()){
                    Intent intent=new Intent(StudentActivity.this, TableActivity.class);
                    ResultModel model=response.body();
                    intent.putExtra("resultList", (Serializable) model.getData());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {
                Log.e(TAG,t.getMessage());
            }
        });
    }
}