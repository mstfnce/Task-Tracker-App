package com.firstapp.hedeftakipuygulamasi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class task_adapter extends RecyclerView.Adapter<task_adapter.settingOnViewHolder>{

    ArrayList<Task> taskList = new ArrayList<>();
    Context context;

    public task_adapter(ArrayList<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    @NonNull
    @Override
    public settingOnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_rc, parent, false);
        return new settingOnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull settingOnViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.title.setText(task.getTitle());
        holder.category.setText(task.getCategory());
//        holder.targetAmount.setText( task.getCompletedAmount() + "/" + task.getTarget());
        holder.taskDate.setText(task.getDate());


        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TaskInfoPage.class);
                intent.putExtra("title", task.getTitle());
                intent.putExtra("target", task.getTarget());
                intent.putExtra("date", task.getDate());
                intent.putExtra("category", task.getCategory());
                if (task.getDescription() != null){
                    intent.putExtra("description", task.getDescription());
                }
                // 🆕 Pozisyonu da ekliyoruz:
                intent.putExtra("position", holder.getAdapterPosition());
                intent.putExtra("completedAmount", task.getCompletedAmount());

                context.startActivity(intent);
            }
        });

        String completedStr = task.getCompletedAmount();
        String targetStr = task.getTarget();

        if (completedStr == null || completedStr.isEmpty()) completedStr = "0";
        if (targetStr == null || targetStr.isEmpty()) targetStr = "1"; // divide by zero önlemi

        try {
            double completed = Double.parseDouble(completedStr);
            double target = Double.parseDouble(targetStr);
            int percent = (int) ((completed / target) * 100);

            holder.percentText.setText(percent + "%");
            holder.progressBar.setProgress(percent);
            holder.targetAmount.setText(completedStr + "/" + targetStr);
        } catch (NumberFormatException e) {
            holder.percentText.setText("0%");
            holder.progressBar.setProgress(0);
            holder.targetAmount.setText("0/" + targetStr);
        }




    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    public class settingOnViewHolder extends RecyclerView.ViewHolder{
        TextView title, targetAmount, taskDate, category, percentText;
        ImageButton infoButton;
        ProgressBar progressBar;

        public settingOnViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleText);
            category = itemView.findViewById(R.id.textView2);
            targetAmount = itemView.findViewById(R.id.targetAmount);
            taskDate = itemView.findViewById(R.id.taskDate);
            infoButton = itemView.findViewById(R.id.infoButton);
            percentText = itemView.findViewById(R.id.percentText);
            progressBar = itemView.findViewById(R.id.progressBar);


        }
    }
}
