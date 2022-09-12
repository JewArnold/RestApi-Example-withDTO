package com.example.REST.controllers;


import com.example.REST.dto.PersonDTO;
import com.example.REST.models.Person;
import com.example.REST.service.PeopleService;
import com.example.REST.util.PersonErrorResponse;
import com.example.REST.util.PersonNotCreatedException;
import com.example.REST.util.PersonNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/people")
@RestController
public class PeopleController {

    private final PeopleService peopleService;

    private final ModelMapper modelMapper;

    @Autowired
    public PeopleController(PeopleService peopleService,
                            ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }




    /*
    Что здесь происходит:
    Клиент посылает запрос с URL /people,
    в ответ на запрос ему отправляется объект в формате json
    В запросе мы не получаем никаких данных
     */

    @GetMapping()
    public List<PersonDTO> getAll() {
        return peopleService.findAll().stream().map(this::convertToPersonDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable(name = "id") int id) {

        return convertToPersonDTO(peopleService.findById(id));
    }


    @PostMapping()//в этом методе можно возвращать любой объект
    //в данном случае возвращаем статус ответа
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO,
                                             //Принимается Json и конвертируется
                                             //в объект Person
                                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) { //если json невалидный
            //обрабатываем ошибку и отправляем
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error :
                    errors) {
                errorMessage.append(error.getField())
                        .append("-").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new PersonNotCreatedException(errorMessage.toString());
        }

        peopleService.save(convertToPerson(personDTO));

        return ResponseEntity.ok(HttpStatus.OK);// возвращаем клиенту "OK"
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }


    @ExceptionHandler //обработчик исключений. В параметре явно указываем какое исключение ловим
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException exception) {
        PersonErrorResponse response = new PersonErrorResponse( //объект, который создаем в случае перехвата исключения
                "Person with this id wasn't found", //сообщение об ошибке
                System.currentTimeMillis()  //текущее время в милисекундах
        );
        //преобазуем response в объект для передачи по сети, указваем статус ответа
        //в HTTP body будет отображено тело response и статус HttpStatus
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);//status 404
    }


    @ExceptionHandler //обработчик исключений. В параметре явно указываем какое исключение ловим
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException exception) {

        PersonErrorResponse response = new PersonErrorResponse( //объект, который создаем в случае перехвата исключения
                exception.getMessage(), //получаем сообщение, которое создали в методе create
                System.currentTimeMillis()  //текущее время в милисекундах
        );

        //преобазуем response в объект для передачи по сети, указваем статус ответа
        //в HTTP body будет отображено тело response и статус HttpStatus
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);//status 400
    }


}
