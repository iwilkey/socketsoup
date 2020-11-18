import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.util.*;

public class Client extends Thread {

	private boolean running;
    private DatagramSocket socket; // Client socket.
    private InetAddress address;
    private byte[] buf;

    Scanner scanner;

    public static void main(String[] args) {
    	new Client();
    }

    public Client() {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
            scanner = new Scanner(System.in);
            System.out.println("Client loaded!");
            send("connect");
        } catch (Exception e) {
            e.printStackTrace();
        }
        start();
    }

    public void run() {
    	running = true;
    	
    	while(running) {
            String message = "";
            while((message = scanner.nextLine()) != "")
                send(message);
    	}

    	socket.close();
    }

    public void send(String msg) {
        try {
            buf = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 8080);
            socket.send(packet); // Send message
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
    	send("disconnect");
        socket.close();
    }

}
