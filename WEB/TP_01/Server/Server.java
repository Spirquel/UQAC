package Server; //always launch the server in first //"sudo java" because port(80) is for admin

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
      
      if (MsgClientRequest_ == null) 
      {
        return;  
      }

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
        case "PUT":
          PUTRequest(Uri);
          break;
        case "DELETE":
          DELETERequest(Uri);
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

  private Map<String, String> parseFormData(String data) 
  {
    Map<String, String> params = new HashMap<>();
    String[] pairs = data.split("&");
    for (String pair : pairs) 
    {
      String[] keyValue = pair.split("=");
      if (keyValue.length > 1) 
      {
          params.put(keyValue[0], URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8));
      }
    }
    return params;
  }

  public void GETRequest(String _Uri) throws IOException 
  {
    File HTML_FILE = new File("Site" + _Uri); // Remove leading '/' from URI

    if(HTML_FILE.exists() && !HTML_FILE.isDirectory()) //if index exist
    {
      FileInputStream HTMLInput = new FileInputStream(HTML_FILE);
      byte[] Data = new byte[(int) HTML_FILE.length()]; //create a byte tab 
      HTMLInput.read(Data); 
      HTMLInput.close();

      MsgServer_ = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: " + Data.length + "\r\n\r\n";
      outToClient_.writeBytes(MsgServer_);
      outToClient_.write(Data);
      outToClient_.flush();
    } 
    else //if file don't exist
    {
      MsgServer_ = "HTTP/1.1 404 Not Found\r\n\r\n" + _Uri + " Not Found";
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
    Map<String, String> params = parseFormData(data);

    File HTML_FILE = new File("Site" + _Uri);

    try(FileWriter HTMLWrite = new FileWriter(HTML_FILE, false)) 
    {
      HTMLWrite.write("<html><body>");
      HTMLWrite.write("Nom: " + params.get("lastname") + "<br>");
      HTMLWrite.write("Prenom: " + params.get("firstname") + "<br>");
      HTMLWrite.write("Email: " + params.get("email") + "<br>");
      HTMLWrite.write("</body></html>");
      HTMLWrite.flush();
    }

    if(HTML_FILE.exists() && !HTML_FILE.isDirectory()) 
    {
      MsgServer_ = "HTTP/1.1 200 OK\r\n\r\n" + _Uri + " Create";
      outToClient_.writeBytes(MsgServer_);
      FileInputStream fis = new FileInputStream(HTML_FILE);
      byte[] buffer = new byte[4096];
      int bytesRead;
      while ((bytesRead = fis.read(buffer)) != -1) 
      {
        outToClient_.write(buffer, 0, bytesRead);
      }
      fis.close();
    } 
    else 
    {
      MsgServer_ = "HTTP/1.1 401 Unauthorized\r\n\r\n" + _Uri + " Not Found";
    }

    outToClient_.writeBytes(MsgServer_);
    outToClient_.flush();
  }

  public void PUTRequest(String _Uri) throws IOException 
  {
    File HTML_FILE = new File("Site" + _Uri);

    StringBuilder payload = new StringBuilder();

    while(inFromClient_.ready()) 
    {
      payload.append((char) inFromClient_.read());
    }

    String data = URLDecoder.decode(payload.toString(), "UTF-8");
    Map<String, String> params = parseFormData(data);

    try (FileWriter HTMLWriter = new FileWriter(HTML_FILE, false)) 
    {
      HTMLWriter.write("<html><body>");
      HTMLWriter.write("Nom: " + params.get("lastname") + "<br>");
      HTMLWriter.write("Prenom: " + params.get("firstname") + "<br>");
      HTMLWriter.write("Email: " + params.get("email") + "<br>");
      HTMLWriter.write("</body></html>");
      HTMLWriter.flush();
    }

    if(HTML_FILE.exists()) 
    {
      MsgServer_ = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: " + HTML_FILE.length() + "\r\n\r\n" + _Uri + " Updated" + "\r\n\r\n";
      outToClient_.writeBytes(MsgServer_);
      FileInputStream fis = new FileInputStream(HTML_FILE);
      byte[] buffer = new byte[4096];
      int bytesRead;

      while((bytesRead = fis.read(buffer)) != -1) 
      {
        outToClient_.write(buffer, 0, bytesRead);
      }

      fis.close();
    } 
    else 
    {
      MsgServer_ = "HTTP/1.1 404 Not Found\r\n\r\nFile could not be created";
      outToClient_.writeBytes(MsgServer_);
    }

    outToClient_.flush();
  }


  public void DELETERequest(String _Uri) throws IOException
  {
    File HTML_FILE = new File("Site" + _Uri);

    if (HTML_FILE.exists() && !HTML_FILE.isDirectory()) 
    {
      HTML_FILE.delete();
      String MsgServer_ = "HTTP/1.1 200 OK\r\n\r\n" + _Uri + " Deleted";
      outToClient_.writeBytes(MsgServer_);
      outToClient_.flush();
    } 
    else 
    {
      String MsgServer_ = "HTTP/1.1 404 Not Found\r\n\r" + _Uri + " Not Found";
      outToClient_.writeBytes(MsgServer_);
      outToClient_.flush();
    }
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
