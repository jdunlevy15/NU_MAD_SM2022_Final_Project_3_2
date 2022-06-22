package com.example.nu_mad_sm2022_final_project_3_2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class DogProfileAdapter extends RecyclerView.Adapter<DogProfileAdapter.ViewHolder> implements Serializable {
    private ArrayList<Dog> dogs;
    private IDogProfileAdapterListener listener;

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
        ImageView imageViewDog = holder.getImageViewDog();
        TextView textViewName = holder.getTextViewName();
        TextView textViewBasicInfo = holder.getTextViewBasicInfo();
        Button buttonAdopt = holder.getButtonAdopt();

        Dog dog = this.dogs.get(position);
        if (dog != null) {
            // TODO: download images

            // TODO: toggle images when clicked

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
