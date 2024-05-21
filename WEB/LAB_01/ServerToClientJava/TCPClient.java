package ServerToClientJava;

import java.io.*; //input/output
import java.net.*; //reseau
import java.util.*;

class TCPClient
{
  public static void main(String argv[]) throws Exception 
  { 
    String sentence; //stock le msg envoyer au server
    String modifiedSentence; //stock le msg reçu du server
        
    /*Scanner scanner = new Scanner (System.in);
    System.out.print("Enter your message \t:");
    String message= scanner.nextLine();*/

    System.out.println("Enter message to send it to SERVER: " );

    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in)); //lit input dans le terminal
    Socket clientSocket = new Socket("localhost", 6789); //connexion avec le TCPServer au port 6789
    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); //envoit les données au server
    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //lit output du serveur

    sentence = inFromUser.readLine(); //stock ce qui est ecrit dans le teminal
    outToServer.writeBytes(sentence + '\n'); //envoit msg au server
    modifiedSentence = inFromServer.readLine(); //lit reponse du server
    System.out.println("FROM SERVER: " + modifiedSentence);
    clientSocket.close(); //ferme connexion au server
  } 
}
