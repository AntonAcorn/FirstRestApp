package ru.acorn.FirstRestApp.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class PersonDTO {
    @Getter
    @Setter
    @NotEmpty(message = "name should not be empty")
    @Size(min = 2, max = 30, message = "name should be between 2 and 30")
    private String name;

    @Getter
    @Setter
    @Min(value = 0, message = "age should be greater than 0")
    private int age;

    @Email
    @NotEmpty(message = "email should not be empty")
    @Getter
    @Setter
    private String email;

    public PersonDTO() {
    }
}
