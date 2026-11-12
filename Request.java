import java.io.PrintWriter;
import java.net.Socket;

public class Request {

    private String type;
    private String[] info;
    private Socket socket;
    private PrintWriter out;

    public Request(String type, String[] info, Socket socket, PrintWriter out) {
        this.type = type;
        this.info = info;
        this.socket = socket;
        this.out = out;
    }

    public String getType() {
        return type;
    }

    public String[] getInfo() {
        return info;
    }

    // Sends a message back to the owner or client who made the request
    public void sendMessage(String message) {
        out.println(message);
    }
}
