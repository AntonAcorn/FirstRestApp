package ru.acorn.FirstRestApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.acorn.FirstRestApp.models.Person;
import ru.acorn.FirstRestApp.services.PeopleService;
import ru.acorn.FirstRestApp.util.PersonErrorResponse;
import ru.acorn.FirstRestApp.util.PersonNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping
    public List<Person> getPeople(){
        return peopleService.findAll();//Jackson конвертирует объекты в JSON
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable("id") int id) throws PersonNotFoundException {
        return peopleService.findOne(id);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e){
        PersonErrorResponse personErrorResponse = new PersonErrorResponse(
                "Person with this id was not found",
                System.currentTimeMillis()
        );
        //в ответе будет тело response и статус в заголовке
        return new ResponseEntity<>(personErrorResponse, HttpStatus.NOT_FOUND); //not found 404
    }
}
