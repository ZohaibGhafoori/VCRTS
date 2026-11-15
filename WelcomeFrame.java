/* Project: VCRTS
 * Class: WelcomeFrame.java
 * Author: Ben, Zohaib, Joanna, Felix
 * Date: November 15, 2026
 * Starting window with description.
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class WelcomeFrame {

    public static void open(VCController controller) {

        JFrame frame = new JFrame("VCRTS - Welcome");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        java.net.URL logoFile = WelcomeFrame.class.getResource("/images/logo.png");
        JLabel title = logoFile != null
                ? new JLabel(new ImageIcon(logoFile))
                : new JLabel("Vehicular Cloud Real Time System");
        panel.add(title);

        panel.add(Box.createVerticalStrut(5));

        JLabel welcome = new JLabel("Welcome to the Vehicular Cloud Real Time System!");
        welcome.setFont(welcome.getFont().deriveFont(java.awt.Font.BOLD, 15f));
        panel.add(welcome);

        panel.add(Box.createVerticalStrut(5));

        JLabel about = new JLabel(
                "<html><body style='width:320px'>"
                + "<ul style='margin-left:18px'>"
                + "<li><b>Owners</b> rent out their parked cars as computers.</li>"
                + "<li><b>Clients</b> submit jobs to run on those cars.</li>"
                + "<li><b>The VC Controller</b> accepts or rejects requests and schedules the jobs.</li>"
                + "</ul></body></html>"
        );
        panel.add(about);

        panel.add(Box.createVerticalStrut(15));

        JButton loginButton = new JButton("Log In");
        panel.add(loginButton);

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
                        BorderFactory.createEmptyBorder(3, 0, 1, 0)
                );
            }
        }

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                LoginFrame.open(controller);
            }
        });

        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
