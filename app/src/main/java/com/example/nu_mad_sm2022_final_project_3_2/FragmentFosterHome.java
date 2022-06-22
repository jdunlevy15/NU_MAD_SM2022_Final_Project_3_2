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
 * Use the {@link FragmentFosterHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFosterHome extends Fragment {
    private static final String ARG_USER_ID = "user_id";
    private User currUser;
    private IFosterHomeListener listener;

    TextView nameHeader;
    Button buttonViewDogsForAdoption, buttonUpdateDog, buttonViewApplications;

    public FragmentFosterHome() {
        // Required empty public constructor
    }


    public static FragmentFosterHome newInstance(User user) {
        FragmentFosterHome fragment = new FragmentFosterHome();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER_ID, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof FragmentFosterHome.IFosterHomeListener) {
            listener = (FragmentFosterHome.IFosterHomeListener) context;
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
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_foster_home, container, false);

        nameHeader = view.findViewById(R.id.textViewFosterName);
        nameHeader.setText(currUser.getFirstName());

        buttonUpdateDog = view.findViewById(R.id.buttonFosterUpdateProfiles);
        buttonViewApplications = view.findViewById(R.id.buttonFosterViewApplications);
        buttonViewDogsForAdoption = view.findViewById(R.id.buttonFosterViewDogs);

        // TODO: set button on click listeners
        buttonViewDogsForAdoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onViewDogProfilesPress();
            }
        });

        return view;
    }

    public interface IFosterHomeListener {
        void onViewDogProfilesPress();
    }
}