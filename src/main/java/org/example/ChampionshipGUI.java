package org.example;

/**import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ChampionshipGUI extends JFrame {
    private  Formula1ChampionshipManager manager;
    private JTable table;

    public ChampionshipGUI(Formula1ChampionshipManager manager) {
        this.manager = manager;
        setTitle("Formula 1 Championship Table");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Buttons
        JButton sortByPointsBtn = new JButton("Sort by Points");
        JButton randomRaceBtn = new JButton("Add Random Race");

        JPanel topPanel = new JPanel();
        topPanel.add(sortByPointsBtn);
        topPanel.add(randomRaceBtn);
        add(topPanel, BorderLayout.NORTH);

        // Table
        table = new JTable();
        refreshTable(manager.getDrivers());
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Actions
        sortByPointsBtn.addActionListener(e -> {
            manager.sortByPoints();
            refreshTable(manager.getDrivers());
        });

        randomRaceBtn.addActionListener(e -> {
            manager.generateRandomRace();
            refreshTable(manager.getDrivers());
        });

        setVisible(true);
    }

    private void refreshTable(List<Formula1Driver> drivers) {
        String[] columns = {"Driver", "Team", "Points", "1st", "2nd", "3rd", "Races"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Formula1Driver d : drivers) {
            Object[] row = {
                    d.getName(),
                    d.getTeam(),
                    d.getPoints(),
                    d.getFirstPositions(),
                    d.getSecondPositions(),
                    d.getThirdPositions(),
                    d.getRacesParticipated()
            };
            model.addRow(row);
        }

        table.setModel(model);
    }
}*/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.time.LocalDate;

public class ChampionshipGUI extends JFrame {

    private Formula1ChampionshipManager manager;
    private JTable table;
    private DefaultTableModel tableModel;

    public ChampionshipGUI(Formula1ChampionshipManager manager) {
        this.manager = manager;
        setTitle("Formula 1 Championship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLayout(new BorderLayout());

        // Table for driver statistics
        String[] columns = {"Driver", "Team", "Points", "1st Places", "2nd", "3rd", "Races"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Top button panel
        JPanel buttonPanel = new JPanel();

        JButton sortByPointsAscBtn = new JButton("Sort by Points ↑");
        JButton sortByWinsBtn = new JButton("Sort by 1st Places");
        JButton randomRaceBtn = new JButton("Random Race");
        JButton probRaceBtn = new JButton("Probabilistic Race");
        JButton viewRacesBtn = new JButton("View Races");
        JButton searchBtn = new JButton("Search Driver Races");

        JTextField searchField = new JTextField(10);

        // Add to button panel
        buttonPanel.add(sortByPointsAscBtn);
        buttonPanel.add(sortByWinsBtn);
        buttonPanel.add(randomRaceBtn);
        buttonPanel.add(probRaceBtn);
        buttonPanel.add(viewRacesBtn);
        buttonPanel.add(searchField);
        buttonPanel.add(searchBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH);

        // Button listeners
        sortByPointsAscBtn.addActionListener(e -> {
            manager.sortDriversByPointsAsc();
            loadDriverTable();
        });

        sortByWinsBtn.addActionListener(e -> {
            manager.sortDriversByWinsDesc();
            loadDriverTable();
        });

        randomRaceBtn.addActionListener(e -> {
            manager.generateRandomRace();
            loadDriverTable();
            JOptionPane.showMessageDialog(this, "Random race generated!");
        });

        probRaceBtn.addActionListener(e -> {
            manager.generateProbabilisticRace();
            loadDriverTable();
            JOptionPane.showMessageDialog(this, "Probabilistic race generated!");
        });

        viewRacesBtn.addActionListener(e -> {
            List<Race> races = manager.getAllRacesSortedByDate();
            StringBuilder sb = new StringBuilder("All Races:\n\n");
            for (Race race : races) {
                sb.append(race.getRaceDate()).append(": ");
                sb.append(race.getRaceResults()).append("\n");
            }
            JTextArea area = new JTextArea(sb.toString());
            area.setEditable(false);
            JScrollPane raceScroll = new JScrollPane(area);
            raceScroll.setPreferredSize(new Dimension(700, 300));
            JOptionPane.showMessageDialog(this, raceScroll, "Race History", JOptionPane.INFORMATION_MESSAGE);
        });

        searchBtn.addActionListener(e -> {
            String driverName = searchField.getText().trim();
            if (driverName.isEmpty()) return;
            List<Race> results = manager.getRacesForDriver(driverName);
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No races found for driver: " + driverName);
                return;
            }
            StringBuilder sb = new StringBuilder("Races for " + driverName + ":\n\n");
         /**   for (Race race : results) {
                Integer pos = race.getPositionOfDriver(driverName);
                sb.append(race.getRaceDate()).append(" - Position: ").append(pos == null ? "DNF" : pos).append("\n");
            }*/
            for (Race race : manager.getAllRacesSortedByDate()) {
                int pos = race.getPositionOfDriver("Hamilton");
                if (pos != -1) {
                    System.out.println("Hamilton finished at position " + pos + " on " + race.getRaceDate());
                }
            }

            JOptionPane.showMessageDialog(this, sb.toString(), "Driver Races", JOptionPane.INFORMATION_MESSAGE);
        });

        loadDriverTable(); // Initial load
        setVisible(true);
    }

    private void loadDriverTable() {
        tableModel.setRowCount(0); // clear
        for (Formula1Driver d : manager.getAllDriversSortedByPointsDesc()) {
            tableModel.addRow(new Object[]{
                    d.getName(),
                    d.getTeam(),
                    d.getPoints(),
                    d.getFirstPositions(),
                    d.getSecondPositions(),
                    d.getThirdPositions(),
                    d.getRacesParticipated()
            });
        }
    }
}

