package org.example;

import java.io.Serializable;

public class Formula1Driver extends Driver implements Serializable {
    private static final long serialVersionUID = 1L;

    private int firstPositions;
    private int secondPositions;
    private int thirdPositions;
    private int points;


    private int racesParticipated;

    public Formula1Driver(String name, String location, String team) {
        super(name, location, team);
    }




    public void updateStats(int position) {
       this.racesParticipated++;
        switch (position) {
            case 1 -> {
                firstPositions++;
                points += 25;
            }
            case 2 -> {
                secondPositions++;
                points += 18;
            }
            case 3 -> {
                thirdPositions++;
                points += 15;
            }
            case 4 -> points += 12;
            case 5 -> points += 10;
            case 6 -> points += 8;
            case 7 -> points += 6;
            case 8 -> points += 4;
            case 9 -> points += 2;
            case 10 -> points += 1;
        }
    }



    public int getFirstPositions() {
        return firstPositions;
    }

    public int getSecondPositions() {
        return secondPositions;
    }

    public int getThirdPositions() {
        return thirdPositions;
    }

    public int getPoints() {
        return points;
    }
    public int getRacesParticipated() {
        return racesParticipated;
    }

    public String getName() {
        return name;
    }

    public String getTeam() {
        return team;
    }

    public String getLocation()
    {
        return location;
    }
    @Override
    public void displayStats() {
        System.out.println("Driver: " + name + ", Team: " + team + ", Points: " + points);
    }

    // Getters, setters, toString...
}
