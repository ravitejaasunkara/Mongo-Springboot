package com.ravi.mongo.Controller;

import com.ravi.mongo.Documents.Person;
import com.ravi.mongo.Repository.PersonRepository;
import com.ravi.mongo.Service.PersonServiceImpl;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("persons")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonServiceImpl personService;

    @PostMapping("/save")
    public void savePerson(@RequestBody Person person) {
        personRepository.save(person);
    }

    @GetMapping("/oldestPerson")
    public List<Document> getOldestPerson() {
        return personService.getOldestPersonByCity();
    }
}
