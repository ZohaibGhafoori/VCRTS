import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ControllerFrame {

    public static void open(VCController controller) {

        JFrame frame = new JFrame("VCRTS · Controller Panel");
        frame.setSize(380, 340);
        frame.setMinimumSize(new java.awt.Dimension(340, 300));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        java.net.URL logoFile = ControllerFrame.class.getResource("/images/logo.png");
        JLabel title = logoFile != null
                ? new JLabel(new ImageIcon(logoFile))
                : new JLabel("Vehicular Cloud Real Time System");
        panel.add(title);

        JLabel signedIn = new JLabel("Signed in as controller: " + controller.getControllerID());
        panel.add(signedIn);

        panel.add(Box.createVerticalStrut(5));

        // Overview of what the controller is managing
        int vehicleCount = controller.getVehicles().size();
        int pendingCount = controller.getPendingJobs().size();
        int completedCount = controller.getCompletedJobs().size();

        JLabel vehiclesLabel = new JLabel("Vehicles: " + vehicleCount);
        panel.add(vehiclesLabel);

        JLabel pendingLabel = new JLabel("Pending Jobs: " + pendingCount);
        panel.add(pendingLabel);

        JLabel completedLabel = new JLabel("Completed Jobs: " + completedCount);
        panel.add(completedLabel);

        panel.add(Box.createVerticalStrut(10));

        JButton logoutButton = new JButton("Log Out");
        panel.add(logoutButton);

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

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                LoginFrame.open();
            }
        });

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
