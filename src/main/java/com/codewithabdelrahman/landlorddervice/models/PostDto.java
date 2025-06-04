package com.codewithabdelrahman.landlorddervice.models;

import jakarta.validation.constraints.*;
import java.util.List;

public class PostDto {
    private String neighborhood;

    private String street;

    private String buildingNumber;

    private String apartmentNumber;

    @Positive(message = "Area must be a positive number")
    @NotNull(message = "Area is required")
    private Double area;

    @PositiveOrZero(message = "Number of rooms must be zero or positive")
    @NotNull(message = "Number of rooms is required")
    private Integer numberOfRooms;

    @PositiveOrZero(message = "Number of bathrooms must be zero or positive")
    @NotNull(message = "Number of bathrooms is required")
    private Integer numberOfBathrooms;

    private Boolean hasInternet;

    private Boolean hasNaturalGas;

    @PositiveOrZero(message = "Number of beds must be zero or positive")
    private Integer numberOfBeds;

    private Boolean isFavorite;

    @Positive(message = "Rent must be a positive number")
    @NotNull(message = "Rent is required")
    private Double rent;

    private Integer floorNumber;

    private Boolean hasElevator;

    private List<String> nearbyServices;

    @NotBlank(message = "Notes are required")
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
    public Double getArea() { return area; }
    public void setArea(Double area) { this.area = area; }
    public Integer getNumberOfRooms() { return numberOfRooms; }
    public void setNumberOfRooms(Integer numberOfRooms) { this.numberOfRooms = numberOfRooms; }
    public Integer getNumberOfBathrooms() { return numberOfBathrooms; }
    public void setNumberOfBathrooms(Integer numberOfBathrooms) { this.numberOfBathrooms = numberOfBathrooms; }
    public Boolean isHasInternet() { return hasInternet; }
    public void setHasInternet(Boolean hasInternet) { this.hasInternet = hasInternet; }
    public Boolean isHasNaturalGas() { return hasNaturalGas; }
    public void setHasNaturalGas(Boolean hasNaturalGas) { this.hasNaturalGas = hasNaturalGas; }
    public Integer getNumberOfBeds() { return numberOfBeds; }
    public void setNumberOfBeds(Integer numberOfBeds) { this.numberOfBeds = numberOfBeds; }
    public Boolean isFavorite() { return isFavorite; }
    public void setFavorite(Boolean favorite) { isFavorite = favorite; }
    public Double getRent() { return rent; }
    public void setRent(Double rent) { this.rent = rent; }
    public Integer getFloorNumber() { return floorNumber; }
    public void setFloorNumber(Integer floorNumber) { this.floorNumber = floorNumber; }
    public Boolean isHasElevator() { return hasElevator; }
    public void setHasElevator(Boolean hasElevator) { this.hasElevator = hasElevator; }
    public List<String> getNearbyServices() { return nearbyServices; }
    public void setNearbyServices(List<String> nearbyServices) { this.nearbyServices = nearbyServices; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
}