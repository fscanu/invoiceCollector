package com.fescacomit;

import com.fescacomit.entities.Customer;
import com.fescacomit.entities.CustomerEmail;
import com.fescacomit.repositories.CustomerEmailsRepository;
import com.fescacomit.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ApplicationJpa {

    private static final Logger log = LoggerFactory.getLogger(ApplicationJpa.class);

    public static void main(String[] args) {
        SpringApplication.run(ApplicationJpa.class);
    }

    @Bean
    public CommandLineRunner demo(CustomerRepository repository, CustomerEmailsRepository emailsRepository) {
        return (args) -> {
            // save a couple of customers
            Customer customer1 = repository.save(new Customer("Jack", "Bauer"));
            repository.save(new Customer("Chloe", "O'Brian"));
            repository.save(new Customer("Kim", "Bauer"));
            repository.save(new Customer("David", "Palmer"));
            repository.save(new Customer("Michelle", "Dessler"));

            List<CustomerEmail> customerEmails = new ArrayList<>();

            CustomerEmail customerEmail1 = new CustomerEmail("federico.scanu@gmail.com");
            CustomerEmail customerEmail2 = new CustomerEmail("scanufe@fescacomit.com");
            CustomerEmail customerEmail3 = new CustomerEmail("bwprivate@gmail.com");

            customerEmail1.setCustomer(customer1);
            customerEmail2.setCustomer(customer1);
            customerEmail3.setCustomer(customer1);

            // save a couple of emails
            emailsRepository.save(customerEmail1);
            emailsRepository.save(customerEmail2);
            emailsRepository.save(customerEmail3);

            // set the customer associated to the emails
            customerEmails.add(customerEmail1);
            customerEmails.add(customerEmail2);
            customerEmails.add(customerEmail3);

            // set email collection to the customer
            customer1.setEmails(customerEmails);

            //save the whole customer object
            repository.save(customer1);

            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            repository.findAll().forEach(customer -> log.info(customer.toString()));

            log.info("");

            // fetch an individual customer by ID
            repository.findById(1L)
                    .ifPresent(customer -> {
                        log.info("Customer found with findById(1L):");
                        log.info("--------------------------------");
                        log.info(customer.toString());
                        log.info("");
                    });

            // fetch customers by last name
            log.info("Customer found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            repository.findByLastName("Bauer").forEach(bauer -> log.info(bauer.toString()));
            log.info("");
        };
    }
}
