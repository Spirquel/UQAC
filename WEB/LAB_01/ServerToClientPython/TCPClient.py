import socket

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect(("localhost", 12345))
s.sendall(b"Bonjour serveur!")
data = s.recv(1024)

print('Reçu', repr(data))
s.close()