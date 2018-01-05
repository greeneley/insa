#!/usr/bin/python3.5

import ciw

N = ciw.create_network(Arrival_distributions=[['Exponential', 0.2]],
					   Service_distributions=[['Exponential', 0.1]],
					   Number_of_servers=[3])

ciw.seed(1)
Q = ciw.Simulation(N)
Q.simulate_until_max_time(1440)
#print(Q.get_all_records())

# -----------------

def exo31(taux_service):
	N = ciw.create_network(Arrival_distributions=[['Exponential', 0.25]],
						   Service_distributions=[['Exponential', taux_service]],
						   Number_of_servers=[4])

	ciw.seed(1)
	Q = ciw.Simulation(N)
	Q.simulate_until_max_time(1440)

# -----------------
# M/D/INF/INF/1000
N = ciw.create_network(Arrival_distributions=[['Exponential', 5]],
					   Service_distributions=[['Deterministic', 4]],
					   Number_of_servers=['Inf'],
					   Queue_capacities=['Inf'])

ciw.seed(1)
Q = ciw.Simulation(N)
Q.simulate_until_max_time(1440)

# -----------------
# G/G/1/INF/INF/SIRO
N = ciw.create_network(Arrival_distributions=[['Uniform', 5, 10]],
					   Service_distributions=[['Uniform', 2, 5]],
					   Number_of_servers=[1],
					   Queue_capacities=['Inf'])

ciw.seed(1)
Q = ciw.Simulation(N)
Q.simulate_until_max_time(1440)

# -----------------
# M/M/4/5

N = ciw.create_network(Arrival_distributions=[['Exponential', 0.25]],
					   Service_distributions=[['Uniform', 0.1]],
					   Number_of_servers=[4],
					   Queue_capacities=[5])

ciw.seed(1)
Q = ciw.Simulation(N)
Q.simulate_until_max_time(1440)


