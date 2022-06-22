package com.example.nu_mad_sm2022_final_project_3_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AppActivity extends AppCompatActivity implements FragmentEmployeeHome.IEmployeeHomeListener, FragmentCreateDogProfile.ICreateDogListener, DogProfileAdapter.IDogProfileAdapterListener, FragmentFosterHome.IFosterHomeListener, FragmentUserHome.IUserHomeListener {
    // String userId;

    // Firebase Authentication / db
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;
    private User currUser;

    // Buttons
    private Button buttonHome;
    private Button buttonLogout;

    private boolean isStarting = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        setTitle("User Home");

        // Initial auth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // If user not authorized, return to sign in activity
        // TODO: check if null is the right thing to check for?
        if (mAuth == null) {
            Toast.makeText(AppActivity.this, "ERROR: App started with no authorized user!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AppActivity.this, SignInActivity.class);
            startActivity(intent);
        }

        // Initially start loading fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerAppActivity,
                        FragmentLoading.newInstance(),
                        "fragment-loading")
                .addToBackStack(null)
                .commit();

        updateUser();

        // Set on click listeners
        buttonHome = findViewById(R.id.buttonAppHome);
        buttonLogout = findViewById(R.id.buttonAppLogout);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginHomeFragment();
            }
        });

        // TODO: set logout listener
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignIn = new Intent(AppActivity.this, SignInActivity.class);
                mAuth.signOut();
                startActivity(toSignIn);
            }
        });


    }

    private void beginHomeFragment() {
        Fragment fragment;
        if (currUser.getRole() == Role.APPLICANT) {
            fragment = FragmentUserHome.newInstance(currUser);
        } else if (currUser.getRole() == Role.EMPLOYEE) {
            fragment = FragmentEmployeeHome.newInstance(currUser);
        } else {
            fragment = FragmentFosterHome.newInstance(currUser);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerAppActivity,
                        fragment,
                        "user-home-screen").commit();

    }

    private void beginViewDogProfilesFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerAppActivity,
                        FragmentViewDogs.newInstance(),
                        "view-dog-profiles").commit();
    }


    /**
     * Update's the activities current authorized user object from firebase db
     */
    private void updateUser() {
        db.collection("users")
                .document(mUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            User user = document.toObject(User.class);
                            currUser = user;

                            Log.d("user", currUser.getFirstName());
                            // Toast.makeText(AppActivity.this, user.getFirstName(), Toast.LENGTH_SHORT).show();

                            // Load home screen if necessary
                            if(isStarting) {
                                isStarting = false;
                                // Begin home-screen fragment
                                beginHomeFragment();
                            }

                        } else {
                            Log.d("user", "task unsuccessful");
                        }
                    }
                });
    }

    @Override
    public void onCreateDogProfilePress() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerAppActivity,
                        FragmentCreateDogProfile.newInstance(),
                        "create-dog-profile").commit();
    }

    @Override
    public void onViewDogProfilesPress() {
        beginViewDogProfilesFragment();
    }

    @Override
    public void backToHomeFragment() {
        beginHomeFragment();
    }

    // Sends user back to home screen after viewing dog profiles
    @Override
    public void onBackButtonPressed() {
        beginHomeFragment();
    }

    // Sends user to adoption application for the specified dog
    @Override
    public void onAdoptButtonPressed(Dog toAdopt) {
        // TODO: start adopt dog fragment
    }
}