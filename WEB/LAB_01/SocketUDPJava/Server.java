package SocketUDPJava;

import java.io.*; 
import java.net.*; 
  
class Server 
{ 
  public static void main(String args[]) throws Exception 
  { 
    try
    { 
      DatagramSocket serverSocket = new DatagramSocket(9876); 
  
      byte[] receiveData = new byte[1024]; 
      byte[] sendData  = new byte[1024]; 
  
      while(true) 
      { 
  
        receiveData = new byte[1024]; 

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 

        System.out.println ("Attente du packet");

        serverSocket.receive(receivePacket); 

        String sentence = new String(receivePacket.getData()); 
  
        InetAddress IPAddress = receivePacket.getAddress(); 
  
        int port = receivePacket.getPort(); 
  
        System.out.println ("De: " + IPAddress + ":" + port);
        System.out.println ("Message: " + sentence);

        String capitalizedSentence = sentence.toUpperCase(); 

        sendData = capitalizedSentence.getBytes(); 
  
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port); 
  
        serverSocket.send(sendPacket); 

      } 

    }
    catch (SocketException ex) 
    {
      System.out.println("Le port UDP 9876 est occup�.");
      System.exit(1);
    }

  } 
}  

