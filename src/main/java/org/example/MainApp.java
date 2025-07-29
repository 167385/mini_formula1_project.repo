package org.example;

import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
       // Formula1ChampionshipManager manager = new Formula1ChampionshipManager();
        Formula1ChampionshipManager manager = new Formula1ChampionshipManager();
        manager.loadFromFile(); //load previous saved data

       // javax.swing.SwingUtilities.invokeLater(() -> new ChampionshipGUI(manager));



        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("""
                \nFormula 1 Championship Menu:
                1. Create Driver
                2. Delete Driver
                3. Change Driver
                4. Display Driver Stats
                5. Display Table
                6. Add Random Race
                7. Save Data
                8. Launch GUI
                0. Exit
            """);

            switch (scanner.nextInt()) {
                case 1 -> manager.createDriver(scanner);
                case 2 -> manager.deleteDriver(scanner);
                case 3 -> manager.changeDriver(scanner);



                case 4 -> {
                    scanner.nextLine(); // clear buffer
                    System.out.print("Enter driver name: ");
                    String name = scanner.nextLine();
                    manager.displayStats(name);
                }
                case 5 -> manager.displayTable();
                case 6 -> manager.generateRandomRace();
                case 7 -> manager.saveToFile();
                case 8 -> javax.swing.SwingUtilities.invokeLater(() -> new ChampionshipGUI(manager));
                case 0 -> {
                    manager.saveToFile();
                    exit = true;
                }
                default -> System.out.println("Invalid input. Try again.");
            }
        }
    }
}
