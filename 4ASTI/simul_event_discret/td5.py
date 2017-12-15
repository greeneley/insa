#!/usr/bin/python3.5

# ==========================================
#                  IMPORT
# ==========================================
import ciw


# ==========================================
# >>>>>>>>>>>>>> EXERCICE 1 <<<<<<<<<<<<<<<<
# ==========================================

"""
Les trois files de reseau sont :
M/M/1/
Taux d'arrivée   : 0.3
Temps de service : 1.0

M/M/2
Taux d'arrivée   : 0.2
Temps de service : 0.4

NULL/M/2
Taux d'arrivée   : NULL
Temps de service : 0.5

Matrice de routage du réseau
[ 0.0 0.3 0.7
  0.0 0.0 1.0
  0.0 0.0 0.0  ]
"""

# ==========================================
# >>>>>>>>>>>>>> EXERCICE 2 <<<<<<<<<<<<<<<<
# ==========================================

# =====================
#         2.1
# =====================

"""
>>>>> Pour la class 0 <<<<<
M/M/1
Taux d'arrivée   : 1.0
Temps de service : 4.0

NULL/M/2
Taux d'arrivée   : NULL
Temps de service : 1.0

NULL/G/3
Taux d'arrivée   : NULL
Temps de service : 0.0

Matrice de routage du réseau
[ 0.0 1.0 0.0
  0.0 0.0 0.0
  0.0 0.0 0.0 ]

>>>>> Pour la class 1 <<<<<
M/M/1
Taux d'arrivée   : 2.0
Temps de service : 6.0

NULL/G/2
Taux d'arrivée   : NULL
Temps de service : 0.0

NULL/M/3
Taux d'arrivée   : NULL
Temps de service : 1.0

Matrice de routage du réseau
[ 0.0 0.0 1.0
  0.0 0.0 0.0
  0.0 0.0 0.0 ]
"""

# =====================
#         2.2
# =====================

"""
Un client de la classe 0 décide de rentrer dans la file :
	- avec 100% de probabilite s'il y a 7+ clients ;
	- avec 50% de probabilite s'il y a entre 3 et 6 clients ;
	- avec 0% de probabilite s'il y a 2- clients.
"""

# =====================
#         2.3
# =====================

def exo23():
	"""Exemple de file d'attente qui montre les capacites de priority.


	File d'attente qui prioritise les clients de la classe 0.
	Les clients entrent dans le eme type de file d'attente
	M/M/1
	Taux d'arrivée   : 5
	Temps de service : 10

	Les resultats montrent bien que les clients 0 attendent beaucoup mons
	longtemps que les clients de la classe 1
	0.15291894776507223
	3.5065047095201005
"""
	N = ciw.create_network(
		Arrival_distributions={'Class 0':[['Exponential', 5]],
							  'Class 1':[['Exponential', 5]]},
		Service_distributions={'Class 0':[['Exponential', 10]],
							  'Class 1':[['Exponential', 10]]},
		Priority_classes={'Class 0':0, 'Class 1':1},
		Number_of_servers=[1]				  
	)

	ciw.seed(1)
	Q = ciw.Simulation(N)
	Q.simulate_until_max_time(100.0)
	recs = Q.get_all_records()

	waits_0 = [r.waiting_time for r in recs if r.customer_class==0]
	print(sum(waits_0)/len(waits_0))

	waits_1 = [r.waiting_time for r in recs if r.customer_class==1]
	print(sum(waits_1)/len(waits_1))
#END_DEF



def exo24():
	"""Resolution de l'exercice


	Matrice de routage du reseau :
	[ 0.2 0.4 0.4 0.0
	  0.0 0.2 0.0 0.8
	  0.0 0.0 0.1 0.9
	  0.0 0.0 0.0 0.0 ]

	Les differentes files sont :
	M/M/1
	G/M/1
	G/M/1
	G/M/1
	"""

	N = ciw.create_network(
		Arrival_distributions =[['Exponential', 1.0],
								'NoArrivals',
								'NoArrivals',
								'NoArrivals'],
		"""Service_distributions =[['Exponential', 2.5],
								['Exponential', 2.0],
								['Exponential', 1.5],
								['Exponential', 2.5]],""",
		Service_distributions = [['Exponential', 1.0],
								['Exponential', 0.5],
								['Exponential', 0.66],
								['Exponential', 0.4]],
		Transition_matrices = [[0.2, 0.4, 0.4, 0.0],
							   [0.0, 0.2, 0.0, 0.8],
							   [0.0, 0.0, 0.1, 0.9],
							   [0.0, 0.0, 0.0, 0.0]],
		Number_of_servers=[1,1,1,1]
	)

	completed_custs = []
	for trial in range(10):
		ciw.seed(trial)
		Q = ciw.Simulation(N)
		Q.simulate_until_max_time(300)
		recs = Q.get_all_records()
		num_completed = len([r for r in recs if r.node==3 and r.arrival_date < 180])
		completed_custs.append(num_completed)
	#END_FOR
	print(sum(completed_custs) / len(completed_custs))
#END_DEF

if __name__ == '__main__':
	#exo23()
	exo24()
