package com.example.demo.webservice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;

import java.util.List;


@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person getPersonById(Long id) {
        return personRepository.findById(id).orElse(null);
    }

    public Person savePerson(Person person) {
        try {
            // Check if the email already exists
            if (personRepository.findByEmail(person.getEmail()) != null) {
                throw new DataIntegrityViolationException("Email already exists");
            }

            return personRepository.save(person);
        } catch (DataIntegrityViolationException e) {
            // Handle duplicate email exception
            throw new IllegalArgumentException("Email already exists");
        }
    }

    public Person updatePerson(Long id, Person updatedPerson) {
        // Check if the person with the given ID exists
        Person existingPerson = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Person not found"));

        // Check if the updated email already exists (excluding the current person)
        // Person existingByEmail = personRepository.findByEmail(updatedPerson.getEmail());
        // if (existingByEmail != null && !existingByEmail.getId().equals(id)) {
        //     throw new DataIntegrityViolationException("Email already exists");
        // }

        // Update the existing person
        existingPerson.setEmail(updatedPerson.getEmail());
        existingPerson.setFirstName(updatedPerson.getFirstName());
        existingPerson.setLastName(updatedPerson.getLastName());

        return personRepository.save(existingPerson);
    }

    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }
}
