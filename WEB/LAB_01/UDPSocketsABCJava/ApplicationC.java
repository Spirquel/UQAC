import java.lang.Thread;
import java.net.*;
import java.io.*;

public class ApplicationC extends Thread 
{
	private DatagramSocket socketToA; //pour envoyer msg vers A
	private InetAddress hostAddress; //pour stocker l'adresse de A
	private byte[] buf = new byte[1000];

	public ApplicationC() 
	{
		try 
		{
			// Auto-assign port number:
			socketToA = new DatagramSocket(); //pour envoit des msg
			hostAddress = InetAddress.getByName("localhost"); //recupere l'ip de A
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
		System.out.println("Application C demarré"); //si pas d'erreur, affiche "demarré"
	}

	public void run() 
	{
		try 
		{
			// Recevoir AB de l'application B
			byte[] bufFromB = new byte[1000]; //stocke les données reçut par B
			DatagramPacket dpFromB = new DatagramPacket(bufFromB, bufFromB.length); //reçoit les données envoyées par B
			DatagramSocket socketFromB;
			socketFromB = new DatagramSocket(10122);
			socketFromB.receive(dpFromB); //reçoit les diagreammes de B sur le port 10122

			String rcvd = Dgram.toString(dpFromB); //convertit les msgs en char 
			String outMessage = rcvd + "C"; //affiche les données reçues par B + "C"

			// Envoyer ABC a l'application A
			socketToA.send(Dgram.toDatagram(outMessage, hostAddress, 10120)); //envoit AB +"C" vers A (A = port 10120) //provient de la classe Dgram.java

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String[] args) 
	{
		new ApplicationC().start();
	}
} ///:~
