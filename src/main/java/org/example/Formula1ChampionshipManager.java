package org.example;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Formula1ChampionshipManager implements ChampionshipManager {
    private List<Formula1Driver> drivers = new ArrayList<>();
    private List<Race> races = new ArrayList<>();

    public List<Formula1Driver> getDrivers() {
        return drivers;
    }

    public void sortByPoints() {
        drivers.sort(Comparator
                .comparingInt(Formula1Driver::getPoints)
                .thenComparingInt(Formula1Driver::getFirstPositions)
                .reversed()
        );
    }

    @Override
    public void createDriver(Scanner scanner) {
        scanner.nextLine(); // flush any leftover input

        System.out.print("Enter driver's name: ");
        String name = scanner.nextLine();

        System.out.print("Enter driver's location: ");
        String location = scanner.nextLine();

        System.out.print("Enter team name (must be unique): ");
        String team = scanner.nextLine();

        // Check if team already exists
        for (Formula1Driver d : drivers) {
            if (d.getTeam().equalsIgnoreCase(team)) {
                System.out.println("A driver for this team already exists.");
                return;
            }
        }

        Formula1Driver newDriver = new Formula1Driver(name, location, team);
        drivers.add(newDriver);
        System.out.println("Driver created and added to championship.");
    }


    @Override
    public void deleteDriver(Scanner scanner) {
        scanner.nextLine(); // flush any leftover input

        System.out.print("Enter the team name of the driver to delete: ");
        String teamName = scanner.nextLine();

        Formula1Driver toRemove = null;

        for (Formula1Driver driver : drivers) {
            if (driver.getTeam().equalsIgnoreCase(teamName)) {
                toRemove = driver;
                break;
            }
        }

        if (toRemove != null) {
            drivers.remove(toRemove);
            System.out.println(" Driver and team '" + teamName + "' have been removed from the championship.");
        } else {
            System.out.println("No driver found with team name: " + teamName);
        }
    }

    @Override
    public void changeDriver(Scanner scanner) {
        scanner.nextLine(); // flush input

        System.out.print("Enter the team name you want to change the driver for: ");
        String teamName = scanner.nextLine();

        Formula1Driver existingDriver = null;

        for (Formula1Driver d : drivers) {
            if (d.getTeam().equalsIgnoreCase(teamName)) {
                existingDriver = d;
                break;
            }
        }

        if (existingDriver != null) {
            System.out.print("Enter the new driver's name: ");
            String newName = scanner.nextLine();

            System.out.print("Enter the new driver's location: ");
            String newLocation = scanner.nextLine();

            // OPTION 1: Just update name and location (keep stats)
            existingDriver.setName(newName);
            existingDriver.setLocation(newLocation);

            System.out.println(" Driver for team '" + teamName + "' has been changed to " + newName);


        } else {
            System.out.println(" No team found with name: " + teamName);
        }
    }



    /**  private Formula1Driver findDriverByName(String name) {
        for (Formula1Driver d : drivers) {
            if (d.getName().equalsIgnoreCase(name)) return d;
        }
        return null;
    }*/
    public void displayStats(String driverName) {
        for (Formula1Driver driver : drivers) {
            if (driver.getName().equalsIgnoreCase(driverName)) {
                driver.displayStats();
                return;
            }
        }
        System.out.println("Driver not found.");
    }



    @Override
    public void displayTable() {
        if (drivers.isEmpty()) {
            System.out.println("No drivers in the championship.");
            return;
        }

        // Sort by points descending, then first positions descending
        drivers.sort(Comparator
                .comparing(Formula1Driver::getPoints)
                .thenComparing(Formula1Driver::getFirstPositions)
                .reversed());

        System.out.printf("%-20s %-20s %-10s %-10s %-10s %-10s %-10s\n",
                "Driver Name", "Team", "Points", "1st", "2nd", "3rd", "Races");

        for (Formula1Driver driver : drivers) {
            System.out.printf("%-20s %-20s %-10d %-10d %-10d %-10d %-10d\n",
                    driver.getName(),
                    driver.getTeam(),
                    driver.getPoints(),
                    driver.getFirstPositions(),
                    driver.getSecondPositions(),
                    driver.getThirdPositions(),
                    driver.getRacesParticipated());
        }
    }


    public void saveToFile() {
        File file = new File("store_data.txt");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(drivers != null ? drivers : new ArrayList<Formula1Driver>());
            out.writeObject(races != null ? races : new ArrayList<Race>());
            out.flush(); // Ensure all data is written
            System.out.println(" Data saved to file: " + file.getAbsolutePath() + ", Drivers: " + (drivers != null ? drivers.size() : 0) + ", Races: " + (races != null ? races.size() : 0));
        } catch (NotSerializableException e) {
            System.err.println(" Serialization error: " + e.getMessage() + ". Ensure Formula1Driver and Race implement Serializable.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println(" IO error saving data to " + file.getAbsolutePath() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        File file = new File("store_data.txt");
        if (!file.exists()) {
            System.out.println("ℹ No previous save file found: " + file.getAbsolutePath());
            drivers = new ArrayList<>();
            races = new ArrayList<>();
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object obj1 = in.readObject();
            Object obj2 = in.readObject();
            if (obj1 instanceof List<?> && obj2 instanceof List<?>) {
                drivers = (List<Formula1Driver>) obj1;
                races = (List<Race>) obj2;
                System.out.println(" Data loaded from file: " + drivers.size() + " drivers, " + races.size() + " races.");
            } else {
                throw new ClassCastException("Unexpected object types in file.");
            }
        } catch (IOException e) {
            System.err.println(" IO error loading data: " + e.getMessage());
            e.printStackTrace();
            drivers = new ArrayList<>();
            races = new ArrayList<>();
        } catch (ClassNotFoundException e) {
            System.err.println(" Class not found: " + e.getMessage());
            e.printStackTrace();
            drivers = new ArrayList<>();
            races = new ArrayList<>();
        } catch (ClassCastException e) {
            System.err.println(" Type mismatch: " + e.getMessage());
            e.printStackTrace();
            drivers = new ArrayList<>();
            races = new ArrayList<>();
        }
    }


    public void generateRandomRace() {
        Collections.shuffle(drivers);
        LinkedHashMap<Integer, Formula1Driver> results = new LinkedHashMap<>();

        for (int i = 0; i < drivers.size(); i++) {
            Formula1Driver driver = drivers.get(i);
            int position = i + 1;
            if (position <= 10) driver.updateStats(position);
            results.put(position, driver);
        }

        Race newRace = new Race(LocalDate.now(), results);
        races.add(newRace);
        // 🏆 Show winner
        Formula1Driver winner = newRace.getDriverByPosition(1);
        if (winner != null) {
            System.out.println(" Winner was: " + winner.getName());
        }

        System.out.println("Random race generated:\n" + newRace);
       // System.out.println("Random race generated:\n" + newRace);
    }

    private Formula1Driver pickWinnerByProbability(List<Formula1Driver> shuffled) {
        double r = Math.random();

        if (r < 0.40) return shuffled.get(0);   // 40% for position 1
        else if (r < 0.70) return shuffled.get(1); // 30% for position 2
        else if (r < 0.80) return shuffled.get(2); // 10% for position 3
        else if (r < 0.90) return shuffled.get(3); // 10% for position 4
        else if (r < 0.92) return shuffled.get(4); // 2%
        else if (r < 0.94) return shuffled.get(5); // 2%
        else if (r < 0.96) return shuffled.get(6); // 2%
        else if (r < 0.98) return shuffled.get(7); // 2%
        else if (r < 1.00) return shuffled.get(8); // 2%
        return shuffled.get(9); // fallback
    }


    public void generateProbabilisticRace() {
        if (drivers.size() < 10) {
            System.out.println("❗ Not enough drivers to generate a race (at least 10 required).");
            return;
        }

        List<Formula1Driver> shuffled = new ArrayList<>(drivers);
        Collections.shuffle(shuffled); // Random starting grid

        Formula1Driver winner = pickWinnerByProbability(shuffled);

        // Prepare race results map
        LinkedHashMap<Integer, Formula1Driver> results = new LinkedHashMap<>();
        results.put(1, winner);

        // Fill remaining positions randomly
        List<Formula1Driver> rest = new ArrayList<>(shuffled);
        rest.remove(winner);
        Collections.shuffle(rest);

        int pos = 2;
        for (Formula1Driver d : rest) {
            results.put(pos++, d);
        }

        // Update stats and points
        for (Map.Entry<Integer, Formula1Driver> entry : results.entrySet()) {
            int position = entry.getKey();
            Formula1Driver driver = entry.getValue();
            if (position <= 10) driver.updateStats(position);
        }

        Race newRace = new Race(LocalDate.now(), results);
        races.add(newRace);
        System.out.println(" Probabilistic race generated:\n" + newRace);
    }
    public List<Formula1Driver> getAllDriversSortedByPointsDesc() {
        return drivers.stream()
                .sorted(Comparator.comparingInt(Formula1Driver::getPoints)
                        .thenComparingInt(Formula1Driver::getFirstPositions)
                        .reversed())
                .toList();
    }
    public List<Race> getAllRacesSortedByDate() {
        return races.stream()
                .sorted(Comparator.comparing(Race::getRaceDate))
                .toList();
    }


    public List<Race> getRacesForDriver(String driverName) {
        List<Race> result = new ArrayList<>();

        for (Race race : races) {
            for (Formula1Driver driver : race.getRaceResults().values()) {
                if (driver.getName().equalsIgnoreCase(driverName)) {
                    result.add(race);
                    break;
                }
            }
        }

        return result;
    }
    public List<Formula1Driver> sortDriversByPointsAsc() {
        return drivers.stream()
                .sorted(Comparator.comparingInt(Formula1Driver::getPoints))
                .toList();
    }
    public List<Formula1Driver> sortDriversByWinsDesc() {
        return drivers.stream()
                .sorted(Comparator.comparingInt(Formula1Driver::getFirstPositions).reversed())
                .toList();
    }

}

