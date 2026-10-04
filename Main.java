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
        JComboBox<String> userType = new JComboBox<>(options);
        panel.add(userType);

        JTextField ownerID = new JTextField();
        JTextField vehicleInfo = new JTextField();
        JTextField residencyTime = new JTextField();
        JTextField[] ownerFields = {ownerID, vehicleInfo, residencyTime};

        panel.add(new JLabel("Owner ID:"));
        panel.add(ownerID);

        panel.add(new JLabel("Vehicle Information:"));
        panel.add(vehicleInfo);

        panel.add(new JLabel("Residency Time (hours):"));
        panel.add(residencyTime);

        JButton clearOwnerButton = new JButton("Clear Owner Fields");

        clearOwnerButton.addActionListener(e -> {
            ownerID.setText("");
            vehicleInfo.setText("");
            residencyTime.setText("");
        });

        panel.add(clearOwnerButton);



       // Felix - Client Section

        JTextField clientID = new JTextField();
        JTextField jobDuration = new JTextField();
        JTextField jobDeadline = new JTextField();
        JTextField[] clientFields = {clientID, jobDuration, jobDeadline};

        panel.add(new JLabel("Client ID:"));
        panel.add(clientID);

        panel.add(new JLabel("Job Duration:"));
        panel.add(jobDuration);

        panel.add(new JLabel("Job Deadline (yyyy-MM-dd):"));
        panel.add(jobDeadline);

        // Clear button for Client fields
        JButton clearClientButton = new JButton("Clear Client Fields");

        clearClientButton.addActionListener(e -> {
            clientID.setText("");
            jobDuration.setText("");
            jobDeadline.setText("");
        });

        panel.add(clearClientButton);

       // Ben - Disable GUI elements depending on user type

     userType.addActionListener(s -> {

        if (userType.getSelectedItem().equals("Owner")) {
            for (JTextField field : ownerFields) {
                field.setEnabled(true);
            }
            for (JTextField field : clientFields) {
                field.setEnabled(false);
            }
        }
        else if(userType.getSelectedItem().equals("Client")) {
            for (JTextField field : clientFields) {
                field.setEnabled(true);
            }
            for (JTextField field : ownerFields) {
                field.setEnabled(false);
            }
        }
       });

        userType.setSelectedItem("Owner");

    // Joanna - File Saving + Timestamp
           
        JButton saveButton = new JButton("Save");

        saveButton.addActionListener(e -> {

            // Ben - Field entry checks 
            if (userType.getSelectedItem().equals("Owner")) {
                // Empty fields
                for (JTextField field : ownerFields) {
                    if (field.getText().trim().isEmpty()){
                        JOptionPane.showMessageDialog(frame, "All fields must be filled before saving","Save Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Residency time must be a positive number
                try {
                    if (Double.parseDouble(residencyTime.getText().trim()) <= 0) {
                        JOptionPane.showMessageDialog(frame, "Residency Time must be greater than 0","Save Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Residency Time must be a number","Save Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if (userType.getSelectedItem().equals("Client")) {
                // Empty fields
                for (JTextField field : clientFields) {
                    if (field.getText().trim().isEmpty()){
                        JOptionPane.showMessageDialog(frame, "All fields must be filled before saving","Save Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Job duration must be a positive number
                try {
                    if (Double.parseDouble(jobDuration.getText().trim()) <= 0) {
                        JOptionPane.showMessageDialog(frame, "Job Duration must be greater than 0","Save Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Job Duration must be a number","Save Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Job deadline must be a date that hasn't passed
                try {
                    if (LocalDate.parse(jobDeadline.getText().trim()).isBefore(LocalDate.now())) {
                        JOptionPane.showMessageDialog(frame, "Job Deadline cannot be in the past","Save Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(frame, "Job Deadline must be a date (yyyy-MM-dd)","Save Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            try (FileWriter writer = new FileWriter("transactions.txt", true)) {

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

                JOptionPane.showMessageDialog(frame,"Information saved");

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Could not save to file: " + ex.getMessage(),
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(saveButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}