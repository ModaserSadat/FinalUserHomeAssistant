package com.example.homeassistant;
import com.google.firebase.database.Exclude;

public class Teacher {
    private String name;
    private String imageURL;
    private String key;
    private String description;
    String phone;

    private int position;

    public Teacher() {
        //empty constructor needed
    }
    public Teacher(int position){
        this.position = position;
    }
    public Teacher(String name, String imageUrl , String Des, String phone) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        this.name = name;
        this.imageURL = imageUrl;
        this.description = Des;
        this.phone=phone;

    }



    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImageUrl() {
        return imageURL;
    }
    public void setImageUrl(String imageUrl) {
        this.imageURL = imageUrl;
    }
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
