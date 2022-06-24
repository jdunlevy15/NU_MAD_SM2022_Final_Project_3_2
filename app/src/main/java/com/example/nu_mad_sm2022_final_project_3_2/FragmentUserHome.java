package com.example.nu_mad_sm2022_final_project_3_2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
    private User currUser;
    private IUserHomeListener listener;

    // UI elements
    TextView nameHeader;
    Button buttonViewDogsForAdoption, buttonViewDogsForFoster, buttonViewAppStatus, buttonVolunteer;

    public FragmentUserHome() {
        // Required empty public constructor
    }

    public static FragmentUserHome newInstance(User user) {
        FragmentUserHome fragment = new FragmentUserHome();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER_ID, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof IUserHomeListener) {
            listener = (IUserHomeListener) context;
        }

        else {
            throw new RuntimeException(context.toString() + " IFosterHomeListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currUser = (User) getArguments().getSerializable(ARG_USER_ID);
            Log.d("user", "in fragment-user-home on create: " + currUser.getFirstName());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_user_home, container, false);

        // Set up ui elements
        nameHeader = view.findViewById(R.id.userHomeUserName);
        nameHeader.setText(currUser.getFirstName());
        buttonViewDogsForAdoption = view.findViewById(R.id.buttonUserHomeScreenAdopt);
        buttonViewAppStatus = view.findViewById(R.id.buttonUserViewStatus);
        buttonVolunteer = view.findViewById(R.id.buttonUserVolunteer);
        buttonViewDogsForFoster = view.findViewById(R.id.buttonUserHomeScreenFoster);


        buttonViewDogsForAdoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onViewDogAdoptProfilesPress();
            }
        });

        buttonViewDogsForFoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onViewDogFosterProfilesPress();
            }
        });

        buttonViewAppStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onViewApplicationPress();
            }
        });

        buttonVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onVolunteerPress();
            }
        });

        return view;
    }

    public interface IUserHomeListener {
        void onViewDogAdoptProfilesPress();
        void onViewDogFosterProfilesPress();
        void onViewApplicationPress();
        void onVolunteerPress();
    }
}