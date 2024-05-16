import java.lang.Thread;
import java.net.*;
import java.io.*;

public class ApplicationB extends Thread 
{
	private DatagramSocket socketToC; //pour envoyer msg vers C
	private InetAddress hostAddress; //pour stocker l'adresse de C
	private byte[] buf = new byte[1000];

	public ApplicationB() 
	{
		try 
		{
			// Auto-assign port number:
			socketToC = new DatagramSocket(); //pour envoit des msg
			hostAddress = InetAddress.getByName("localhost"); //recupere l'ip de C
		} 
		catch (UnknownHostException e) 
		{
			System.err.println("Cannot find host");
			System.exit(1);
		} 
		catch (SocketException e) 
		{
			System.err.println("Can't open socket");
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Application B demarré"); //si pas d'erreur, affiche "demarré"
	}

	public void run() 
	{
		try 
		{
			// Recevoir A de l'application A
			byte[] bufFromA = new byte[1000]; //stocke les données reçut par A
			DatagramPacket dpFromA = new DatagramPacket(bufFromA, bufFromA.length); //reçoit les données envoyées par A
			DatagramSocket socketFromA;

			socketFromA = new DatagramSocket(10121);
			socketFromA.receive(dpFromA); //reçoit les diagreammes de A sur le port 10121

			String rcvd = Dgram.toString(dpFromA); //convertit les msgs en char 
			String outMessage = rcvd + "B"; //affiche les données reçues par A + "B"

			// Envoyer AB a l'application C
			socketToC.send(Dgram.toDatagram(outMessage, hostAddress, 10122)); //envoit A +"B" vers C //provient de la classe Dgram.java

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String[] args) 
	{
		new ApplicationB().start();
	}
} ///:~
