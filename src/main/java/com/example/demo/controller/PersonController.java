package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Person;
import com.example.demo.webservice.PersonService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/persons")

public class PersonController {
     @Autowired
    private PersonService personService;

    @GetMapping
public ResponseEntity<Map<String, Object>> getAllPersons() {
    List<Person> persons = personService.getAllPersons();

    Map<String, Object> response = new HashMap<>();
    response.put("persons", persons);
    response.put("errorCode", 200);
    response.put("errorMessage", "Success");

    return ResponseEntity.ok().body(response);
}


    @GetMapping("/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable Long id) {
        Person person = personService.getPersonById(id);
    
        if (person != null) {
            return ResponseEntity.ok(person);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person with ID " + id + " not found");
        }
    }
    

  @PostMapping("/savePerson")
    public ResponseEntity<String> savePerson(@RequestBody Person person) {
        try {
            validatePerson(person);

            // Explicitly set all attributes
            Person newPerson = new Person();
            newPerson.setFirstName(person.getFirstName());
            newPerson.setLastName(person.getLastName());
            newPerson.setEmail(person.getEmail());

            // Save the person
            Person savedPerson = personService.savePerson(newPerson);

            // Create a response with the saved person details
            return ResponseEntity.ok("Person saved successfully. ID: " + savedPerson.getId());
        } catch (IllegalArgumentException e) {
            // Handle validation errors
            return ResponseEntity.badRequest().body("Invalid input data: " + e.getMessage());
        } catch (Exception e) {
            // Handle other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving person: " + e.getMessage());
        }
    }

    private void validatePerson(Person person) {
        // Example validation: Check for null or empty values
    
        if (person == null) {
            throw new IllegalArgumentException("Person data is null");
        }
    
        if (person.getFirstName() == null || person.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
    
        if (person.getLastName() == null || person.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
    
        if (person.getEmail() == null || person.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePerson(@PathVariable Long id, @RequestBody Person updatedPerson) {
        Person updated = personService.updatePerson(id, updatedPerson);
        if (updated != null) {
            return ResponseEntity.ok("Person with ID " + id + " updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.ok("Person with ID " + id + " deleted successfully");
    }
    
}
