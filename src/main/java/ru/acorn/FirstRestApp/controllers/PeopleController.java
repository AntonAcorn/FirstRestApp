package ru.acorn.FirstRestApp.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.acorn.FirstRestApp.dto.PersonDTO;
import ru.acorn.FirstRestApp.models.Person;
import ru.acorn.FirstRestApp.services.PeopleService;
import ru.acorn.FirstRestApp.util.PersonErrorResponse;
import ru.acorn.FirstRestApp.util.PersonNotCreatedException;
import ru.acorn.FirstRestApp.util.PersonNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/people")
public class PeopleController {
    private final ModelMapper modelMapper;
    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService,ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<PersonDTO> getPeople() {
        return peopleService.findAll().stream().map(this::convertToPersonDto).collect(Collectors.toList());//Jackson конвертирует объекты в JSON
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable("id") int id) throws PersonNotFoundException {
        return convertToPersonDto(peopleService.findOne(id));
    }

    @PostMapping//в binding result лежат ошибки аннотации @valid
    //парсим объект, который передаем в формате json, к примеру, из postman
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors
            ) {
                errorMessage.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new PersonNotCreatedException(errorMessage.toString());
        }
        peopleService.save(convertToPerson(personDTO));
        //отправляем http ответ с пустым телом и статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }


    //перехватываем исключения в контроллерах
    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
        PersonErrorResponse personErrorResponse = new PersonErrorResponse(
                "Person with this id was not found",
                System.currentTimeMillis()
        );
        //в ответе будет тело response и статус в заголовке
        return new ResponseEntity<>(personErrorResponse, HttpStatus.NOT_FOUND); //not found 404
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e) {
        PersonErrorResponse personErrorResponse = new PersonErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );
        return new ResponseEntity<>(personErrorResponse, HttpStatus.BAD_REQUEST);
    }


    private Person convertToPerson(PersonDTO personDTO) {
        //mapper находит недостающие поля и автоматически назначает их. т.е. мапит DTO и модель
        //создается в конфигурации как бин и внедряется через конструктор

        //мапаем вручную, лучше использовать зависимость modelmapper
//        Person person = new Person();
//        person.setName(personDTO.getName());
//        person.setAge(personDTO.getAge());
//        person.setEmail(personDTO.getEmail());

        return modelMapper.map(personDTO, Person.class);
    }


    private PersonDTO convertToPersonDto(Person person){
        return modelMapper.map(person, PersonDTO.class);
    }
}
