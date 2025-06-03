package com.firstapp.hedeftakipuygulamasi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TaskInfoPage extends AppCompatActivity {
    TextView backButton, title, progressText, description, descriptionText, dateText, taskDate, targetAmount, categoryText, percentText, deleteButton;
    Button updateButton;
    ProgressBar progressBar;
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_info_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        progressText = findViewById(R.id.progressText);
        description = findViewById(R.id.description);
        descriptionText = findViewById(R.id.descriptionText);
        dateText = findViewById(R.id.dateText);
        taskDate = findViewById(R.id.taskDate);
        targetAmount = findViewById(R.id.targetAmountText);
        categoryText = findViewById(R.id.categoryText);
        percentText = findViewById(R.id.percentText);
        updateButton = findViewById(R.id.updateButton);
        progressBar = findViewById(R.id.progressBar);
        deleteButton = findViewById(R.id.deleteButton);


        Intent intent = getIntent();
        String titleStr = intent.getStringExtra("title");
        String target = intent.getStringExtra("target");
        String date = intent.getStringExtra("date");
        String category = intent.getStringExtra("category");
        String description = intent.getStringExtra("description");
        String completedAmount = intent.getStringExtra("completedAmount");

        if (completedAmount == null) completedAmount = "0";

        title.setText(titleStr);
        targetAmount.setText( completedAmount + "/" + target);  // güncel ilerleme ekleyeceksen burada düzenle
        taskDate.setText(date);
        categoryText.setText(category);
        descriptionText.setText(description);

        if (completedAmount == null || completedAmount.isEmpty()) {
            completedAmount = "0";
        }
        try {
            int progress = (int) ((Double.parseDouble(completedAmount) / Double.parseDouble(target)) * 100);
            progressBar.setProgress(progress);
            percentText.setText(progress + "%");
            targetAmount.setText(completedAmount + "/" + target);
        } catch (NumberFormatException e) {
            progressBar.setProgress(0);
            percentText.setText("0%");
            targetAmount.setText("0/" + target);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TaskInfoPage.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        int position = getIntent().getIntExtra("position", -1);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TaskInfoPage.this, update_progress.class);
                i.putExtra("title", titleStr);
                i.putExtra("position", position);
                title.setText(titleStr);
                startActivity(i);
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.app.AlertDialog.Builder(TaskInfoPage.this)
                        .setTitle("Delete The Task")
                        .setMessage("Are you sure about deleting that task?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            int position = getIntent().getIntExtra("position", -1);
                            if (position != -1) {
                                SharedPreferences sharedPreferences = getSharedPreferences("saveData", MODE_PRIVATE);
                                Gson gson = new Gson();
                                String json = sharedPreferences.getString("taskList", null);

                                Type type = new TypeToken<ArrayList<Task>>() {}.getType();
                                ArrayList<Task> taskList = gson.fromJson(json, type);

                                if (taskList != null && position < taskList.size()) {
                                    taskList.remove(position);

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    String updatedJson = gson.toJson(taskList);
                                    editor.putString("taskList", updatedJson);
                                    editor.apply();

                                    Toast.makeText(TaskInfoPage.this, "Task has been deleted succesfully!", Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(TaskInfoPage.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Toast.makeText(TaskInfoPage.this, "Error: Task not found\"", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(TaskInfoPage.this, "Invalid task position", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });



    }
}