package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server implements Runnable {

	public static final int PORT = 5555;
	private DatagramSocket socket;
	private boolean running;
	private byte[] buf = new byte[256];

	public Server() {
		
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}
	
	public void start() {
		System.out.print("Starting UDP socket...");
		try {
			socket = new DatagramSocket(PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		System.out.println("     Completed");
		run();
	}

	@Override
	public void run() {
		running = true;
		System.out.println("Starting UDP listener");
		while (running) {
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				System.out.println("Waiting for packet...");
				socket.receive(packet);

				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				packet = new DatagramPacket(buf, buf.length, address, port);
				
				String received = new String(packet.getData(), 0, packet.getLength());
				System.out.println("Packet Recieved ||| port [" + port + "] ||| address [" + address + "] ||| message [" + received + "]!");
				
				if (received.equals("end")) {
					running = false;
					continue;
				}
				System.out.println("Sending reply");
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		socket.close();
	}

}
