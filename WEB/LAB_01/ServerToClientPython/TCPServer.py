import socket

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind(("localhost", 12345))
s.listen(1)
conn, addr = s.accept()

while True:
    data = conn.recv(1024)
    if not data: break
    conn.sendall(data)

conn.close()