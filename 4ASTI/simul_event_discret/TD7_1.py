# -*- coding: utf-8 -*-
"""
Created on Tue Dec 19 11:19:04 2017

@author: yhou
"""

import ciw
import matplotlib.pyplot as plt


N = ciw.create_network(
    Arrival_distributions= [['Exponential', 1/5]],
    Service_distributions= [['Exponential', 1/3]], 
    Batching_distributions=[['Custom', [1, 2, 3, 4], [2/5, 1/3, 1/6 , 1/10]]],
    Number_of_servers= [1]
    )

Q = ciw.Simulation(N)
Q.simulate_until_max_time(4*60)
recs = Q.get_all_records()
