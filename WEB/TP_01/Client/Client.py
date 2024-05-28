import socket

url = "https://httpbin.org/status/404"
Host_Link_ = "https://httpbin.org"
Path_Request_ = "/status/404"

if Host_Link_.startswith("https://"):
    Port_ = 443
    Host_Link_ = Host_Link_[8:] #for delete https://
elif Host_Link_.startswith("http://"):
    Port_ = 80 
    Host_Link_ = Host_Link_[7:] #for delete http://
else:
    print("link : '{}' isn't valide, without 'http' or 'https' your link isn't real".format(Host_Link_))

def Create_TCP_Connection(_host_link, _port): #for create a socket connection (TCP and Server)
    SocketClient_ = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    SocketClient_.connect((_host_link, _port))

    return SocketClient_

def Send_GET_Request(_socket_client, _path_request, _host_link): #for send to server a get request
    Request = "GET {} HTTP/1.1\r\n".format(_path_request) #Complet Request
    
    Header = "Host: {}\r\n".format(_host_link)

    Connection = "Connection: close\r\n\r\n" #for close the connection

    _socket_client.sendall((Request + Header + Connection).encode()) #send the request to server

    Answer_Server = _socket_client.recv(4096) #read the server answer 

    while Answer_Server:
        print(Answer_Server.decode()) #see the server answer
        Answer_Server = _socket_client.recv(4096)
        All_Server_Msg += Answer_Server

    All_Server_Msg = All_Server_Msg.decode() #for convert byte to str
    return All_Server_Msg

def Handle_Answer(_all_server_msg):
    Header_Body_Split = _all_server_msg.split('\r\n\r\n', 1)  #'\r\n\r\n' because is space for http
    Headers = Header_Body_Split[0]
    if len(Header_Body_Split) > 1:
        Body = Header_Body_Split[1] 
    else:
        Body = ''
    
    Status_Line = Headers.split('\r\n')[0] 
    Http_Version, Status_Code, Status_Msg = Status_Line.split(' ', 2) #Http_Version = HTTP/1.1; Status_Code = code; Status_Msg = messafe
    
    print("Status HTTP: {} {}".format(Status_Code, Status_Msg))
    print("Header Answer: {}".format(Headers))
    print("Body HTML Answer: {}".format(Body))

    if Status_Code == '401':
        print("Error: Authentification requis(401 Unauthorized)")
    elif Status_Code == '404':
        print("Error: Page not found (404 Not Found)")
    elif Status_Code == '500':
        print("Error: Error Server (500 Internal Server Error)")

SocketClient_ = Create_TCP_Connection(Host_Link_, Port_) #socket connexion

TCP_text = "TCP client connexion at: {} with Port: {}"
print(TCP_text.format(Host_Link_, Port_))

Handle_Answer(Send_GET_Request(SocketClient_, Path_Request_, Host_Link_))

SocketClient_.close()
print("Client connexion close")