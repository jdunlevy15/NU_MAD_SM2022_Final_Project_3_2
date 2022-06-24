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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCreateApplication#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCreateApplication extends Fragment {

    // aruguments
    private static final String ARG_DOG_ID = "dog_id";
    private Dog currDog;

    // debug tag
    private final String TAG = "dog";

    // appactivity communication
    private IApplicationListener aListener;

    // firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;

    // declare view items
    private TextView statusTV, dogNameTV, headerTV;
    private EditText firstNameET, lastNameET, ageET, addressET, cityET, stateET,
            emailET, phoneET, question1ET, question2ET, question3ET, question4ET, question5ET;
    private Button submitButton;

    public FragmentCreateApplication() {
        // Required empty public constructor
    }

    public static FragmentCreateApplication newInstance(Dog dog) {
        FragmentCreateApplication fragment = new FragmentCreateApplication();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DOG_ID ,dog);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currDog = (Dog) getArguments().getSerializable(ARG_DOG_ID);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IApplicationListener) {
            aListener = (IApplicationListener) context;
        } else {
            throw new RuntimeException(context.toString() + "Must Implement Interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.create_adoption_application, container, false);

        // fire base init
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // unique ids
        String userID = mUser.getUid();
        String applicationID = UUID.randomUUID().toString();
        String dogID = currDog.getId();

        // init view items
        statusTV = view.findViewById(R.id.applicationStatusLabel);
        headerTV = view.findViewById(R.id.applicationHeader);
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
        question3ET = view.findViewById(R.id.applicationQuestion3EditTextCreate);
        question4ET = view.findViewById(R.id.applicationQuestion4EditTextCreate);
        question5ET = view.findViewById(R.id.applicationQuestion5EditTextCreate);
        dogNameTV = view.findViewById(R.id.dogName);
        submitButton = view.findViewById(R.id.applicationSubmitButton);

        // init dog name in header
        if (currDog.getStatus().equals(DogStatus.ADOPT)) {
            headerTV.setText(R.string.application_adopt_header);
        } else if (currDog.getStatus().equals(DogStatus.FOSTER)){
            headerTV.setText(R.string.application_foster_header);
        }

        dogNameTV.setText(currDog.getName().toUpperCase());

        // fill in view items with user info
        db.collection("users").document(userID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // get user object
                        User currentUserObject = documentSnapshot.toObject(User.class);

                        // get values of user object
                        String userFirstName = currentUserObject.getFirstName();
                        String userLastName = currentUserObject.getLastName();
                        String userAge = String.valueOf(currentUserObject.getAge());
                        Address userFullAddress = currentUserObject.getAddress();
                        String userAdrress = userFullAddress.getAddress();
                        String userCity = userFullAddress.getCity();
                        String userState = userFullAddress.getState();
                        String userEmail = currentUserObject.getEmail();
                        String userPhone = currentUserObject.getPhoneNumber();

                        // init edit texts to values
                        firstNameET.setText(userFirstName);
                        lastNameET.setText(userLastName);
                        ageET.setText(userAge);
                        addressET.setText(userAdrress);
                        cityET.setText(userCity);
                        stateET.setText(userState);
                        emailET.setText(userEmail);
                        phoneET.setText(userPhone);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Unable To Retrieve User Data", Toast.LENGTH_SHORT).show();
                    }
                });

        // submit application to db when submit is pressed
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // input validation
                String inputtedFirstName = firstNameET.getText().toString();
                String inputtedLastName = lastNameET.getText().toString();
                String inputtedAge = ageET.getText().toString();
                String inputtedAddress = addressET.getText().toString();
                String inputtedCity = cityET.getText().toString();
                String inputtedState = stateET.getText().toString();
                String inputtedEmail = emailET.getText().toString();
                String inputtedPhone = phoneET.getText().toString();
                String inputtedQuestion1 = question1ET.getText().toString();
                String inputtedQuestion2 = question2ET.getText().toString();
                String inputtedQuestion3 = question3ET.getText().toString();
                String inputtedQuestion4 = question4ET.getText().toString();
                String inputtedQuestion5 = question5ET.getText().toString();

                if (inputtedFirstName.equals("") || inputtedLastName.equals("") ||
                        inputtedAge.equals("") || inputtedAddress.equals("") ||
                        inputtedCity.equals("") || inputtedState.equals("") ||
                        inputtedEmail.equals("") || inputtedPhone.equals("") ||
                        inputtedQuestion1.equals("") || inputtedQuestion2.equals("") ||
                        inputtedQuestion3.equals("") || inputtedQuestion4.equals("") ||
                        inputtedQuestion5.equals("")) {
                    Toast.makeText(getContext(), "Fill Out All Fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // build application object
                Address userAddress = new Address(inputtedAddress, inputtedCity, inputtedState);
                ApplicationStatus userStatus = ApplicationStatus.PENDING;
                Application application = new Application(applicationID, currDog.getStatus(), inputtedFirstName, inputtedLastName,
                        Integer.parseInt(inputtedAge), userAddress, inputtedEmail, inputtedPhone,
                        inputtedQuestion1, inputtedQuestion2, inputtedQuestion3, inputtedQuestion4, inputtedQuestion5, userStatus, userID, dogID);

                // send to firebase db
                db.collection("applications").document(applicationID)
                        .set(application)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Application Sucessfully Submitted!", Toast.LENGTH_SHORT).show();
                                aListener.backToHome();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Application Failure", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return view;
    }

    public interface IApplicationListener {
        void backToHome();
    }

}