package com.example.todoapp;

public class User {

    public String name,email,phone,DOB,gender;

    public User(String name,String email,String phone,String DOB,String gender){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.DOB = DOB;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", DOB='" + DOB + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
