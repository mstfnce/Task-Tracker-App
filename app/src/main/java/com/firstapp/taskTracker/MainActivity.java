package com.firstapp.hedeftakipuygulamasi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button allButton, activeButton, completedButton;
    ImageButton addButton;
    DonutProgress donutProgress;
    RecyclerView recyclerView;
    task_adapter task_adapter;
    ArrayList<Task> taskList = new ArrayList<>();
    ArrayList<Task> originalList = new ArrayList<>(); // filtrelenmemiş tüm görevler
    private String currentFilter = "all";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        allButton = findViewById(R.id.allButton);
        activeButton = findViewById(R.id.activeButton);
        completedButton = findViewById(R.id.completedButton);
        addButton = findViewById(R.id.addButton);

        donutProgress = findViewById(R.id.donut_progress);

        recyclerView = findViewById(R.id.rc);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        task_adapter = new task_adapter(taskList, MainActivity.this);
        recyclerView.setAdapter(task_adapter);




        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewGoalPage.class);
                startActivity(i);
            }
        });



        View.OnClickListener segmentClickListener = v -> {
            allButton.setBackgroundResource(R.drawable.segment_left_unselected);
            activeButton.setBackgroundResource(R.drawable.segment_middle_unselected);
            completedButton.setBackgroundResource(R.drawable.segment_right_unselected);

            if (v.getId() == R.id.allButton) {
                allButton.setBackgroundResource(R.drawable.segment_left_selected);
                filterTasks("all");
            } else if (v.getId() == R.id.activeButton) {
                activeButton.setBackgroundResource(R.drawable.segment_middle_selected);
                filterTasks("active");
            } else if (v.getId() == R.id.completedButton) {
                completedButton.setBackgroundResource(R.drawable.segment_right_selected);
                filterTasks("completed");
            }
        };


        allButton.setOnClickListener(segmentClickListener);
        activeButton.setOnClickListener(segmentClickListener);
        completedButton.setOnClickListener(segmentClickListener);

        getSavedTaskFromSharedPreferences();

        getUpdatedData();





    }

    @Override
    protected void onResume() {
        super.onResume();
        getSavedTaskFromSharedPreferences();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void getSavedTaskFromSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("saveData", Context.MODE_PRIVATE);
        String taskListJson = sharedPreferences.getString("taskList", null);

        if (taskListJson != null){
            Gson gson = new Gson();
            java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<ArrayList<Task>>(){}.getType();
            ArrayList<Task> savedList = gson.fromJson(taskListJson, type);

            taskList.clear();
            originalList.clear(); // orijinal listeyi sıfırla
            taskList.addAll(savedList);
            originalList.addAll(savedList); // yedeğe al


            int totalPercent = 0;
            int taskCountWithTarget = 0;

            for (Task task : taskList) {
                String completedStr = task.getCompletedAmount() != null ? task.getCompletedAmount() : "0";
                String targetStr = task.getTarget() != null ? task.getTarget() : "0";

                try {
                    double completed = Double.parseDouble(completedStr);
                    double target = Double.parseDouble(targetStr);

                    if (target > 0) {
                        int percent = (int) ((completed / target) * 100);
                        totalPercent += percent;
                        taskCountWithTarget++;
                    }
                } catch (NumberFormatException e) {
                    // geçersiz sayı varsa atla
                }
            }

// Ortalama yüzdelik
            int overallProgress = taskCountWithTarget > 0 ? totalPercent / taskCountWithTarget : 0;

// DonutProgress'e yaz
            donutProgress.setProgress(overallProgress);
            donutProgress.setText(overallProgress + "%");

            originalList.clear();
            originalList.addAll(taskList);
            filterTasks(currentFilter);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void getUpdatedData(){
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("position")) {
            int position = intent.getIntExtra("position", -1);
            if (position >= 0 && position < taskList.size()) {
                Task taskToUpdate = taskList.get(position);

                if (intent.hasExtra("updatedProgress")) {
                    String updatedProgress = intent.getStringExtra("updatedProgress");
                    taskToUpdate.setCompletedAmount(updatedProgress);
                }

                if (intent.hasExtra("updatedTarget")) {
                    String updatedTarget = intent.getStringExtra("updatedTarget");
                    taskToUpdate.setTarget(updatedTarget);
                }

                // Güncellenmiş listeyi SharedPreferences'e geri yaz
                SharedPreferences sharedPreferences = getSharedPreferences("saveData", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String updatedListJson = gson.toJson(taskList);
                editor.putString("taskList", updatedListJson);
                editor.apply();

                task_adapter.notifyDataSetChanged();
                filterTasks(currentFilter);
            }
        }
    }
    private void filterTasks(String type) {
        currentFilter = type;
        taskList.clear();

        if (type.equals("all")) {
            taskList.addAll(originalList);
        } else if (type.equals("active")) {
            for (Task task : originalList) {
                try {
                    double completed = Double.parseDouble(task.getCompletedAmount());
                    double target = Double.parseDouble(task.getTarget());
                    if (completed < target) taskList.add(task);
                } catch (Exception ignored) {}
            }
        } else if (type.equals("completed")) {
            for (Task task : originalList) {
                try {
                    double completed = Double.parseDouble(task.getCompletedAmount());
                    double target = Double.parseDouble(task.getTarget());
                    if (completed >= target) taskList.add(task);
                } catch (Exception ignored) {}
            }
        }

        task_adapter.notifyDataSetChanged();
    }

}