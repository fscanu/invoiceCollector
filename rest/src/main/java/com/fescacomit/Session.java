package com.fescacomit;

public class Session {

    private User user;

    public Session() {
        super();
    }

    public Session(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
