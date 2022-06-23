package com.example.nu_mad_sm2022_final_project_3_2;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class Dog implements Serializable {
    private String id;
    private String name;
    private String breed;
    private int age;
    private Gender gender;
    private DogStatus status;
    private String color; // Enum type? would probably require a dropdown for creating dog profile
    private int currentWeight;
    private int potentialWeight;
    private boolean fenceRequired;
    private boolean houseTrained;
    private Training obedienceTraining;
    private Needs exerciseNeeds;
    private Needs groomingNeeds;
    private Needs sheddingAmount;
    private Needs ownerExperienceNeeded;
    private Reaction reactionToNewPeople;
    private User fosterParent;
    private User owner;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Dog(String id, String name, String breed, int age, Gender gender, DogStatus status, String color, int currentWeight, int potentialWeight, boolean fenceRequired, boolean houseTrained, Training obedienceTraining, Needs exerciseNeeds, Needs groomingNeeds, Needs sheddingAmount, Needs ownerExperienceNeeded, Reaction reactionToNewPeople) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.gender = gender;
        this.status = status;
        this.color = color;
        this.currentWeight = currentWeight;
        this.potentialWeight = potentialWeight;
        this.fenceRequired = fenceRequired;
        this.houseTrained = houseTrained;
        this.obedienceTraining = obedienceTraining;
        this.exerciseNeeds = exerciseNeeds;
        this.groomingNeeds = groomingNeeds;
        this.sheddingAmount = sheddingAmount;
        this.ownerExperienceNeeded = ownerExperienceNeeded;
        this.reactionToNewPeople = reactionToNewPeople;
    }

    public Dog() {
    }

    @Override
    public String toString() {
        return "Dog{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", gender=" + gender +
                ", status=" + status +
                ", color='" + color + '\'' +
                ", currentWeight=" + currentWeight +
                ", potentialWeight=" + potentialWeight +
                ", fenceRequired=" + fenceRequired +
                ", houseTrained=" + houseTrained +
                ", obedienceTraining=" + obedienceTraining +
                ", exerciseNeeds=" + exerciseNeeds +
                ", groomingNeeds=" + groomingNeeds +
                ", sheddingAmount=" + sheddingAmount +
                ", ownerExperienceNeeded=" + ownerExperienceNeeded +
                ", reactionToNewPeople=" + reactionToNewPeople +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public DogStatus getStatus() {
        return status;
    }

    public void setStatus(DogStatus status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(int currentWeight) {
        this.currentWeight = currentWeight;
    }

    public int getPotentialWeight() {
        return potentialWeight;
    }

    public void setPotentialWeight(int potentialWeight) {
        this.potentialWeight = potentialWeight;
    }

    public boolean isFenceRequired() {
        return fenceRequired;
    }

    public void setFenceRequired(boolean fenceRequired) {
        this.fenceRequired = fenceRequired;
    }

    public boolean isHouseTrained() {
        return houseTrained;
    }

    public void setHouseTrained(boolean houseTrained) {
        this.houseTrained = houseTrained;
    }

    public Training getObedienceTraining() {
        return obedienceTraining;
    }

    public void setObedienceTraining(Training obedienceTraining) {
        this.obedienceTraining = obedienceTraining;
    }

    public Needs getExerciseNeeds() {
        return exerciseNeeds;
    }

    public void setExerciseNeeds(Needs exerciseNeeds) {
        this.exerciseNeeds = exerciseNeeds;
    }

    public Needs getGroomingNeeds() {
        return groomingNeeds;
    }

    public void setGroomingNeeds(Needs groomingNeeds) {
        this.groomingNeeds = groomingNeeds;
    }

    public Needs getSheddingAmount() {
        return sheddingAmount;
    }

    public void setSheddingAmount(Needs sheddingAmount) {
        this.sheddingAmount = sheddingAmount;
    }

    public Needs getOwnerExperienceNeeded() {
        return ownerExperienceNeeded;
    }

    public void setOwnerExperienceNeeded(Needs ownerExperienceNeeded) {
        this.ownerExperienceNeeded = ownerExperienceNeeded;
    }

    public Reaction getReactionToNewPeople() {
        return reactionToNewPeople;
    }

    public void setReactionToNewPeople(Reaction reactionToNewPeople) {
        this.reactionToNewPeople = reactionToNewPeople;
    }

    public User getFosterParent() {
        return fosterParent;
    }

    public void setFosterParent(User fosterParent) {
        this.fosterParent = fosterParent;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}