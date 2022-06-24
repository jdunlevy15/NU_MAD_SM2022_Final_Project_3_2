package com.example.nu_mad_sm2022_final_project_3_2;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Text;

public class FragmentDisplayApplication extends Fragment {

    // arguments
    private static final String ARG_USER = "role";
    private static final String ARG_APP = "application";
    Application thisApplication;
    Role role;

    // debug tag
    private final String TAG = "application";

    // firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;

    // view items to hide sicne this is for the user
    private Button approveButton, rejectButton, backButton;
    private TextView headerTV, firstNameET, lastNameET, ageET, addressET, cityET, stateET,
            emailET, phoneET, question1ET, question2ET, question3ET, question4ET, question5ET, dogLabelET;
    private TextView statusTV;

    // activity communication
    private IDisplayApplicationListener dListener;

    public FragmentDisplayApplication() {
        // Required empty public constructor
    }

    public static FragmentDisplayApplication newInstance(Role role, Application application) {
        FragmentDisplayApplication fragment = new FragmentDisplayApplication();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, role);
        args.putSerializable(ARG_APP, application);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            role = (Role) getArguments().getSerializable(ARG_USER);
            thisApplication = (Application) getArguments().getSerializable(ARG_APP);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IDisplayApplicationListener) {
            dListener = (IDisplayApplicationListener) context;
        } else {
            throw new RuntimeException(context.toString() + "Must Implement Interface");
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
        question3ET = view.findViewById(R.id.applicationQuestion3EditText);
        question4ET = view.findViewById(R.id.applicationQuestion4EditText);
        question5ET = view.findViewById(R.id.applicationQuestion5EditText);
        statusTV = view.findViewById(R.id.applicationStatusLabel);
        dogLabelET = view.findViewById(R.id.dogName);
        backButton = view.findViewById(R.id.applicationBackButton);
        headerTV = view.findViewById(R.id.applicationHeader);

        // firebase
        db = FirebaseFirestore.getInstance();
;
        // hide approve and reject button since the user shouldnt be able to use them
        if (role == Role.ADOPTER) {
            approveButton.setEnabled(false);
            approveButton.setVisibility(View.INVISIBLE);
            rejectButton.setEnabled(false);
            rejectButton.setVisibility(View.INVISIBLE);
        }

        // fill in based on application
        if (thisApplication.getApplicationType().equals(DogStatus.ADOPT)) {
            headerTV.setText(R.string.application_adopt_header);
        } else if (thisApplication.getApplicationType().equals(DogStatus.FOSTER)){
            headerTV.setText(R.string.application_foster_header);
        }

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
        question3ET.setText(thisApplication.getQuestion3());
        question4ET.setText(thisApplication.getQuestion4());
        question5ET.setText(thisApplication.getQuestion5());

        // get dog info for title
        db.collection("dogs")
                .document(thisApplication.getDogID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Dog thisDog = documentSnapshot.toObject(Dog.class);
                        if (thisDog != null) {
                            dogLabelET.setText(thisDog.getName());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "DB Rerieval Failure", Toast.LENGTH_SHORT).show();
                    }
                });

        // set approval onclick
        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ensure applicant doesn't currently have a foster dog ??



                // Mark application as accepted
                thisApplication.setStatus(ApplicationStatus.ACCEPTED);
                db.collection("applications")
                        .document(thisApplication.getApplicationID())
                        .set(thisApplication)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "application succsesfully approved");
                                dListener.backToRecyclerView(role);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Failure to Approve Applicant", Toast.LENGTH_SHORT).show();
                            }
                        });

                // If foster application, update user to foster type, and update dog's foster field
                DocumentReference userRef = db.collection("users")
                        .document(thisApplication.getUserID());

                if(thisApplication.getApplicationType().equals(DogStatus.FOSTER)) {
                    userRef.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    User applicant = documentSnapshot.toObject(User.class);
                                    userRef.update("role", Role.FOSTER)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    userRef.update("dogId", thisApplication.getDogID())
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    // Update the dog's owner field to the approved applicant
                                                                    DocumentReference dogRef = db.collection("dogs")
                                                                            .document(thisApplication.getDogID());

                                                                    dogRef.update("fosterParent", applicant)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    dogRef.update("owner", null)
                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    dogRef.update("status", DogStatus.ADOPT);
                                                                                                }
                                                                                            })
                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                @Override
                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                    Toast.makeText(getContext(), "Unable to update dog's owner field in db.", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            });
                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    e.printStackTrace();
                                                                                    Toast.makeText(getContext(), "Unable to update dog's foster field in db.", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    e.printStackTrace();
                                                                    Toast.makeText(getContext(), "Unable to update user's dogId field", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(getContext(), "Unable to update user to FOSTER role.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Unable to find user in db", Toast.LENGTH_SHORT).show();
                                }
                            });



                } else if (thisApplication.getApplicationType().equals(DogStatus.ADOPT)) {
                    // If adoption application, change the user type back to normal user
                    // Update the dogs foster field to null, owner field to the approved applicant
                    userRef.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    User applicant = documentSnapshot.toObject(User.class);
                                    Role newRole = Role.APPLICANT;
                                    if (applicant.getRole().equals(Role.EMPLOYEE)) {
                                        newRole = Role.EMPLOYEE;
                                    }
                                    userRef.update("role", newRole)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    userRef.update("dogId", thisApplication.getDogID())
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    DocumentReference dogRef = db.collection("dogs")
                                                                            .document(thisApplication.getDogID());
                                                                    dogRef
                                                                            .get()
                                                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                        @Override
                                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                            Dog dog = documentSnapshot.toObject(Dog.class);

                                                                                            dogRef.update("fosterParent", null)
                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void unused) {
                                                                                                            dogRef.update("owner", applicant)
                                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onSuccess(Void unused) {
                                                                                                                            DocumentReference fosterRef = db.collection("users").document(dog.getFosterParent().getId());
                                                                                                                            Role newRole = Role.APPLICANT;
                                                                                                                            if (dog.getFosterParent().getRole().equals(Role.EMPLOYEE)) {
                                                                                                                                newRole = Role.EMPLOYEE;
                                                                                                                            }

                                                                                                                            fosterRef.update("role", newRole)
                                                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                        @Override
                                                                                                                                        public void onSuccess(Void unused) {
                                                                                                                                            //TODO: change to fosterDogId?
                                                                                                                                            fosterRef.update("dogId", null);
                                                                                                                                        }
                                                                                                                                    });

                                                                                                                        }
                                                                                                                    })
                                                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                                                        @Override
                                                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                                                            Toast.makeText(getContext(), "Unable to update dog's owner field in db.", Toast.LENGTH_SHORT).show();
                                                                                                                        }
                                                                                                                    });
                                                                                                        }
                                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                                        @Override
                                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                                            e.printStackTrace();
                                                                                                            Toast.makeText(getContext(), "Unable to update dog's foster field in db.", Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                    });


                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    e.printStackTrace();
                                                                    Toast.makeText(getContext(), "Unable to update user's dogId field", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(getContext(), "Unable to update user to APPLICANT role.", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Unable to find user in db", Toast.LENGTH_SHORT).show();
                                }
                            });

                }



                //
            }
        });

        // set rejection on click
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisApplication.setStatus(ApplicationStatus.REJECTED);
                db.collection("applications")
                        .document(thisApplication.getApplicationID())
                        .set(thisApplication)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "application succsesfully rejected");
                                dListener.backToRecyclerView(role);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Failure to Reject Applicant", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // nav using back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dListener.backToRecyclerView(role);
            }
        });

        return view;
    }

    public interface IDisplayApplicationListener {
        void backToRecyclerView(Role role);
        void onApplicationApproved(Application app, User user);
    }
}