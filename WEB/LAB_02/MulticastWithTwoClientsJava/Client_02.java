package MulticastWithTwoClientsJava;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
//comme Client_01
public class Client_02 
{
	final static String Adress = "230.0.0.1";
	final static int PORT = 6000;

	public static void main(String[] args) throws UnknownHostException 
    {
        InetAddress address = InetAddress.getByName(Adress);
        byte[] buf = new byte[256];

        try (MulticastSocket clientSocket = new MulticastSocket(PORT))
        {
            clientSocket.joinGroup(address);
            
            while (true) 
            {
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                clientSocket.receive(msgPacket);

                String msg = new String(buf, 0, buf.length);
                System.out.println("Client 2 received msg: " + msg);
            }
        }
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
	}
}