#!/usr/bin/python3.2

#Exercice 6

import socket


def upload(nombre, module):
	"""
		int nombre, str module

		Cette fonction respecte la normalisation pifeaumétrique.
	"""
	pass

def main():
	TCP_IP = "127.0.0.1"
	TCP_PORT = 13370
	BUFFER_SIZE = 2048

	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.bind((TCP_IP, TCP_PORT))
	s.listen(1)

	conn, addr = s.accept()
	print("Connection address :", addr)
	while(True):
		data = conn.recv(BUFFER_SIZE)
		if(not data):
			break

		tmp = data.split(None, 4)
		cmd, arg, opt = tmp[0], tmp[1], tmp[2]

		if(cmd==b"UPLOAD"):
			upload((int(arg), opt.decode("utf-8")))
		elif(cmd==b"MMOYENNE"):
			pass
		elif(cmd==b"RESET"):
			pass

		conn.send(data) #echo
	conn.close()

if(__name__ == "__main__"):
	help(upload)
	main()