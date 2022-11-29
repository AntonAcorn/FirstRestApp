package ru.acorn.FirstRestApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController//каждый метод с аннотацией @ResponseBody + @Controller
@RequestMapping("/api")
public class FirstRestController {
    @ResponseBody //спринг понимает, что мы не возвращаем названия представлений, а только данные
    @GetMapping("/sayHello")
    public String sayHello(){
        return "Hello World";
    }
}
