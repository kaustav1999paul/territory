package com.territory.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.territory.entity.ExampleEntity;
import com.territory.repository.ExampleRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final ExampleRepository repository;

    @GetMapping("/ping")
    public String ping() {
        return "Backend Running";
    }

    @PostMapping("/insert")
    public ExampleEntity insert() {

        ExampleEntity entity = ExampleEntity.builder()
                .message("hello from pi")
                .build();

        return repository.save(entity);
    }
}