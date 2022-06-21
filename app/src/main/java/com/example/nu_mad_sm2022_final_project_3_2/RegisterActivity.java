package com.example.nu_mad_sm2022_final_project_3_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    EditText editTextFirstName, editTextLastName, editTextAge, editTextAddress, editTextCity,
            editTextState, editTextEmail, editTextPhone, editTextUsername, editTextPassword,
            editTextConfirmPassword;

    RadioGroup radioGroupAccountType;
    RadioButton radioButtonApplicant, radioButtonFoster, radioButtonEmployee, radioButtonParent;
    Role accountType = null;

    Button buttonSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        setTitle("Register");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Set up layout items
        editTextFirstName = findViewById(R.id.applicationFirstNameEditText);
        editTextLastName = findViewById(R.id.applicationLastNameEditText);
        editTextAge = findViewById(R.id.applicationAgeEditText);
        editTextAddress = findViewById(R.id.applicationAddressEditText);
        editTextCity = findViewById(R.id.applicationCityEditText);
        editTextState = findViewById(R.id.applicationStateEditText);
        editTextEmail = findViewById(R.id.applicationEmailEditText);
        editTextPhone = findViewById(R.id.applicationPhoneEditText);
        editTextUsername = findViewById(R.id.accountUsernameEditText);
        editTextPassword = findViewById(R.id.accountPasswordEditText);
        editTextConfirmPassword = findViewById(R.id.accountConfirmPasswordEditText);
        radioGroupAccountType = findViewById(R.id.accountTypeRadioGroup);
        radioButtonEmployee = findViewById(R.id.accountEmployeeRadioButton);
        radioButtonFoster = findViewById(R.id.accountFosterParentRadioButton);
        radioButtonApplicant = findViewById(R.id.accountApplicantRadioButton);
        radioButtonParent = findViewById(R.id.accountParentRadioButton);
        buttonSignUp = findViewById(R.id.accountSignUpButton);

        radioGroupAccountType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.accountEmployeeRadioButton) {
                    Log.d("signup", "employee radio button pressed");
                    accountType = Role.EMPLOYEE;
                } else if (checkedId == R.id.accountFosterParentRadioButton) {
                    Log.d("signup", "foster radio button pressed");
                    accountType = Role.FOSTER;
                } else if (checkedId == R.id.accountApplicantRadioButton) {
                    Log.d("signup", "user radio button pressed");
                    accountType = Role.APPLICANT;
                } else {
                    Log.d("signup", "adopter radio button pressed");
                    accountType = Role.ADOPTER;
                }
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("signup", "Button pressed");
                // Confirm valid inputs (no empty fields)
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String age = editTextAge.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();
                String city = editTextCity.getText().toString().trim();
                String state = editTextState.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();



                if (firstName.equals("") || lastName.equals("") || age.equals("") || address.equals("") ||
                        city.equals("") || state.equals("") || email.equals("") || phone.equals("") ||
                        username.equals("") || password.equals("") || confirmPassword.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please make sure all fields are filled.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (accountType == null) {
                    Toast.makeText(RegisterActivity.this, "Please select an account type.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Check both password entries match
                if (!editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match!", Toast.LENGTH_LONG).show();
                    editTextPassword.setText("");
                    editTextConfirmPassword.setText("");
                    return;
                }

                Log.d("signup", "valid input");

                // TODO: sign up user in fire base, Create new user object, add to db
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("signup", "successful register");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = mAuth.getUid();

                            Address userAddress = new Address(address, city, state);
                            int ageInt = Integer.parseInt(age);

                            // Create new user with the new id
                            User thisUser = new User(userId, firstName, lastName, ageInt, userAddress,
                                    phone, email, username, accountType);

                            // Add new user to db
                            db.collection("users")
                                    .document(userId)
                                    .set(thisUser)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("signup", "successful add to db");
                                            Toast.makeText(RegisterActivity.this, "User added to db!", Toast.LENGTH_LONG).show();
                                            // TODO: start "main" activity, and sign the user in???
                                            Intent intent = new Intent(RegisterActivity.this, AppActivity.class);
                                            // intent.putExtra(USER_KEY, user.getUid());
                                            startActivity(intent);


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // TODO: how to handle authentication created but unable to add to db?
                                        }
                                    });


                            // Sign in, go to main activity

                        } else{
                            Toast.makeText(RegisterActivity.this, "Unable to register new user.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}