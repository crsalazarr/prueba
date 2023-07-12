/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package p;

/**
 *
 * @author csala
 */
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HospitalVaccinationApp extends JFrame implements ActionListener {
    private JTextField nameTextField;
    private JComboBox<Integer> modernaComboBox;
    private JComboBox<Integer> sinovacComboBox;
    private JComboBox<Integer> pfizerComboBox;
    private JDateChooser dateChooser;
    private JTextArea savedDataTextArea;
    private JPanel resultPanel;
    private List<Patient> patients;
    private JButton saveButton;

    public HospitalVaccinationApp() {
        setTitle("Hospital Vaccination App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2));
        JLabel nameLabel = new JLabel("Nombre Completo:");
        nameTextField = new JTextField();

        JLabel vaccinesLabel = new JLabel("Vacunas:");
        modernaComboBox = new JComboBox<>(new Integer[]{0, 1, 2, 3});
        sinovacComboBox = new JComboBox<>(new Integer[]{0, 1, 2, 3});
        pfizerComboBox = new JComboBox<>(new Integer[]{0, 1, 2, 3});

        JLabel dateLabel = new JLabel("Fecha:");
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yy");
        dateChooser.getDateEditor().addPropertyChangeListener("date", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                enableSaveButton(dateChooser.getDate() != null);
            }
        });

        inputPanel.add(nameLabel);
        inputPanel.add(nameTextField);
        inputPanel.add(vaccinesLabel);
        inputPanel.add(new JLabel()); // Empty label for spacing
        inputPanel.add(createVaccinePanel("Moderna", modernaComboBox));
        inputPanel.add(createVaccinePanel("Sinovac", sinovacComboBox));
        inputPanel.add(createVaccinePanel("Pfizer", pfizerComboBox));
        inputPanel.add(dateLabel);
        inputPanel.add(dateChooser);

        saveButton = new JButton("Guardar");
        saveButton.addActionListener(this);
        saveButton.setEnabled(false); // Inicialmente deshabilitado

        JButton resultButton = new JButton("Resultado");
        resultButton.addActionListener(this);
        JButton clearButton = new JButton("Limpiar");
        clearButton.addActionListener(this);
        JButton exitButton = new JButton("Salir");
        exitButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(resultButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);

        savedDataTextArea = new JTextArea(10, 20);
        savedDataTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(savedDataTextArea);

        resultPanel = new JPanel(); // Panel para mostrar el resultado
        resultPanel.setLayout(new BorderLayout());

        patients = new ArrayList<>();

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Guardar")) {
            String name = nameTextField.getText();
            int modernaDoses = (Integer) modernaComboBox.getSelectedItem();
            int sinovacDoses = (Integer) sinovacComboBox.getSelectedItem();
            int pfizerDoses = (Integer) pfizerComboBox.getSelectedItem();
            Date selectedDate = dateChooser.getDate();
            String date = formatDate(selectedDate);

            Patient patient = new Patient(name, modernaDoses, sinovacDoses, pfizerDoses, date);
            patients.add(patient);

            savedDataTextArea.append("Nombre: " + name + "\n");
            savedDataTextArea.append("Vacunas:\n");
            savedDataTextArea.append("Moderna: " + modernaDoses + " dosis\n");
            savedDataTextArea.append("Sinovac: " + sinovacDoses + " dosis\n");
            savedDataTextArea.append("Pfizer: " + pfizerDoses + " dosis\n");
            savedDataTextArea.append("Fecha: " + date + "\n\n");

            nameTextField.setText("");
            modernaComboBox.setSelectedIndex(0);
            sinovacComboBox.setSelectedIndex(0);
            pfizerComboBox.setSelectedIndex(0);
            dateChooser.setDate(null);

            enableSaveButton(false); // Deshabilitar el botón después de guardar

        } else if (e.getActionCommand().equals("Resultado")) {
            createResultPanel();
        } else if (e.getActionCommand().equals("Regresar")) {
            removeResultPanel();
        } else if (e.getActionCommand().equals("Limpiar")) {
            clearData();
        } else if (e.getActionCommand().equals("Salir")) {
            System.exit(0);
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        return sdf.format(date);
    }

    private JPanel createVaccinePanel(String vaccineName, JComboBox<Integer> comboBox) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(vaccineName + ":");
        panel.add(label, BorderLayout.WEST);
        panel.add(comboBox, BorderLayout.CENTER);
        return panel;
    }

    private void createResultPanel() {
        resultPanel.removeAll();
        resultPanel.add(new VennDiagramPanel(patients));
        resultPanel.add(createRegresarButton(), BorderLayout.SOUTH);
        getContentPane().add(resultPanel, BorderLayout.CENTER);
        getContentPane().validate();
        getContentPane().repaint();
    }

    private void removeResultPanel() {
        getContentPane().remove(resultPanel);
        getContentPane().validate();
        getContentPane().repaint();
    }

    private JButton createRegresarButton() {
        JButton regresarButton = new JButton("Regresar");
        regresarButton.addActionListener(this);
        return regresarButton;
    }

    private void clearData() {
        nameTextField.setText("");
        modernaComboBox.setSelectedIndex(0);
        sinovacComboBox.setSelectedIndex(0);
        pfizerComboBox.setSelectedIndex(0);
        dateChooser.setDate(null);
        savedDataTextArea.setText("");
        patients.clear();
        removeResultPanel();
        enableSaveButton(false); // Deshabilitar el botón después de limpiar
    }

    private void enableSaveButton(boolean enabled) {
        saveButton.setEnabled(enabled);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HospitalVaccinationApp::new);
    }
}

class Patient {
    private String name;
    private int modernaDoses;
    private int sinovacDoses;
    private int pfizerDoses;
    private String date;

    public Patient(String name, int modernaDoses, int sinovacDoses, int pfizerDoses, String date) {
        this.name = name;
        this.modernaDoses = modernaDoses;
        this.sinovacDoses = sinovacDoses;
        this.pfizerDoses = pfizerDoses;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public int getModernaDoses() {
        return modernaDoses;
    }

    public int getSinovacDoses() {
        return sinovacDoses;
    }

    public int getPfizerDoses() {
        return pfizerDoses;
    }

    public String getDate() {
        return date;
    }
}

class VennDiagramPanel extends JPanel {
    private List<Patient> patients;
    private int countModernaSinovac;
    private int countModernaPfizer;
    private int countSinovacPfizer;
    private int countModernaSinovacPfizer;
    private int countModernaOnly;
    private int countSinovacOnly;
    private int countPfizerOnly;

    public VennDiagramPanel(List<Patient> patients) {
        this.patients = patients;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (patients != null) {
            countModernaSinovac = 0;
            countModernaPfizer = 0;
            countSinovacPfizer = 0;
            countModernaSinovacPfizer = 0;
            countModernaOnly = 0;
            countSinovacOnly = 0;
            countPfizerOnly = 0;

            for (Patient patient : patients) {
                int modernaDoses = patient.getModernaDoses();
                int sinovacDoses = patient.getSinovacDoses();
                int pfizerDoses = patient.getPfizerDoses();

                if (modernaDoses > 0 && sinovacDoses > 0 && pfizerDoses == 0) {
                    countModernaSinovac++;
                } else if (modernaDoses > 0 && sinovacDoses == 0 && pfizerDoses > 0) {
                    countModernaPfizer++;
                } else if (modernaDoses == 0 && sinovacDoses > 0 && pfizerDoses > 0) {
                    countSinovacPfizer++;
                } else if (modernaDoses > 0 && sinovacDoses > 0 && pfizerDoses > 0) {
                    countModernaSinovacPfizer++;
                } else if (modernaDoses > 0 && sinovacDoses == 0 && pfizerDoses == 0) {
                    countModernaOnly++;
                } else if (modernaDoses == 0 && sinovacDoses > 0 && pfizerDoses == 0) {
                    countSinovacOnly++;
                } else if (modernaDoses == 0 && sinovacDoses == 0 && pfizerDoses > 0) {
                    countPfizerOnly++;
                }
            }

            int width = getWidth();
            int height = getHeight();
            int circleSize = Math.min(width, height) / 2;

            int centerX = width / 2;
            int centerY = height / 2;

            int radius = (int) (circleSize * 0.4);

            // Dibujar el contorno de los círculos
            g.setColor(Color.RED);
            g.drawOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
            g.setColor(Color.BLUE);
            g.drawOval(centerX, centerY - radius, 2 * radius, 2 * radius);
            g.setColor(Color.GREEN);
            g.drawOval(centerX - radius / 2, centerY, 2 * radius, 2 * radius);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString(Integer.toString(countModernaSinovac), centerX - 30, centerY - radius + 130);
            g.drawString(Integer.toString(countModernaPfizer), centerX + 20, centerY - radius + 40);
            g.drawString(Integer.toString(countSinovacPfizer), centerX + 90, centerY + 50);
            g.drawString(Integer.toString(countModernaSinovacPfizer), centerX + 20, centerY + 20);
            
            g.drawString(Integer.toString(countModernaOnly), centerX -40, centerY - 20);
            g.drawString(Integer.toString(countSinovacOnly), centerX+30, centerY+100);
            g.drawString(Integer.toString(countPfizerOnly), centerX+120, centerY-20);

            g.drawString("Moderna", centerX - 40, centerY - radius - 40);
            g.drawString("Pfizer", centerX + radius - 20, centerY - radius - 40);
            g.drawString("Sinovac", centerX - radius / 2 - 30, centerY + radius + 80);
        }
    }
}