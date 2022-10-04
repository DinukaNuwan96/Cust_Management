package com.customermanagement.CustomerManagement;

import com.customermanagement.CustomerManagement.model.Customer;
import com.customermanagement.CustomerManagement.model.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
class Initializer implements CommandLineRunner {

    private final CustomerRepository repository;

    public Initializer(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) {
        Stream.of("a","b","c").forEach(name ->
                repository.save(new Customer(name))
        );

        repository.findAll().forEach(System.out::println);
    }
}