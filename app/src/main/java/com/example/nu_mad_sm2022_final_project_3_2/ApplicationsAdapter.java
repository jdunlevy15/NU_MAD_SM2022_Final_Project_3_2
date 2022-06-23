package com.example.nu_mad_sm2022_final_project_3_2;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ViewHolder> {

    private ArrayList<Application> applications;
    private Role role;
    private IAdapterListener aListener;

    public ApplicationsAdapter(ArrayList<Application> applications, Context context, Role role) {
        this.applications = applications;
        this.role = role;
        if (context instanceof IAdapterListener) {
            this.aListener = (IAdapterListener) context;
        }
        else{
            throw new RuntimeException(context.toString()+ "must implement interface");
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.application_select_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Application application = applications.get(position);
        // change color of card based on application status
        if (application.getStatus() == ApplicationStatus.ACCEPTED) {
            holder.getApplicationCardView().setCardBackgroundColor(Color.parseColor("#54FF72"));
        }
        if (application.getStatus() == ApplicationStatus.REJECTED) {
            holder.getApplicationCardView().setCardBackgroundColor(Color.parseColor("#FF7254"));
        }
        String applicantName = application.getFirstName();
        String dogID = applications.get(position).getDogID();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("dogs")
                .document(dogID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Dog dog = task.getResult().toObject(Dog.class);
                            String dogName = dog.getName();
                            holder.getApplicantName().setText(applicantName);
                            holder.getDogName().setText(dogName);
                            holder.getViewButton().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (role == Role.ADOPTER) {
                                        aListener.toApplicationView(Role.ADOPTER, application);
                                    }
                                    else if (role == Role.FOSTER) {
                                        aListener.toApplicationView(Role.FOSTER, application);
                                    }
                                    else  {
                                        aListener.toApplicationView(Role.EMPLOYEE, application);
                                    }
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return applications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView dogName;
        private final TextView applicantName;
        private final Button viewButton;
        private final CardView applicationCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dogName = itemView.findViewById(R.id.cardDogNameTextView);
            applicantName = itemView.findViewById(R.id.cardApplicantNameTextView);
            viewButton = itemView.findViewById(R.id.cardViewApplicationButton);
            applicationCardView = itemView.findViewById(R.id.applicationCardView);
        }

        public TextView getDogName() {
            return dogName;
        }

        public TextView getApplicantName() {
            return applicantName;
        }

        public Button getViewButton() {
            return viewButton;
        }

        public CardView getApplicationCardView() {
            return applicationCardView;
        }
    }

    public interface IAdapterListener {
        void toApplicationView(Role role, Application application);
    }
}
