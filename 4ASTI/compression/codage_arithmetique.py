#!/usr/bin/python3.5

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

print("Le codage arithmetique de `%s` est [%f, %f]" % (mot, out[-1][0], out[-1][-1]))

