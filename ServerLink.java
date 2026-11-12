import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;

public class ServerLink {

    private static final int PORT = 5000;   // must match the server's port
    private final String host = System.getProperty("vcrts.host", "localhost");
    private final Consumer<String> onMessage;
    private Socket socket;
    private PrintWriter out;

    public ServerLink(Consumer<String> onMessage) {
        this.onMessage = onMessage;
    }

    // Sends one line to the VC Controller, connecting first if needed
    public synchronized void send(String line) throws IOException {
        if (socket == null || socket.isClosed()) {
            connect();
        }
        out.println(line);
        if (out.checkError()) {
            close();
            throw new IOException("Connection to the VC Controller was lost");
        }
    }

    private void connect() throws IOException {
        final Socket s = new Socket(host, PORT);
        socket = s;
        out = new PrintWriter(s.getOutputStream(), true);
        final BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        // Background thread that waits for ACK / ACCEPTED / REJECTED
        Thread reader = new Thread(new Runnable() {
            public void run() {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        final String message = line;
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() { onMessage.accept(message); }
                        });
                    }
                } catch (IOException ex) {
                    // connection dropped
                }
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        onMessage.accept("DISCONNECTED|0|Connection to the VC Controller closed.");
                    }
                });
            }
        });
        reader.setDaemon(true);
        reader.start();
    }

    public synchronized void close() {
        try {
            if (socket != null) socket.close();
        } catch (IOException ex) { }
        socket = null;
    }
}