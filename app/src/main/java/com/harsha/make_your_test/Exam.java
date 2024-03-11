package com.harsha.make_your_test;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Exam extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private List<DataSnapshot> questionSnapshots;
    private int currentQuestionIndex = 0;
    private int correctAnswerIndex;

    private int selectedAnswer;
    private TextView questionTextView;
    private TextView option1TextView;
    private TextView option2TextView;
    private int marks = 0;
    private TextView option3TextView;
    private TextView option4TextView;
    private TextView actionButton, previous, save;
    private int questions;
    private ArrayList<Integer> answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        Intent in = getIntent();
        String code = in.getStringExtra("enteredCode");
        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
                .child(code)
                .child("questions");
        Log.e(TAG, "code" + code);
        // Initialize TextViews
        questionTextView = findViewById(R.id.ques);
        option1TextView = findViewById(R.id.otion1);
        option2TextView = findViewById(R.id.otion2);
        option3TextView = findViewById(R.id.otion3);
        option4TextView = findViewById(R.id.otion4);
        actionButton = findViewById(R.id.save);
        previous = findViewById(R.id.previous);
        save = findViewById(R.id.next);
        save.setVisibility(View.INVISIBLE);
        ArrayList<Integer> answer = new ArrayList<>();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Exam.this, MainActivity.class);
                startActivity(in);
            }
        });

        option1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Change background for selected option
                option1TextView.setBackgroundResource(R.drawable.selected);
                option2TextView.setBackgroundResource(R.drawable.notselected);
                option3TextView.setBackgroundResource(R.drawable.notselected);
                option4TextView.setBackgroundResource(R.drawable.notselected);
                selectedAnswer = 0;
                answer.add(selectedAnswer);
            }
        });

        option2TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option2TextView.setBackgroundResource(R.drawable.selected);
                option3TextView.setBackgroundResource(R.drawable.notselected);
                option4TextView.setBackgroundResource(R.drawable.notselected);
                option1TextView.setBackgroundResource(R.drawable.notselected);
                selectedAnswer = 1;
                answer.add(selectedAnswer);
            }
        });

        option3TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option3TextView.setBackgroundResource(R.drawable.selected);
                option4TextView.setBackgroundResource(R.drawable.notselected);
                option1TextView.setBackgroundResource(R.drawable.notselected);
                option2TextView.setBackgroundResource(R.drawable.notselected);
                selectedAnswer = 2;
                answer.add(selectedAnswer);
            }
        });

        option4TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option4TextView.setBackgroundResource(R.drawable.selected);
                option1TextView.setBackgroundResource(R.drawable.notselected);
                option2TextView.setBackgroundResource(R.drawable.notselected);
                option3TextView.setBackgroundResource(R.drawable.notselected);
                selectedAnswer = 3;
                answer.add(selectedAnswer);
            }
        });



        // Load questions from Firebase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionSnapshots = new ArrayList<>();
                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    questionSnapshots.add(questionSnapshot);
                }
                questions = questionSnapshots.size();
                if (questionSnapshots.size() == 1) {
                    save.setVisibility(View.VISIBLE);
                }
                displayQuestion(questionSnapshots.get(currentQuestionIndex));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        // Set OnClickListener for the previous button
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentQuestionIndex--;
                if (currentQuestionIndex < questionSnapshots.size()) {
                    // Display the previous question
                    displayQuestion(questionSnapshots.get(currentQuestionIndex));
                    int storedAnswerIndex = answer.get(currentQuestionIndex);
                    // Set the background of the stored answer
                    if (storedAnswerIndex == 0) {
                        option1TextView.setBackgroundResource(R.drawable.selected);
                    }
                    if (storedAnswerIndex == 1) {
                        option2TextView.setBackgroundResource(R.drawable.selected);
                    }
                    if (storedAnswerIndex == 2) {
                        option3TextView.setBackgroundResource(R.drawable.selected);
                    }
                    if (storedAnswerIndex == 4) {
                        option4TextView.setBackgroundResource(R.drawable.selected);
                    }
                } else {
                    // No more questions
                    // You can handle this scenario accordingly
                }
            }
        });

        // Set OnClickListener for the action button
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedAnswer == correctAnswerIndex) {
                    marks++;
                }
                Toast.makeText(Exam.this, "YOUR MARKS ARE: " + marks, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "question" + questions);

                // Move to the next question
                currentQuestionIndex++;
                questions--;

                resetOptionBackgrounds();
                // Check if there are more questions
                if (currentQuestionIndex < questionSnapshots.size()) {
                    // Display the next question
                    displayQuestion(questionSnapshots.get(currentQuestionIndex));
                    if (answer != null) {
                        int storedAnswerIndex = answer.get(currentQuestionIndex);
                        // Set the background of the stored answer
                        if (storedAnswerIndex == 0) {
                            option1TextView.setBackgroundResource(R.drawable.selected);
                        }
                        if (storedAnswerIndex == 1) {
                            option2TextView.setBackgroundResource(R.drawable.selected);
                        }
                        if (storedAnswerIndex == 2) {
                            option3TextView.setBackgroundResource(R.drawable.selected);
                        }
                        if (storedAnswerIndex == 4) {
                            option4TextView.setBackgroundResource(R.drawable.selected);
                        }
                    }
                }
            }

        });
    }

    // Method to check if there's only one question left


    // Method to reset option backgrounds
    private void resetOptionBackgrounds() {
        // Reset background for all options
        option1TextView.setBackgroundResource(R.drawable.notselected);
        option2TextView.setBackgroundResource(R.drawable.notselected);
        option3TextView.setBackgroundResource(R.drawable.notselected);
        option4TextView.setBackgroundResource(R.drawable.notselected);
    }

    // Method to display the current question
    private void displayQuestion(DataSnapshot questionSnapshot) {
        // Get question data
        String questionText = questionSnapshot.child("questionText").getValue(String.class);
        String option1 = questionSnapshot.child("options").child("0").getValue(String.class);
        String option2 = questionSnapshot.child("options").child("1").getValue(String.class);
        String option3 = questionSnapshot.child("options").child("2").getValue(String.class);
        String option4 = questionSnapshot.child("options").child("3").getValue(String.class);
        correctAnswerIndex = questionSnapshot.child("correctAnswerIndex").getValue(Integer.class);

        // Set text to TextViews
        questionTextView.setText(questionText);
        option1TextView.setText(option1);
        option2TextView.setText(option2);
        option3TextView.setText(option3);
        option4TextView.setText(option4);

        // Check if there's a stored answer for this question

    }
}