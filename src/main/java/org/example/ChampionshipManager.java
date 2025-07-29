package org.example;

import java.util.List;
import java.util.Scanner;

public interface ChampionshipManager {

        void createDriver(Scanner scanner);
        void deleteDriver(Scanner scanner);
        void changeDriver(Scanner scanner);
        void displayStats(String driverName);
        void displayTable();
        void generateRandomRace();
        void saveToFile();
        void loadFromFile();

}
