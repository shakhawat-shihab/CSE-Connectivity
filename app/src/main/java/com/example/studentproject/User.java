package com.example.studentproject;

public class User {
    private String Id, Name, Mobile, Home, Batch, Blood, Email, Password,isModerator,isAdmin,imageUrl,Facebook;
    private String Key;

    public User() {
    }

    public User(String id, String name, String mobile, String home, String batch, String blood, String email, String password) {
        Id = id;
        Name = name;
        Mobile = mobile;
        Home = home;
        Batch = batch;
        Blood = blood;
        Email = email;
        Password = password;
    }

    public User(String id, String name, String mobile, String home, String batch, String blood, String email,String isModerator, String isAdmin,String imageUrl,String facebook) {
        Id = id;
        Name = name;
        Mobile = mobile;
        Home = home;
        Batch = batch;
        Blood = blood;
        Email = email;
        this.isModerator=isModerator;
        this.isAdmin=isAdmin;
        this.imageUrl=imageUrl;
        Facebook=facebook;
    }

    public User(String id, String name, String mobile, String home, String batch, String blood, String email) {
        Id = id;
        Name = name;
        Mobile = mobile;
        Home = home;
        Batch = batch;
        Blood = blood;
        Email = email;
    }
    public User(String id, String name, String mobile, String home, String batch, String blood,String imageUrl, String email,String facebook) {
        Id = id;
        Name = name;
        Mobile = mobile;
        Home = home;
        Batch = batch;
        Blood = blood;
        this.imageUrl=imageUrl;
        Email = email;
        Facebook=facebook;
    }
    public User(String id, String name, String batch, String email, String mobile) {
        Id = id;
        Name = name;
        Mobile = mobile;
        Batch = batch;
        Email = email;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getHome() {
        return Home;
    }

    public void setHome(String home) {
        Home = home;
    }

    public String getBatch() {
        return Batch;
    }

    public void setBatch(String batch) {
        Batch = batch;
    }

    public String getBlood() {
        return Blood;
    }

    public void setBlood(String blood) {
        Blood = blood;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getIsModerator() {
        return isModerator;
    }

    public void setIsModerator(String isModerator) {
        this.isModerator = isModerator;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFacebook() {
        return Facebook;
    }

    public void setFacebook(String facebook) {
        Facebook = facebook;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
