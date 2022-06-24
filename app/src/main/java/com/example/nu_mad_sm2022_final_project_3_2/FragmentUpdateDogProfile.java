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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentUpdateDogProfile extends Fragment {

    // debug tag
    private final String TAG = "dog";

    // firebase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    // data sending
    private FragmentCreateDogProfile.ICreateDogListener cListener;

    // view items needed
    private EditText dogNameET, dogAgeET, dogBreedET, dogColorET, dogCurrentSizeET, dogPotentialSizeET;
    private RadioGroup dogSexRG, dogStatusRG, dogObedienceTraningRG, dogHouseTrainingRG, dogFenceRequiredRG;
    private RadioGroup dogExerciseNeedsRG, dogExperienceNeedsRG, dogSheddingAmountRG, dogGroomingNeedsRG, dogReactionRG;
    private Button submitButton;

    // values to be inputted by user
    private Gender dogSex = null;
    private DogStatus dogStatus= null;
    private Training dogObedienceTraining = null;
    private Needs dogExerciseNeeds = null;
    private Needs dogExperienceNeeds = null;
    private Needs dogGroomingNeeds = null;
    private Needs dogSheddingAmount = null;
    private Reaction dogReaction = null;
    private boolean houseSet = false;
    private boolean houseTrained = false;
    private boolean fenceSet = false;
    private boolean fenceRequired = false;

    // dog id
    private String dogID = null;

    public FragmentUpdateDogProfile() {
        // Required empty public constructor
    }

    public static FragmentUpdateDogProfile newInstance() {
        FragmentUpdateDogProfile fragment = new FragmentUpdateDogProfile();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof FragmentCreateDogProfile.ICreateDogListener) {
            cListener = (FragmentCreateDogProfile.ICreateDogListener) context;
        } else {
            throw new RuntimeException(context.toString() + "Must Implement Interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.create_dog_profile, container, false);

        // init view items
        dogNameET = view.findViewById(R.id.editTextDogName);
        dogAgeET = view.findViewById(R.id.editTextCreateDogAge);
        dogBreedET = view.findViewById(R.id.editTextCreateDogBreed);
        dogColorET = view.findViewById(R.id.editTextCreateColor);
        dogCurrentSizeET = view.findViewById(R.id.editTextCreateDogSize);
        dogPotentialSizeET = view.findViewById(R.id.editTextCreatePotentialSize);
        dogSexRG = view.findViewById(R.id.radioGroupCreateDogSex);
        dogStatusRG = view.findViewById(R.id.radioGroupCreateStatus);
        dogObedienceTraningRG = view.findViewById(R.id.radioGroupCreateDogObedienceTraining);
        dogHouseTrainingRG = view.findViewById(R.id.radioGroupCreateHouseTrained);
        dogFenceRequiredRG = view.findViewById(R.id.radioGroupCreateFenceRequired);
        dogExerciseNeedsRG = view.findViewById(R.id.radioGroupCreateExerciseNeeds);
        dogExperienceNeedsRG = view.findViewById(R.id.radioGroupCreateOwnerExperience);
        dogSheddingAmountRG = view.findViewById(R.id.radioGroupCreateShedding);
        dogGroomingNeedsRG = view.findViewById(R.id.radioGroupCreateGrooming);
        dogReactionRG = view.findViewById(R.id.radionGroupReaction);
        submitButton = view.findViewById(R.id.buttonContinueCreateProfile);

        // firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // check if user even has a dog
        db.collection("users")
                .document(mUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User loggedIn = documentSnapshot.toObject(User.class);
                        if (loggedIn.getDogId() == null) {
                            Toast.makeText(getContext(), "You dont foster a dog", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        db.collection("dogs")
                                .document(loggedIn.getDogId())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Dog myDog = documentSnapshot.toObject(Dog.class);
                                        dogNameET.setText(myDog.getName());
                                        dogAgeET.setText(String.valueOf(myDog.getAge()));
                                        dogBreedET.setText(myDog.getBreed());
                                        dogColorET.setText(myDog.getColor());
                                        dogCurrentSizeET.setText(String.valueOf(myDog.getCurrentWeight()));
                                        dogPotentialSizeET.setText(String.valueOf(myDog.getCurrentWeight()));
                                        dogID = myDog.getId();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "DB Failure", Toast.LENGTH_SHORT).show();
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

        // get data from radio buttons
        dogSexRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButtonCreateMale) {
                    dogSex = Gender.MALE;
                } else {
                    dogSex = Gender.FEMALE;
                }
            }
        });

        dogStatusRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButtonAdopt) {
                    dogStatus = DogStatus.ADOPT;
                } else {
                    dogStatus = DogStatus.FOSTER;
                }
            }
        });

        dogObedienceTraningRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButtonCreateNoneObedience) {
                    dogObedienceTraining = Training.BASIC;
                }
                else if (i == R.id.radioButtonCreateAdvancedObedience) {
                    dogObedienceTraining = Training.WELL;
                }
                else {
                    dogObedienceTraining = Training.NEEDS;
                }
            }
        });

        dogHouseTrainingRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButtonYesHousetrained) {
                    houseSet = true;
                    houseTrained = true;
                }
                else {
                    houseSet = true;
                    houseTrained = false;
                }
            }
        });

        dogFenceRequiredRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButtonCreateYesFence) {
                    fenceSet = true;
                    fenceRequired = true;
                }
                else {
                    fenceSet = true;
                    fenceRequired = false;
                }
            }
        });

        dogExperienceNeedsRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButtonCreateOwnerExperienceNone) {
                    dogExperienceNeeds = Needs.NONE;
                }
                else if (i == R.id.radioButtonCreateOwnerExperienceAdvanced) {
                    dogExperienceNeeds = Needs.HIGH;
                }
                else {
                    dogExperienceNeeds = Needs.LOW;
                }
            }
        });

        dogExerciseNeedsRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButtonCreateExerciseHigh) {
                    dogExerciseNeeds = Needs.HIGH;
                }
                else if (i == R.id.radioButtonCreateExerciseModerate) {
                    dogExerciseNeeds = Needs.MODERATE;
                }
                else {
                    dogExerciseNeeds = Needs.LOW;
                }
            }
        });

        dogGroomingNeedsRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButtonCreateGroomingHigh) {
                    dogGroomingNeeds = Needs.HIGH;
                }
                else if (i == R.id.radioButtonCreateGroomingModerate) {
                    dogGroomingNeeds = Needs.MODERATE;
                }
                else {
                    dogGroomingNeeds = Needs.LOW;
                }
            }
        });

        dogSheddingAmountRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButtonCreateSheddingHigh) {
                    dogSheddingAmount = Needs.HIGH;
                }
                else if (i == R.id.radioButtonCreateSheddingModerate) {
                    dogSheddingAmount = Needs.MODERATE;
                }
                else {
                    dogSheddingAmount = Needs.LOW;
                }
            }
        });

        dogReactionRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButtonReactionCautous) {
                    dogReaction = Reaction.CAUTIOUS;
                }
                else {
                    dogReaction = Reaction.FRIENDLY;
                }
            }
        });

        // when submit button is pressed
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // input validation
                if (!houseSet || !fenceSet) {
                    Toast.makeText(getContext(), "Fill Out All Fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String dogAge = dogAgeET.getText().toString();
                String dogBreed = dogBreedET.getText().toString();
                String dogColor = dogColorET.getText().toString();
                String dogName = dogNameET.getText().toString();
                String dogCurrentSize = dogCurrentSizeET.getText().toString();
                String dogPotentialSize = dogPotentialSizeET.getText().toString();

                if (dogAge.equals("") || dogBreed.equals("") ||
                        dogColor.equals("") || dogName.equals("") ||
                        dogCurrentSize.equals("") || dogPotentialSize.equals("")) {

                    Toast.makeText(getContext(), "Fill Out All Fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int ageInt = Integer.parseInt(dogAge);

                if (dogSex == null || dogStatus == null || dogObedienceTraining == null ||
                        dogExerciseNeeds == null || dogExperienceNeeds == null ||
                        dogGroomingNeeds == null || dogSheddingAmount == null || dogReaction == null) {
                    Toast.makeText(getContext(), "Fill Out All Fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // get dog to set id
                if (dogID == null) {
                    Toast.makeText(getContext(), "Error Getting Dog", Toast.LENGTH_SHORT).show();
                }

                // create dog object
                Dog dogToAdd = new Dog(dogID, dogName, dogBreed, ageInt, dogSex, dogStatus,
                        dogColor, Integer.parseInt(dogCurrentSize), Integer.parseInt(dogPotentialSize), fenceRequired,
                        houseTrained, dogObedienceTraining, dogExerciseNeeds,
                        dogGroomingNeeds, dogSheddingAmount, dogExperienceNeeds,
                        dogReaction);

                // working !
                Log.d(TAG, dogToAdd.toString());

                // add to database
                db.collection("dogs")
                        .document(dogID)
                        .set(dogToAdd)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Successfully Created Dog Profile", Toast.LENGTH_SHORT).show();
                                // go back to the home fragment
                                cListener.backToHomeFragment();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Failed to Create Dog Profile", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return view;
    }
}