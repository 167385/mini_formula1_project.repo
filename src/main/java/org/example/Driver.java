package org.example;

import java.io.Serializable;

public abstract class Driver implements Serializable  {
    private static final long serialVersionUID = 1L;

    protected String name;
    protected String location;
    protected String team;

    public Driver(String name, String location, String team) {
        this.name = name;
        this.location = location;
        this.team = team;
    }

    public abstract void displayStats();

    public void setTeam(String team) {
        this.team = team;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getters and setters
    // ✅ Getters
    public String getName() {
        return name;
    }

    public String getTeam() {
        return team;
    }

}
