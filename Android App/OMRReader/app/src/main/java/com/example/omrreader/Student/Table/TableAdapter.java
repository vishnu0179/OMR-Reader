package com.example.omrreader.Student.Table;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.omrreader.R;
import com.example.omrreader.Student.Result.ResultDataModel;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    Context context;
    List<ResultDataModel> tableModelList;

    public TableAdapter(Context context, List<ResultDataModel> tableModelList) {
        this.context = context;
        this.tableModelList = tableModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.tablerow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if(tableModelList!=null&&tableModelList.size()>0){
            final ResultDataModel tableModel=tableModelList.get(position);
            holder.score.setText(tableModel.getScore());
            holder.testId.setText(tableModel.getTest_id());
            holder.enrollment.setText(tableModel.getEnrollment());
            holder.answerSheet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri=Uri.parse(tableModel.getAnswer_sheet_img());
                    Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return tableModelList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView score,enrollment,testId,answerSheet;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            score=itemView.findViewById(R.id.score);
            enrollment=itemView.findViewById(R.id.enrollment);
            testId=itemView.findViewById(R.id.testID);
            answerSheet=itemView.findViewById(R.id.answerSheet);
        }
    }
}
