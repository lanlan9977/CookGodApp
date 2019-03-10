package com.cookgod.broadcast;


import java.util.Set;

public class State {
    private String type;
    private String user;
    private Set<String> users;

    public State() {
    }

    public State(String type, String user, Set<String> users) {
        this.type = type;
        this.user = user;
        this.users = users;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
