package com.harsha.make_your_test;


import static androidx.fragment.app.FragmentManager.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class Second extends AppCompatActivity {

    private EditText questionEditText, option1EditText, option2EditText, option3EditText, option4EditText;
    private RadioGroup optionsRadioGroup;
    private RadioButton radioButton;
    private Button saveButton;
    private QuestionViewModel quizViewModel;
    private int remainingQuestions = 1;
    private int answer;
    private  int teext;
private    String enteredCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        answer = -1; // Default value if no option is selected

        questionEditText = findViewById(R.id.question);
        option1EditText = findViewById(R.id.opt1text);
        option2EditText = findViewById(R.id.opt2text);
        option3EditText = findViewById(R.id.opt3text);
        option4EditText = findViewById(R.id.opt4text);
          teext = getIntent().getIntExtra("number", 0);

          String name = getIntent().getStringExtra("name");
          enteredCode = getIntent().getStringExtra("code");

        optionsRadioGroup = findViewById(R.id.group);
        saveButton = findViewById(R.id.button);
        Log.e(TAG,"number" +teext);
        optionsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                answer = optionsRadioGroup.indexOfChild(findViewById(checkedId));
             }
        });

        quizViewModel = new ViewModelProvider(this).get(QuestionViewModel.class);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuizQuestion();
            }
        });
    }

    private void saveQuizQuestion() {
        String question = questionEditText.getText().toString();
        String option1 = option1EditText.getText().toString();
        String option2 = option2EditText.getText().toString();
        String option3 = option3EditText.getText().toString();
        String option4 = option4EditText.getText().toString();

        List<String> options = Arrays.asList(option1, option2, option3, option4);

        if (TextUtils.isEmpty(question) || options.contains("")) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (remainingQuestions == teext-1) {
            saveButton.setText("SUBMIT");
        }
        if (remainingQuestions == teext) {
            // Delay finishing the activity to provide a smooth transition
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent in = new Intent(Second.this,First.class);
                    startActivity(in);
                    finish();
                    // Finish this activity after a short delay
                    finish();
                }
            }, 500); // You can adjust the delay time as needed (in milliseconds)
        }
        quizViewModel.saveQuestion(question, options, answer,enteredCode);
        remainingQuestions++;
        questionEditText.getText().clear();
        option1EditText.getText().clear();
        option2EditText.getText().clear();
        option3EditText.getText().clear();
        option4EditText.getText().clear();
        optionsRadioGroup.clearCheck();
        Toast.makeText(this, "Question saved successfully", Toast.LENGTH_SHORT).show();


        }
    }

