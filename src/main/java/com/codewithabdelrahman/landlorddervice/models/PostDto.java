package com.codewithabdelrahman.landlorddervice.models;

import jakarta.validation.constraints.*;
import java.util.List;

public class PostDto {
    @NotBlank(message = "Neighborhood is required")
    private String neighborhood;

    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "Building number is required")
    private String buildingNumber;

    @NotBlank(message = "Apartment number is required")
    private String apartmentNumber;

    @Positive(message = "Area must be a positive number")
    private double area;

    @PositiveOrZero(message = "Number of rooms must be zero or positive")
    private int numberOfRooms;

    @PositiveOrZero(message = "Number of bathrooms must be zero or positive")
    private int numberOfBathrooms;

    private boolean hasInternet;

    private boolean hasNaturalGas;

    @PositiveOrZero(message = "Number of beds must be zero or positive")
    private int numberOfBeds;

    private boolean isFavorite;

    @Positive(message = "Rent must be a positive number")
    private double rent;

    private int floorNumber;

    private boolean hasElevator;

    private List<String> nearbyServices;

    private String notes;

    @NotEmpty(message = "At least one image is required")
    private List<String> images;

    // Getters and Setters
    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public String getBuildingNumber() { return buildingNumber; }
    public void setBuildingNumber(String buildingNumber) { this.buildingNumber = buildingNumber; }
    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String apartmentNumber) { this.apartmentNumber = apartmentNumber; }
    public double getArea() { return area; }
    public void setArea(double area) { this.area = area; }
    public int getNumberOfRooms() { return numberOfRooms; }
    public void setNumberOfRooms(int numberOfRooms) { this.numberOfRooms = numberOfRooms; }
    public int getNumberOfBathrooms() { return numberOfBathrooms; }
    public void setNumberOfBathrooms(int numberOfBathrooms) { this.numberOfBathrooms = numberOfBathrooms; }
    public boolean isHasInternet() { return hasInternet; }
    public void setHasInternet(boolean hasInternet) { this.hasInternet = hasInternet; }
    public boolean isHasNaturalGas() { return hasNaturalGas; }
    public void setHasNaturalGas(boolean hasNaturalGas) { this.hasNaturalGas = hasNaturalGas; }
    public int getNumberOfBeds() { return numberOfBeds; }
    public void setNumberOfBeds(int numberOfBeds) { this.numberOfBeds = numberOfBeds; }
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
    public double getRent() { return rent; }
    public void setRent(double rent) { this.rent = rent; }
    public int getFloorNumber() { return floorNumber; }
    public void setFloorNumber(int floorNumber) { this.floorNumber = floorNumber; }
    public boolean isHasElevator() { return hasElevator; }
    public void setHasElevator(boolean hasElevator) { this.hasElevator = hasElevator; }
    public List<String> getNearbyServices() { return nearbyServices; }
    public void setNearbyServices(List<String> nearbyServices) { this.nearbyServices = nearbyServices; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
}