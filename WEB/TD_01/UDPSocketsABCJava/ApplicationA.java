//Tests the ChatterServer by starting multiple
//clients, each of which sends datagrams.
import java.lang.Thread;
import java.net.*;
import java.io.*;

public class ApplicationA extends Thread 
{
  private DatagramSocket socketToB; //pour envoyer msg vers B
  private InetAddress hostAddress; //pour stocker l'adresse de B
  private byte[] buf = new byte[1000];

  public ApplicationA() 
  {
    try 
    {
      // Auto-assign port number:
      socketToB = new DatagramSocket(); //pour envoit des msg
      hostAddress = InetAddress.getByName("localhost"); //recupere l'ip de B
    } 
    catch(UnknownHostException e) 
    {
      System.err.println("Cannot find host");
      System.exit(1);
    } 
    catch(SocketException e) 
    {
      System.err.println("Can't open socket");
      e.printStackTrace();
      System.exit(1);
    } 
    System.out.println("Application A demarré"); //si pas d'erreur, affiche "demarré"
  }

  public void run()
  {
    try 
    {
      String outMessage = "A" ;
      // Envoyer A a l'application B
      socketToB.send(Dgram.toDatagram(outMessage,hostAddress, 10121)); //envoit vers outMessage vers B

      // Recevoir ABC de l'application C
      byte[] bufFromC = new byte[1000]; //stocke les données reçut par C
      DatagramPacket dpFromC = new DatagramPacket(bufFromC, bufFromC.length); //reçoit les données envoyées par C
      DatagramSocket socketFromC; 

      socketFromC = new DatagramSocket(10120); 
      socketFromC.receive(dpFromC); //reçoit les diagreammes de C sur le port 10120
      //Afficher ABC
      String rcvd = Dgram.toString(dpFromC); //convertit les msgs en char //provient de la classe Dgram.java
      System.out.println(rcvd); //affiche les données reçues par C
    } 
    catch(IOException e) 
    {
      e.printStackTrace();
      System.exit(1);
    }
  }
  public static void main(String[] args) 
  {
    new ApplicationA().start();
  }
} ///:~
