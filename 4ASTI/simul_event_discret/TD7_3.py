# -*- coding: utf-8 -*-
"""
Created on Tue Dec 19 10:09:31 2017

@author: yhou
"""

# -*- coding: utf-8 -*-
"""
Created on Mon Dec 18 14:38:08 2017

@author: yhou
"""
import random 
def probability_of_baulking(n):
    if n <= 6:
        return 0.0
    else:
        return 1.0

def probability_of_baulking_no(n):
    return 0.0

def probability_service_type1():
    return random.uniform(3.0,10.0)+2
def probability_service_type2():
    return random.triangular(3.0,8.0,5.0)+1
def probability_service_type3():
    return random.expovariate(1/4)+2
def proba_maintenance():
    return random.normalvariate(20,2)

import ciw
from statistics import * 

N = ciw.create_network( 
        Arrival_distributions={'Class 0': [['Exponential', 0.3*1/20]],'Class 1': [['Exponential', 0.5*1/20]],'Class 2': [['Exponential', 0.2*1/20]],'Class 3': [['Deterministic',12*60]]},
        Service_distributions={'Class 0': [['UserDefined', probability_service_type1]],'Class 1': [['UserDefined', probability_service_type2]],'Class 2': [['UserDefined', probability_service_type3]],'Class 3': [['UserDefined', proba_maintenance]]},
        Number_of_servers=[1],
        Queue_capacities=[10],
        Priority_classes={'Class 0': 1,'Class 1': 2,'Class 2':2,'Class 3':0},
        Baulking_functions={'Class 0':[probability_of_baulking_no] , 'Class 1':[probability_of_baulking_no],'Class 2': [probability_of_baulking],'Class 3':[probability_of_baulking_no]}
)
servicetimes_moyen={'all':[],0:[],1:[],2:[]}
waits_moyen={'all':[],0:[],1:[],2:[]}
for trial in range(1000):
    ciw.seed(trial)
    Q = ciw.Simulation(N)
    Q.simulate_until_max_time(24*60)
    recs = Q.get_all_records()
    waits_moyen['all'].append(mean([r.waiting_time for r in recs]))
    waits_moyen[0].append(mean([r.waiting_time for r in recs if r.customer_class==0]))
    waits_moyen[1].append(mean([r.waiting_time for r in recs if r.customer_class==1]))
    waits_moyen[2].append(mean([r.waiting_time for r in recs if r.customer_class==2]))
    servicetimes_moyen['all'].append(mean([r.service_time for r in recs]))
    servicetimes_moyen[0].append(mean([r.service_time for r in recs if r.customer_class==0]))
    servicetimes_moyen[1].append(mean([r.service_time for r in recs if r.customer_class==1]))
    servicetimes_moyen[2].append(mean([r.service_time for r in recs if r.customer_class==2]))
print("Moyenne duree d'attente: %f"% mean(waits_moyen['all']))
print("Moyenne duree d'attente Type 1: %f"% mean(waits_moyen[0]))
print("Moyenne duree d'attente Type 2: %f"% mean(waits_moyen[1]))
print("Moyenne duree d'attente Type 3: %f"% mean(waits_moyen[2]))
print("Moyenne duree de service: %f"% mean(servicetimes_moyen['all']))
print("Moyenne duree de service Type 1: %f"% mean(servicetimes_moyen[0]))
print("Moyenne duree de service Type 2: %f"% mean(servicetimes_moyen[1]))
print("Moyenne duree de service Type 3: %f"% mean(servicetimes_moyen[2]))
