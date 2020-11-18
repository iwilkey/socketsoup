import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client extends Thread {
	private boolean running = false;
    private DatagramSocket socket; // Client socket.
    private InetAddress address;
    private byte[] buf;

    public static void main(String[] args) {
    	new Client().start();
    }

    public Client() {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
            send("connect");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
    	running = true;
    	
    	while(running) {
    		// must be able to recieve information
    		// must be able to send information
    	}

    	socket.close();
    }

    public String send(String msg) {
        try {
            buf = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 8080);
            socket.send(packet); // Send message

            packet = new DatagramPacket(buf, buf.length); // Init a packet for a response
            socket.receive(packet);  // Response
            return new String(packet.getData(), 0, packet.getLength()); // Return response
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Internal Client Error!";
    }

    public void close() {
    	send("disconnect");
        socket.close();
    }

}
