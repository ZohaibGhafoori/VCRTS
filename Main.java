import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("VCRTS");
        frame.setSize(350, 500);
        frame.setMinimumSize(frame.getSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));


        // Zohaib - GUI + Owner Section

        JLabel title = new JLabel("Vehicular Cloud Real Time System");
        panel.add(title);

        JLabel userTypeLabel = new JLabel("Select User Type:");
        panel.add(userTypeLabel);

        String[] options = {"Owner", "Client"};
        JComboBox<String> userType = new JComboBox<String>(options);
        panel.add(userType);

        // Small space
        panel.add(Box.createVerticalStrut(5));

        // Owner section
        JLabel ownerLabel = new JLabel("Owner Information");
        panel.add(ownerLabel);

        // Small instruction for Owner
        JLabel ownerNote = new JLabel("Enter the vehicle owner's information below.");
        panel.add(ownerNote);

        JTextField ownerID = new JTextField();
        JTextField vehicleInfo = new JTextField();
        JTextField residencyTime = new JTextField();
        JTextField[] ownerFields = {ownerID, vehicleInfo, residencyTime};

        // Zohaib - Owner fields
        JLabel ownerIDLabel = new JLabel("Owner ID:");
        panel.add(ownerIDLabel);
        panel.add(ownerID);

        JLabel vehicleInfoLabel = new JLabel("Vehicle Information:");
        panel.add(vehicleInfoLabel);
        panel.add(vehicleInfo);

        JLabel residencyTimeLabel = new JLabel("Residency Time (hours):");
        panel.add(residencyTimeLabel);
        panel.add(residencyTime);

        // Button to clear Owner information
        JButton clearOwnerButton = new JButton("Clear Owner Fields");

        clearOwnerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ownerID.setText("");
                vehicleInfo.setText("");
                residencyTime.setText("");
            }
        });

        panel.add(clearOwnerButton);

        // Zohaib - Everything that belongs to the Owner section
        JComponent[] ownerSection = {
                ownerLabel, ownerNote,
                ownerIDLabel, ownerID,
                vehicleInfoLabel, vehicleInfo,
                residencyTimeLabel, residencyTime,
                clearOwnerButton
        };


        // Felix - Client Section

        JTextField clientID = new JTextField();
        JTextField jobDuration = new JTextField();
        JTextField jobDeadline = new JTextField();
        JTextField[] clientFields = {clientID, jobDuration, jobDeadline};

        // Zohaib - Client section heading and note
        JLabel clientLabel = new JLabel("Client Information");
        panel.add(clientLabel);

        JLabel clientNote = new JLabel("Enter the client's information below.");
        panel.add(clientNote);

        // Zohaib - Client fields
        JLabel clientIDLabel = new JLabel("Client ID:");
        panel.add(clientIDLabel);
        panel.add(clientID);

        JLabel jobDurationLabel = new JLabel("Job Duration:");
        panel.add(jobDurationLabel);
        panel.add(jobDuration);

        JLabel jobDeadlineLabel = new JLabel("Job Deadline (yyyy-MM-dd):");
        panel.add(jobDeadlineLabel);
        panel.add(jobDeadline);

        // Clear button for Client fields
        JButton clearClientButton = new JButton("Clear Client Fields");

        clearClientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clientID.setText("");
                jobDuration.setText("");
                jobDeadline.setText("");
            }
        });

        panel.add(clearClientButton);

        // Zohaib - Everything that belongs to the Client section
        JComponent[] clientSection = {
                clientLabel, clientNote,
                clientIDLabel, clientID,
                jobDurationLabel, jobDuration,
                jobDeadlineLabel, jobDeadline,
                clearClientButton
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

                    // Residency time must be a positive number
                    try {

                        if (Double.parseDouble(residencyTime.getText().trim()) <= 0) {

                            JOptionPane.showMessageDialog(
                                    frame,
                                    "Residency Time must be greater than 0",
                                    "Save Error",
                                    JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                    } catch (NumberFormatException ex) {

                        JOptionPane.showMessageDialog(
                                frame,
                                "Residency Time must be a number",
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

                    // Job duration must be a positive number
                    try {

                        if (Double.parseDouble(jobDuration.getText().trim()) <= 0) {

                            JOptionPane.showMessageDialog(
                                    frame,
                                    "Job Duration must be greater than 0",
                                    "Save Error",
                                    JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }

                    } catch (NumberFormatException ex) {

                        JOptionPane.showMessageDialog(
                                frame,
                                "Job Duration must be a number",
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

                    // Open the file (true means add to the end of the file)
                    FileWriter writer = new FileWriter("transactions.txt", true);

                    writer.write("Time: " + LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");

                    writer.write("User Type: " + userType.getSelectedItem() + "\n");


                    if (userType.getSelectedItem().equals("Owner")) {

                        // Owner info
                        writer.write("Owner ID: " + ownerID.getText() + "\n");
                        writer.write("Vehicle Information: " + vehicleInfo.getText() + "\n");
                        writer.write("Residency Time: " + residencyTime.getText() + "\n");

                        for (JTextField field : ownerFields) {
                            field.setText("");
                        }
                    }


                    if (userType.getSelectedItem().equals("Client")) {

                        // Client info
                        writer.write("Client ID: " + clientID.getText() + "\n");
                        writer.write("Job Duration: " + jobDuration.getText() + "\n");
                        writer.write("Job Deadline: " + jobDeadline.getText() + "\n");

                        for (JTextField field : clientFields) {
                            field.setText("");
                        }
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

        panel.add(saveButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}