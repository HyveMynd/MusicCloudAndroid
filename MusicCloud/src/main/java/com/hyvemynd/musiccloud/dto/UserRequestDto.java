package com.hyvemynd.musiccloud.dto;

/**
 * Created by Andres on 12/10/13.
 */
public class UserRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public UserRequestDto(){
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.password = "";

    }

    public UserRequestDto(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
