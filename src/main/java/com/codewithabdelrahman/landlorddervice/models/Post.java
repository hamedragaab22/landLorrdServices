package com.codewithabdelrahman.landlorddervice.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Document(collection = "posts")
public class Post {
    @Id
    private String id;
    private String userId; // Reference to the user who created the post
    private String neighborhood; // الحي
    private String street; // الشارع
    private String buildingNumber; // رقم العمارة
    private String apartmentNumber; // رقم الشقة
    private double area; // المساحة (م²)
    private int numberOfRooms; // عدد الغرف
    private int numberOfBathrooms; // عدد الحمام
    private boolean hasInternet; // هل في الإنترنت
    private boolean hasNaturalGas; // هل في غاز الطبيعي
    private int numberOfBeds; // عدد السراير
    private boolean isFavorite; // المفضلة
    private double rent; // الإيجار
    private int floorNumber; // رقم الطابق
    private boolean hasElevator; // هل في مصعد
    private List<String> nearbyServices; // الخدمات المجاورة
    private String notes; // الملاحظات
    private Date createdAt; // تاريخ الإضافة
    private List<String> images; // الصور (URLs or references)

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
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
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
}