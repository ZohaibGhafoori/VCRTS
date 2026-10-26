import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.*;

public class LoginFrame {

    // Ben - Accounts are saved one per line as username:password
    private static final Path ACCOUNTS_FILE = Paths.get("accounts.txt");

    // VC Controller accounts are private so normal users can't sign in as one
    private static final Path CONTROLLER_ACCOUNTS_FILE = Paths.get("controller_accounts.txt");

    public static void open() {

        JFrame frame = new JFrame("VCRTS - Login");
        frame.setSize(380, 400);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        java.net.URL logoFile = LoginFrame.class.getResource("/images/logo.png");
        JLabel title = logoFile != null
                ? new JLabel(new ImageIcon(logoFile))
                : new JLabel("Vehicular Cloud Real Time System");
        panel.add(title);

        JLabel note = new JLabel("Log in or create a new account for the selected role.");
        panel.add(note);

        panel.add(Box.createVerticalStrut(5));

        JLabel usernameLabel = new JLabel("Username:");
        panel.add(usernameLabel);
        JTextField username = new JTextField();
        panel.add(username);

        JLabel passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel);
        JPasswordField password = new JPasswordField();
        panel.add(password);

        JLabel roleLabel = new JLabel("Sign in as:");
        panel.add(roleLabel);
        String[] roles = {"Owner / Client", "VC Controller"};
        JComboBox<String> role = new JComboBox<String>(roles);
        panel.add(role);

        panel.add(Box.createVerticalStrut(10));

        JButton loginButton = new JButton("Log In");
        panel.add(loginButton);

        panel.add(Box.createVerticalStrut(5));

        JButton createButton = new JButton("Create Account");
        panel.add(createButton);

        for (java.awt.Component c : panel.getComponents()) {
            JComponent comp = (JComponent) c;
            comp.setAlignmentX(JComponent.LEFT_ALIGNMENT);

            if (comp instanceof JTextField || comp instanceof JButton || comp instanceof JComboBox) {
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

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String name = username.getText().toLowerCase().trim();
                String pass = new String(password.getPassword());

                // Which role the user picked
                String selectedRole = (String) role.getSelectedItem();
                Path file = getAccountsFile(selectedRole);

                if (name.isEmpty() || pass.isEmpty()) {
                    showError(frame, "Enter a username and password");
                    return;
                }

                try {

                    if (!checkLogin(file, name, pass)) {
                        showError(frame, "Incorrect username or password");
                        password.setText("");
                        return;
                    }

                } catch (IOException ex) {
                    showError(frame, "Could not read accounts: " + ex.getMessage());
                    return;
                }

                // Close the login window and open the window for the chosen role
                frame.dispose();

                if (selectedRole.equals("VC Controller")) {
                    VCController controller = new VCController(name);
                    ControllerFrame.open(controller);
                }
                else {
                    Main.openMainWindow();
                }
            }
        });


        // Ben - Create a new account
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String name = username.getText().toLowerCase().trim();
                String pass = new String(password.getPassword());

                // Save the account under the role the user picked
                String selectedRole = (String) role.getSelectedItem();
                Path file = getAccountsFile(selectedRole);

                // Colons separate the parts of each line in accounts.txt
                if (name.isEmpty() || name.contains(":") || name.contains(" ")) {
                    showError(frame, "Username cannot be empty or contain spaces or colons");
                    return;
                }

                if (pass.length() < 6) {
                    showError(frame, "Password must be at least 6 characters");
                    return;
                }

                try {

                    if (findAccount(file, name) != null) {
                        showError(frame, "That username is already taken");
                        return;
                    }

                } catch (IOException ex) {
                    showError(frame, "Could not read accounts: " + ex.getMessage());
                    return;
                }

                // Have the user type the password again to catch typos
                JPasswordField confirm = new JPasswordField();
                int choice = JOptionPane.showConfirmDialog(
                        frame,
                        new Object[] {"Confirm password:", confirm},
                        "Create Account",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (choice != JOptionPane.OK_OPTION) {
                    return;
                }

                if (!pass.equals(new String(confirm.getPassword()))) {
                    showError(frame, "Passwords do not match");
                    return;
                }

                try {
                    saveAccount(file, name, pass);

                } catch (IOException ex) {
                    showError(frame, "Could not save account: " + ex.getMessage());
                    return;
                }

                JOptionPane.showMessageDialog(
                        frame,
                        "Account created. You can now log in."
                );
                password.setText("");
            }
        });


        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private static void showError(JFrame frame, String message) {
        JOptionPane.showMessageDialog(
                frame,
                message,
                "Login Error",
                JOptionPane.ERROR_MESSAGE
        );
    }


    // Each role has its own accounts file
    private static Path getAccountsFile(String selectedRole) {

        if (selectedRole.equals("VC Controller")) {
            return CONTROLLER_ACCOUNTS_FILE;
        }
        else {
            return ACCOUNTS_FILE;
        }
    }


    // Returns the saved username and password, or null if there isn't one
    private static String[] findAccount(Path file, String name) throws IOException {

        if (!Files.exists(file)) {
            return null;
        }

        for (String line : Files.readAllLines(file)) {
            String[] parts = line.split(":", 2);

            if (parts.length == 2 && parts[0].equals(name)) {
                return parts;
            }
        }

        return null;
    }


    private static boolean checkLogin(Path file, String name, String pass) throws IOException {

        String[] account = findAccount(file, name);
        return account != null && account[1].equals(pass);
    }


    private static void saveAccount(Path file, String name, String pass) throws IOException {

        FileWriter writer = new FileWriter(file.toFile(), true);
        writer.write(name + ":" + pass + "\n");
        writer.close();
    }
}
