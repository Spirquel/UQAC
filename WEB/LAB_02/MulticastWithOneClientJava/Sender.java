package MulticastWithOneClientJava;

import java.io.*;
import java.net.*;

public class Sender 
{
  public static void main(String[] args) 
  {
    DatagramSocket socket = null; //socket pour envoyer le msg
    DatagramPacket outPacket = null; //sert à envoyer le msg
    byte[] outBuf; //msg en byte
    final int PORT = 8080; //final veut dire que la valeur ne peut pas être modif plus tard dans le code (c'est une constante)
 
    try 
    {
      socket = new DatagramSocket();
      long counter = 0; //sert à gérer les msgs uniques
      String msg;
 
      while (true) 
      {
        msg = "multicast! " + counter;
        counter++; //afin de savoir combine de cast ont été fait
        outBuf = msg.getBytes(); //conversion du msg en byte
 
        //Send to multicast IP address and port
        InetAddress address = InetAddress.getByName("224.0.0.1"); //adresse pour le multicast
        outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT); //packet avec différente données
 
        socket.send(outPacket); //envoit du packet
 
        System.out.println("Le serveur envoi : " + msg); //affichage du message envoyé
        try 
        {
          Thread.sleep(500); //pause de 500ms avant d'envoyer le prochain message
        } 
        catch (InterruptedException ie) 
        {
        }
      }
    } 
    catch (IOException ioe) 
    {
      System.out.println(ioe);
    }
  }
}
