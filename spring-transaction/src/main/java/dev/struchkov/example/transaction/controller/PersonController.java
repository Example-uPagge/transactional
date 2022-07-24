package dev.struchkov.example.transaction.controller;

import dev.struchkov.example.transaction.domain.Person;
import dev.struchkov.example.transaction.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonRepository personRepository;

    @GetMapping("{id}")
    public Optional<Person> getById(@PathVariable Long id) {
        return personRepository.findById(id);
    }

}
