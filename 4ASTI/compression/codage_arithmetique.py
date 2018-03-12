#!/usr/bin/python3.5

# ========================
#          TODO
# ========================
# Faire une classe
#


# ========================
#         GLOBALS
# ========================

# e, n, t, v, .
#interv = [0.0, 0.3, 0.6, 0.8, 0.9, 1.0]
#probs  = [0.3, 0.3, 0.2, 0.1, 0.1]

# inf, sup, index
"""
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
"""

# ========================
#          CLASS
# ========================

class CodeurArithmetique(object):
	"""Implementation du codage arithmetique. Permet de decoder et de decoder"""

	def __init__(self, probs=[], intervals=[], chars=""):
		"""Initialise l'objet et met en place un dictionnaire d'encodage

			@param probs     const float[] List des probs tel que P(chars[i]) = probs[i]
			@param intervals const float[] Interval de depart pour l'encodage [0.0, <custom>, 1.0]
			@param chars     const string  Contient toutes les lettres dans leur ordre de prob
		"""

		self.probs     = probs
		self.intervals = intervals
		self.encodage  = chars
		self.dict      = self.init_dict(chars)
	#END_DEF

	############################# DICTIONNAIRES
	def init_dict(self, chars):
		"""Cree un dictionnaire dont le contenu est de la forme `'lettre':(borne_inf, borne_sup, index_probs)`

			@param  chars const string La chaine contenant tous les mots de l'alphabet du code
			@return dic         dict   Le dictionnaire nouvellement cree
		"""

		dic = {}
		for i in range(len(chars)):
			dic[chars[i]] = (self.intervals[i], self.intervals[i+1], i)
		#END_FOR

		return dic
	#END_DEF

	def create_dict(self, text):
		# TODO
		pass
	#END_DEF

	
	############################# ENCODE / DECODE
	def encode_intervals(self, inf, sup):
		"""Encode l'intervalle [inf, sup], credant une profondeur supplementaire

			@param  inf   const float   Borne inferieure de l'intervalle 
			@param  sup   const float   Borne superieure de l'intervalle
			@return coded       float[] Intervalle nouvellement calcule
		"""

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
		"""Decode le mot de taille `size` depuis sa representation flottante `nombre`

			@param  nombre  const float  Un nombre suppose appartenant a l'intervalle du mot a retrouver
			@param  size    const int    Le nombre de caracteres a decoder
			@return decoded       string La representation du nombre dans l'alphabet du code
		"""

		decoded = ""
		coded   = nombre

		for _ in range(size):
			for i in range(len(self.intervals)):
				# On recherche l'intervalle correspondant a chaque caractere
				if( coded >= self.intervals[i] and coded < self.intervals[i+1] ):
					# S'il match, on y inscrit la lettre adequate
					decoded += self.encodage[i]
					# On calcul la nouvelle valeur du nombre a decoder
					coded   = (coded - self.intervals[i]) / self.probs[i]
					break
				#END_IF
			#END_FOR
		#END_FOR

		return decoded
	#END_DEF

	def encode(self, word):
		"""Renvoie les bornes de l'intervalle permettant de coder `word` en flottant

			@param  word const string Le mot a encoder dans l'alphabet du code
			@return _          tuple  Resultat de l'encodage (borne_inf, borne_sup, taille_mot)
		"""

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



	############################# Fichiers
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
	c1 = CodeurArithmetique()

	# e, s, i, p
	PROBS         = [0.4, 0.2, 0.2, 0.2]
	INTERV        = [0.0, 0.4, 0.6, 0.8, 1.0]
	ENCODAGE      = "esip"

	mot           = "esipe"
	codeur        = CodeurArithmetique(PROBS, INTERV, ENCODAGE)
	test_encode   = codeur.encode(mot)
	index_decoded = (test_encode[0] + test_encode[1]) / 2.0
	test_decode   = codeur.decode(index_decoded, len(mot))

	print("Le codage arithmetique de `%s` est [%f, %f]" % (mot, test_encode[0], test_encode[1]))
	print("Le decodage de `%f` est `%s`" % (index_decoded, test_decode))
#END_MAIN

if __name__ == '__main__':
	main()