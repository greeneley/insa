# -*- coding: utf-8 -*-
"""
TD5
"""
import ciw
N = ciw.create_network(
        Arrival_distributions=[['Exponential', 1],
                               'NoArrivals',
                               'NoArrivals',
                               'NoArrivals'],
        Service_distributions=[['Exponential', 5/2],
                              ['Exponential', 2],
                              ['Exponential', 3/2],
                              ['Exponential', 5/2]],
        Transition_matrices=[[0.2, 0.4, 0.4, 0.0],
                             [0.0, 0.2, 0.0, 0.8],
                             [0.0, 0.0, 0.1, 0.9],
                             [0.0, 0.0, 0.0, 0.0]],
        Number_of_servers=[1, 1, 1, 1]
)
completed_custs = []
for trial in range(10):
    ciw.seed(trial)
    Q = ciw.Simulation(N)
    Q.simulate_until_max_time(200)
    recs = Q.get_all_records()
    num_completed = len([r for r in recs if r.node==3 and r.arrival_date < 180])
    completed_custs.append(num_completed)
sum(completed_custs) / len(completed_custs)
    