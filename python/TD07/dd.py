#!/usr/bin/python3.2

def copieFichier(source, dest):
	"copie integrale d'un fichier vers un autre"
	fs = open(source, 'r')
	fd = open(dest, 'w')

	for ligne in fs:
		fd.write(ligne)

	fs.close()
	fd.close()
	return

import sys, getopt

def main(argv):
	inputfile = ''
	outputfile = ''

	try:
		opts, args = getopt.getopt(argv, "hi:o:",["ifile=","ofile="])
	except getopt.GetoptError:
		sys.exit(2)

	for opt, arg in opts:
		if (opt == '-h'):
			sys.exit()
		elif opt in {"-i", "--ifile"}:
			inputfile = arg
		elif opt in {"-o", "--ofile"}:
			outputfile = arg
	print("Output file is ", inputfile)
	print("Output file is ", outputfile)

	copieFichier(inputfile, outputfile)

if(__name__ == "__main__"):
	main(sys.argv[1:])