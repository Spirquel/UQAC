package MulticastWithTwoClientsJava;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Client_01 
{
	final static String Adress = "230.0.0.1"; //ip multicast
	final static int PORT = 6000; //port

	public static void main(String[] args) throws UnknownHostException 
    {
        InetAddress address = InetAddress.getByName(Adress); //adresse du multicast
        byte[] buf = new byte[256]; //stock le msg en byte

        try (MulticastSocket clientSocket = new MulticastSocket(PORT))
        {
            clientSocket.joinGroup(address); //rejoint l'adresse pour le multicast
            
            while (true) 
            {
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length); //crée un packet
                clientSocket.receive(msgPacket); //recoit le packet

                String msg = new String(buf, 0, buf.length); //convertit le msg en string
                System.out.println("Client 1 received msg: " + msg); //affiche le msg reçu
            }
        }
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
	}
}
