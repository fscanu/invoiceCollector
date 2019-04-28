package com.fescacomit.entities;

import javax.persistence.*;

@Entity
public class CustomerEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;


    private String emailAddress;

    protected CustomerEmail() {
    }


    public CustomerEmail(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return String.format("CustomerEmail[id=%d, emailAddress='%s']",
                id, emailAddress);
    }
}
