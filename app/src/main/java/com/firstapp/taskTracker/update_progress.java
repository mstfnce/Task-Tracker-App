package com.firstapp.hedeftakipuygulamasi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class update_progress extends AppCompatActivity {
    TextView cancelButton, saveButton, title, previousProgress;
    EditText updateProgress, targetEdit;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_progress);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cancelButton = findViewById(R.id.cancel);
        saveButton = findViewById(R.id.save);
        title = findViewById(R.id.titleText);
        previousProgress = findViewById(R.id.previousProgress);
        updateProgress = findViewById(R.id.updateProgressEdit);
        targetEdit = findViewById(R.id.targetEdit);

        Intent intent = getIntent();
        String titleStr = intent.getStringExtra("title");
        title.setText(titleStr);




        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(update_progress.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newProgress = updateProgress.getText().toString().trim();
                String newTarget = targetEdit.getText().toString().trim();
                int position = getIntent().getIntExtra("position", -1);

                // Eğer iki alan da boşsa, kullanıcıya uyarı ver ve return et
                if (newProgress.isEmpty() && newTarget.isEmpty()) {
                    Toast.makeText(update_progress.this, "Please fill at least one blank.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(update_progress.this, MainActivity.class);

                if (!newProgress.isEmpty()) {
                    intent.putExtra("updatedProgress", newProgress);
                }

                if (!newTarget.isEmpty()) {
                    intent.putExtra("updatedTarget", newTarget);
                }

                intent.putExtra("position", position);
                startActivity(intent);
                finish();

            }
        });


    }
}