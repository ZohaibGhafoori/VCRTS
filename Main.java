import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {

    public static void main(String[] args) {

        FlatLightLaf.setup();

    }

    public static void openMainWindow() {

        JFrame frame = new JFrame("VCRTS");
        frame.setSize(480, 640);
        frame.setMinimumSize(new java.awt.Dimension(400, 540));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));


        // Zohaib - GUI + Owner Section

        // Ben - Logo at the top
        java.net.URL logoFile = Main.class.getResource("/images/logo.png");
        JLabel title = logoFile != null
                ? new JLabel(new ImageIcon(logoFile))
                : new JLabel("Vehicular Cloud Real Time System");
        panel.add(title);

        JLabel userTypeLabel = new JLabel("Select User Type:");
        panel.add(userTypeLabel);

        String[] options = {"Owner", "Client"};
        JComboBox<String> userType = new JComboBox<String>(options);
        panel.add(userType);

        // Small space
        panel.add(Box.createVerticalStrut(5));

        // Small instruction for Owner
        JLabel ownerNote = new JLabel("Enter the vehicle owner's information below.");
        panel.add(ownerNote);

        JTextField ownerID = new JTextField();
        JTextField vehicleInfo = new JTextField();
        DurationInput residencyTime = new DurationInput();
        JTextField[] ownerFields = {ownerID, vehicleInfo};

        // Zohaib - Owner fields
        JLabel ownerIDLabel = new JLabel("Owner ID:");
        panel.add(ownerIDLabel);
        panel.add(ownerID);

        JLabel vehicleInfoLabel = new JLabel("Vehicle Information:");
        panel.add(vehicleInfoLabel);
        panel.add(vehicleInfo);

        JLabel residencyTimeLabel = new JLabel("Residency Time:");
        panel.add(residencyTimeLabel);
        panel.add(residencyTime);


        // Zohaib - Everything that belongs to the Owner section
        JComponent[] ownerSection = {
                ownerNote,
                ownerIDLabel, ownerID,
                vehicleInfoLabel, vehicleInfo,
                residencyTimeLabel, residencyTime
        };


        // Felix - Client Section

        JTextField clientID = new JTextField();
        DurationInput jobDuration = new DurationInput();
        JTextField jobDeadline = new JTextField();
        JTextField[] clientFields = {clientID, jobDeadline};

        JLabel clientNote = new JLabel("Enter the client's information below.");
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


        // Zohaib - Everything that belongs to the Client section
        JComponent[] clientSection = {
                clientNote,
                clientIDLabel, clientID,
                jobDurationLabel, jobDuration,
                jobDeadlineLabel, jobDeadline
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


        // Joanna - File Saving + Timestamp

        JButton saveButton = new JButton("Save");

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                // Ben - Field entry checks

                if (userType.getSelectedItem().equals("Owner")) {

                    // Empty fields
                    for (JTextField field : ownerFields) {

                        if (field.getText().trim().isEmpty()) {

                            JOptionPane.showMessageDialog(
                                    frame,
                                    "All fields must be filled before saving",
                                    "Save Error",
                                    JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }
                    }

                    // Residency time must be longer than 0
                    if (residencyTime.totalMinutes() <= 0) {

                        JOptionPane.showMessageDialog(
                                frame,
                                "Residency Time must be greater than 0",
                                "Save Error",
                                JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }
                }


                if (userType.getSelectedItem().equals("Client")) {

                    // Empty fields
                    for (JTextField field : clientFields) {

                        if (field.getText().trim().isEmpty()) {

                            JOptionPane.showMessageDialog(
                                    frame,
                                    "All fields must be filled before saving",
                                    "Save Error",
                                    JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }
                    }

                    // Job duration must be longer than 0
                    if (jobDuration.totalMinutes() <= 0) {

                        JOptionPane.showMessageDialog(
                                frame,
                                "Job Duration must be greater than 0",
                                "Save Error",
                                JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }

                    // Job deadline must be a date that hasn't passed
                    try {

                        if (LocalDate.parse(jobDeadline.getText().trim())
                                .isBefore(LocalDate.now())) {

                            JOptionPane.showMessageDialog(
                                    frame,
                                    "Job Deadline cannot be in the past",
                                    "Save Error",
                                    JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                    } catch (DateTimeParseException ex) {

                        JOptionPane.showMessageDialog(
                                frame,
                                "Job Deadline must be a date (yyyy-MM-dd)",
                                "Save Error",
                                JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }
                }


                try {

                    // Zohaib - Client info goes into this folder 
                    String fileName = "client_transactions.txt";
                    
                    // Zohaib - Owner info goes into this folder
                    if (userType.getSelectedItem().equals("Owner")) {
                        fileName = "owner_transactions.txt";
                    }

                    // Open's the file 
                    FileWriter writer = new FileWriter(fileName, true);

                    writer.write("Time: " + LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");

                    writer.write("User Type: " + userType.getSelectedItem() + "\n");


                    if (userType.getSelectedItem().equals("Owner")) {

                        // Owner info
                        writer.write("Owner ID: " + ownerID.getText() + "\n");
                        writer.write("Vehicle Information: " + vehicleInfo.getText() + "\n");
                        writer.write("Residency Time: " + residencyTime.describe() + "\n");

                        for (JTextField field : ownerFields) {
                            field.setText("");
                        }
                        residencyTime.clear();
                    }


                    if (userType.getSelectedItem().equals("Client")) {

                        // Client info
                        writer.write("Client ID: " + clientID.getText() + "\n");
                        writer.write("Job Duration: " + jobDuration.describe() + "\n");
                        writer.write("Job Deadline: " + jobDeadline.getText() + "\n");

                        for (JTextField field : clientFields) {
                            field.setText("");
                        }
                        jobDuration.clear();
                    }

                    writer.write("----------------------\n");

                    // Close the file when we are done writing
                    writer.close();

                    JOptionPane.showMessageDialog(
                            frame,
                            "Information saved"
                    );

                } catch (IOException ex) {

                    JOptionPane.showMessageDialog(
                            frame,
                            "Could not save to file: " + ex.getMessage(),
                            "Save Error",
                            JOptionPane.ERROR_MESSAGE
                    );
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


    // Ben - Separate hours and minutes boxes, so users don't have to type 8.5 for 8h 30m
    static class DurationInput extends JPanel {

        private final JSpinner hours = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        private final JSpinner minutes = new JSpinner(new SpinnerNumberModel(0, 0, 59, 5));

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
                // Invalid text goes back to the last good value
            }

            return (Integer) hours.getValue() * 60 + (Integer) minutes.getValue();
        }

        void clear() {
            hours.setValue(0);
            minutes.setValue(0);
        }

        // Example: "8 hours 30 minutes"
        String describe() {
            int total = totalMinutes();
            return (total / 60) + " hours " + (total % 60) + " minutes";
        }
    }
}