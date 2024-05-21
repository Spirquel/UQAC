package ServerFTPJava.Client_Server_Java;

import java.net.*;
import java.io.*;
/**
 *
 * @author XXXX
 */

class FTPClient
{
    public static void main(String args[]) throws Exception
    {
        Socket soc = new Socket("127.0.0.1",6754); //connection au server via le port 6754 (et prend comme IP "127.0.0.1")
        transferfileClient t = new transferfileClient(soc); //communication avec le server 
        t.displayMenu();
    }
}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

class transferfileClient
{
    Socket ClientSoc;

    DataInputStream din;
    DataOutputStream dout;
    BufferedReader br;
      /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/    
    transferfileClient(Socket soc)
    {
        try
        {
            ClientSoc = soc;
            din = new DataInputStream(ClientSoc.getInputStream());
            dout = new DataOutputStream(ClientSoc.getOutputStream());
            br = new BufferedReader(new InputStreamReader(System.in)); //lire les entrée utilisateur (ce que l'utilisateur ecrit)
        }
        catch(Exception ex)
        {
        }
	}
    
      /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/    
    
    void SendFile() throws Exception
    {
        String filename;
        System.out.print("Enter File Name :");
        filename = br.readLine(); //lit le nom du fichier
        
        File f = new File("Files_Clients/"+filename);
        if(!f.exists()) //si le nom n'existe pas, alors le fichier indiquer ne peut pas être envoyé
        {
            System.out.println("\n{{{File not exists at the client side... Please re-enter the correct name for the file...}}}");
            dout.writeUTF("File not found");
            return;
        }

        dout.writeUTF(filename);

        String msgFromServer = din.readUTF(); //lit ce qui est envoyé par le user
        String option = "Y";
        if(msgFromServer.compareTo("File Already Exists") == 0)
        {
            option = "none";
            while(!option.equalsIgnoreCase("Y")&&!option.equalsIgnoreCase("N"))
            {
                System.out.println("File Already Exists. Want to OverWrite (Y/N) ?"); //si "Y" alors indique le fichier existe déjà et qu'on veut pas sup
                option = br.readLine();
            }
            dout.writeUTF(option);
        }

        if(option.equalsIgnoreCase("Y"))
        {
            System.out.println("Sending File ..."); //envoit du fichier
            FileInputStream fin = new FileInputStream(f); 
            int ch;
            do
            {
        	    ch = fin.read();
        	    dout.writeUTF(String.valueOf(ch)); //convertion en string
            }while(ch != -1); //lecture du fichier ju'=squ'à la fin
            fin.close();
        }
        System.out.println(din.readUTF());
    }

      /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    void ReceiveFile() throws Exception
    {
        String fileName;
        System.out.print("Enter File Name :");
        fileName = br.readLine(); //lecture du nom du file
        dout.writeUTF(fileName); //affiche le nom du file
        String msgFromServer = din.readUTF();
        if(msgFromServer.compareTo("File Not Found") == 0) //si fichier non trouvé
        {
            System.out.println("File not found on Server ...:(");
            return;
        }
        else if(msgFromServer.compareTo("READY") == 0) 
        {
            System.out.println("Receiving File ..."); //recherche du file
            File f = new File("Files_Clients/"+fileName); //affiche le lien du fichier
            //File f=new File("C:\\Users\\QIYAN\\workspace\\FTPClient\\bin\\Files_Of_Client\\abc.txt");
            if(f.exists())
            {
            	/*
                String Option="none";
                while(!Option.equalsIgnoreCase("Y")&&!Option.equalsIgnoreCase("N"))
                {
                    System.out.println("File Already Exists. Do you want to OverWrite (Y/N) ?");
                    Option=br.readLine();
                    if(Option.equalsIgnoreCase("N"))
                    {
                        dout.writeUTF("N");
                        return;
		            }
                    else
                        dout.writeUTF("Y");
                }
                */
            	
            }
            dout.writeUTF("Y");
            FileOutputStream fout = new FileOutputStream(f);
            int ch;
            String temp;
            do
            {
                temp = din.readUTF();
                ch = Integer.parseInt(temp);
                if(ch != -1) //si le fichier est lu entierement
                {
                    fout.write(ch); //ecriture du fichier dans le server
                }
            }while(ch != -1);
            fout.close();
            System.out.println(din.readUTF()); //affichage du contenu du file
        }
    }
    
      /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    public void displayMenu() throws Exception
    {
        while(true)
        {
            System.out.println("\n\n[ MENU ]");
            System.out.println("1. Send a File");
            System.out.println("2. Receive a File");
            System.out.println("3. Exit");
            System.out.print("\nEnter Choice :");
            int choice;
            choice = Integer.parseInt(br.readLine());
            if(choice == 1)
            {
                dout.writeUTF("SEND");
                SendFile(); //si send est écrit on lance sa methode
            }
            else if(choice == 2)
            {
                dout.writeUTF("GET");
                ReceiveFile(); //si get est ecrit on lance sa methode
            }
            else
            {
                dout.writeUTF("DISCONNECT");
                System.exit(1); //si Disconnect on se deconnect
            }
        }
    }
      /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/    
}
