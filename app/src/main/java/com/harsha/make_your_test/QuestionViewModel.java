package com.harsha.make_your_test;

import androidx.lifecycle.ViewModel;

import java.util.List;

public class QuestionViewModel extends ViewModel {
    private FirebaseRepository firebaseRepository;

    public QuestionViewModel() {
        firebaseRepository = new FirebaseRepository();
    }

    public void saveQuestion(String questionText, List<String> options, int correctAnswerIndex, String enteredCode) {
        firebaseRepository.saveQuestion(questionText, options, correctAnswerIndex,enteredCode);
    }
}
