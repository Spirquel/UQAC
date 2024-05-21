package ServerFTPJava.Client_Server_Java;

import java.net.*;
import java.io.*;
/**
 *
 * @author Hamid Mcheick
 */

public class ServerCode
{
    public static void main(String args[]) throws Exception
    {
        ServerSocket soc = new ServerSocket(6754); //num port entrant
        System.out.println("\n\nFTP Server Started on Port Number 6754");
        while(true)
        {
            System.out.println("Waiting for Connection ..."); 
            transferfile t = new transferfile(soc.accept()); //attend connexion client et crée un socket
        }
    }
}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

class transferfile extends Thread
{
    Socket ClientSoc;
    DataInputStream din;
    DataOutputStream dout;
      /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        //the constructor:
    transferfile(Socket soc)
    {
        try
        {
            ClientSoc = soc; //initialisation du client
            din = new DataInputStream(ClientSoc.getInputStream()); //initialise entrée du thread
            dout = new DataOutputStream(ClientSoc.getOutputStream()); //initialise sortie du thread
            System.out.println("FTP Client Connected ...");
            start(); //lance le thread
        }
        catch(Exception ex)
        {
        }
    }
        
      /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/        

    void SendFile() throws Exception
    {
        String filename = din.readUTF(); //enregistre nom du fichier
        System.out.println("File Name is:"+ filename); 
        String fn = "Files_Server/"+filename; 
        File f = new File(fn);
        if(!f.exists()) //si fichier existe pas 
        {
            dout.writeUTF("File Not Found"); //writeUTF fonctionne comme pour print, ça permet d'écrire dans le flux de sortie mais pour un server
            return;
        }
        else
        {
            dout.writeUTF("READY");
            String option = din.readUTF(); //lit si le client veut ou non le file
            if(option.equalsIgnoreCase("N")) //si le client rentre N il reçoit rien
            {
                dout.writeUTF("<<<<<<<<<<<<<<<<<<<<<< wasn't transfered... >>>>>>>>>>>>>>>>>>>>>>");
                return;
            }
            FileInputStream fin = new FileInputStream(f); //permet de lire en byte le fichier pour que le server capte ce qui est dit
            int ch;
            do
            {
                ch = fin.read(); //lecture du fichier en octet
                dout.writeUTF(String.valueOf(ch)); //convertion d'octet en string et envoit visuel au client
            }
            while(ch != -1); //-1 = fin du fichier
            fin.close();
            dout.writeUTF("<<<<<<<<<<<<<<<<<<<<<< File Receive Successfully >>>>>>>>>>>>>>>>>>>>>>");
        }
    }
    
      /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
       
    void ReceiveFile() throws Exception
    {
        String filename = din.readUTF(); //save nom du file
	    if(filename.compareTo("File not found") == 0) //si fle vide
        {
            return;
        }
        File f = new File("Files_Server/"+filename); //chemin vers le file
        String option;

        if(f.exists())
        {
            dout.writeUTF("File Already Exists"); //si fichier existe, alors demande si on le sup ou pas
            option = din.readUTF();
        }
        else
        {
            dout.writeUTF("SendFile"); //demande envoit du fichier de la part du client
            option="Y";
        }

        if(option.equalsIgnoreCase("Y")) //si option = "Y"
        {
            FileOutputStream fout = new FileOutputStream(f); //flux de sortie
            int ch;
            String temp;
            do
            {
                temp = din.readUTF(); //lit les octets
                ch = Integer.parseInt(temp);
                if(ch != -1) //si fin de lecture du fichier
                {
                    fout.write(ch); //ecriture du fichier dans le server
                }
            }while(ch != -1);
            fout.close();
            dout.writeUTF("<<<<<<<<<<<<<<<<<<<<<< File Send Successfully >>>>>>>>>>>>>>>>>>>>>>");
        }
        else //si pas "Y" et donc pas voulut sup le fichier existant
        {
            dout.writeUTF("<<<<<<<<<<<<<<<<<<<<<< File wasn't sent >>>>>>>>>>>>>>>>>>>>>>");
            return;
        }
    }

      /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        
    @Override
        public void run()
        {
            while(true)
            {
                try
                {
                    System.out.println("Waiting for Command ...");
                    String Command = din.readUTF(); //lit demande client
                    if(Command.compareTo("GET") == 0) //recuperation de fichier
                    {
                        System.out.println("\tGET Command Received ...\nSo the server must send the file to the client..");
                        SendFile();
                        continue;
                    }
                    else if(Command.compareTo("SEND") == 0) //envoit fichier au serveur
                    {
                        System.out.println("\tSEND Command Receiced ...\nSo the server must receive the file from the client..");
                        ReceiveFile();
                        continue;
                    }
                    else if(Command.compareTo("DISCONNECT") == 0) //deconnexion serveur
                    {
                        System.out.println("\tDisconnect Command Received ...(So the server must close the connection with the client..)");
                        System.exit(1);
                    }
                }
                catch(Exception ex)
                {
                }
            }
        }
          /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
}

/**********************  The end of Server program..  ***********************************/
