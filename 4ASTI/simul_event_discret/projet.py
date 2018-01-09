####################################################
####################################################
# Thanh Luu
# 4A STI - TD3 MUL
# SIMULATION A EVENEMENTS DISCRETS
# PROJET "SALLE D'URGENCE"
####################################################
####################################################


# ==========================================
#                  IMPORTS
# ==========================================
import ciw
import random
from statistics import *


# ==========================================
#            CONSTANTES GLOBALES
# ==========================================
MINUTE   = 1
HEURE    = 60
JOUR     = 1440
WEEK_END = 7200
SEMAINE  = 10080
MOIS     = 44640 #31 jours

WARMUP   = 100
COOLDOWN = 100


# ==========================================
#                  CLASSES
# ==========================================

class Reseau(object):
	"""docstring for ClassName"""

	def __init__(self, warmup_time=0, cooldown_time=0):
		self.proportion_class = [0.03, 0.05, 0.18, 0.54, 0.20]
		self.recs             = None # Pour les records
		self.Q                = None # Pour la simulation
		self.warmup           = warmup_time
		self.cooldown         = cooldown_time
		self.max_simul_t      = 0

		# Definition du reseau
		self.N                = ciw.create_network(
			Arrival_distributions={
				'Class 0':[['TimeDependent', self.arrival_class_0],
						   'NoArrivals',
						   'NoArrivals',
						   'NoArrivals',
						   'NoArrivals',
						   'NoArrivals',
						   'NoArrivals'
				],
				'Class 1':[['TimeDependent', self.arrival_class_1],
						   'NoArrivals','NoArrivals','NoArrivals',
						   'NoArrivals','NoArrivals','NoArrivals'],
				'Class 2':[['TimeDependent', self.arrival_class_2],
						   'NoArrivals','NoArrivals','NoArrivals',
						   'NoArrivals','NoArrivals','NoArrivals'],
				'Class 3':[['TimeDependent', self.arrival_class_3],
						   'NoArrivals','NoArrivals','NoArrivals',
						   'NoArrivals','NoArrivals','NoArrivals'],
				'Class 4':[['TimeDependent', self.arrival_class_4],
						   'NoArrivals','NoArrivals','NoArrivals',
						   'NoArrivals','NoArrivals','NoArrivals']
			},
			Service_distributions={
				'Class 0':[['Exponential', 6.0],
						   ['Deterministic', 0.0],
						   ['Deterministic', 0.0],
						   ['Deterministic', 0.0],
						   ['Deterministic', 0.0],
						   ['Deterministic', 0.0],
						   ['Deterministic', 0.0]
				],
				'Class 1':[['Exponential', 6.0],
						   ['Triangular', 3, 7, 5],
						   ['Triangular', 30, 60, 45],
						   ['Uniform', 8, 15],
						   ['Triangular', 30, 60, 45],
						   ['Uniform', 8, 15],
						   ['Triangular', 10, 18, 15]
				],
				'Class 2':[['Exponential', 6.0],
						   ['Triangular', 3, 7, 5],
						   ['Triangular', 30, 60, 45],
						   ['Uniform', 8, 15],
						   ['Triangular', 30, 60, 45],
						   ['Uniform', 8, 15],
						   ['Triangular', 10, 15, 12]
				],
				'Class 3':[['Exponential', 6.0],
						   ['Triangular', 3, 7, 5],
						   ['Triangular', 30, 60, 45],
						   ['Uniform', 8, 15],
						   ['Triangular', 30, 60, 45],
						   ['Uniform', 8, 15],
						   ['Triangular', 8, 12, 10]
				],
				'Class 4':[['Exponential', 6.0],
						   ['Triangular', 3, 7, 5],
						   ['Triangular', 30, 60, 45],
						   ['Uniform', 8, 15],
						   ['Triangular', 30, 60, 45],
						   ['Uniform', 8, 15],
						   ['Triangular', 6, 9, 7.5]
				]
			},
			Transition_matrices={
				'Class 0': [[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
							[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
							[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
							[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
							[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
							[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
							[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]],

				'Class 1': [[0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0],
							[0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0],
							[0.0, 0.0, 0.0, 0.23, 0.0, 0.0, 0.0],
							[0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0],
							[0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0],
							[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0],
							[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]],

				'Class 2': [[0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0],
							[0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0],
							[0.0, 0.0, 0.0, 0.23, 0.0, 0.0, 0.0],
							[0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0],
							[0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0],
							[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0],
							[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]],

				'Class 3': [[0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0],
							[0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0],
							[0.0, 0.0, 0.0, 0.23, 0.0, 0.0, 0.0],
							[0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0],
							[0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0],
							[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0],
							[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]],

				'Class 4': [[0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0],
							[0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0],
							[0.0, 0.0, 0.0, 0.23, 0.0, 0.0, 0.0],
							[0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0],
							[0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0],
							[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0],
							[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]],
			},
			Number_of_servers=[
				'Inf',
				'Inf',
				[[4, 480], [6, 960], [4,1440]],
				[[4, 480], [6, 960], [4,1440]],
				[[4, 480], [6, 960], [4,1440]],
				[[4, 480], [6, 960], [4,1440]],
				[[4, 480], [6, 960], [4,1440]]
			]
		)
	#END_DEF

	# =======================
	#        PRINTING
	# =======================

	def analyze(self):
		if(self.recs == None):
			print("Effectuer d'abord la simulation !")

		avg_service = {'all':[],
						0   :[],
						1   :[],
						2   :[],
						3   :[],
						4   :[]}
		avg_wait    = {'all':[],0:[],1:[],2:[],3:[],4:[]}
		nb_patient  = {'all':[],0:[],1:[],2:[],3:[],4:[]}


		avg_service['all'].append(mean([r.service_time for r in self.recs if r.arrival_date > self.warmup and r.arrival_date < self.max_simul_t]))
		avg_wait['all'].append(mean([r.waiting_time for r in self.recs if r.arrival_date > self.warmup and r.arrival_date < self.max_simul_t]))
		nb_patient['all'].append(len([r.id_number for r in self.recs if r.arrival_date > self.warmup and r.arrival_date < self.max_simul_t]))

		for i in range(5):
			avg_service[i].append(mean([r.service_time for r in self.recs if r.customer_class==i and r.arrival_date > self.warmup and r.arrival_date < self.max_simul_t]))
			avg_wait[i].append(mean([r.waiting_time for r in self.recs if r.customer_class==i and r.arrival_date > self.warmup and r.arrival_date < self.max_simul_t]))
			nb_patient[i].append(len([r.id_number for r in self.recs if r.customer_class==i and r.arrival_date > self.warmup and r.arrival_date < self.max_simul_t]))
		#END_FOR

		print("\nNombre total de patients diagnostiques : %d" % nb_patient['all'][0])
		for i in range(5):
			print("Nombre de patients diagnostiques [Code %d] : %d (%.2f)" % (i+1, nb_patient[i][0], nb_patient[i][0]/nb_patient['all'][0]*100))

		print("\nMoyenne duree d'attente : %.2f mins" % mean(avg_wait['all']))
		for i in range(5):
			print("Attente moyenne [Code %d] : %.2f mins" % (i+1, avg_wait[i][0]))
		#END_FOR

		print("\nMoyenne duree de service: %.2f mins" % mean(avg_service['all']))
		for i in range(5):
			print("Temps de service moyen [Code %d] : %.2f mins" % (i+1, avg_service[i][0]))
		#END_FOR
	#END_DEF


	# =======================
	#         METHODS
	# =======================

	def compute(self, temps):
		self.max_simul_t = temps
		self.Q = ciw.Simulation(self.N)
		self.Q.simulate_until_max_time(self.warmup+temps+self.cooldown)
		self.recs = self.Q.get_all_records()
	#END_DEF


	# =======================
	#  ARRIVAL DISTRIBUTIONS
	# =======================

	def arrival_classes(self, proportion, t):
		time = t % SEMAINE #On regade jour de la semaine

		if(time < WEEK_END): #Si on est en semaine
			time = time%JOUR
			#On verifie desormais l'heure
			if(time < 8*HEURE):
				return random.expovariate(proportion*1/12)
			elif(time < 20*HEURE):
				return random.expovariate(proportion*1/10)
			else:
				return random.expovariate(proportion*1/12)
		else: #On est en week-end
			time = time%JOUR
			if(time < 8*HEURE):
				return random.expovariate(proportion*1/25)
			elif(time < 20*HEURE):
				return random.expovariate(proportion*1/20)
			else:
				return random.expovariate(proportion*1/25)
		#END_IF
	#END_DEF

	def arrival_class_0(self, t):
		return self.arrival_classes(self.proportion_class[0], t)
	#END_DEF

	def arrival_class_1(self, t):
		return self.arrival_classes(self.proportion_class[1], t)
	#END_DEF

	def arrival_class_2(self, t):
		return self.arrival_classes(self.proportion_class[2], t)
	#END_DEF

	def arrival_class_3(self, t):
		return self.arrival_classes(self.proportion_class[3], t)
	#END_DEF

	def arrival_class_4(self, t):
		return self.arrival_classes(self.proportion_class[4], t)
	#END_DEF

#END_CLASS


# ==========================================
#                  MAIN
# ==========================================

if __name__ == '__main__':
	print("==============    INIT   ==============")
	projet = Reseau(WARMUP, COOLDOWN)
	print("OK")
	print("============== COMPUTING ==============")
	projet.compute(4*SEMAINE)
	print("OK")
	print("==============   PRINT   ==============")
	projet.analyze()
#END_IF