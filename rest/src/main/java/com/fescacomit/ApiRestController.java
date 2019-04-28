package com.fescacomit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api")
public class ApiRestController {

    private static final String     template = "Hello, %s!";
    private final        AtomicLong counter  = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public User user(@RequestBody UserToSave user) {
        return new User(user.getUser().getUsername(), user.getUser().getUsername(), "ddqsd", "dsqdqsd");
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public User login(@RequestBody UserToSave user) {
        return new User(user.getUser().getUsername(), user.getUser().getUsername(), "ddqsd", "dsqdqsd");

    }

    @RequestMapping(value = "/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserToSave insertUser(@RequestBody String jsonString) throws IOException {
        UserToSave user = new ObjectMapper().readValue(jsonString, UserToSave.class);
        return user;

    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public User saveUser(@RequestBody UserToSave user) {
        return new User(user.getUser().getUsername(), user.getUser().getUsername(), "ddqsd", "dsqdqsd");

    }
}
