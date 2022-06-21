package com.example.nu_mad_sm2022_final_project_3_2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentUserHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUserHome extends Fragment {
    private static final String ARG_USER_ID = "user_id";
    private String userID;

    // UI elements
    TextView nameHeader;
    Button buttonViewDogsForAdoption, buttonViewAppStatus, buttonVolunteer;

    public FragmentUserHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FragmentUserHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentUserHome newInstance(String userID) {
        FragmentUserHome fragment = new FragmentUserHome();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getString(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_user_home, container, false);

        // Set up ui elements
        nameHeader = view.findViewById(R.id.userHomeUserName);
        buttonViewDogsForAdoption = view.findViewById(R.id.buttonUserHomeScreenAdopt);
        buttonViewAppStatus = view.findViewById(R.id.buttonUserAppStatus);
        buttonVolunteer = view.findViewById(R.id.buttonUserVolunteer);

        // TODO: update name header to show user's first name
        // nameHeader.setText(user.getFirstName());

        // TODO: button on click listeners (delegate to app activity to start new fragment)

        return view;
    }
}