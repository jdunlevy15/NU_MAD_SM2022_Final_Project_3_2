package com.example.nu_mad_sm2022_final_project_3_2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ViewHolder> {

    private ArrayList<Application> applications;
    private boolean isUser;
    private IAdapterListener aListener;

    public ApplicationsAdapter(ArrayList<Application> applications, Context context, boolean isUser) {
        this.applications = applications;
        this.isUser = isUser;
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
                                    if (isUser) {
                                        aListener.toApplicationView(true, application);
                                    } else  {
                                        aListener.toApplicationView(false, application);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dogName = itemView.findViewById(R.id.cardDogNameTextView);
            applicantName = itemView.findViewById(R.id.cardApplicantNameTextView);
            viewButton = itemView.findViewById(R.id.cardViewApplicationButton);
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
    }

    public interface IAdapterListener {
        void toApplicationView(boolean fromUser, Application application);
    }
}
