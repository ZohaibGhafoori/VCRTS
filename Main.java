import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("VCRTS");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


        // Zohaib - GUI + Owner Section

        JLabel title = new JLabel("Vehicular Cloud Real Time System");
        panel.add(title);

        JLabel userTypeLabel = new JLabel("Select User Type:");
        panel.add(userTypeLabel);

        String[] options = {"Owner", "Client"};
        JComboBox<String> userType = new JComboBox<>(options);
        panel.add(userType);

        JLabel ownerLabel = new JLabel("Owner Information");
        panel.add(ownerLabel);

        JTextField ownerID = new JTextField();
        JTextField vehicleInfo = new JTextField();
        JTextField residencyTime = new JTextField();

        panel.add(new JLabel("Owner ID:"));
        panel.add(ownerID);

        panel.add(new JLabel("Vehicle Information:"));
        panel.add(vehicleInfo);

        panel.add(new JLabel("Residency Time:"));
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

        panel.add(new JLabel("Client ID:"));
        panel.add(clientID);

        panel.add(new JLabel("Job Duration:"));
        panel.add(jobDuration);

        panel.add(new JLabel("Job Deadline:"));
        panel.add(jobDeadline);

        // Clear button for Client fields
        JButton clearClientButton = new JButton("Clear Client Fields");

        clearClientButton.addActionListener(e -> {
            clientID.setText("");
            jobDuration.setText("");
            jobDeadline.setText("");
        });

        panel.add(clearClientButton);

    // Joanna - File Saving + Timestamp
           
        JButton saveButton = new JButton("Save");

        saveButton.addActionListener(e -> {

            try (FileWriter writer = new FileWriter("transactions.txt", true)) {

                writer.write("Time: " + LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
                writer.write("User Type: " + userType.getSelectedItem() + "\n");

                if (userType.getSelectedItem().equals("Owner")) {
                    // Owner info
                    writer.write("Owner ID: " + ownerID.getText() + "\n");
                    writer.write("Vehicle Information: " + vehicleInfo.getText() + "\n");
                    writer.write("Residency Time: " + residencyTime.getText() + "\n");
                } else {
                    // Client info
                    writer.write("Client ID: " + clientID.getText() + "\n");
                    writer.write("Job Duration: " + jobDuration.getText() + "\n");
                    writer.write("Job Deadline: " + jobDeadline.getText() + "\n");
                }

                writer.write("----------------------\n");

                JOptionPane.showMessageDialog(frame,
                        "Information saved");

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Could not save to file: " + ex.getMessage(),
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(saveButton);


        // Ben - Testing / Errors / GitHub

        JButton testButton =
                new JButton("Test Program");

        testButton.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    frame,
                    "Testing still in progress"
            );
        });
        panel.add(testButton);


        frame.add(panel);
        frame.setVisible(true);
    }
}