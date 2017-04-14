import numpy

arrayX = []
arrayT = []

def opIris():
	with open("iris.data", "r") as fd:
		for ligne in fd:
			arrayX.append([])
			arrayT.append([])
			split = ligne.split(',')
			if(split[-1]=="Iris-setosa\n"):
				arrayT[-1].append(1.0)
			else:
				arrayT[-1].append(-1.0)
			for val in split[:-1]:
				arrayX[-1].append(float(val))

def opEx():
	with open("ex.data", "r") as fd:
		for ligne in fd:
			arrayX.append([])
			arrayT.append([])
			split = ligne.split(',')
			if(split[-1]=="1\n"):
				arrayT[-1].append(1.0)
			else:
				arrayT[-1].append(-1.0)
			for val in split[:-1]:
				arrayX[-1].append(float(val))

opIris()
#opEx()

arrayX = numpy.array(arrayX)
arrayT = numpy.array(arrayT)

print(arrayX)
print(arrayT)

#print(arrayX)
#print(arrayT)

A = numpy.dot(numpy.transpose(arrayX), arrayX)
B = numpy.dot(numpy.transpose(arrayX), arrayT)

W = numpy.dot(numpy.linalg.inv(A), B)
print(W)