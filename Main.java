/* Project: VCRTS
 * Class: Main.java
 * Author: Ben, Zohaib, Joanna, Felix
 * Date: November 1, 2026
 * Starts the program and shows the owner and client form.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        // FlatLaf library for GUI sleek design
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception ex) {
            System.out.println("FlatLaf not found");
        }

        VCController controller = new VCController("VC");

        controller.loadVehicles("owner_transactions.txt");
        controller.loadJobs("client_transactions.txt");

        LoginFrame.open(controller);
    }

    public static void openMainWindow(VCController controller) {

        JFrame frame = new JFrame("VCRTS");
        frame.setSize(480, 640);
        frame.setMinimumSize(new java.awt.Dimension(400, 540));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Zohaib - GUI + Owner Section

        // Back button to return to log in page
        JButton backButton = new JButton("Log Out");

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                LoginFrame.open(controller);
            }
        });

        // Logo
        java.net.URL logoFile = Main.class.getResource("/images/logo.png");
        JLabel title = logoFile != null
                ? new JLabel(new ImageIcon(logoFile))
                : new JLabel("Vehicular Cloud Real Time System");

        JPanel topRow = new JPanel(new java.awt.BorderLayout());
        topRow.add(title, java.awt.BorderLayout.WEST);

        JPanel buttonHolder = new JPanel(new java.awt.GridBagLayout());
        buttonHolder.add(backButton);
        topRow.add(buttonHolder, java.awt.BorderLayout.EAST);

        topRow.setMaximumSize(
                new java.awt.Dimension(
                        Integer.MAX_VALUE,
                        topRow.getPreferredSize().height
                )
        );
        panel.add(topRow);

        JLabel description = new JLabel("Owners rent out their parked cars. Clients submit jobs.");
        panel.add(description);

        JLabel userTypeLabel = new JLabel("Select User Type:");
        panel.add(userTypeLabel);

        String[] options = {"Owner", "Client"};
        JComboBox<String> userType = new JComboBox<String>(options);
        panel.add(userType);

        panel.add(Box.createVerticalStrut(5));

        // Small instruction for Owner
        JLabel ownerNote = new JLabel("Enter vehicle owner information below.");
        panel.add(ownerNote);

        JTextField ownerID = new JTextField();
        JTextField vehicleInfo = new JTextField();
        JTextField licensePlate = new JTextField();
        JTextField vehicleModel = new JTextField();

        // Zohaib - Vehicle type dropdown
        String[] vehicleTypes = {"Sedan", "SUV", "Coupe", "Motorcycle", "Other"};
        JComboBox<String> vehicleType = new JComboBox<String>(vehicleTypes);
        DurationInput residencyTime = new DurationInput();
        JTextField[] ownerFields = {ownerID, vehicleInfo};

        // Zohaib - Owner fields
        JLabel ownerIDLabel = new JLabel("Owner ID:");
        panel.add(ownerIDLabel);
        panel.add(ownerID);

        JLabel vehicleInfoLabel = new JLabel("Vehicle Information:");
        panel.add(vehicleInfoLabel);
        panel.add(vehicleInfo);

        JLabel licensePlateLabel = new JLabel("License Plate:");
        panel.add(licensePlateLabel);
        panel.add(licensePlate);

        JLabel vehicleModelLabel = new JLabel("Vehicle Model:");
        panel.add(vehicleModelLabel);
        panel.add(vehicleModel);

        // Zohaib - Show vehicle type dropdown
        JLabel vehicleTypeLabel = new JLabel("Vehicle Type:");
        panel.add(vehicleTypeLabel);
        panel.add(vehicleType);

        JLabel residencyTimeLabel = new JLabel("Residency Time:");
        panel.add(residencyTimeLabel);
        panel.add(residencyTime);


        // Zohaib - Everything that belongs to the Owner section
        JComponent[] ownerSection = {
                ownerNote,
                ownerIDLabel, ownerID,
                vehicleInfoLabel, vehicleInfo,
                licensePlateLabel, licensePlate,
                vehicleModelLabel, vehicleModel,
                vehicleTypeLabel, vehicleType,
                residencyTimeLabel, residencyTime
        };


        // Felix - Client Section

        JTextField clientID = new JTextField();
        DurationInput jobDuration = new DurationInput();
        JTextField jobDeadline = new JTextField();
        JTextField[] clientFields = {clientID, jobDeadline};

        JLabel clientNote = new JLabel("Enter client information below.");
        panel.add(clientNote);

        // Felix - Client fields
        JLabel clientIDLabel = new JLabel("Client ID:");
        panel.add(clientIDLabel);
        panel.add(clientID);

        JLabel jobDurationLabel = new JLabel("Job Duration:");
        panel.add(jobDurationLabel);
        panel.add(jobDuration);

        JLabel jobDeadlineLabel = new JLabel("Job Deadline (yyyy-MM-dd):");
        panel.add(jobDeadlineLabel);
        panel.add(jobDeadline);

        // Job priority
        JLabel jobPriorityLabel = new JLabel("Job Priority:");
        panel.add(jobPriorityLabel);

        JSpinner jobPriority = new JSpinner(new SpinnerNumberModel(
                Job.DEFAULT_PRIORITY, Job.HIGHEST_PRIORITY, Job.LOWEST_PRIORITY, 1));

        // Keep the box small
        JPanel jobPriorityRow = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        jobPriorityRow.add(jobPriority);
        jobPriorityRow.setMaximumSize(
                new java.awt.Dimension(
                        Integer.MAX_VALUE,
                        jobPriorityRow.getPreferredSize().height
                )
        );
        panel.add(jobPriorityRow);


        // Zohaib - Everything that belongs to the Client section
        JComponent[] clientSection = {
                clientNote,
                clientIDLabel, clientID,
                jobDurationLabel, jobDuration,
                jobDeadlineLabel, jobDeadline,
                jobPriorityLabel, jobPriorityRow
        };


        // Ben - Align elements to the left
        for (java.awt.Component c : panel.getComponents()) {
            JComponent comp = (JComponent) c;
            comp.setAlignmentX(JComponent.LEFT_ALIGNMENT);

            if (comp instanceof JTextField || comp instanceof JComboBox) {
                comp.setMaximumSize(
                        new java.awt.Dimension(
                                Integer.MAX_VALUE,
                                comp.getPreferredSize().height
                        )
                );
            }

            if (comp instanceof JLabel) {
                comp.setBorder(
                        BorderFactory.createEmptyBorder(5, 0, 1, 0)
                );
            }
        }


        // Zohaib - Shows only the user selection
        userType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent s) {

                if (userType.getSelectedItem().equals("Owner")) {

                    frame.setTitle("VCRTS · Owner Panel");

                    // Zohaib - Show Owner section
                    for (JComponent comp : ownerSection) {
                        comp.setVisible(true);
                    }

                    // Hide Client section
                    for (JComponent comp : clientSection) {
                        comp.setVisible(false);
                    }
                }

                else if (userType.getSelectedItem().equals("Client")) {

                    frame.setTitle("VCRTS · Client Panel");

                    // Show Client section
                    for (JComponent comp : clientSection) {
                        comp.setVisible(true);
                    }

                    //  Hide Owner section
                    for (JComponent comp : ownerSection) {
                        comp.setVisible(false);
                    }
                }

                // Refresh the screen so the layout updates
                panel.revalidate();
                panel.repaint();
            }
        });

        // Zohaib - Starts the program with Owner selected
        frame.setTitle("VCRTS · Owner Panel");

        for (JComponent comp : clientSection) {
            comp.setVisible(false);
        }


        // Joanna - File Saving (Now handled by Server)

        JButton saveButton = new JButton("Save");

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // Ben - Field entry checks

                if (userType.getSelectedItem().equals("Owner")) {

                    // Empty fields
                    for (JTextField field : ownerFields) {
                        if (field.getText().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "All fields must be filled before saving", "Save Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    // Owner ID can only have numbers
                    String ownerIDText = ownerID.getText().trim();
                    for (int i = 0; i < ownerIDText.length(); i++) {
                        if (!Character.isDigit(ownerIDText.charAt(i))) {
                            JOptionPane.showMessageDialog(frame, "Owner ID must be a number", "Save Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    ownerID.setText(ownerIDText);

                    // License plate checks
                    String plate = licensePlate.getText().trim().toUpperCase();
                    if (plate.length() < 2 || plate.length() > 8) {
                        JOptionPane.showMessageDialog(frame, "License plate must be 2 to 8 characters", "Save Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    for (int i = 0; i < plate.length(); i++) {
                        char ch = plate.charAt(i);
                        if (!Character.isLetterOrDigit(ch) && ch != ' ' && ch != '-') {
                            JOptionPane.showMessageDialog(frame, "License plate can only have letters, numbers, spaces and dashes", "Save Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    for (int i = 0; i < controller.getVehicles().size(); i++) {
                        if (controller.getVehicles().get(i).getVehicleID().equalsIgnoreCase(plate)) {
                            JOptionPane.showMessageDialog(frame, "A vehicle with this license plate is already registered", "Save Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    licensePlate.setText(plate);

                    // Residency time must be longer than 0
                    if (residencyTime.totalMinutes() <= 0) {
                        JOptionPane.showMessageDialog(frame, "Residency Time must be greater than 0", "Save Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }


                if (userType.getSelectedItem().equals("Client")) {

                    // Empty fields
                    for (JTextField field : clientFields) {
                        if (field.getText().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "All fields must be filled before saving", "Save Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    // Client ID can only have numbers
                    String clientIDText = clientID.getText().trim();
                    for (int i = 0; i < clientIDText.length(); i++) {
                        if (!Character.isDigit(clientIDText.charAt(i))) {
                            JOptionPane.showMessageDialog(frame, "Client ID must be a number", "Save Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    clientID.setText(clientIDText);

                    // Job duration must be longer than 0
                    if (jobDuration.totalMinutes() <= 0) {
                        JOptionPane.showMessageDialog(frame, "Job Duration must be greater than 0", "Save Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Job deadline must be a date that hasn't passed
                    try {
                        if (LocalDate.parse(jobDeadline.getText().trim()).isBefore(LocalDate.now())) {
                            JOptionPane.showMessageDialog(frame, "Job Deadline cannot be in the past", "Save Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (DateTimeParseException ex) {
                        JOptionPane.showMessageDialog(frame, "Job Deadline must be a date (yyyy-MM-dd)", "Save Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // --- CONNECTING TO SERVER ---
                try {
                    if (userType.getSelectedItem().equals("Owner")) {
                        String selectedType = (String) vehicleType.getSelectedItem();
                        
                        // Send to Server!
                        OwnerRequestClient.sendRequest(
                            ownerID.getText(),
                            licensePlate.getText(),
                            vehicleModel.getText(),
                            selectedType,
                            residencyTime.totalMinutes() / 60.0
                        );

                        // Clear fields
                        for (JTextField field : ownerFields) field.setText("");
                        residencyTime.clear();
                        
                    } else if (userType.getSelectedItem().equals("Client")) {
                        // Force the JSpinner to save whatever is currently typed
                        try { jobPriority.commitEdit(); } catch (Exception ex) {}

                        // Connect to server
                        try (java.net.Socket socket = new java.net.Socket("localhost", 5000);
                             java.io.PrintWriter out = new java.io.PrintWriter(socket.getOutputStream(), true)) {
                             
                            out.println("CLIENT");
                            out.println(clientID.getText());
                            out.println(jobDuration.totalMinutes());
                            out.println(jobDeadline.getText().trim());
                            out.println(jobPriority.getValue());
                            
                        } catch (java.io.IOException ex) {
                            throw new RuntimeException("Server connection failed");
                        }

                        // Clear fields after sending
                        for (JTextField field : clientFields) field.setText("");
                        jobDuration.clear();
                        jobPriority.setValue(Job.DEFAULT_PRIORITY);
                    }

                    JOptionPane.showMessageDialog(frame, "Request sent to Server successfully!");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Could not connect to server: " + ex.getMessage(), "Server Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ben - Clear fields button
        JButton clearButton = new JButton("Clear Fields");

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // Clear confirm popup
                int choice = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure you want to clear all fields?",
                        "Clear Fields",
                        JOptionPane.YES_NO_OPTION
                );

                if (choice != JOptionPane.YES_OPTION) {
                    return;
                }

                boolean owner = userType.getSelectedItem().equals("Owner");
                JTextField[] fields = owner ? ownerFields : clientFields;

                for (JTextField field : fields) {
                    field.setText("");
                }

                (owner ? residencyTime : jobDuration).clear();
                jobPriority.setValue(Job.DEFAULT_PRIORITY);
            }
        });

        // Ben - Center Save and Clear buttons
        JPanel buttonRow = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 0));
        
        buttonRow.add(saveButton);
        buttonRow.add(clearButton);

        buttonRow.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        buttonRow.setMaximumSize(
                new java.awt.Dimension(
                        Integer.MAX_VALUE,
                        buttonRow.getPreferredSize().height
                )
        );

        panel.add(Box.createVerticalStrut(15));
        panel.add(buttonRow);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Ben - Separate hours and minutes boxes
    static class DurationInput extends JPanel {

        private final JSpinner hours = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        private final JSpinner minutes = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));

        DurationInput() {
            setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

            add(hours);
            add(new JLabel(" hours   "));
            add(minutes);
            add(new JLabel(" minutes"));

            setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, getPreferredSize().height));
        }

        int totalMinutes() {
            // Keep numbers typed into a box even if the user hasn't clicked away yet
            try {
                hours.commitEdit();
                minutes.commitEdit();
            } catch (java.text.ParseException ex) {
                // Invalid text goes back to last good value
            }

            return (Integer) hours.getValue() * 60 + (Integer) minutes.getValue();
        }

        void clear() {
            hours.setValue(0);
            minutes.setValue(0);
        }

        String describe() {
            int total = totalMinutes();
            return (total / 60) + " hours " + (total % 60) + " minutes";
        }
    }
}