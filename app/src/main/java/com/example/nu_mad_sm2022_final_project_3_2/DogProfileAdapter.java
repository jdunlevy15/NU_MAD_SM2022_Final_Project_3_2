package com.example.nu_mad_sm2022_final_project_3_2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import android.content.Context;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class DogProfileAdapter extends RecyclerView.Adapter<DogProfileAdapter.ViewHolder> implements Serializable {
    private ArrayList<Dog> dogs;
    private IDogProfileAdapterListener listener;
    // Create a storage reference from our app
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    HashMap<String, Integer> mapDogToImageIndex = new HashMap<>();

    // ArrayList<StorageReference>  imageFiles = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton imageButtonBack;
        private final ImageView imageViewDog;
        private final TextView textViewName, textViewBasicInfo, textViewNoImages;
        private final ProgressBar progressBar;
        private final Button buttonAdopt, buttonMoreInfo;
        private final View view;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            imageButtonBack = view.findViewById(R.id.imageButtonMenu);
            imageViewDog = view.findViewById(R.id.imageViewDogProfile);
            imageViewDog.bringToFront();
            textViewName = view.findViewById(R.id.textViewDogName);
            textViewBasicInfo = view.findViewById(R.id.textViewDogBasicInfo);
            progressBar = view.findViewById(R.id.progressBarDogProfile);
            buttonAdopt = view.findViewById(R.id.buttonAdopt);
            buttonMoreInfo = view.findViewById(R.id.buttonMoreDetails);
            textViewNoImages = view.findViewById(R.id.textViewNoImagesFound);
        }

        public ImageButton getImageButtonBack() {return imageButtonBack;}
        public ImageView getImageViewDog() {return imageViewDog;}
        public TextView getTextViewName() {return textViewName;}
        public TextView getTextViewBasicInfo() {return textViewBasicInfo;}
        public TextView getTextViewNoImages() {return textViewNoImages;}
        public ProgressBar getProgressBar() {return progressBar;}
        public Button getButtonAdopt() {return buttonAdopt;}
        public Button getButtonMoreInfo() {return buttonMoreInfo;}
        public View getView() {return view;}
    }

    // TODO: take in context? (see UserAdapter from last assignment)
    public DogProfileAdapter(Dogs dogs, Context context) {
        if (dogs != null) {
            this.dogs = dogs.getDogs();
        } else {
            this.dogs = new ArrayList<>();
        }



        if (context instanceof IDogProfileAdapterListener) {
            this.listener = (IDogProfileAdapterListener) context;
        } else {
            throw new RuntimeException(context.toString()+" must implement IDogProfileAdapterListener");
        }
    }

    @NonNull
    @Override
    public DogProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogProfileAdapter.ViewHolder holder, int position) {
        // Get ui elements
        ProgressBar progressBar = holder.getProgressBar();
        ImageButton imageButtonBack = holder.getImageButtonBack();
        imageButtonBack.setVisibility(View.GONE);
        ImageView imageViewDog = holder.getImageViewDog();
        TextView textViewName = holder.getTextViewName();
        TextView textViewBasicInfo = holder.getTextViewBasicInfo();
        Button buttonAdopt = holder.getButtonAdopt();
        Button buttonMoreInfo = holder.getButtonMoreInfo();
        TextView textViewNoImages = holder.getTextViewNoImages();

        // Local image array
        ArrayList<StorageReference> images;


        Dog dog = this.dogs.get(position);
        mapDogToImageIndex.put(dog.getId(), 0);
        if (dog != null) {

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
                                theseImages.get(mapDogToImageIndex.get(dog.getId())).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        progressBar.setVisibility(View.GONE);
                                        textViewNoImages.setVisibility(View.INVISIBLE);
                                        Glide.with(holder.getView()).load(uri).into(imageViewDog);
                                        mapDogToImageIndex.put(dog.getId(), mapDogToImageIndex.get(dog.getId()) + 1);
                                    }
                                });
                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                textViewNoImages.setVisibility(View.VISIBLE);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("images","couldn't load images");
                            // setImageFiles(new ArrayList<StorageReference>());
                            imageViewDog.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });

            // TODO: toggle images when clicked
            imageViewDog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageViewDog.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    imagesRef.listAll()
                            .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                @Override
                                public void onSuccess(ListResult listResult) {
                                    // imageFiles = new ArrayList<StorageReference>(listResult.getItems());
                                    // setImageFiles(new ArrayList<StorageReference>(listResult.getItems()));
                                    ArrayList<StorageReference> theseImages = new ArrayList<StorageReference>(listResult.getItems());

                                    // Set first image
                                    if (theseImages.size() > 0) {
                                        int currIndex = mapDogToImageIndex.get(dog.getId());
                                        if (currIndex >= theseImages.size()) {
                                            mapDogToImageIndex.put(dog.getId(), 0);
                                            currIndex = 0;
                                        }
                                        theseImages.get(currIndex).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Glide.with(holder.getView()).load(uri).into(imageViewDog);
                                                progressBar.setVisibility(View.GONE);
                                                imageViewDog.setVisibility(View.VISIBLE);
                                                textViewNoImages.setVisibility(View.INVISIBLE);
                                                mapDogToImageIndex.put(dog.getId(), mapDogToImageIndex.get(dog.getId()) + 1);
                                            }
                                        });
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        textViewNoImages.setVisibility(View.VISIBLE);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("images","couldn't load images");
                                    // setImageFiles(new ArrayList<StorageReference>());
                                    imageViewDog.setVisibility(View.INVISIBLE);
                                    progressBar.setVisibility(View.VISIBLE);
                                }
                            });
                }
            });


            // Update text views:
            textViewName.setText(dog.getName());
            String basicInfo = dog.getBreed() + ", Age: " + dog.getAge() + ", " + dog.getGender();
            textViewBasicInfo.setText(basicInfo);


            // Set on click listeners
            /*
            imageButtonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onBackButtonPressed();
                }
            });

             */

            buttonAdopt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAdoptButtonPressed(dog);
                }
            });

            buttonMoreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("description", "button pressed");
                    listener.onMoreInfoButtonPressed(dog);
                }
            });
        }


    }

    /*
    private void setImageFiles(ArrayList<StorageReference> items) {
        this.imageFiles = items;
        notifyDataSetChanged();
    }

     */

    @Override
    public int getItemCount() {
        if (this.dogs != null) {
            return this.dogs.size();
        }
        return 0;
    }

    public void updateDogs(ArrayList<Dog> dogs) {
        this.dogs = dogs;
        this.notifyDataSetChanged();
        Log.d("dogs", "in adapter update dogs, number of dogs: " + dogs.size());
    }

    public interface IDogProfileAdapterListener {
        void onBackButtonPressed();
        void onAdoptButtonPressed(Dog toAdopt);
        void onMoreInfoButtonPressed(Dog dog);
    }


}
