package com.example.nu_mad_sm2022_final_project_3_2;

import androidx.annotation.Keep;

import java.util.ArrayList;

@Keep
public class Dogs {
    private ArrayList<Dog> dogs;

    public Dogs(ArrayList<Dog> dogs) {
        this.dogs = dogs;
    }

    public Dogs() {
    }

    public ArrayList<Dog> getDogs() {
        return dogs;
    }

    public void setDogs(ArrayList<Dog> dogs) {
        this.dogs = dogs;
    }
}
