#!/usr/bin/python3.5

# ========================
#           TODO
# ========================
# Faire une classe
#

"""
class CodeurArithmetique(object):
	Implementation du codage arithmetique. Permet de decoder et de decoder

	def __init__(self, tab_prob, interv_lettres):

		# Initialisation des probabilites

		# Init des intervalles
"""

# e, s, i, p
interv = [0.0, 0.4, 0.6, 0.8, 1.0]
probs  = [0.4, 0.2, 0.2, 0.2]
encodage = "esip"

# e, n, t, v, .
#interv = [0.0, 0.3, 0.6, 0.8, 0.9, 1.0]
#probs  = [0.3, 0.3, 0.2, 0.1, 0.1]

# inf, sup, index
dic = {
	'e':(0.0, 0.4, 0),
	's':(0.4, 0.6, 1),
	'i':(0.6, 0.8, 2),
	'p':(0.8, 1.0, 3),

	#'e':(0.0, 0.3, 0),
	'n':(0.3, 0.6, 1),
	't':(0.6, 0.8, 2),
	'v':(0.8, 0.9, 3),
	'.':(0.9, 1.0, 4)
}

def code(inf, sup):
	out  = [inf]
	last = inf
	for p in probs:
		borne = (sup-inf)*p + last
		last  = borne
		out.append(borne)
	out.append(sup)
	return out
#END_DEF

def decode(coded, taille):
	out    = ""
	nombre = coded

	for _ in range(taille):
		for i in range(len(interv)-1):
			if( nombre >= interv[i] and nombre < interv[i+1] ):
				out += encodage[i]
				nombre = (nombre - interv[i]) / probs[i]
				break
		#END_FOR
	#END_FOR

	return out
#END_DEF


# ====================
#        TEST
# ====================
#mot = "vent."
mot = "esipe"


out = []
out.append(interv)
for i in range(len(mot)):
	character = mot[i]
	index_inf = dic[character][2]
	index_sup = dic[character][2]+1

	borne_inf = out[i][index_inf]
	borne_sup = out[i][index_sup]
	out.append(code(borne_inf, borne_sup))

for intevalles in out:
	print(intevalles)

print("")
test_decode = decode(0.221, 5)
print("Le codage arithmetique de `%s` est [%f, %f]" % (mot, out[-1][0], out[-1][-1]))
print("Le decodage de 0.221 est `%s`" % (test_decode))

