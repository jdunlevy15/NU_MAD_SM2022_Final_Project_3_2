package com.example.nu_mad_sm2022_final_project_3_2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentDisplayUserApplication extends Fragment {

    // debug tag
    private final String TAG = "application";

    // firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;

    // view items to hide sicne this is for the user
    private Button approveButton, rejectButton;

    public FragmentDisplayUserApplication() {
        // Required empty public constructor
    }

    public static FragmentDisplayUserApplication newInstance() {
        FragmentDisplayUserApplication fragment = new FragmentDisplayUserApplication();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.view_adoption_application, container, false);

        // init view items
        approveButton = view.findViewById(R.id.applicationApproveButton);
        rejectButton = view.findViewById(R.id.applicationRejectButton);

        // hide approve and reject button since the user shouldnt be able to use them
        approveButton.setEnabled(false);
        approveButton.setVisibility(View.INVISIBLE);
        rejectButton.setEnabled(false);
        rejectButton.setVisibility(View.INVISIBLE);

        return view;
    }
}