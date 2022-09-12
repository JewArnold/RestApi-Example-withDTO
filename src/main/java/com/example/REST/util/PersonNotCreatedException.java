package com.example.REST.util;

public class PersonNotCreatedException extends RuntimeException{
    public PersonNotCreatedException(String message){
        super(message);  //Переопределили конструктор супера
    }
}
