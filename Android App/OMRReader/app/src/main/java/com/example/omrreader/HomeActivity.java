package com.example.omrreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.omrreader.database.AddDatabase;
import com.example.omrreader.Details.DetailsActivity;
import com.example.omrreader.Details.DetailsModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    RelativeLayout relativeLayout;
    FloatingActionButton homeFloatingBtn;
    RecyclerView homeRecyclerView;
    private List<DetailsModel> detailsModelList=new ArrayList<>();
    private String TAG=this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        relativeLayout=findViewById(R.id.homeLayout);
        AddDatabase addDatabase=new AddDatabase(this);
        detailsModelList=addDatabase.getAllData();
        if(detailsModelList!=null&&detailsModelList.size()!=0)
            relativeLayout.setVisibility(View.INVISIBLE);
        Collections.reverse(detailsModelList);
        Log.e(TAG+"size",detailsModelList.size()+"");
        homeRecyclerView=findViewById(R.id.homeRecyclerView);
        homeRecyclerView.setHasFixedSize(true);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        HomeAdapter adapter=new HomeAdapter(this,detailsModelList);
        homeRecyclerView.setAdapter(adapter);
        homeFloatingBtn=findViewById(R.id.homeFloatingBtn);
        homeFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this, DetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}