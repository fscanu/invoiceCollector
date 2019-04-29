package com.fescacomit;

import com.fescacomit.service.api.GmailApiService;
import com.fescacomit.service.api.GmailApiServiceImpl;
import com.fescacomit.service.gmail.auth.GmailAuthorizationService;
import com.fescacomit.service.gmail.auth.GmailAuthorizationServiceImpl;
import com.fescacomit.service.gmail.constants.GmailConstants;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api")
public class ApiRestController {
    private static final String template = "Hello, %s!";

    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private final GmailApiService apiService;

    @Autowired
    private final GmailAuthorizationService authorizationService;

    private final Gmail gmailService;

    public ApiRestController() {
        apiService = new GmailApiServiceImpl();
        authorizationService = new GmailAuthorizationServiceImpl();
        gmailService = authorizationService.getGmailService();
    }

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
        final List<Label> labels = apiService.fetchLabels(gmailService, GmailConstants.USER_ID);
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
