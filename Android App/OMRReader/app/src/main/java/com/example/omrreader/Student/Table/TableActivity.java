package com.example.omrreader.Student.Table;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.omrreader.R;
import com.example.omrreader.Student.Result.ResultDataModel;

import java.util.ArrayList;
import java.util.List;

public class TableActivity extends AppCompatActivity {
    private String TAG=this.getClass().getSimpleName();
    RecyclerView recyclerView;
    TableAdapter adapter;
    List<ResultDataModel> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        list= (List<ResultDataModel>) getIntent().getSerializableExtra("resultList");
        recyclerView=findViewById(R.id.tableRecyclerView);
        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new TableAdapter(this,list);
        recyclerView.setAdapter(adapter);
    }



}