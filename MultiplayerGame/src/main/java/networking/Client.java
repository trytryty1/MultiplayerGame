package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {

	private DatagramSocket socket;
	private InetAddress address;

	private byte[] buf;

	public Client() {
		try {
			socket = new DatagramSocket();
			address = InetAddress.getByName("localhost");
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Client client = new Client();
		System.out.println(client.sendEcho("TEST"));
	}

	public String sendEcho(String msg) {
		try {
			buf = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, Server.PORT);
			socket.send(packet);
			packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			String received = new String(packet.getData(), 0, packet.getLength());
			return received;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "no reply";
	}

	public void close() {
		socket.close();
	}

}
