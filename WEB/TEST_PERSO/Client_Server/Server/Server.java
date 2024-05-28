package WEB.TEST_PERSO.Client_Server.Server; //always launch the server in first //"sudo java" because port(80) is for admin

import java.io.*;
import java.net.*;

class Server
{ 
  public String MsgClient_; //msg client stock in this variable
  public String MsgServer_Maj_;

  public ServerSocket ServerSocket_;
  public Socket connectionSocket_;
  public BufferedReader inFromClient_;
  public DataOutputStream  outToClient_;

  public Server() throws IOException //pour chaque attribut mettre des com plus détaillés
  {
    ServerSocket_ = new ServerSocket(6789); //connexion to port 80
  }

  public void Declaration_Param_Client() throws IOException
  {
    connectionSocket_ = ServerSocket_.accept(); //wait a connexion for that server
    inFromClient_ = new BufferedReader(new InputStreamReader(connectionSocket_.getInputStream())); //Take the msg client
    outToClient_ = new DataOutputStream(connectionSocket_.getOutputStream()); //send server msg to client
  }

  public void Return_Value_To_Client(String _ClientMsg) throws IOException //faire methode, mettre des com et faire une boucle dans la boucle pour ecrire à l'infini 
  { //modify the client msg and send it to client
    MsgServer_Maj_ = _ClientMsg.toUpperCase() + '\n';
    outToClient_.writeBytes(MsgServer_Maj_);
  }

  public void Code_Construction() throws IOException
  {
    System.out.println("client connected!");

    MsgClient_ = inFromClient_.readLine(); //read client msg

    if(MsgClient_.equals("sudo stop server")) //if it's a admin
    {
      Return_Value_To_Client("sudo stop server");
      System.out.println("tentative admin acces by client"); 
      MsgClient_ = inFromClient_.readLine();

      if(MsgClient_.equals("azerty")) //passeword for close server
      {
        Return_Value_To_Client("correct");
        System.out.println("server stopping by admin"); 
        ServerSocket_.close();
      }
      else
      {
        Return_Value_To_Client("wrong");
      }
    }
    else
    {
      System.out.println("Message from Client : " + MsgClient_); //show the client msg
      Return_Value_To_Client(MsgClient_); //send a client msg modify
    }

    MsgClient_ = inFromClient_.readLine();
    
    if(MsgClient_.equals("Yes")) //if client said yes, so affiche him msg
    {
      MsgClient_ = inFromClient_.readLine();
      System.out.println("Message from Client : " + MsgClient_);
      Return_Value_To_Client(MsgClient_);
    }
  }

  public static void main(String argv[]) throws Exception 
  { 
    System.out.println("server is activate!");
    Server server = new Server();

    while(true) 
    { 
      server.Declaration_Param_Client();
      server.Code_Construction();
    } 
  } 
}
