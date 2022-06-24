package com.example.nu_mad_sm2022_final_project_3_2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDogDescription#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDogDescription extends Fragment {
    private TextView textViewName, textViewBasicInfo, textViewStatus, textViewColor,
            textViewCurrentSize, textViewPotentialSize, textViewFence, textViewHouseTrained,
    textViewObedience, textViewExercise, textViewGrooming, textViewShedding, textViewOwnerExperience,
    textViewReaction;
    private ImageView imageViewDog;
    private ImageButton imageButtonBack;
    private Button buttonAdopt;
    private IDogDescriptionListener listener;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    // the fragment initialization parameters,
    private static final String ARG_DOG = "dog";

    private Dog dog;

    public FragmentDogDescription() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param dog Parameter 1.
     * @return A new instance of fragment FragmentDogDescription.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDogDescription newInstance(Dog dog) {
        FragmentDogDescription fragment = new FragmentDogDescription();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DOG, dog);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof IDogDescriptionListener) {
            listener = (IDogDescriptionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "Must Implement IDogDescriptionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dog = (Dog) getArguments().getSerializable(ARG_DOG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dog_description, container, false);

        textViewName = view.findViewById(R.id.textViewDescriptionName);
        textViewBasicInfo = view.findViewById(R.id.textViewDescriptionBasicInfo);
        textViewStatus = view.findViewById(R.id.textViewDescriptionStatus);
        textViewColor = view.findViewById(R.id.textViewColorPlaceholder);
        textViewCurrentSize = view.findViewById(R.id.textViewCurrentSizePlaceholder);
        textViewPotentialSize = view.findViewById(R.id.textViewPotentialSizePlaceholder);
        textViewFence = view.findViewById(R.id.textViewFenceRequiredPlaceholder);
        textViewHouseTrained = view.findViewById(R.id.textViewHouseTrainedPlaceholder);
        textViewObedience = view.findViewById(R.id.textViewObedienceTrainingPlaceholder);
        textViewExercise = view.findViewById(R.id.textViewExerciseNeedsPlaceholder);
        textViewGrooming = view.findViewById(R.id.textViewGroomingNeedsLabel);
        textViewShedding = view.findViewById(R.id.textViewSheddingAmountPlaceholder);
        textViewOwnerExperience = view.findViewById(R.id.textViewOwnerExerpiencePlaceholder);
        textViewReaction = view.findViewById(R.id.textViewReactionPlaceholder);

        imageButtonBack = view.findViewById(R.id.imageButtonDescriptionBack);
        buttonAdopt = view.findViewById(R.id.buttonDescriptionAdopt);

        imageViewDog = view.findViewById(R.id.imageViewDescriptionPhoto);

        // TODO: load image



        // Set text fields
        if (this.dog != null) {
            textViewName.setText(dog.getName());
            String basicInfo = dog.getBreed() + ", " + dog.getAge() + ", " + dog.getGender();
            textViewBasicInfo.setText(basicInfo);
            textViewStatus.setText(dog.getStatus().toString());
            textViewColor.setText(dog.getColor());
            textViewCurrentSize.setText(Integer.toString(dog.getCurrentWeight()));
            textViewPotentialSize.setText(Integer.toString(dog.getPotentialWeight()));
            if (dog.isFenceRequired()) {
                textViewFence.setText("Yes");
            } else {
                textViewFence.setText("No");
            }
            if (dog.isHouseTrained()) {
                textViewHouseTrained.setText("Yes");
            } else {
                textViewHouseTrained.setText("No");
            }
            textViewObedience.setText(dog.getObedienceTraining().toString());
            textViewExercise.setText(dog.getExerciseNeeds().toString());
            textViewGrooming.setText(dog.getGroomingNeeds().toString());
            textViewShedding.setText(dog.getSheddingAmount().toString());
            textViewOwnerExperience.setText(dog.getOwnerExperienceNeeded().toString());
            textViewReaction.setText(dog.getReactionToNewPeople().toString());

            // ArrayList<StorageReference>  imageFiles = new ArrayList<>();
            String path = "images/" + dog.getId() + "/";
            StorageReference imagesRef = storageRef.child(path);
            imagesRef.listAll()
                    .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            // imageFiles = new ArrayList<StorageReference>(listResult.getItems());
                            // setImageFiles(new ArrayList<StorageReference>(listResult.getItems()));
                            ArrayList<StorageReference> theseImages = new ArrayList<StorageReference>(listResult.getItems());

                            // Set first image
                            if (theseImages.size() > 0) {
                                theseImages.get(0).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // progressBar.setVisibility(View.GONE);
                                        // textViewNoImages.setVisibility(View.INVISIBLE);
                                        Glide.with(view).load(uri).into(imageViewDog);
                                        //mapDogToImageIndex.put(dog.getId(), mapDogToImageIndex.get(dog.getId()) + 1);
                                    }
                                });
                            } else {
                                //progressBar.setVisibility(View.GONE);
                                //textViewNoImages.setVisibility(View.VISIBLE);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("images", "couldn't load images");
                            // setImageFiles(new ArrayList<StorageReference>());
                            imageViewDog.setVisibility(View.INVISIBLE);
                            //progressBar.setVisibility(View.VISIBLE);
                        }
                    });

            // TODO: set adopt on click listener
            buttonAdopt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAdoptButtonPressed(dog);
                }
            });

            imageButtonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDogDescriptionBackPressed();
                }
            });

        }


        return view;
    }

    public interface IDogDescriptionListener {
        void onAdoptButtonPressed(Dog dog);
        void onDogDescriptionBackPressed();
    }
}