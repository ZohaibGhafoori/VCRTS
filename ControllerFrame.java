import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ControllerFrame {

    private static String showTime(int minutes) {
        return (minutes / 60) + "h " + (minutes % 60) + "m";
    }

    public static void open(VCController controller, String username) {

        JFrame frame = new JFrame("VCRTS · Controller Panel");
        frame.setSize(810, 580);
        frame.setMinimumSize(new java.awt.Dimension(620, 480));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        //table stuff
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

        JPanel buttonHolder = new JPanel(new java.awt.GridBagLayout());
        buttonHolder.add(logoutButton);
        topRow.add(buttonHolder, java.awt.BorderLayout.EAST);

        topRow.setMaximumSize(
                new java.awt.Dimension(
                        Integer.MAX_VALUE,
                        topRow.getPreferredSize().height
                )
        );
        panel.add(topRow);

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

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                LoginFrame.open(controller);
            }
        });

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true); 
    }
}
