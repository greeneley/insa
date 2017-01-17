#!/usr/bin/python3.2

import socket

def main():
	TCP_IP = "127.0.0.1"
	TCP_PORT = 1337
	BUFFER_SIZE = 1024
	MESSAGE = "Hello, world"

	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.bind((TCP_IP, TCP_PORT))
	s.send(MESSAGE)
	data = s.recv(BUFFER_SIZE)
	s.close()

	print("Received data :", data)


if(__name__=="__main__"):
	main()