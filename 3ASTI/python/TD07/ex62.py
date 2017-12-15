#!/usr/bin/python3

import socket

TCP_IP = '127.0.0.1'
TCP_PORT = 6262
BUFFER_SIZE = 1024
MESSAGE = "Hello world !"

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((TCP_IP, TCP_PORT))
s.send(b'Hello world !')
data = s.recv(BUFFER_SIZE)
s.close()

print("Receive data : ", data)