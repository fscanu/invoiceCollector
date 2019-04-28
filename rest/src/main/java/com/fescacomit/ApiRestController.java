package com.fescacomit;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Session insertUser(@RequestBody User user) {
        Session session = new Session();
        session.setUser(user);
        return session;

    }

    @RequestMapping(value = "/user/linkgmail", method = RequestMethod.POST)
    public User linkgmail(@RequestBody User user) {
        return user;
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public User user(@RequestBody Session user) {
        return new User(user.getUser().getUsername(), user.getUser().getUsername(), "ddqsd", "dsqdqsd");
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public User login(@RequestBody Session user) {
        return new User(user.getUser().getUsername(), user.getUser().getUsername(), "ddqsd", "dsqdqsd");

    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public User saveUser(@RequestBody Session user) {
        return new User(user.getUser().getUsername(), user.getUser().getUsername(), "ddqsd", "dsqdqsd");

    }
}
