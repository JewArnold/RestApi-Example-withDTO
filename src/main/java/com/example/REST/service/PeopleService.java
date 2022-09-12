package com.example.REST.service;

import com.example.REST.models.Person;
import com.example.REST.repository.PeopleRepository;
import com.example.REST.util.PersonErrorResponse;
import com.example.REST.util.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }


    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findById(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        //если нашли человека, то возвращаем его
        //если нет, то выбрасываем исключение
        return person.orElseThrow(PersonNotFoundException::new);//объект класса Supplier
    }

    @Transactional//чтобы могли изменять БД
    public void save(Person person){
        peopleRepository.save(person);
    }

}
