package com.example.nu_mad_sm2022_final_project_3_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AppActivity extends AppCompatActivity {
    // String userId;

    // Firebase Authentication / db
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

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

        // TODO: get the actual user from the db

        // Begin home-screen fragment
        // TODO: check for user type (once different user home screens are implemented)
        // ALSO TODO: send fragments the user object instead of just the id???
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerAppActivity,
                        FragmentUserHome.newInstance(mUser.getUid()),
                        "user-home-screen").commit();
    }
}