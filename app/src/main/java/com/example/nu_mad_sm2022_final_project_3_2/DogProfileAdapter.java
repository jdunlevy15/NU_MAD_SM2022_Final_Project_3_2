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

public class DogProfileAdapter extends RecyclerView.Adapter<DogProfileAdapter.ViewHolder> implements Serializable {
    private ArrayList<Dog> dogs;
    private IDogProfileAdapterListener listener;
    // Create a storage reference from our app
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    ArrayList<StorageReference>  imageFiles = new ArrayList<>();
    int imageIndex = 0;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton imageButtonBack;
        private final ImageView imageViewDog;
        private final TextView textViewName, textViewBasicInfo;
        private final ProgressBar progressBar;
        private final Button buttonAdopt;
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
        }

        public ImageButton getImageButtonBack() {return imageButtonBack;}
        public ImageView getImageViewDog() {return imageViewDog;}
        public TextView getTextViewName() {return textViewName;}
        public TextView getTextViewBasicInfo() {return textViewBasicInfo;}
        public ProgressBar getProgressBar() {return progressBar;}
        public Button getButtonAdopt() {return buttonAdopt;}
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


        Dog dog = this.dogs.get(position);
        if (dog != null) {
            // TODO: download images

            // ArrayList<StorageReference>  imageFiles = new ArrayList<>();
            String path = "images/" + dog.getId() + "/";
            StorageReference imagesRef = storageRef.child(path);
            imagesRef.listAll()
                    .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            // imageFiles = new ArrayList<StorageReference>(listResult.getItems());
                            setImageFiles(new ArrayList<StorageReference>(listResult.getItems()));


                            // Set first image
                            if (imageFiles.size() > 0) {
                                imageFiles.get(0).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        progressBar.setVisibility(View.GONE);
                                        Glide.with(holder.getView()).load(uri).into(imageViewDog);
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("images","couldn't load images");
                        }
                    });

            // TODO: toggle images when clicked

            /*
            imageViewDog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("images", "image clicked");
                    progressBar.setVisibility(View.VISIBLE);
                    imageViewDog.setVisibility(View.INVISIBLE);
                    imageIndex++;
                    if (imageIndex >= imageFiles.size()) {
                        imageIndex = 0;
                    }

                    Log.d("images","new images index: " + imageIndex);

                    imageFiles.get(imageIndex).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("images","got new image!");
                            Log.d("images", uri.toString());
                            progressBar.setVisibility(View.GONE);
                            imageViewDog.setVisibility(View.VISIBLE);

                            // Picasso.get().load("https://i.imgur.com/DvpvklR.png").into(imageView);
                            Picasso.get().load(uri).into(imageViewDog);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("images","couldn't get new image :(");
                        }
                    });
                }
            });

             */

            // Update text views:
            textViewName.setText(dog.getName());
            String basicInfo = dog.getBreed() + ", " + dog.getAge() + ", " + dog.getGender();
            textViewBasicInfo.setText(basicInfo);


            // Set on click listeners
            imageButtonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onBackButtonPressed();
                }
            });

            buttonAdopt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAdoptButtonPressed(dog);
                }
            });
        }


    }

    private void setImageFiles(ArrayList<StorageReference> items) {
        this.imageFiles = items;
        notifyDataSetChanged();
    }

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
    }


}
