package com.harsha.make_your_test;

import static com.harsha.make_your_test.Register.RC_SIGN_IN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class First extends AppCompatActivity {
    TextView enter, das, taketest, maketest;
    EditText codeEditText;
    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign In
        mGoogleSignInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build());

        enter = findViewById(R.id.enter);
        das = findViewById(R.id.das);
        taketest = findViewById(R.id.taketaest);
        maketest = findViewById(R.id.maktest);
        codeEditText = findViewById(R.id.enter);

        enter.setVisibility(View.INVISIBLE);
        das.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        taketest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enter.setVisibility(View.VISIBLE);
                das.setVisibility(View.VISIBLE);
            }
        });

        das.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser == null) {
                    // User is not signed in, initiate Google Sign-In
                    signInWithGoogle();
                } else {
                    // User is signed in, proceed with exam
                    proceedToExam();

                }
            }
        });

        maketest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(First.this, Register.class);
                startActivity(in);
                finish();
            }
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken(), resultCode);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken, int resultCode){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(First.this,"sucessfully logged in",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void proceedToExam() {
        // Get the entered code from the EditText
        String enteredCode = codeEditText.getText().toString();

        if (enteredCode.isEmpty()) {
            Toast.makeText(First.this, "Please enter a code", Toast.LENGTH_SHORT).show();
            return; // Exit the method if the code is empty
        }

        Intent intent = new Intent(First.this, Exam.class);
        intent.putExtra("enteredCode", enteredCode);
        startActivity(intent);
        finish();
    }
}
