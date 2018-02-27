#!/usr/bin/python3.5

# ========================
#          TODO
# ========================
# Faire une classe
#


# ========================
#         GLOBALS
# ========================

# e, s, i, p
INTERV   = [0.0, 0.4, 0.6, 0.8, 1.0]
PROBS    = [0.4, 0.2, 0.2, 0.2]
ENCODAGE = "esip"

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


# ========================
#          CLASS
# ========================

class CodeurArithmetique(object):
	"""Implementation du codage arithmetique. Permet de decoder et de decoder"""

	def __init__(self, probs=[], intervals=[], chars=""):
		self.probs     = probs
		self.intervals = intervals
		self.encodage  = chars
		self.dict      = self.init_dict(chars)
	#END_DEF

	########################
	##### DICTIONNAIRES ####
	########################

	def init_dict(self, chars):
		dic = {}
		for i in range(len(chars)):
			dic[chars[i]] = (self.intervals[i], self.intervals[i+1], i)
		#END_FOR

		return dic
	#END_DEF

	def create_dict(self, text):
	#END_DEF

	########################
	####### ENCODING #######
	########################

	def encode_intervals(self, inf, sup):
		coded = [inf]

		last  = inf
		for p in self.probs:
			bound = (sup-inf)*p + last
			coded.append(bound)
			last  = bound
		#END_FOR
		coded.append(sup)

		return coded
	#END_DEF

	def decode(self, nombre, size):
		decoded = ""
		coded   = nombre

		for _ in range(size):
			for i in range(len(self.intervals)):
				if( coded >= self.intervals[i] and coded < self.intervals[i+1] ):
					decoded += self.encodage[i]
					coded   = (coded - self.intervals[i]) / self.probs[i]
					break
			#END_FOR
		#END_FOR

		return decoded
	#END_DEF

	def encode(self, word):
		coded = self.intervals

		for i in range(len(word)):
			character = word[i]
			index_inf = self.dict[character][2]
			index_sup = self.dict[character][2] + 1

			infimum   = coded[index_inf]
			supremum  = coded[index_sup]
			coded     = self.encode_intervals(infimum, supremum)
		#END_FOR

		return (coded[0], coded[-1], len(word))
	#END_DEF


	########################
	####### Fichiers #######
	########################

	def encode_file(self, src, dst):
		with open(src, 'r') as fSrc:
			# Parcours du fichier, creation du dictionnaire
			self.dict = self.create_dict(fSrc.read())

			# Compression du fichier
			fSrc.seek(0)
			with open(dst, 'w+b') as fDst:
				for mot in fSrc.read().split(' '):
					coded = self.encode(mot)

					mean   = (coded[0] + coded[1]) / 2
					taille = coded[2]

					# write encoded to file
					#fDst.write()
				#END_FOR
			#END_WITH
		#END_WITH
	#END_DEF

	def decode_file(self, src, dst):
		with open(src, 'r') as f:
			pass
		#END_WITH
	#END_DEF
#END_CLASS

# ====================
#        TEST
# ====================
def main():
	#mot = "vent."
	mot = "esipe"

	c1 = CodeurArithmetique()

	codeur      = CodeurArithmetique(PROBS, INTERV, ENCODAGE)
	test_encode = codeur.encode(mot)
	test_decode = codeur.decode(0.221, 5)
	print("Le codage arithmetique de `%s` est [%f, %f]" % (mot, test_encode[0], test_encode[1]))
	print("Le decodage de `0.221` est `%s`" % (test_decode))
#END_MAIN

if __name__ == '__main__':
	main()