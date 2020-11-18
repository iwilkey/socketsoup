import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.util.*;

public class Server extends Thread {

	public static class Session {
		public InetAddress address;
		public int port;
		public Session(InetAddress address, int port) {
			this.address = address;
			this.port = port;
		}
	}

	private DatagramSocket socket;
	private boolean running;
	private ArrayList<Session> sessions;
	private byte[] buffer;

	public static void main(String[] args) {
		new Server().start();
	}

	public Server() {
		try {
			sessions = new ArrayList<>();
			buffer = new byte[256];
			socket = new DatagramSocket(8080);
			System.out.println("Server now listening on port 8080");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean connectSession(InetAddress address, int port) {
		Session session = new Session(address, port);
		if(sessions.contains(session)) return false;
		sessions.add(session);
		return true;
	}

	private boolean disconnectSession(InetAddress address, int port) {
		Session session = new Session(address, port);
		if(!sessions.contains(session)) return false;
		sessions.remove(session);
		return true;
	}

	public void run() {
		running = true;

		loop: while(running) {
			try {
				clearBuffer();

				// Get any incoming packet. Dicern address and port.
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				log(address + ":" + port + "; " + new String(packet.getData()));

				// Send out a packet to an adress and port!
				for(Session s : sessions)
					send("Message was sent from " + address, s.address, s.port);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		socket.close();
	}

	private void send(String message, InetAddress address, int port) {
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
		String sent = new String(packet.getData(), 0, packet.getLength());

		switch(sent) {
			case "connect": connectSession(address, port); break;
			case "disconnect": disconnectSession(address, port); break;
		}

		try {
			socket.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void log(String message) {
		System.out.println(message);
	}

	private void clearBuffer() {
		buffer = new byte[256];
	}

}