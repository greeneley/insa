# -*- coding: utf-8 -*-
"""
Created on Tue Dec 19 10:25:34 2017

@author: yhou
"""

import ciw
from statistics import * 
import random 
def arrival_proba(t):
    t=t+8*60
    if (t%(24*7*60))<(24*60*5): 
        a=t%24.0
        if (a >=8.0) & (a<=12.0*60):
            return random.expovariate(1/8)
        if (a >12*60) & (a<=14*60):
            return random.expovariate(1/4)
        if (a>14*60) & (a >=18*60):
            return random.expovariate(1/8)
        if (a>18*60) & (a <=20*60): 
            return random.expovariate(1/4)
        else: 
            return random.expovariate(1/10)
    else:
        return random.expovariate(1/10)

def service_proba(t):
    t=t+8*60
    if t%(24*7*60)<(24*5*60): 
        a=t%24.0
        if (a>=8.0*60) & (a<=12.0*60):
            return random.expovariate(1/20)
        if (a >12.0*60) & (a<=14.0*60):
            return random.expovariate(1/15)
        if (a>14.0*60) & (a >=18.0*60):
            return random.expovariate(1/20)
        if (a>18.0*60) & (a <=20.0*60): 
            return random.expovariate(1/18)
        else: 
            return random.expovariate(1/25)
    else:
        return random.expovariate(1/25)

class CustomArrivalNode(ciw.ArrivalNode):
    def decide_baulk(self, next_node, next_individual):
        if next_node.number_of_individuals<next_node.c:
            self.send_individual(next_node, next_individual)
        else:
            self.record_baulk(next_node)

class CustomNode(ciw.Node):
    def take_servers_off_duty(self):
        to_delete = self.servers[::1]  # copy
        for s in self.servers:
            s.shift_end = self.next_event_date
            if s.cust is not False:
                self.interrupted_individuals.append(s.cust)
                self.interrupted_individuals[-1].service_end_date = self.next_shift_change
                self.interrupted_individuals[-1].service_time =0
        self.interrupted_individuals.sort(key=lambda x: (x.priority_class,x.arrival_date))
        for obs in to_delete:
            self.kill_server(obs)
    def begin_interrupted_individuals_service(self, current_time, srvr):
        ind = [i for i in self.interrupted_individuals][0]
        self.attach_server(srvr, ind)
        self.interrupted_individuals.remove(ind)

N = ciw.create_network(
    Arrival_distributions= {'Class 0':[['TimeDependent',arrival_proba]]},
    Service_distributions= {'Class 0':[['TimeDependent',service_proba]]}, 
    Number_of_servers=[([[0,8*60],[10, (10)*60], 
                        [0,(12)*60], [10, (13)*60],
                        [0,(24*2+8)*60], [25, (24*2+20)*60],
                        [0,(24*3+8)*60], [25, (24*3+20)*60],
                        [0,(24*4+8)*60], [25, (24*4+20)*60],
                        [0,(24*5+9)*60], [25, (24*5+18)*60],
                        [0,(24*6+9)*60], [25, (24*6+18)*60],
                        [0,(24*7)*60]],True)]
)
nb=[]
for trial in range(1000):
    ciw.seed(trial)
    Q = ciw.Simulation(N,arrival_node_class=CustomArrivalNode, node_class=CustomNode)
    Q.simulate_until_max_time(24*7*60)
    recs = Q.get_all_records()
    nb.append(len(recs))


print(mean(nb))
