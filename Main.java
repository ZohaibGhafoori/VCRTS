
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
        frame.setSize(460, 500);
        frame.setMinimumSize(new java.awt.Dimension(400, 500));
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
        JTextField licensePlate = new JTextField();
        JTextField vehicleInfo = new JTextField();

        // Zohaib - Vehicle type dropdown
        String[] vehicleTypes = {"Electric", "Hybrid", "Gas"};
        JComboBox<String> vehicleType = new JComboBox<String>(vehicleTypes);
        DurationInput residencyTime = new DurationInput();
        JTextField[] ownerFields = {ownerID, licensePlate, vehicleInfo};

        // Zohaib - Owner fields
        JLabel ownerIDLabel = new JLabel("Owner ID:");
        panel.add(ownerIDLabel);
        panel.add(ownerID);

        JLabel licensePlateLabel = new JLabel("License Plate:");
        panel.add(licensePlateLabel);
        panel.add(licensePlate);

        JLabel vehicleInfoLabel = new JLabel("Vehicle Model:");
        panel.add(vehicleInfoLabel);
        panel.add(vehicleInfo);

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
                licensePlateLabel, licensePlate,
                vehicleInfoLabel, vehicleInfo,
                vehicleTypeLabel, vehicleType,
                residencyTimeLabel, residencyTime
        };


        // Felix - Client Section

        JTextField clientID = new JTextField();
        DurationInput jobDuration = new DurationInput();
        JTextField[] clientFields = {clientID};

        JLabel clientNote = new JLabel("Enter client information below.");
        panel.add(clientNote);

        // Felix - Client fields
        JLabel clientIDLabel = new JLabel("Client ID:");
        panel.add(clientIDLabel);
        panel.add(clientID);

        JLabel jobDurationLabel = new JLabel("Job Duration:");
        panel.add(jobDurationLabel);
        panel.add(jobDuration);

        JLabel jobDeadlineLabel = new JLabel("Job Deadline:");
        panel.add(jobDeadlineLabel);

        // Dropdown month options
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};

        String[] days = new String[31];
        for (int i = 0; i < 31; i++) {
            days[i] = "" + (i + 1);
        }

        String[] years = new String[3];
        int thisYear = LocalDate.now().getYear();
        for (int i = 0; i < 3; i++) {
            years[i] = "" + (thisYear + i);
        }

        JComboBox<String> deadlineMonth = new JComboBox<String>(months);
        JComboBox<String> deadlineDay = new JComboBox<String>(days);
        JComboBox<String> deadlineYear = new JComboBox<String>(years);

        // Today's date
        deadlineMonth.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        deadlineDay.setSelectedIndex(LocalDate.now().getDayOfMonth() - 1);
        deadlineYear.setSelectedIndex(0);

        JPanel deadlineRow = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        deadlineRow.add(deadlineMonth);
        deadlineRow.add(new JLabel("  "));
        deadlineRow.add(deadlineDay);
        deadlineRow.add(new JLabel("  "));
        deadlineRow.add(deadlineYear);
        deadlineRow.setMaximumSize(
                new java.awt.Dimension(
                        Integer.MAX_VALUE,
                        deadlineRow.getPreferredSize().height
                )
        );
        panel.add(deadlineRow);

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
                jobDeadlineLabel, deadlineRow,
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
                        BorderFactory.createEmptyBorder(8, 0, 3, 0)
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

                    // License plate checks
                    String plate = licensePlate.getText().trim().toUpperCase();

                    if (plate.length() < 2 || plate.length() > 8) {

                        JOptionPane.showMessageDialog(
                                frame,
                                "License plate must be 2 to 8 characters",
                                "Save Error",
                                JOptionPane.ERROR_MESSAGE
                        );

                        return;
                    }

                    for (int i = 0; i < plate.length(); i++) {
                        char ch = plate.charAt(i);

                        if (!Character.isLetterOrDigit(ch) && ch != ' ' && ch != '-') {

                            JOptionPane.showMessageDialog(
                                    frame,
                                    "License plate can only have letters, numbers, spaces and dashes",
                                    "Save Error",
                                    JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }
                    }

                    for (int i = 0; i < controller.getVehicles().size(); i++) {

                        if (controller.getVehicles().get(i).getVehicleID().equalsIgnoreCase(plate)) {

                            JOptionPane.showMessageDialog(
                                    frame,
                                    "A vehicle with this license plate is already registered",
                                    "Save Error",
                                    JOptionPane.ERROR_MESSAGE
                            );

                            return;
                        }
                    }

                    licensePlate.setText(plate);

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
                    String deadline = getDeadline(deadlineMonth, deadlineDay, deadlineYear);

                    try {

                        if (LocalDate.parse(deadline).isBefore(LocalDate.now())) {

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
                    System.out.println("Saving to: " + new java.io.File(fileName).getAbsolutePath());
                    FileWriter writer = new FileWriter(fileName, true);

                    String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    writer.write("Time: " + time + "\n");

                    writer.write("User Type: " + userType.getSelectedItem() + "\n");


                    if (userType.getSelectedItem().equals("Owner")) {

                        // Zohaib - Connect Owner and Vehicle classes
                        String selectedType = (String) vehicleType.getSelectedItem();

                        Vehicle vehicle = new Vehicle(
                                licensePlate.getText(),
                                vehicleInfo.getText(),
                                selectedType,
                                residencyTime.totalMinutes() / 60.0,
                                "Unknown",
                                "Unknown"
                        );

                        controller.registerVehicle(ownerID.getText(), vehicle);

                        // Check the connection
                        System.out.println("Owner ID: " + vehicle.getOwner().getUserID());
                        System.out.println("Vehicles: " + vehicle.getOwner().getVehicles().size());

                        // Owner info
                        writer.write("Owner ID: " + ownerID.getText() + "\n");
                        writer.write("License Plate: " + licensePlate.getText() + "\n");
                        writer.write("Vehicle Model: " + vehicleInfo.getText() + "\n");

                        // Zohaib - Save selected vehicle type
                        writer.write("Vehicle Type: " + vehicleType.getSelectedItem() + "\n");

                        writer.write("Residency Time: " + residencyTime.describe() + "\n");

                        for (JTextField field : ownerFields) {
                            field.setText("");
                        }
                        residencyTime.clear();
                    }


                    if (userType.getSelectedItem().equals("Client")) {

                        // Save what was typed in the priority box
                        try {
                            jobPriority.commitEdit();
                        } catch (java.text.ParseException ex) {
                            // Bad input keeps the old value
                        }

                        // Client info
                        writer.write("Client ID: " + clientID.getText() + "\n");
                        String jobID = Job.generateJobID();
                        writer.write("Job ID: " + jobID + "\n");
        
                        writer.write("Job Duration: " + jobDuration.describe() + "\n");
                        writer.write("Job Deadline: " + getDeadline(deadlineMonth, deadlineDay, deadlineYear) + "\n");
                        writer.write("Job Priority: " + jobPriority.getValue() + "\n");

                        int priority = Integer.parseInt(jobPriority.getValue().toString());

                        Job job = new Job(
                                jobID,
                                jobDuration.totalMinutes(),
                                getDeadline(deadlineMonth, deadlineDay, deadlineYear),
                                priority
                        );
                        job.setSubmitTime(time);

                        controller.addJob(clientID.getText(), job);

                        for (JTextField field : clientFields) {
                            field.setText("");
                        }
                        jobDuration.clear();
                        deadlineMonth.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
                        deadlineDay.setSelectedIndex(LocalDate.now().getDayOfMonth() - 1);
                        deadlineYear.setSelectedIndex(0);
                        jobPriority.setValue(Job.DEFAULT_PRIORITY);
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
                deadlineMonth.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
                deadlineDay.setSelectedIndex(LocalDate.now().getDayOfMonth() - 1);
                deadlineYear.setSelectedIndex(0);
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




    // Converts dropdown entries into dates
    static String getDeadline(JComboBox<String> month, JComboBox<String> day, JComboBox<String> year) {
        String m = "" + (month.getSelectedIndex() + 1);
        String d = day.getSelectedItem().toString();

        if (m.length() == 1) {
            m = "0" + m;
        }

        if (d.length() == 1) {
            d = "0" + d;
        }

        return year.getSelectedItem() + "-" + m + "-" + d;
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
