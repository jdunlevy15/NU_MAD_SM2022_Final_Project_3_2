package com.example.nu_mad_sm2022_final_project_3_2;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class FragmentDisplayApplication extends Fragment {

    // arguments
    private static final String ARG_USER = "user_type";
    private static final String ARG_APP = "application";
    Application thisApplication;
    boolean isUser;

    // debug tag
    private final String TAG = "application";

    // firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;

    // view items to hide sicne this is for the user
    private Button approveButton, rejectButton;
    private TextView firstNameET, lastNameET, ageET, addressET, cityET, stateET,
            emailET, phoneET, question1ET, question2ET;
    private TextView statusTV;

    public FragmentDisplayApplication() {
        // Required empty public constructor
    }

    public static FragmentDisplayApplication newInstance(boolean userType, Application application) {
        FragmentDisplayApplication fragment = new FragmentDisplayApplication();
        Bundle args = new Bundle();
        args.putBoolean(ARG_USER, userType);
        args.putSerializable(ARG_APP, application);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isUser = getArguments().getBoolean(ARG_USER);
            thisApplication = (Application) getArguments().getSerializable(ARG_APP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.view_adoption_application, container, false);

        // init view items
        approveButton = view.findViewById(R.id.applicationApproveButton);
        rejectButton = view.findViewById(R.id.applicationRejectButton);
        firstNameET = view.findViewById(R.id.applicationFirstNameEditText);
        lastNameET = view.findViewById(R.id.applicationLastNameEditText);
        ageET = view.findViewById(R.id.applicationAgeEditText);
        addressET = view.findViewById(R.id.applicationAddressEditText);
        cityET = view.findViewById(R.id.applicationCityEditText);
        stateET = view.findViewById(R.id.applicationStateEditText);
        emailET = view.findViewById(R.id.applicationEmailEditText);
        phoneET = view.findViewById(R.id.applicationPhoneEditText);
        question1ET = view.findViewById(R.id.applicationQuestion1EditText);
        question2ET = view.findViewById(R.id.applicationQuestion2EditText);
        statusTV = view.findViewById(R.id.applicationStatusLabel);

        // hide approve and reject button since the user shouldnt be able to use them
        if (isUser) {
            approveButton.setEnabled(false);
            approveButton.setVisibility(View.INVISIBLE);
            rejectButton.setEnabled(false);
            rejectButton.setVisibility(View.INVISIBLE);
        }

        // fill in based on application
        ApplicationStatus applicationStatus = thisApplication.getStatus();
        statusTV.setText(applicationStatus.toString());
        if (applicationStatus == ApplicationStatus.ACCEPTED) {
            statusTV.setTextColor(Color.GREEN);
        }
        if (applicationStatus == ApplicationStatus.REJECTED) {
            statusTV.setTextColor(Color.RED);
        }
        firstNameET.setText(thisApplication.getFirstName());
        lastNameET.setText(thisApplication.getLastName());
        ageET.setText(String.valueOf(thisApplication.getAge()));
        Address thisAddress = thisApplication.getAddress();
        addressET.setText(thisAddress.getAddress());
        cityET.setText(thisAddress.getCity());
        stateET.setText(thisAddress.getState());
        emailET.setText(thisApplication.getEmail());
        phoneET.setText(thisApplication.getPhone());
        question1ET.setText(thisApplication.getQuestion1());
        question2ET.setText(thisApplication.getQuestion2());

        return view;
    }
}