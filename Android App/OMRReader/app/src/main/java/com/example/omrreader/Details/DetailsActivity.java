package com.example.omrreader.Details;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.omrreader.HomeActivity;
import com.example.omrreader.MainActivity;
import com.example.omrreader.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Timestamp;

public class DetailsActivity extends AppCompatActivity {
    TextView setName,correct,wrong,questions;
    FloatingActionButton saveButton;
    String name,setId,correctAns,wrongAns,noOfQuestions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initialize();
    }

    private void initialize() {
        setName=findViewById(R.id.subName);
        correct=findViewById(R.id.correct);
        wrong=findViewById(R.id.wrong);
        questions=findViewById(R.id.questions);
        saveButton=findViewById(R.id.homeFloatingBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    private void check() {
        if(setName.getText().toString().trim().isEmpty()){
            setName.setError("Please enter name");
            setName.requestFocus();
            return;
        }
        if(questions.getText().toString().trim().isEmpty()){
            questions.setError("Please enter no. of questions");
            questions.requestFocus();
            return;
        }
        if(correct.getText().toString().trim().isEmpty()){
            correct.setError("Please enter correct marks");
            correct.requestFocus();
            return;
        }
        if(wrong.getText().toString().trim().isEmpty()){
            wrong.setError("Please enter wrong marks");
            wrong.requestFocus();
            return;
        }
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());

        name=setName.getText().toString().trim();
        setId=name+timestamp;
        correctAns=correct.getText().toString().trim();
        wrongAns=wrong.getText().toString().trim();
        noOfQuestions=questions.getText().toString().trim();

        DetailsModel detailsModel=new DetailsModel(setId,name,correctAns,wrongAns,noOfQuestions);

        Intent intent=new Intent(DetailsActivity.this, MainActivity.class);
        intent.putExtra("detailsModel",detailsModel);
        intent.putExtra("choose","details");
        startActivity(intent);

    }
}