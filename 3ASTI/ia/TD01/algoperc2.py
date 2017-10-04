# A. Hafiane, INSA CVL,
# cours STI 3A, 2016-2017

import numpy as np
#import matplotlib.pyplot as plt

#------------------------#
# Algo perceptron        #         
#------------------------#
def perce(X,Y,w_ini,w0,eta, seuil):
    N,D=np.shape(X);
    max_iter=10000; # nombre maximal d'iteration
  

    w=w_ini        # Initilaisation 
    nb_iter= 0        # compteur d iterations

    mal_classe=N     # le nombre de points mal classes

    while(mal_classe>seuil) and (nb_iter<max_iter):
        nb_iter=nb_iter+1 # epoch
        mal_classe=0
        Dw=np.zeros(D) # initialisation du gradient

        for i in range(0,N):
	    # w0*X[i,0] + w[1]*X[i,1]+w[2]X[i,2]...., Y c'est le label de la classe ;
            if(Y[i]*(np.dot(w,X[i,:])+w0)<0):
                mal_classe=mal_classe+1
                Dw=Dw-Y[i]*X[i,:]  #calcul du changement
              
        w=w-eta*Dw # mise a jour du vecteur W

    return w


#----------------------------------------#
# fonction calcul echantillon mal classe #         
#----------------------------------------#
def calcul_erreur(X,Y,W,w0):
    N,D=np.shape(X);
    erreur=0; 

    for i in range(0,N):
        h = np.dot(W,X[i,:])+w0

        if h*Y[i]<0:  # echantillon mal classe
            erreur=erreur+1

    return erreur/float(N), erreur # pourcentage d'erreur et nombre d'echantillons mal classes


#------------------------#
# preparation de donnees #         
#------------------------#

# charger les vecteurs de caracteristiques 
X =np.loadtxt('iris_a.txt',usecols=(0,1,2,3),delimiter=',',dtype=float)
# charger les labels
Y = np.loadtxt('iris_a.txt',usecols=(4,),delimiter=',',dtype=int)


N,D=np.shape(X);
w_ini=np.random.randn(D)
w0 = np.random.randn(1)
eta = 0.01
seuil = 3

# 50 % de donnees pour l'apprentissage
X_a=X[0:25,:]                 # 50% d'echantillon de la classe  +1
X_a=np.vstack((X_a,X[50:75,:])) # ajout au tableau 50%  d'echantillon de la classe -1

Y1=Y[0:25]                  # label classe +1    
Y1=np.hstack((Y1,Y[50:75])) # label classe -1




#------------------------#
# apprentissage          #         
#------------------------#

#plt.scatter(X[0:50,1],X[0:50,2],color='b')
#plt.scatter(X[50:100,1],X[50:100,2],color='r')
#plt.show()
#print Y1

W = perce(X_a,Y1,w_ini,w0,eta,seuil)


#------------------------#
# evaluation de l'erreur #         
#------------------------#
X_b=X[26:50,:]                 # 50% d'echantillon de la classe  +1
X_b=np.vstack((X_b,X[76:100,:])) # ajout au tableau 50%  d'echantillon de la classe -1

Y2=Y[26:50]                  # label classe +1    
Y2=np.hstack((Y1,Y[76:100])) # label classe -1


print calcul_erreur(X_b,Y2,W,w0)
