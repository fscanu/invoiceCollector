package com.fescacomit.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String emailsString;

    @OneToMany(
            cascade = {CascadeType.ALL},
            orphanRemoval = true,
            mappedBy = "customer",
            fetch = FetchType.EAGER,
            targetEntity = CustomerEmail.class)
    private List<CustomerEmail> emails = new ArrayList<>();

    protected Customer() {
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailsString = "";
    }

    @Override
    public String toString() {
        String customerString = String.format(
                "Customer[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName);
        emails.forEach(email -> emailsString = emailsString.concat(email.toString()));
        return customerString + emailsString;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<CustomerEmail> getEmails() {
        return emails;
    }

    public void setEmails(List<CustomerEmail> emails) {
        this.emails = emails;
    }
}
