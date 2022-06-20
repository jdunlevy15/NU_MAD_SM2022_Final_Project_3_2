package com.example.nu_mad_sm2022_final_project_3_2;

public class Dog {
    private final String id;
    private String name;
    private String breed;
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

    public Dog(String id) {
        this.id = id;
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