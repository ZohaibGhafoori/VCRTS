/* Project: VCRTS
 * Class: ControllerFrame.java
 * Author: Ben, Zohaib, Joanna, Felix
 * Date: November 1, 2026
 * The VC Controller window.
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ControllerFrame {

    // converts minutes to hours and minutes ex. 80 -> 1h 20m
    private static String showTime(int minutes) {
        return (minutes / 60) + "h " + (minutes % 60) + "m";
    }

    // Connection status labels
    private static void showServerStatus(JLabel statusLabel) {
        if (VCServer.isOnline()) {
            statusLabel.setText("\u25CF Online");
            statusLabel.setForeground(new java.awt.Color(0, 150, 0));
        }
        else {
            statusLabel.setText("\u2298 Offline");
            statusLabel.setForeground(java.awt.Color.RED);
        }
    }

    // Requests from Owners and Clients
    private static DefaultListModel<Request> waitingRequests = new DefaultListModel<Request>();

    // Checks if server has been started already to prevent server from starting more than once
    private static boolean serverStarted = false;

    public static void open(VCController controller, String username) {

        // Start the server so owners and clients can send requests
        if (!serverStarted) {
            VCServer server = new VCServer();
            Thread serverThread = new Thread(server);
            serverThread.start();
            serverStarted = true;
        }

        JFrame frame = new JFrame("VCRTS · Controller Panel");
        frame.setSize(810, 760);
        frame.setMinimumSize(new java.awt.Dimension(620, 480));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        //table 
        String[] columns = {"Order", "Job ID", "Client ID", "Priority", "Duration", "Waiting Time", "Completion Time", "Deadline"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable jobTable = new JTable(tableModel);
        jobTable.setDefaultEditor(Object.class, null);

        JButton logoutButton = new JButton("Log Out");

        java.net.URL logoFile = ControllerFrame.class.getResource("/images/logo.png");
        JLabel title = logoFile != null
                ? new JLabel(new ImageIcon(logoFile))
                : new JLabel("Vehicular Cloud Real Time System");

        JPanel topRow = new JPanel(new java.awt.BorderLayout());
        topRow.add(title, java.awt.BorderLayout.WEST);

        // Server status label
        JLabel statusLabel = new JLabel();
        showServerStatus(statusLabel);

        // Check the server once a second and update the label
        Timer statusTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showServerStatus(statusLabel);
            }
        });
        statusTimer.start();

        JPanel buttonHolder = new JPanel(new java.awt.GridBagLayout());
        buttonHolder.add(statusLabel);
        buttonHolder.add(Box.createHorizontalStrut(15));
        buttonHolder.add(logoutButton);
        topRow.add(buttonHolder, java.awt.BorderLayout.EAST);

        topRow.setMaximumSize(
                new java.awt.Dimension(
                        Integer.MAX_VALUE,
                        topRow.getPreferredSize().height
                )
        );
        panel.add(topRow);

        JLabel description = new JLabel("Schedule jobs by priority and see when each one will finish.");
        panel.add(description);

        panel.add(Box.createVerticalStrut(5));

        // Overview of what the controller is managing
        int vehicleCount = controller.getVehicles().size();
        int pendingCount = controller.getPendingJobs().size();

        JLabel vehiclesLabel = new JLabel("Vehicles: " + vehicleCount);
        panel.add(vehiclesLabel);

        JLabel pendingLabel = new JLabel("Pending Jobs: " + pendingCount);
        panel.add(pendingLabel);

        JLabel averageLabel = new JLabel("Average Waiting Time:");
        panel.add(averageLabel);

        panel.add(Box.createVerticalStrut(10));

        JScrollPane tableScroll = new JScrollPane(jobTable);
        panel.add(tableScroll);

        panel.add(Box.createVerticalStrut(5));

        JButton calculateButton = new JButton("Calculate Completion Time");
        panel.add(calculateButton);

        panel.add(Box.createVerticalStrut(5));

        JButton deleteButton = new JButton("Delete Job");
        panel.add(deleteButton);

        panel.add(Box.createVerticalStrut(10));

        // Requests waiting for the VC Controller
        JLabel requestsLabel = new JLabel("Requests:");
        panel.add(requestsLabel);

        JList<Request> requestList = new JList<Request>(waitingRequests);
        JScrollPane requestScroll = new JScrollPane(requestList);
        requestScroll.setPreferredSize(new java.awt.Dimension(0, 100));
        panel.add(requestScroll);

        panel.add(Box.createVerticalStrut(5));

        JButton acceptButton = new JButton("Accept");
        panel.add(acceptButton);

        panel.add(Box.createVerticalStrut(5));

        JButton rejectButton = new JButton("Reject");
        panel.add(rejectButton);

        for (java.awt.Component c : panel.getComponents()) {
            JComponent comp = (JComponent) c;
            comp.setAlignmentX(JComponent.LEFT_ALIGNMENT);

            if (comp instanceof JButton) {
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

        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                controller.reloadData();
                vehiclesLabel.setText("Vehicles: " + controller.getVehicles().size());
                pendingLabel.setText("Pending Jobs: " + controller.getPendingJobs().size());

                List<Job> schedule = controller.calculateCompletionTimes();
                //System.out.println("jobs scheduled: " + schedule.size());

                if (schedule.size() == 0) {
                    JOptionPane.showMessageDialog(frame, "There are no jobs to schedule.");
                    return;
                }

                //clear table first so it doesnt add duplicates
                tableModel.setRowCount(0);

                for(int i=0; i<schedule.size(); i++) {
                    Job job = schedule.get(i);
                    int order = i + 1;

                    Object[] row = {
                            order,
                            job.getJobID(),
                            job.getClient().getUserID(),
                            job.getPriority(),
                            showTime(job.getJobDuration()),
                            showTime(job.getWaitingTime()),
                            showTime(job.getCompletionTime()),
                            job.getJobDeadline()
                    };

                    tableModel.addRow(row);
                }

                double average = controller.getAverageWaitingTime(schedule);
                String avg = String.format("%.2f", average);

                averageLabel.setText("Average Waiting Time: " + avg + " minutes");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int row = jobTable.getSelectedRow();

                if (row == -1) {
                    JOptionPane.showMessageDialog(frame, "Select a job in the table first.");
                    return;
                }

                // The table is in the same order as the schedule
                List<Job> schedule = controller.calculateCompletionTimes();
                Job job = schedule.get(row);

                int choice = JOptionPane.showConfirmDialog(
                        frame,
                        "Delete job " + job.getJobID() + " for " + job.getClient().getUserID() + "?",
                        "Delete Job",
                        JOptionPane.YES_NO_OPTION
                );

                if (choice != JOptionPane.YES_OPTION) {
                    return;
                }

                controller.deleteJob(job);

                tableModel.setRowCount(0);
                averageLabel.setText("Average Waiting Time:");
                pendingLabel.setText("Pending Jobs: " + controller.getPendingJobs().size());

                if (controller.getJobList().size() > 0) {
                    calculateButton.doClick();
                }
            }
        });

        // Accept Request button
        acceptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Request request = requestList.getSelectedValue();

                if (request == null) {
                    JOptionPane.showMessageDialog(frame, "You must select a request first.");
                    return;
                }

                waitingRequests.removeElement(request);
            }
        });

        // Reject Request button
        rejectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Request request = requestList.getSelectedValue();

                if (request == null) {
                    JOptionPane.showMessageDialog(frame, "You must select a request first.");
                    return;
                }

                waitingRequests.removeElement(request);

            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusTimer.stop();
                frame.dispose();
                LoginFrame.open(controller);
            }
        });

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
