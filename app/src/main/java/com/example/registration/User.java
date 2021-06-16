package com.example.registration;

public class User {
    String Displayname;
    String Email;
    String Mobile;
    String age;

    public  User(String Username,String email,String mobile, String age){
        this.Displayname=Username;
        this.Email=email;
        this.Mobile=mobile;
        this.age=age;

    }

    public String getDisplayname() {
        return Displayname;
    }

    public void setDisplayname(String displayname) {
        Displayname = displayname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

}