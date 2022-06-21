package com.example.nu_mad_sm2022_final_project_3_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AppActivity extends AppCompatActivity implements FragmentEmployeeHome.IEmployeeHomeListener, FragmentCreateDogProfile.ICreateDogListener {
    // String userId;

    // Firebase Authentication / db
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;
    private User currUser;

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

        // TODO: get the actual user from the db, start home screen
        updateUser();


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
                                // TODO: check for user type (once different user home screens are implemented)
                                Fragment fragment;
                                if (user.getRole() == Role.APPLICANT) {
                                    fragment = FragmentUserHome.newInstance(user);
                                } else if (user.getRole() == Role.EMPLOYEE) {
                                    fragment = FragmentEmployeeHome.newInstance(user);
                                } else {
                                    fragment = FragmentFosterHome.newInstance(user);
                                }

                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragmentContainerAppActivity,
                                                fragment,
                                                "user-home-screen").commit();
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
    public void backToHomeFragment() {
        // send back to home fragment
    }
}