import socket
import argparse
import urllib.parse
import ssl

def Host_and_Path(_url):
    if _url.startswith("https://"):
        _port = 443
        _url = _url[8:]
    elif _url.startswith("http://"):
        _port = 80
        _url = _url[7:]
    else:
        print("'{}' isn't valid, without 'http' or 'https' your link isn't real".format(_url))
        return None, None, None

    _host_link = _url.split('/')[0] #extract the host from the URL
    _path = '/' + '/'.join(_url.split('/')[1:]) #extract the path from the URL

    return _host_link, _path, _port

def Create_TCP_Connection(_host_link, _port):
    raw_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM) #create a raw socket
    if _port == 443:
        context = ssl.create_default_context() #create SSL context for HTTPS
        SocketClient_ = context.wrap_socket(raw_socket, server_hostname=_host_link) #wrap the socket with SSL
    else:
        SocketClient_ = raw_socket
    SocketClient_.connect((_host_link, _port)) #connect to the server
    return SocketClient_

def Send_HTTP_Request(_sock, _method, _host, _path, _headers, _data):
    header_lines = "\r\n".join(f"{key}: {value}" for key, value in _headers.items()) #format headers
    if _data:
        body = urllib.parse.urlencode(_data)
        request = f"{_method} {_path} HTTP/1.1\r\nHost: {_host}\r\n{header_lines}\r\nContent-Length: {len(body)}\r\n\r\n{body}" #create request with body
    else: 
        request = f"{_method} {_path} HTTP/1.1\r\nHost: {_host}\r\n{header_lines}\r\n\r\n" #create request without body
    _sock.sendall(request.encode('utf-8'))

def Receive_Response(_sock, _host):
    response = b"" #initialize empty response
    while True:
        part = _sock.recv(4096)
        if not part:
            break
        response += part
    
    if _host == "www.google.com": #for read google link
        try:
            print("Full response:\n", response.decode('utf-8'))
        except UnicodeDecodeError:
            print("Response contains binary data:")
            print(response)

    return response.decode('utf-8')

def Handle_Response(_response):
    headers, _, body = _response.partition('\r\n\r\n')
    status_line = headers.split('\r\n')[0] #get the status line
    parts = status_line.split(' ', 2)
    if len(parts) < 3:
        raise ValueError("Invalid status line: {}".format(status_line))
    version, status_code, reason = parts
    status_code = int(status_code) #convert status code to integer
    return status_code, headers, body

def main(URL_, METHOD_):
    HOST_, PATH_, PORT_ = Host_and_Path(URL_)
    SocketClient_ = Create_TCP_Connection(HOST_, PORT_)
    HEADERS_ = {"Connection": "close"}

    if METHOD_ == "POST" or METHOD_ == "PUT":
        firstname = input("Enter Firstname: ")
        lastname = input("Enter Lastname: ")
        email = input("Enter Email: ")
        DATA_ = {"firstname": firstname, "lastname": lastname, "email": email} #data send
    else:
        DATA_ = None

    Send_HTTP_Request(SocketClient_, METHOD_, HOST_, PATH_, HEADERS_, DATA_)
    REPONSE_ = Receive_Response(SocketClient_, HOST_)
    STATUT_CODE_, HEADERS_, BODY_ = Handle_Response(REPONSE_)

    print("Status Code:", STATUT_CODE_)
    print("Headers:\n", HEADERS_)
    if STATUT_CODE_ == 200:
        print("Body:\n", BODY_)
    elif STATUT_CODE_ in range(300, 400):
        print("Redirection found.")
        for line in HEADERS_.split('\r\n'):
            if line.lower().startswith('location:'):
                new_url = line.split(' ', 1)[1].strip()
                print("Redirecting to:", new_url)
                main(new_url, METHOD_)
    elif STATUT_CODE_ == 404:
        print("Error: Page not found (404 Not Found)")
    SocketClient_.close()

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='HTTP Client')
    parser.add_argument('--url', type=str, required=True, help='URL to request')
    parser.add_argument('--method', type=str, default='GET', help='HTTP method to use')
    args = parser.parse_args()

    main(args.url, args.method)
