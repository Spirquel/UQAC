package WEB.TEST_PERSO.Client_Server.Clients;

import java.io.*; //input/output
import java.net.*; //reseau
import java.util.*;

class Client
{
  public String MsgClient_; //stock client msg
  public String MsgServer_; //stock server msg

  public Socket ClientSocket_;
  public BufferedReader inFromUser_;
  public DataOutputStream outToServer_;
  public BufferedReader inFromServer_;

  public Client() throws Exception
  {
    ClientSocket_= new Socket("localhost", 6789); //connexion at 6789 port
    inFromUser_ = new BufferedReader(new InputStreamReader(System.in)); //msg from user
    outToServer_ = new DataOutputStream(ClientSocket_.getOutputStream()); //msg send to server
    inFromServer_ = new BufferedReader(new InputStreamReader(ClientSocket_.getInputStream())); //msg received to server
  }

  public String LaunchInput() throws IOException //for enter the client msg and sed it to server
  {
    MsgClient_ = inFromUser_.readLine(); //stock the client msg
    outToServer_.writeBytes(MsgClient_ + '\n'); //send to server
    return MsgClient_;
  }

  public void Code_Conctruction() throws IOException
  {
    System.out.println("Enter message to send it to SERVER: ");

    MsgClient_ = LaunchInput();
    MsgServer_ = inFromServer_.readLine(); //stock msg server
    
    if(MsgServer_.equals("SUDO STOP SERVER"))
    {
      System.out.println("Enter passeword: ");
      MsgClient_ = LaunchInput();
      MsgServer_ = inFromServer_.readLine(); //stock msg server

      if(MsgServer_.equals("CORRECT"))
      {
        ClientSocket_.close(); //close the client session
      }
      else if(MsgServer_.equals("WRONG"))
      {
        System.out.println("Wrong passeword, close session");
        ClientSocket_.close(); //close the client session
      }
    }
    else
    {
      System.out.println("from server: " + MsgServer_);
    }

    System.out.print("other msg ? [Yes/No]: "); //for relance a client msg to server
    MsgClient_ = LaunchInput();
    
    if(MsgClient_.equals("Yes"))
    {
      System.out.println("Enter message to send it to SERVER: ");
      MsgClient_ = LaunchInput();
      MsgServer_ = inFromServer_.readLine(); //stock msg server
      System.out.println("from server: " + MsgServer_);
    }
    else if(MsgClient_.equals("No"))
    {
      ClientSocket_.close(); //close the client session
    }
    else
    {
      System.out.println("Don't choose, close the client session");
      ClientSocket_.close(); //close the client session
    }
  }

  public static void main(String argv[]) throws Exception 
  { 
    Client client = new Client();
    client.Code_Conctruction();
  } 
}
