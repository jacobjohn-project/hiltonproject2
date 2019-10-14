package com.futurearts.hiltonnewproj.modelclasses;

public class LoginDetails {
    String password,userName,userName_password;

    public LoginDetails() {
    }

    public LoginDetails(String password, String userName, String userName_password) {
        this.password = password;
        this.userName = userName;
        this.userName_password = userName_password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName_password() {
        return userName_password;
    }

    public void setUserName_password(String userName_password) {
        this.userName_password = userName_password;
    }
}
