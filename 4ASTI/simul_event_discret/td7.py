#!/usr/bin/python3.5

import ciw
import random

N = ciw.create_network(
	Arrival_distributions=[['Deterministic', 12.0]],
	Service_distributions=[['Deterministic', 180.0]],
	Batching_distributions=[['Custom', [1,2,3,4], [0.4, 0.35, 0.15, 0.1]]],
	Number_of_servers=[1]
)

Q = ciw.Simulation(N)
Q.simulate_until_max_time(60.0*60.0*4.0)
recs = Q.get_all_records()

print(len(Q.nodes[-1].all_individuals))