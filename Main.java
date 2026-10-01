import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("VCRTS");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // =========================================
        // Zohaib - GUI + Owner Section
        // =========================================

        JLabel title = new JLabel("Vehicular Cloud Real Time System");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);

        panel.add(Box.createVerticalStrut(10));

        JLabel userTypeLabel = new JLabel("Select User Type:");
        panel.add(userTypeLabel);

        String[] options = {"Owner", "Client"};
        JComboBox<String> userType = new JComboBox<>(options);
        panel.add(userType);

        panel.add(Box.createVerticalStrut(10));

        JPanel ownerPanel = new JPanel();
        ownerPanel.setLayout(new GridLayout(3, 2, 5, 5));
        ownerPanel.setBorder(
                BorderFactory.createTitledBorder("Owner Information")
        );

        JTextField ownerID = new JTextField();
        JTextField vehicleInfo = new JTextField();
        JTextField residencyTime = new JTextField();

        ownerPanel.add(new JLabel("Owner ID:"));
        ownerPanel.add(ownerID);

        ownerPanel.add(new JLabel("Vehicle Information:"));
        ownerPanel.add(vehicleInfo);

        ownerPanel.add(new JLabel("Residency Time:"));
        ownerPanel.add(residencyTime);

        panel.add(ownerPanel);

        panel.add(Box.createVerticalStrut(10));


        // =========================================
        // Felix - Client Section
        // =========================================

        JTextField clientID = new JTextField();
        JTextField jobDuration = new JTextField();
        JTextField jobDeadline = new JTextField();

        panel.add(new JLabel("Client ID:"));
        panel.add(clientID);

        panel.add(new JLabel("Job Duration:"));
        panel.add(jobDuration);

        panel.add(new JLabel("Job Deadline:"));
        panel.add(jobDeadline);


        // =========================================
        // Joana - File Saving + Timestamp
        // =========================================

        JButton saveButton = new JButton("Save");

        saveButton.addActionListener(e -> {

            try {
                FileWriter writer =
                        new FileWriter("transactions.txt", true);

                writer.write(
                        "Time: " + LocalDateTime.now() + "\n"
                );

                writer.write(
                        "User Type: "
                        + userType.getSelectedItem()
                        + "\n"
                );

                writer.write("----------------------\n");

                writer.close();

                JOptionPane.showMessageDialog(
                        frame,
                        "Information saved"
                );

            } catch (IOException ex) {

                System.out.println(
                        "Error saving file"
                );
            }
        });

        panel.add(saveButton);


        // =========================================
        // Ben - Testing / Errors / GitHub
        // =========================================

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