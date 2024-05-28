package MulticastWithOneClientJava;

//si sender pas lancer, alors il recoit rien

import java.io.*;
import java.net.*;

public class Receiver 
{
  public static void main(String[] args) 
  {
    MulticastSocket socket = null;
    DatagramPacket inPacket = null; //sert à stocker les msgs recus
    byte[] inBuf = new byte[256]; //stockage en byte des msgs
    try 
    {
      //Prepare to join multicast group
      socket = new MulticastSocket(8080); //multicast au port 8080
      InetAddress address = InetAddress.getByName("224.0.0.1"); //adresse pour le multicast
      socket.joinGroup(address); //socket (point de communication sur un reseau) rejoint le groupe multicast
 
      while (true) 
      {
        inPacket = new DatagramPacket(inBuf, inBuf.length); // pour stocker les msgs recus
        socket.receive(inPacket); //attends le msg à recevoir
        String msg = new String(inBuf, 0, inPacket.getLength()); //converti les bytes en string
        System.out.println("De " + inPacket.getAddress() + " Message : " + msg); //affcihe adresse expediteur et le msg recut
      }
    } 
    catch (IOException ioe) 
    {
      System.out.println(ioe); //pour choper les exceptions
    }
  }
}
