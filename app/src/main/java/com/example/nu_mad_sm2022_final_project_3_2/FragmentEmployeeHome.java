package com.example.nu_mad_sm2022_final_project_3_2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentEmployeeHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentEmployeeHome extends Fragment {
    private static final String ARG_USER_ID = "user_id";
    private User currUser;

    TextView nameHeader;
    Button buttonViewDogsForAdoption, getButtonViewDogsForFoster, buttonAddDog, buttonViewApplications;
    IEmployeeHomeListener eListener;

    public FragmentEmployeeHome() {
        // Required empty public constructor
    }


    public static FragmentEmployeeHome newInstance(User user) {
        FragmentEmployeeHome fragment = new FragmentEmployeeHome();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER_ID, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currUser = (User) getArguments().getSerializable(ARG_USER_ID);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof IEmployeeHomeListener) {
            eListener = (IEmployeeHomeListener) context;
        }

        else {
            throw new RuntimeException(context.toString() + "Must Implement Interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_employee_home, container, false);

        nameHeader = view.findViewById(R.id.textViewEmployeeName);
        nameHeader.setText(currUser.getFirstName());
        buttonAddDog = view.findViewById(R.id.buttonEmployeeAddDog);
        buttonViewApplications = view.findViewById(R.id.buttonEmployeeViewApplications);
        buttonViewDogsForAdoption = view.findViewById(R.id.buttonEmployeeHomescreenViewDogs);
        getButtonViewDogsForFoster = view.findViewById(R.id.buttonEmployeeHomescreenViewDogsFoster);

        // TODO: button on click listeners (delegate to app activity to start new fragment)
        buttonAddDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eListener.onCreateDogProfilePress();
            }
        });

        buttonViewDogsForAdoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eListener.onViewDogAdoptProfilesPress();
            }
        });

        getButtonViewDogsForFoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eListener.onViewDogFosterProfilesPress();
            }
        });

        buttonViewApplications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eListener.onViewApplicationsPress();
            }
        });


        return view;
    }

    public interface IEmployeeHomeListener {
        void onCreateDogProfilePress();
        void onViewDogAdoptProfilesPress();
        void onViewDogFosterProfilesPress();
        void onViewApplicationsPress();
    }
}