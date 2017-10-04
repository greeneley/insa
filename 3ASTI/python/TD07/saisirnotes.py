#!/usr/bin/python3.2

dictToto = {'Nom':'Toto', 'Note':-1}
dictTiti = {'Nom':'Titi', 'Note':-1}
dictTata = {'Nom':'Tata', 'Note':-1}
etudiants = {'Toto':dictTata, 'Titi':dictTiti, 'Tata':dictToto}

for element in etudiants:
   note = int(input("Note de l'etudiant "+str(element)+" : "))
   etudiants[element]['Note'] = note

for element in etudiants:
   print(etudiants[element])
