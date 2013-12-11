package com.hyvemynd.musiccloud.dto;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

/**
 * Created by Andres on 12/11/13.
 */
public class UserResponseDto {
    private String firstName;
    private String lastName;
    private String email;

    public UserResponseDto(){
        this("","","");
    }

    public UserResponseDto(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
