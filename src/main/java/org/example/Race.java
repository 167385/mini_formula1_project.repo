package org.example;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class Race implements Serializable {
    private static final long serialVersionUID = 1L;

    private final LocalDate raceDate;

    // LinkedHashMap maintains insertion order (e.g., position -> driver)
    private final LinkedHashMap<Integer, Formula1Driver> raceResults;

    public Race(LocalDate raceDate, LinkedHashMap<Integer, Formula1Driver> raceResults) {
        this.raceDate = raceDate;
        this.raceResults = raceResults;
    }

    public LocalDate getRaceDate() {
        return raceDate;
    }

    public Map<Integer, Formula1Driver> getRaceResults() {
        return raceResults;
    }

    public Formula1Driver getDriverByPosition(int position) {
        return raceResults.get(position);
    }
    public int getPositionOfDriver(String driverName) {
        for (Map.Entry<Integer, Formula1Driver> entry : raceResults.entrySet()) {
            if (entry.getValue().getName().equalsIgnoreCase(driverName)) {
                return entry.getKey(); // Return the race position (1-based)
            }
        }
        return -1; // Not found
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Race Date: " + raceDate + "\n");
        for (Map.Entry<Integer, Formula1Driver> entry : raceResults.entrySet()) {
            sb.append("Position ").append(entry.getKey()).append(": ")
                    .append(entry.getValue().getName())
                    .append(" (").append(entry.getValue().getTeam()).append(")\n");
        }
        return sb.toString();
    }
}

