package com.fescacomit.repositories;


import com.fescacomit.entities.CustomerEmail;
import org.springframework.data.repository.CrudRepository;

public interface CustomerEmailsRepository extends CrudRepository<CustomerEmail, Long> {
}
