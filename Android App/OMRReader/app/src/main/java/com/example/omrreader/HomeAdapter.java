package com.example.omrreader;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.omrreader.Details.DetailsModel;
import com.example.omrreader.Student.StudentActivity;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private Context context;
    private List<DetailsModel> detailsModelList;

    public HomeAdapter(Context context, List<DetailsModel> detailsModelList) {
        this.context = context;
        this.detailsModelList = detailsModelList;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.homerow, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {

        final DetailsModel detailsModel=detailsModelList.get(position);
        holder.fileName.setText(detailsModel.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, StudentActivity.class);
                intent.putExtra("setId",detailsModel.getSetId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return detailsModelList.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder{
        TextView fileName;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName=itemView.findViewById(R.id.fileName);
        }
    }
}
