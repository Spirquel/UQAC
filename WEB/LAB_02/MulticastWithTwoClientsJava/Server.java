package MulticastWithTwoClientsJava;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server 
{
	final static String Adress = "230.0.0.1"; //ip multicast
	final static int PORT = 6000; //port

	public static void main(String[] args) throws UnknownHostException, InterruptedException 
	{
		InetAddress mcastAddr = InetAddress.getByName(Adress); //adresse du multicast
		
		try (DatagramSocket serverSocket = new DatagramSocket()) 
		{
			String msg = "This is a multicast message sent from server"; //msg à envoyer
			DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, mcastAddr, PORT); //creation packet
			serverSocket.send(msgPacket); //envoit du packet
			
			System.out.println("Server sent packet with msg: " + msg); //affiche le msg envoyé par le client
            Thread.sleep(500); //attend 500ms avant de renvoyer le msg
		}
		catch (IOException ex) 
		{
            ex.printStackTrace();
        }
	}
}