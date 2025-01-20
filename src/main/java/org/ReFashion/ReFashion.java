package org.ReFashion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReFashion extends JFrame {
    private List<Kleidungsstueck> kleidungsListe;
    private JTable kleidungstabelle;
    private DefaultTableModel tableModel;
    private JTextField anzahlFeld;
    private JTextArea korbArea;
    private JLabel totalPreisLabel;
    private List<KorbEintrag> korb;

    public ReFashion() {
        setTitle("ReFashion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Initialisiere Objekte
        kleidungsListe = new ArrayList<>();
        korb = new ArrayList<>();
        initObjekte();

        // Tabelle und Model erstellen5
        String[] spaltenNamen = {"Name", "Farbe", "Preis", "Größe"};
        tableModel = new DefaultTableModel(spaltenNamen, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Alle Zellen sind nicht editierbar
            }
        };
        kleidungstabelle = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(kleidungstabelle);

        // Filterbereich
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());

        JComboBox<String> filterBox = new JComboBox<>(new String[]{"Alle", "Rot", "Blau", "Schwarz"});
        filterPanel.add(new JLabel("Filter nach Farbe:"));
        filterPanel.add(filterBox);

        JButton filterButton = new JButton("Anwenden");
        filterPanel.add(filterButton);

        JComboBox<String> sortBox = new JComboBox<>(new String[]{"Name", "Preis"});
        filterPanel.add(new JLabel("Sortieren nach:"));
        filterPanel.add(sortBox);

        JButton sortButton = new JButton("Sortieren");
        filterPanel.add(sortButton);

        // Kaufbereich
        JPanel kaufPanel = new JPanel();
        kaufPanel.setLayout(new FlowLayout());

        anzahlFeld = new JTextField(5);
        kaufPanel.add(new JLabel("Anzahl:"));
        kaufPanel.add(anzahlFeld);

        JButton kaufenButton = new JButton("In den Korb legen");
        kaufPanel.add(kaufenButton);

        // Korbbereich
        korbArea = new JTextArea(10, 30);
        korbArea.setEditable(false);
        JScrollPane korbScrollPane = new JScrollPane(korbArea);
        JPanel korbPanel = new JPanel();
        korbPanel.setLayout(new BorderLayout());
        korbPanel.add(new JLabel("Warenkorb:"), BorderLayout.NORTH);
        korbPanel.add(korbScrollPane, BorderLayout.CENTER);

        totalPreisLabel = new JLabel("Totalpreis: 0.00 EUR");
        korbPanel.add(totalPreisLabel, BorderLayout.SOUTH);

        // Hauptpanel erstellen und Komponenten hinzufügen
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(filterPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(kaufPanel, BorderLayout.WEST);
        bottomPanel.add(korbPanel, BorderLayout.EAST);

        // Hauptpanel und Korbpanel zum JFrame hinzufügen
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Events
        filterButton.addActionListener(e -> filterTabelle((String) filterBox.getSelectedItem()));
        sortButton.addActionListener(e -> sortiereTabelle((String) sortBox.getSelectedItem()));
        kaufenButton.addActionListener(e -> fuegeZumKorbHinzu());

        // Tabelle initial auffüllen
        aktualisiereTabelle(kleidungsListe);

        // Sichtbarkeit setzen
        setVisible(true);
    }

    private void initObjekte() {
        kleidungsListe.add(new Kleidungsstueck("T-Shirt", "Rot", 19.99, "M"));
        kleidungsListe.add(new Kleidungsstueck("Hose", "Blau", 39.99, "L"));
        kleidungsListe.add(new Kleidungsstueck("Jacke", "Schwarz", 59.99, "XL"));
        kleidungsListe.add(new Kleidungsstueck("Pullover", "Blau", 29.99, "M"));
        kleidungsListe.add(new Kleidungsstueck("Socken", "Weiß", 5.99, "One Size"));
        kleidungsListe.add(new Kleidungsstueck("Hemd", "Grün", 49.99, "L"));
        kleidungsListe.add(new Kleidungsstueck("Schal", "Rot", 15.99, "One Size"));
        kleidungsListe.add(new Kleidungsstueck("Jeans", "Blau", 45.99, "M"));
        kleidungsListe.add(new Kleidungsstueck("Kapuzenpullover", "Schwarz", 39.99, "XL"));
        kleidungsListe.add(new Kleidungsstueck("Mütze", "Grau", 12.99, "One Size"));
    }

    private void aktualisiereTabelle(List<Kleidungsstueck> liste) {
        tableModel.setRowCount(0); // Tabelle leeren
        for (Kleidungsstueck k : liste) {
            tableModel.addRow(new Object[]{k.getName(), k.getFarbe(), k.getPreis(), k.getGröße()});
        }
    }

    private void filterTabelle(String filter) {
        List<Kleidungsstueck> gefiltert = "Alle".equals(filter) ?
                kleidungsListe :
                kleidungsListe.stream()
                        .filter(k -> k.getFarbe().equalsIgnoreCase(filter))
                        .collect(Collectors.toList());
        aktualisiereTabelle(gefiltert);
    }

    private void sortiereTabelle(String kriterium) {
        kleidungsListe.sort((k1, k2) -> {
            switch (kriterium) {
                case "Name":
                    return k1.getName().compareToIgnoreCase(k2.getName());
                case "Preis":
                    return Double.compare(k1.getPreis(), k2.getPreis());
                default:
                    return 0;
            }
        });
        aktualisiereTabelle(kleidungsListe);
    }

    private void fuegeZumKorbHinzu() {
        int selectedRow = kleidungstabelle.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Kleidungsstück aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String anzahlText = anzahlFeld.getText();
        int anzahl;
        try {
            anzahl = Integer.parseInt(anzahlText);
            if (anzahl <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Bitte geben Sie eine gültige Anzahl ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = (String) tableModel.getValueAt(selectedRow, 0);
        double preis = (double) tableModel.getValueAt(selectedRow, 2);

        korb.add(new KorbEintrag(name, anzahl, preis));
        aktualisiereKorb();
    }

    private void aktualisiereKorb() {
        korbArea.setText("");
        double totalPreis = 0;

        for (KorbEintrag eintrag : korb) {
            korbArea.append(eintrag.getName() + " x " + eintrag.getAnzahl() + " = " + String.format("%.2f", eintrag.getPreis() * eintrag.getAnzahl()) + " EUR\n");
            totalPreis += eintrag.getPreis() * eintrag.getAnzahl();
        }

        totalPreisLabel.setText("Totalpreis: " + String.format("%.2f", totalPreis) + " EUR");
    }



    public static void main(String[] args) {
        new ReFashion();
    }
}

class Kleidungsstueck {
    private String name;
    private String farbe;
    private double preis;
    private String größe;

    public Kleidungsstueck(String name, String farbe, double preis, String größe) {
        this.name = name;
        this.farbe = farbe;
        this.preis = preis;
        this.größe = größe;
    }

    public String getName() {
        return name;
    }

    public String getFarbe() {
        return farbe;
    }

    public double getPreis() {
        return preis;
    }

    public String getGröße() {
        return größe;
    }
}

class KorbEintrag {
    private String name;
    private int anzahl;
    private double preis;

    public KorbEintrag(String name, int anzahl, double preis) {
        this.name = name;
        this.anzahl = anzahl;
        this.preis = preis;
    }

    public String getName() {
        return name;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public double getPreis() {
        return preis;
    }

    public String ausgeben(){
        return "Name:" + name + " Anzahl:" + anzahl + " Preis:" + preis;
    }

}
