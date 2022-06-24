package com.example.nu_mad_sm2022_final_project_3_2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AppActivity extends AppCompatActivity implements FragmentEmployeeHome.IEmployeeHomeListener,
        FragmentCreateDogProfile.ICreateDogListener, DogProfileAdapter.IDogProfileAdapterListener,
        FragmentFosterHome.IFosterHomeListener, FragmentUserHome.IUserHomeListener,
        FragmentCreateApplication.IApplicationListener,FragmentDogDescription.IDogDescriptionListener,
        ApplicationsAdapter.IAdapterListener, FragmentDisplayApplication.IDisplayApplicationListener,
        FragmentCameraController.ICameraControllerListener {
    // String userId;

    // Firebase Authentication / db
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore db;
    private User currUser;
    private FirebaseStorage storage;

    // Buttons
    private Button buttonHome;
    private Button buttonLogout;

    private boolean isStarting = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        setTitle("Fetcher");

        // Initial auth
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // If user not authorized, return to sign in activity
        // TODO: check if null is the right thing to check for?
        if (mAuth == null) {
            Toast.makeText(AppActivity.this, "ERROR: App started with no authorized user!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AppActivity.this, SignInActivity.class);
            startActivity(intent);
        }

        // Initially start loading fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerAppActivity,
                        FragmentLoading.newInstance(),
                        "fragment-loading")
                .addToBackStack(null)
                .commit();

        updateUser();

        // Set on click listeners
        buttonHome = findViewById(R.id.buttonAppHome);
        buttonLogout = findViewById(R.id.buttonAppLogout);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginHomeFragment();
            }
        });


        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignIn = new Intent(AppActivity.this, SignInActivity.class);
                mAuth.signOut();
                startActivity(toSignIn);
            }
        });


    }

    private void beginHomeFragment() {
        Fragment fragment;
        if (currUser.getRole() == Role.APPLICANT) {
            fragment = FragmentUserHome.newInstance(currUser);
        } else if (currUser.getRole() == Role.EMPLOYEE) {
            fragment = FragmentEmployeeHome.newInstance(currUser);
        } else {
            fragment = FragmentFosterHome.newInstance(currUser);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerAppActivity,
                        fragment,
                        "user-home-screen").commit();

    }

    private void beginViewDogProfilesFragment(DogStatus status) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerAppActivity,
                        FragmentViewDogs.newInstance(status),
                        "view-dog-profiles").addToBackStack(null).commit();
    }


    /**
     * Update's the activities current authorized user object from firebase db
     */
    private void updateUser() {
        db.collection("users")
                .document(mUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            User user = document.toObject(User.class);
                            currUser = user;

                            Log.d("user", currUser.getFirstName());
                            // Toast.makeText(AppActivity.this, user.getFirstName(), Toast.LENGTH_SHORT).show();

                            // Load home screen if necessary
                            if(isStarting) {
                                isStarting = false;
                                // Begin home-screen fragment
                                beginHomeFragment();
                            }

                        } else {
                            Log.d("user", "task unsuccessful");
                        }
                    }
                });
    }

    @Override
    public void onCreateDogProfilePress() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerAppActivity,
                        FragmentCreateDogProfile.newInstance(),
                        "create-dog-profile").commit();
    }

    @Override
    public void onViewDogAdoptProfilesPress() {
        beginViewDogProfilesFragment(DogStatus.ADOPT);
    }

    @Override
    public void onViewDogFosterProfilesPress() {
        Log.d("role", "In onViewFosterProfilesPress");
        beginViewDogProfilesFragment(DogStatus.FOSTER);
    }

    @Override
    public void onUpdateDogProfilePress() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerAppActivity,
                        FragmentUpdateDogProfile.newInstance(),
                        "update-dog-profile").commit();
    }

    @Override
    public void onViewApplicationsPress() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerAppActivity,
                        FragmentApplicationRecyclerView.newInstance(Role.EMPLOYEE),
                        "appliation-view").commit();
    }

    // application view from user
    @Override
    public void onViewApplicationPress() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerAppActivity,
                        FragmentApplicationRecyclerView.newInstance(Role.ADOPTER),
                        "appliation-view").commit();
    }

    @Override
    public void backToHomeFragment() {
        beginHomeFragment();
    }



    @Override
    public void startDogProfileCamera(String dogId) {
        // Start camera fragment to update dog profile pictures
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerAppActivity,
                        FragmentCameraController.newInstance(dogId),
                        "camera-controller").addToBackStack(null).commit();
    }


    // Sends user back to home screen after viewing dog profiles
    @Override
    public void onBackButtonPressed() {
        beginHomeFragment();
    }

    // Sends user to adoption application for the specified dog
    @Override
    public void onAdoptButtonPressed(Dog toAdopt) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerAppActivity,
                        FragmentCreateApplication.newInstance(toAdopt),
                        "create-application").commit();
    }

    @Override
    public void backToHome() {
        beginHomeFragment();
    }

    @Override
    public void toApplicationView(Role role, Application application) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerAppActivity,
                        FragmentDisplayApplication.newInstance(role, application),
                        "view-application").commit();
    }

    @Override
    public void onDogDescriptionBackPressed() {
        // beginViewDogProfilesFragment();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onMoreInfoButtonPressed(Dog dog) {
        Log.d("description", dog.toString());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerAppActivity,
                        FragmentDogDescription.newInstance(dog),
                        "create-dog-profile").commit();
    }

    @Override
    public void backToRecyclerView(Role role) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerAppActivity,
                        FragmentApplicationRecyclerView.newInstance(role),
                        "application-view").commit();
    }


    @Override
    public void onApplicationApproved(Application app, User user) {

    }

    @Override
    public void onPhotoTaken(Uri imageURI, String dogId) {
        Log.d("photo", "in on photo taken");
        // Upload the given image URI to storage for the given dog id
        String path = "images/" + dogId + "/" + UUID.randomUUID();
        Log.d("photo", path);
        StorageReference storageReference = storage.getReference(path);
        UploadTask uploadImage = storageReference.putFile(imageURI);
        uploadImage.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("photo", "upload success");
                getSupportFragmentManager().popBackStack();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("photo", "upload failed");
                e.printStackTrace();
                Toast.makeText(AppActivity.this, "Image upload failed! Try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    String currDogId;

    ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_OK){
                        Intent data = result.getData();
                        Uri selectedImageUri = data.getData();
                        onPhotoTaken(selectedImageUri, currDogId);
                    }
                }
            }
    );

    @Override
    public void onGalleryPress(String dogID) {
        this.currDogId = dogID;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.putExtra("DOG_ID", dogID);
        galleryLauncher.launch(intent);

    }
}