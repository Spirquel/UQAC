package SocketUDPJava;

import java.io.*; 
import java.net.*; 
  
class Client 
{ 
  public static void main(String args[]) throws Exception 
  { 
    try 
    {
      String serverHostname = new String ("127.0.0.1");

      if (args.length > 0)
        serverHostname = args[0];
  
      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in)); 
  
      DatagramSocket clientSocket = new DatagramSocket(); 
  
      InetAddress IPAddress = InetAddress.getByName(serverHostname); 
      System.out.println ("Tentative de connexion � " + IPAddress + ") via le port UDP 9876");
  
      byte[] sendData = new byte[1024]; 
      byte[] receiveData = new byte[1024]; 
  
      System.out.print("Enterer le Message: ");
      String sentence = inFromUser.readLine(); 
      sendData = sentence.getBytes();         

      System.out.println ("Envoi des donn�es � " + sendData.length + " bits au serveur.");
      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876); 
  
      clientSocket.send(sendPacket); 
  
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 
  
      System.out.println ("Attente de reception du packet");
      clientSocket.setSoTimeout(10000);

      try 
      {
        clientSocket.receive(receivePacket); 
        String modifiedSentence = new String(receivePacket.getData()); 
  
        InetAddress returnIPAddress = receivePacket.getAddress();
     
        int port = receivePacket.getPort();

        System.out.println ("Du serveur: " + returnIPAddress + ":" + port);
        System.out.println("Message: " + modifiedSentence); 

      }
      catch (SocketTimeoutException ste)
      {
        System.out.println ("Delais d�pass�, le packet est perdu");
      }
  
      clientSocket.close(); 
    }
    catch (UnknownHostException ex) 
    { 
      System.err.println(ex);
    }
    catch (IOException ex) 
    {
      System.err.println(ex);
    }
  } 
} 
