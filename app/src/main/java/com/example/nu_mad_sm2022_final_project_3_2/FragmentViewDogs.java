package com.example.nu_mad_sm2022_final_project_3_2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FragmentViewDogs extends Fragment {
    // Firebase
    private FirebaseAuth mAuth;
    // private FirebaseUser mUser;
    private FirebaseFirestore db;
    private Dogs dogs;
    private DogStatus status;
    private RecyclerView recyclerViewDogProfiles;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private DogProfileAdapter dogProfileAdapter;

    private static final String ARG_STATUS = "param_status";

    public FragmentViewDogs() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment FragmentViewDogs.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentViewDogs newInstance(DogStatus status) {
        FragmentViewDogs fragment = new FragmentViewDogs();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = (DogStatus) getArguments().getSerializable(ARG_STATUS);
        }

        // Initialize firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        dogs = new Dogs();

        // Update dogs
        updateDogs();
    }


    private void updateDogs() {
        Log.d("dogs", "updaing the dogs!");
        db.collection("dogs")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d("dogs", "got the dogs!");
                        ArrayList<Dog> allDogs = new ArrayList<Dog>(queryDocumentSnapshots.toObjects(Dog.class));
                        ArrayList<Dog> statusDogs = new ArrayList<>();
                        for (Dog dog : allDogs) {
                            if (dog.getStatus().equals(status)) {
                                statusDogs.add(dog);
                            }
                        }
                        dogs.setDogs(statusDogs);
                        dogProfileAdapter.updateDogs(dogs.getDogs());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),
                                "Unable to get dog profiles from database results may not be up to date",
                                Toast.LENGTH_LONG ).show();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_dogs, container, false);

        // Set up recycler view
        recyclerViewDogProfiles = view.findViewById(R.id.recyclerViewDogProfiles);
        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        dogProfileAdapter = new DogProfileAdapter(dogs, getContext());
        recyclerViewDogProfiles.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewDogProfiles.setAdapter(dogProfileAdapter);




        // Update dogs when they change in the database
        CollectionReference dogsCollection = db.collection("dogs");
        dogsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    dogsCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            ArrayList<Dog> newDogs = new ArrayList<Dog>(queryDocumentSnapshots.toObjects(Dog.class));
                            ArrayList<Dog> statusDogs = new ArrayList<>();
                            for (Dog dog : newDogs) {
                                if (dog.getStatus().equals(status)) {
                                    statusDogs.add(dog);
                                }
                            }
                            dogs.setDogs(statusDogs);
                            dogProfileAdapter.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Dog Profiles may not be up to date!", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });

        return view;
    }
}