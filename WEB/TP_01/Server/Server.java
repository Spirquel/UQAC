package Server; //always launch the server in first //"sudo java" because port(80) is for admin

//faut faire avec les champs ou pas pour le server on affiche juste un site ?

import java.io.*;
import java.net.*;

class Server
{ 
  private short port_ = 80;
  private String MsgClientRequest_;
  private String MsgClientRequestHeader_;
  private String MsgServer_;

  private ServerSocket ServerSocket_;
  private Socket connectionSocket_;
  private BufferedReader inFromClient_;
  private DataOutputStream outToClient_;

  public Server() throws IOException
  {
    ServerSocket_ = new ServerSocket(port_);
  }

  public void Declaration_Param_Client() throws IOException
  {
    connectionSocket_ = ServerSocket_.accept(); //wait a connexion for that server
    inFromClient_ = new BufferedReader(new InputStreamReader(connectionSocket_.getInputStream())); //Take the msg client
    outToClient_ = new DataOutputStream(connectionSocket_.getOutputStream()); //send server msg to client
    new Thread(ClientRunnable()).start(); //for check each client connexion and execute ClientRunnable methode 
  }

  public Runnable ClientRunnable() throws IOException
  {
    return new Runnable() 
    {
      @Override
      public void run() //for excecute the code in Thread (if a other name, Thread().start() don't work)
      {
        Code_Construction();
      }
    };
  }

  public void Code_Construction()
  {
    try 
    {
      System.out.println("Client connected!");
        
      MsgClientRequest_ = inFromClient_.readLine();
      System.out.println("Request from Client: " + MsgClientRequest_);

      String[] PartsOfClientRequest = MsgClientRequest_.split(" ");
      String Method = PartsOfClientRequest[0]; //GET or POST
      String Uri = PartsOfClientRequest[1]; //ex : index.html
      System.out.println("Method from Client: " + Method + "\nURI from Client: " + Uri);
        
      while((MsgClientRequestHeader_ = inFromClient_.readLine()) != null && !MsgClientRequestHeader_.isEmpty()) //if null, end for the msg 
      {
        System.out.println("Header from Client: " + MsgClientRequestHeader_);
      }

      switch(Method)
      {
        case "GET":
          GETRequest(Uri);
          break;
        case "POST":
          POSTRequest(Uri);
          break;
      }
    }
    catch (IOException e) 
    {
      System.out.println("Error client: " + e.getMessage());
    }
    finally 
    {
      try 
      {
        if (connectionSocket_ != null) 
        {
          connectionSocket_.close();
        }
      } 
      catch (IOException e) 
      {
        System.out.println("Error closing client socket: " + e.getMessage());
      }
    }
  }

  public void GETRequest(String _Uri) throws IOException 
  {
    File Index = new File("Site" + _Uri); // Remove leading '/' from URI

    if(Index.exists() && !Index.isDirectory()) //if index exist
    {
      FileInputStream IndexInput = new FileInputStream(Index);
      byte[] Data = new byte[(int) Index.length()]; //create a byte tab 
      IndexInput.read(Data); 
      IndexInput.close();

      MsgServer_ = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: " + Data.length + "\r\n\r\n";
      outToClient_.writeBytes(MsgServer_);
      outToClient_.write(Data);
      outToClient_.flush();
    } 
    else //if file don't exist
    {
      MsgServer_ = "HTTP/1.1 404 Not Found\r\n\r\nFile Not Found";
      outToClient_.writeBytes(MsgServer_);
      outToClient_.flush();
    }
  }

  public void POSTRequest(String _Uri) throws IOException 
  {
    StringBuilder payload = new StringBuilder();

    while(inFromClient_.ready()) 
    {
      payload.append((char) inFromClient_.read());
    }

    String data = URLDecoder.decode(payload.toString(), "UTF-8");

    File Index = new File("Site" + _Uri); //path file saveclient

    if(Index.exists() && !Index.isDirectory()) //if index exists
    {
      try (FileWriter IndexWrite = new FileWriter(Index, false))
      {
        IndexWrite.write("<html><body>");
        IndexWrite.write("Données enregistrées : " + data); //for save data
        IndexWrite.write("</body></html>");
      }

      MsgServer_ = "HTTP/1.1 200 OK\r\n\r\nFile Updated";
      MsgServer_ = "HTTP/1.1 303 See Other\r\nLocation: " + _Uri + "\r\n\r\n";
    }
    else //If file doesn't exist
    {
      MsgServer_ = "HTTP/1.1 401 Unauthorized\r\n\r\nFile Not Found";
    }

    outToClient_.writeBytes(MsgServer_);
    outToClient_.flush();
  }

    public static void main(String argv[]) throws Exception 
    { 
      System.out.println("server is activate!");
      Server server = new Server();

      while(true)
      {
        server.Declaration_Param_Client(); 
      }
    } 
  }
