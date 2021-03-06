package com.example.nu_mad_sm2022_final_project_3_2;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentApplicationRecyclerView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentApplicationRecyclerView extends Fragment {

    // args
    private static final String ARG_USER = "role";
    Role role;

    // tag
    private final String TAG = "recycle";

    // view items
    private RecyclerView applicationsRecyclerView;
    private RecyclerView.LayoutManager applicationsRecyclerViewLayoutManager;
    private ApplicationsAdapter applicationsAdapter;

    // firebase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public FragmentApplicationRecyclerView() {
        // Required empty public constructor
    }

    public static FragmentApplicationRecyclerView newInstance(Role role) {
        FragmentApplicationRecyclerView fragment = new FragmentApplicationRecyclerView();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, role);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            role = (Role) getArguments().getSerializable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_application_recycler_view, container, false);

        // recycler view setup
        applicationsRecyclerView = view.findViewById(R.id.applicationRecyclerView);
        applicationsRecyclerViewLayoutManager = new LinearLayoutManager(getContext());
        applicationsRecyclerView.setLayoutManager(applicationsRecyclerViewLayoutManager);

        // firebase setup
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // get applications just for this user
        if (role == Role.ADOPTER) {
            db.collection("applications")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<Application> applications = new ArrayList<Application>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Application tempApp = document.toObject(Application.class);
                                    Log.d(TAG, "applicantid: " + tempApp.getUserID());
                                    Log.d(TAG, "user logged in id: " + mUser.getUid());
                                    if (tempApp.getUserID().equals(mUser.getUid())) {
                                        applications.add(tempApp);
                                    }
                                }
                                applicationsAdapter = new ApplicationsAdapter(applications, getContext(), Role.ADOPTER);
                                applicationsRecyclerView.setAdapter(applicationsAdapter);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "error getting applications", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        // get only dogs that this foster parent is an owner of
        else if (role == Role.FOSTER) {
            db.collection("users")
                    .document(mUser.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User loggedIn = documentSnapshot.toObject(User.class);
                            db.collection("applications")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                ArrayList<Application> applications = new ArrayList<Application>();
                                                if (loggedIn.getDogId() != null) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Application tempApp = document.toObject(Application.class);
                                                        if (tempApp.getDogID().equals(loggedIn.getDogId()) && tempApp.getStatus().equals(ApplicationStatus.PENDING)) {
                                                            applications.add(tempApp);
                                                        }
                                                    }
                                                }
                                                else {
                                                    Toast.makeText(getContext(), "You dont foster a dog", Toast.LENGTH_SHORT).show();
                                                }
                                                applicationsAdapter = new ApplicationsAdapter(applications, getContext(), Role.FOSTER);
                                                applicationsRecyclerView.setAdapter(applicationsAdapter);
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "error getting applications", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "DB Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        // get all ACTIVE applications
        else {
            db.collection("applications")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<Application> applications = new ArrayList<Application>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Application tempApp = document.toObject(Application.class);
                                    if (tempApp.getStatus().equals(ApplicationStatus.PENDING)) {
                                        applications.add(tempApp);
                                    }
                                    Log.d(TAG, "onComplete: " + tempApp.toString());
                                }
                                applicationsAdapter = new ApplicationsAdapter(applications, getContext(), Role.EMPLOYEE);
                                applicationsRecyclerView.setAdapter(applicationsAdapter);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "error getting applications", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        return view;
    }
}