package com.bugtrack.entity;

/**
 * The passwordChange provides data structure
 * to change password of current user
 * @version 0.9.9 8 July 2016
 * @author  Sergey Samsonov
 */
public class passwordChange {

    private String  confirmPassword;

    private String  oldPassword;

    private String  password;

    private String  rightPassword;

    public passwordChange() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getRightPassword() {
        return rightPassword;
    }

    public void setRightPassword(String rightPassword) {
        this.rightPassword = rightPassword;
    }

}
