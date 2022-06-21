package com.example.nu_mad_sm2022_final_project_3_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    EditText editTextEmail, editTextPassword;
    Button buttonSignIn, buttonCreateAccount;

    final static String USER_KEY = "authorized-user";

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //reload();
            // TODO: go to "main" activity
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.loginEmailEditText);
        editTextPassword = findViewById(R.id.loginPasswordEditText);
        buttonSignIn = findViewById(R.id.loginSignInButton);
        buttonCreateAccount = findViewById(R.id.loginCreateAccountButton);

        // TODO: sign in using firebase auth, start the "main" activity given the current user
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for valid email and password input
                String inputEmail = editTextEmail.getText().toString();
                String inputPassword = editTextPassword.getText().toString();

                if (inputEmail.trim().equals("")) {
                    Toast.makeText(SignInActivity.this, "Please enter your email address", Toast.LENGTH_LONG).show();
                    return;
                }

                if (inputPassword.trim().equals("")) {
                    Toast.makeText(SignInActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    return;
                }

                // Sign in with firebase auth
                mAuth.signInWithEmailAndPassword(inputEmail, inputPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // TODO: start main activity with this user!
                                    Intent intent = new Intent(SignInActivity.this, AppActivity.class);
                                    // intent.putExtra(USER_KEY, user.getUid());
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(SignInActivity.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        // TODO: start create account activity
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}