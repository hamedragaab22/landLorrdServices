package com.codewithabdelrahman.landlorddervice.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "landlords")
public class AppUser {
    @Id
    private String id;

    private String fullName;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String password;
    private String role;
    private Date createdAt;
    private String profileImage;
    private String token;
//    private String nationalIdFront;
//    private String nationalIdBack;
    private String verificationCode;

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

//    public String getNationalIdFront() {
//        return nationalIdFront;
//    }
//
//    public void setNationalIdFront(String nationalIdFront) {
//        this.nationalIdFront = nationalIdFront;
//    }
//
//    public String getNationalIdBack() {
//        return nationalIdBack;
//    }
//
//    public void setNationalIdBack(String nationalIdBack) {
//        this.nationalIdBack = nationalIdBack;
//    }
}
