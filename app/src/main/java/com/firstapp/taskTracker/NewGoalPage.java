package com.firstapp.hedeftakipuygulamasi;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class NewGoalPage extends AppCompatActivity {
    TextView cancel, save, date;
    EditText title, targetAmount, description;
    Calendar calendar;
    Spinner spinner;
    ArrayAdapter arrayAdapter;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_goal_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cancel = findViewById(R.id.cancel);
        save = findViewById(R.id.save);
        date = findViewById(R.id.dueDate);
        spinner = findViewById(R.id.spinner);
        title = findViewById(R.id.title);
        targetAmount = findViewById(R.id.targetAmount);
        description = findViewById(R.id.description);

        calendar = Calendar.getInstance();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        arrayAdapter = ArrayAdapter.createFromResource(this,R.array.category_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ApplySharedPref")
            @Override
            public void onClick(View v) {
                saveGivenData();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewGoalPage.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void saveGivenData(){
        String titleTarget = title.getText().toString().trim();
        String selectedDate = date.getText().toString().trim();
        String selectedTarget = targetAmount.getText().toString().trim();
        String selectedCategory = spinner.getSelectedItem().toString();
        String writtenDescription = description.getText().toString().trim();

        if (titleTarget.isEmpty() || selectedDate.isEmpty() || selectedTarget.isEmpty()) {
            Toast.makeText(NewGoalPage.this, "Please fill in the blanks", Toast.LENGTH_SHORT).show();;
            return;
        }
        Task newTask = new Task(titleTarget, false, selectedDate, selectedTarget, selectedCategory, writtenDescription, "0");

        SharedPreferences sharedPreferences = getSharedPreferences("saveData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String existingListJson = sharedPreferences.getString("taskList", null);
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<ArrayList<Task>>(){}.getType();
        //TypeToken → Gson’ın ArrayList<Task>'i doğru anlaması için tür bilgisi sağlar.
        ArrayList<Task> taskList = existingListJson == null ? new ArrayList<>() : gson.fromJson(existingListJson, type);
        //Eğer veri yoksa boş bir liste oluşturur (new ArrayList<>()), varsa JSON’dan gerçek listeye çevirir (fromJson ile).
        taskList.add(newTask);

        String updatedListJson = gson.toJson(taskList);
        editor.putString("taskList", updatedListJson);
        editor.apply();

        Toast.makeText(NewGoalPage.this, "Task has been saved", Toast.LENGTH_SHORT).show();

        setResult(RESULT_OK);
        finish();
    }

    private void showDatePickerDialog(){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(NewGoalPage.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    @SuppressLint("DefaultLocale") String formattedDate = String.format("%02d %s %04d",
                            selectedDay,
                            new DateFormatSymbols().getShortMonths()[selectedMonth],
                            selectedYear);
                    date.setText(formattedDate);
                }, year, month, day);
        datePickerDialog.show();
    }





}