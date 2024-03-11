package com.harsha.make_your_test;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FirebaseRepository {

    private static final String TAG = "FirebaseRepository";
    private DatabaseReference databaseReference;

    public FirebaseRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    public void saveQuestion(String questionText, List<String> options, int correctAnswerIndex, String enteredCode) {
        if (databaseReference == null || enteredCode == null || enteredCode.isEmpty()) {
            // Firebase database reference not initialized properly
            Log.e(TAG, "Firebase database reference not initialized properly");
            return;
        }

        // Reference the questions node under the entered code
        DatabaseReference enteredCodeRef = databaseReference.child(enteredCode).child("questions");

        String key = enteredCodeRef.push().getKey();
        if (key == null || key.isEmpty()) {
            Log.e(TAG, "Error generating unique key");
            return;
        }

        Question question = new Question(questionText, options, correctAnswerIndex);
        enteredCodeRef.child(key).setValue(question)
                .addOnSuccessListener(aVoid -> {
                    // Handle success
                    Log.d(TAG, "Question saved successfully");
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Log.e(TAG, "Failed to save question: " + e.getMessage(), e);
                    // Log the error or show an error message to the user
                });
    }
}
