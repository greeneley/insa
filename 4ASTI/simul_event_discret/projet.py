#!/usr/bin/python3.5

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
import matplotlib.pyplot as plt 


# ==========================================
#            CONSTANTES GLOBALES
# ==========================================

# Les valeurs sont en minutes
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
    """Implementation du reseau de files d'attente du projet. Voir compte-rendu pour plus d'informations."""

    #########################
    #      INIT METHOD
    #########################

    def __init__(self, warmup_time=0, cooldown_time=0):
        """Initialise le reseau ainsi que les variables utiles.

            @param list[float]    proportion_class La i-eme valeur contient la proportion de la class n°i.
            @param int            max_simul_t      Temps en mins de la simulation sans compter le cooldown.
            @param int            cooldown         Temps de cooldown de la simulation.
            @param int            warmup           Temps de warmup de la simulation.
            @param list           recs             Liste contenant des objets de type ciw.Records issue de la simulation.
            @param ciw.Simulation Q                Objet ciw.Simulation.
        """
        self.proportion_class = [0.03, 0.05, 0.18, 0.54, 0.20]
        self.max_simul_t      = 0
        self.cooldown         = cooldown_time
        self.warmup           = warmup_time
        self.recs             = None # Pour les records
        self.Q                = None # Pour la simulation

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
                           ['Triangular', 3, 7, 5],
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
                'Class 0': [[0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0],
                            [0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0],
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
            Priority_classes={'Class 0': 0, 
                              'Class 1': 1,
                              'Class 2': 2,
                              'Class 3': 3,
                              'Class 4': 4
            },
            Number_of_servers=[
                [[4, 480], [6, 960], [4,1440]],
                [[4, 480], [6, 960], [4,1440]],
                [[4, 480], [6, 960], [4,1440]],
                [[4, 480], [6, 960], [4,1440]],
                [[4, 480], [6, 960], [4,1440]],
                [[4, 480], [6, 960], [4,1440]],
                [[4, 480], [6, 960], [4,1440]]
            ]
        )
    #END_DEF

    #########################
    #      PRINT METHOD
    #########################

    def trials(self, nb_trials=1, temps=SEMAINE, export=False):
        """Effectue et affiche des analyses de la simulation.

            @param int     nb_trials Le nombre de trials a effectuer. Si rien n'est indique, n'effectue qu'une seule simulation.
            @param int     temps     Le nombre de temps en minutes a simuler. Une semaine par defaut.
            @param boolean export    Indique a la classe s'il faut exporter les resultats. False par defaut.s

        Les parametres analyses sont actuellement, par classe et globalement :
            - temps de service moyen
            - temps d'attente moyen
            - nombre de patients moyens
        """

        # =====================
        #       ERRORS
        # =====================
        if(nb_trials<0):
            print("Erreur sur le nombre de trials")
            return
        #END_IF

        # =====================
        #      Variables
        # =====================
        avg_service   = {'all':[],0:[],1:[],2:[],3:[],4:[]}
        avg_wait      = {'all':[],0:[],1:[],2:[],3:[],4:[]}
        nb_patient    = {'all':[],0:[],1:[],2:[],3:[],4:[]}
        interventions = [ {'all':[],0:[],1:[],2:[],3:[],4:[]}, 
                          {'all':[],0:[],1:[],2:[],3:[],4:[]} ]


        # =====================
        #      Calculs
        # =====================

        for trial in range(nb_trials):
            if(nb_trials>1):
                ciw.seed(trial)
            #END_IF
            self.compute(temps)

            # ======
            # Init compteur
            # ======
            for entry in nb_patient:
                nb_patient[entry].append(0)
            #END_FOR

            for i in range(2):
                for entry in interventions[i]:
                    interventions[i][entry].append(0)
                #END_FOR
            #END_FOR


            # ======
            # Data recovering
            # ======
            avg_service['all'].append(mean([r.service_time for r in self.recs if r.arrival_date > self.warmup and r.arrival_date < self.max_simul_t]))
            avg_wait['all'].append(mean([r.waiting_time for r in self.recs if r.arrival_date > self.warmup and r.arrival_date < self.max_simul_t]))

            # Sur de petite duree, il se peut qu'aucune classe 0 n'apparaissent, on doit donc l'ignorer
            for i in range(5):
                try:
                    avg_service[i].append(mean([r.service_time for r in self.recs if r.customer_class==i and r.arrival_date > self.warmup and r.arrival_date < self.max_simul_t]))
                    avg_wait[i].append(mean([r.waiting_time for r in self.recs if r.customer_class==i and r.arrival_date > self.warmup and r.arrival_date < self.max_simul_t]))
                    
                    # Nombre de patients
                    temp_patients = []
                    temp_interv   = [ [], [] ]
                    for r in self.recs:
                        if( r.arrival_date > self.warmup and r.arrival_date < self.max_simul_t and r.customer_class==i):
                            if(r.id_number not in temp_patients):
                                temp_patients.append(r.id_number)
                                nb_patient['all'][trial] += 1
                                nb_patient[i][trial]     += 1
                            #END_IF

                            if(r.node==3):
                                interventions[0][i][trial]     += 1
                                interventions[0]['all'][trial] += 1
                            #END_IF

                            if(r.node==7):
                                interventions[1][i][trial]     += 1
                                interventions[1]['all'][trial] += 1
                            #END_IF

                        #END_IF
                    #END_FOR
                except Exception as e:
                    pass
                #END_TRY
            #END_FOR
        #END_FOR


        # =====================
        #      Affichage
        # =====================
        #
        # Pour les blocs try/except : on doit prendre en compte la non presence de classe 0
        # sur les petites durees. Il faut donc prendre en compte d'eventuelles erreurs.

        print("\nNombre moyen de patients diagnostiques : %.f" % mean(nb_patient['all']))
        try:
            print("Nombre moyen de patients diagnostiques [Code %d] : %.f (%.2f %%)" % (1, mean(nb_patient[0]), mean(nb_patient[0])/mean(nb_patient['all'])*100))
        except Exception as e:
            print("Nombre moyen de patients diagnostiques [Code 1] : 0 (0.00 %%)")
        #END_TRY
        for i in range(1, 5):
            print("Nombre moyen de patients diagnostiques [Code %d] : %.f (%.2f %%)" % (i+1, mean(nb_patient[i]), mean(nb_patient[i])/mean(nb_patient['all'])*100))
        #END_FOR

        print("\nNombre moyen de patients en deuxieme intervention : %.f (%.2f %%)" % (mean(interventions[1]['all']), mean(interventions[1]['all'])/mean(interventions[0]['all'])*100))
        for i in range(1,5):
            print("Nombre moyen de patients en deuxieme intervention [Code %d] : %.f (%.2f %%)" % (i+1, mean(interventions[1][i]), mean(interventions[1][i])/mean(interventions[0][i])*100))
        #END_FOR

        print("\nDuree moyenne d'attente : %.2f mins" % mean(avg_wait['all']))
        try:
            print("Attente moyenne [Code %d] : %.2f mins" % (1, mean(avg_wait[0])))
        except Exception as e:
            print("Attente moyenne [Code 1] : 0.00 mins")
        #END_TRY
        for i in range(1, 5):
            print("Attente moyenne [Code %d] : %.2f mins" % (i+1, mean(avg_wait[i])))
        #END_FOR

        print("\nDuree moyen de service: %.2f mins" % mean(avg_service['all']))
        try:
            print("Temps de service moyen [Code %d] : %.2f mins" % (1, mean(avg_service[0])))
        except Exception as e:
            print("Temps de service moyen [Code 1] : 0.00 mins")
        #END_TRY
        for i in range(1, 5):
            print("Temps de service moyen [Code %d] : %.2f mins" % (i+1, mean(avg_service[i])))
        #END_FOR
    #END_DEF


    #########################
    #    COMPUTE METHOD
    #########################

    def compute(self, temps):
        """Fixe le temps de simulation et simule le reseau de files d'attente.

            @param int temps Nombre de minutes pendant laquelle effectuer la simulation.

        La simulation prend en compte les parametres de temps donnes par le warmup et le cooldown.
        A la fin de la simulation, la methode enregistre les records de l'objet Ciw.Simulation.
        """
        self.max_simul_t = temps
        self.Q = ciw.Simulation(self.N)
        self.Q.simulate_until_max_time(self.warmup+temps+self.cooldown)
        self.recs = self.Q.get_all_records()
    #END_DEF


    #########################
    #  ARRIVAL DISTRIBUTIONS
    #########################

    def arrival_classes(self, proportion, t):
        """Calcule la probabilite d'arrivee d'une classe de proportion 'proportion' a l'instant 't'.

            @param  float proportion Proportion de la classe qui demande le calcul.
            @param  int   t          Temps auquel doit etre calcule la probabilite.

            @return float            La probabilite resultant d'une distribution exponentielle.

        Toutes les classes suivent une distribution d'arrivee exponentielle.
        Nous n'avons donc besoin que d'avoir le temps et la proportion pour pouvoir renvoyer les bons
        resultats selon les horaires d'ouverture des salles. C'est pourquoi chaque methode de type
        self.arrival_class_X appelle cette methode pour s'identifier.
        """

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
    # Voir constantes : WARUMP = COOLDOWN = 100 mins
    projet = Reseau(WARMUP, COOLDOWN)
    print("OK")
    print("============== COMPUTING ==============")
    # Sur X trials, on effectue une simulation de Y temps et on exporte les resultats si True
    projet.trials(10, SEMAINE,True)

    # On fait par defaut une seule simulation d'une semaine sans exporter les resultats
    #projet.trials()
#END_IF
