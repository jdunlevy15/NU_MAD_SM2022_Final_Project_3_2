package com.example.nu_mad_sm2022_final_project_3_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start sign in activity
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);

    }
}