package ServerToClientJava;

//toujours lancer le server en premier

import java.io.*; 
import java.net.*; 

class TCPServer
{ 
  public static void main(String argv[]) throws Exception 
  { 
    String clientSentence; //stock msg envoyé par le client 
    String capitalizedSentence; //stock le message en majuscule

    ServerSocket welcomeSocket = new ServerSocket(6789); //crée un server au port 6789
    System.out.println("SERVER Is Ready!" );

    while(true) 
    { 
      Socket connectionSocket = welcomeSocket.accept(); //attend la connexion client et crée le seuveur s'il y a un client
      BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())); //lit les données envoyées par le client
      DataOutputStream  outToClient = new DataOutputStream(connectionSocket.getOutputStream()); //envoit données au client

      clientSentence = inFromClient.readLine(); //lit msg envoyé par client
      System.out.println("Mesasge from Client : " + clientSentence); 
      capitalizedSentence = clientSentence.toUpperCase() + '\n'; //convertit msg en majuscule
      outToClient.writeBytes(capitalizedSentence); //envoit le msg en maj
    } 
  } 
}
