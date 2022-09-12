package com.example.REST.dto;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class PersonDTO {

    @NotEmpty(message = "name shouldn't be empty")
    @Size(min = 2, max = 30, message = "name should be from 2 to 30 chars")
    private String name;

    @NotEmpty(message = "Email shouldn't be empty")
    private String email;

    @Min(value = 0, message = "age should be greater then 0")
    private int age;

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
