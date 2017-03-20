#include <stdio.h>
#include <stdlib.h>
// Quelques petites modifications éventuelles
// ------------------------------------------------------------------
// 1er cas
// ------------------------------------------------------------------
// lpsolve est "bien sinstallé" sur la machine avec les bibliothèques adéquates
#include "./lpsolve/lp_lib.h"
// la compilation peut se faire alors de la manière suivante
//      gcc Exemple.c -llpsolve55 -lcolamd -ldl -lm -o Exemple
// Pour exécuter, il faut que la variable LD_LIBRARY_PATH
// soit bien initialisée, par exemple (dans le .bashrc)
// export  LD_LIBRARY_PATH=:/usr/lib/lp_solve

// ------------------------------------------------------------------
// 2ème cas
// ------------------------------------------------------------------
// Vous avez installé dans votre HOME le répertoire pour lp_solve
// #include "le chemin complet de lp_lib.h"
// la compilation peut se faire alors de la manière suivante
// gcc exple.c  liblpsolve55.so
// en précisant où se trouve la bibliothèque liblpsolve55.so
// Pour exécuter, il faut que la variable LD_LIBRARY_PATH
// soit bien initialisée, par exemple (dans le .bashrc)
// export  LD_LIBRARY_PATH=:"Le bon chemin de la bibliothèque dynamique"



void create_random_graph(int n, REAL*** PMat, int proba);
void create_line(int n, int ** pLigne);

int main()
{
  // Si on veut un peu plus d'aléatoire (à la fin du programme)
  //srand(time(0));
  /*** Exemple qui définit le problème suivant:
       Max 3x + 2 y
       st
           x - y <= 6
	   x + 2y <= 17
	   2x - y <= 4
	   x,y >=0
   */
  lprec *lp;
  lp = make_lp(3, 2);
  // 1ère forme : directement les valeurs
  // Pour chaque élément, 
  //             set_mat(problème, 
  //                     numéro de la contrainte,
  //                     numéro de la variable,
  //                     valeur affectée);
  set_mat(lp, 1, 1, 1);
  set_mat(lp, 1, 2, -1);
  set_mat(lp, 2, 1, 1);
  set_mat(lp, 2, 2, 2);
  set_mat(lp, 3, 1, 2);
  set_mat(lp, 3, 2, -1);

  // Les bornes sur les variables
  set_upbo(lp, 1, 3);   // x <= 3
  // Ces deux bornes sont implicites
  set_lowbo(lp, 1, 0);  // x >= 0
  set_lowbo(lp, 2, 0);  // y >= 0

  // La fonction objectif : Max 3x + 2y
  REAL obj[1+2] = {0, 3, 2};
  set_obj_fn(lp, obj);
  set_maxim(lp);

  // Le membre droit
  REAL RH[1+3] = {0, 6, 17, 4};
  set_rh_vec(lp, RH);

  // Un peu de cosmétique: on change les noms pour que cela soit plus
  // joli 
  set_lp_name(lp, (char *) "Premier Exemple");
  set_col_name(lp, 1, (char *) "x");
  set_col_name(lp, 2, (char *) "y");

  // Affichage
  write_lp(lp,NULL);

  // Résolution un peu verbeuse.
  int ret = solve(lp);

  // Récupération des résultats directement
  printf("Résultats:\n\tFonction objectif : %f\n", 
	 get_objective(lp));
  int i;

  REAL var[2], *ptr_var; 
  get_variables(lp, var);
  get_ptr_variables(lp, &ptr_var);
  for(i=0; i<2 ;i++){
    printf("\t%s = %f\n",
	   get_col_name(lp,i+1),
	   var[i]);
  }
  
  delete_lp(lp);

  printf("\n-----------------------------------------------------\n");
  // 2ème forme Création du modèle colonne par colonne
  // Il faut fixer le nombre de lignes dans la définition du modèle
  // Dans cette forme, l'indice 0 représente la fonction objectif.
  printf("\nAutre forme pour remplir la matrice initiale\n");

  lp = make_lp(3,0);
  
  // Ajout d'une colonne simple
  REAL column[1+3] = {3, 1, 1, 2};
  /* must be 1 more than number of rows ! */
  add_column(lp,column);

 
  
  // Ajout d'une colonne en n'indiquant que les valeurs non nulles
  REAL sparsecolumn[4] = {2, -1, 2, -1}; 
  /* must be the number of non-zero values */
  int rowno[4] = {0, 1, 2, 3};

  add_columnex(lp, 4, sparsecolumn, rowno); 
  write_lp(lp,NULL);

  // La suite peut se faire comme précédemment...
  delete_lp(lp);

  // 3ème forme: ajout contrainte par contrainte
  printf("\nEncore une autre forme pour remplir la matrice initiale\n");

  lp = make_lp(0,2);
  // La fonction objectif
  REAL	OBJ[1+2] = {0, 3, 2};
  set_obj_fn(lp, OBJ);

  set_add_rowmode(lp, TRUE);

  
  // Ajout d'une contrainte directement
  REAL row[1+2] ={0, 1, -1};     /* must be 1 more than number of columns ! */

  add_constraint(lp, row, GE, 6.0); // LE, GE ou EQ

  // Ajout d'une contrainte quand on a des valeurs nulles
  REAL sparserow[2] = {1, 2}; /* must be the number of non-zero values */
  int colno[2]= {1, 2};  // On précise les index des colonnes où cela doit être non nul

  add_constraintex(lp, 2, sparserow, colno, EQ, 17.0); 

  // Dernière ligne
  row[1] = 2;
  row[2] = -1;
  add_constraint(lp, row, LE, 4.0);



  set_add_rowmode(lp, FALSE);
  write_lp(lp,NULL);
  set_verbose(lp, CRITICAL); // NEUTRAL, CRITICAL, SEVERE, IMPORTANT, NORMAL (défaut), DETAILED, FULL

  ret = solve(lp);

  // Récupération des résultats directement
  if (get_solutioncount(lp)==0)
    printf("Pas de solution\n");
  else
    printf("Résultats:\n\tFonction objectif : %f\n", 
	   get_objective(lp));


  /* Create a new LP model */
  /* lprec *lp2; */
  /* lp2 = make_lp(4, 0); */
  /* printf("Modèle initial avant insertion de données \n"); */
  /* write_lp(lp,NULL); */
  /* printf("Insertion de données \n"); */

  /* if(lp == NULL) { */
  /*   fprintf(stderr, "Unable to create new LP model\n"); */
  /*   return(1); */
  /* } */

  /* column[0] = 1.0; /\* the objective value *\/ */
  /* column[1] = 2.0; */
  /* column[2] = 0.0; */
  /* column[3] = 3.0; */
  /* add_column(lp, column); */

  /* int i; */
  /* for ( i = 0 ; i<3; i++) */
  /*   { */
  /*     rowno[0] = 0; sparsecolumn[0] = (3*(i+1)*1.0-1 ); /\* the objective value *\/ */
  /*     rowno[1] = (1+i)%3+1; sparsecolumn[1] = (3*(i+1)%3*2.0- 6); */
  /*     rowno[2] = i%3 +1; sparsecolumn[2] = 3.0; */
  /*     add_columnex(lp, 3, sparsecolumn, rowno); */
  /*   } */

  /* for ( i = 0 ; i<=3; i++) */
  /*     set_rh(lp, i, i-1.0); /\* sets the value 1.0 for RHS row 1 *\/ */
  /* set_bounds(lp, 1, 1.0, 2.0); */
  /* set_lowbo(lp, 1, 1.0); */

  /* set_mat(lp, 1, 2, 12); */
  /* //set_maxim(lp); */
  /* //print_lp(lp); */

  


  /* for (i=0; i<3; i++) */
  /*   printf("Variable %f: %f\n", var[i], ptr_var[i]);  */
  delete_lp(lp);

  // Exemple de création d'un graphe aléatoire.
  /*
  int n= 5;
  REAL ** Graph;
  create_random_graph(n, &Graph, 60);

  int  j;
  for(i = 0; i < n; i++){
    for(j=0; j < n; j++)
      printf("%f\t", Graph[i][j]);
    printf("\n");
  }
  */

  printf("\n-----------------------------------------------------\n");

  lp = make_lp(1, 2);
  // 1ère forme : directement les valeurs
  // Pour chaque élément, 
  //             set_mat(problème, 
  //                     numéro de la contrainte,
  //                     numéro de la variable,
  //                     valeur affectée);
  set_mat(lp, 1, 1, 1);
  set_mat(lp, 1, 2, 1);

  // Les bornes sur les variables
  //set_upbo(lp, 1, 3);   // x <= 3
  // Ces deux bornes sont implicites
  //set_lowbo(lp, 1, 0);  // x >= 0
  //set_lowbo(lp, 2, 0);  // y >= 0

  // La fonction objectif : Max 3x + 2y
  REAL obj2[1+2] = {0, 1, 2};
  set_obj_fn(lp, obj2);
  set_maxim(lp);

  // Le membre droit
  REAL RH2[1+3] = {0, 5};
  set_rh_vec(lp, RH2);

  // Un peu de cosmétique: on change les noms pour que cela soit plus
  // joli 
  set_lp_name(lp, (char *) "Troisieme Exemple");
  set_col_name(lp, 1, (char *) "x");
  set_col_name(lp, 2, (char *) "y");

  // Affichage
  write_lp(lp,NULL);

  // Résolution un peu verbeuse.
  ret = solve(lp);

  // Récupération des résultats directement
  printf("Résultats:\n\tFonction objectif : %f\n", 
   get_objective(lp));

  get_variables(lp, var);
  get_ptr_variables(lp, &ptr_var);
  for(i=0; i<2 ;i++){
    printf("\t%s = %f\n",
     get_col_name(lp,i+1),
     var[i]);
  }
  
  delete_lp(lp);

  printf("\n-----------------------------------------------------\n");

  // Si on veut un peu plus d'aléatoire (à la fin du programme)
  //srand(time(0));
  /*** Exemple qui définit le problème suivant:
     Max 3x + 2 y
       st
     x - y <= 6
     x + 2y <= 17
     2x - y <= 4
     x,y >=0
   */

  // 3ème forme: ajout contrainte par contrainte
  printf("\nEncore une autre forme pour remplir la matrice initiale\n");

  lp = make_lp(0,2);
  // La fonction objectif
  REAL  OBJ3[1+2] = {0, 1, 2};
  set_obj_fn(lp, OBJ3);
  set_add_rowmode(lp, TRUE);

  // Ajout d'une contrainte directement
  REAL row3[1+2] = {0, 1, 1};     /* must be 1 more than number of columns ! */

  add_constraint(lp, row3, LE, 5.0); // LE, GE ou EQ

  // Dernière ligne
  row3[1] = 2;
  row3[2] = -1;
  add_constraint(lp, row3, GE, 0.0);

  set_add_rowmode(lp, FALSE);
  write_lp(lp,NULL);
  set_verbose(lp, CRITICAL); // NEUTRAL, CRITICAL, SEVERE, IMPORTANT, NORMAL (défaut), DETAILED, FULL

  ret = solve(lp);

  // Récupération des résultats directement
  if (get_solutioncount(lp)==0)
    printf("Pas de solution\n");
  else
    printf("Résultats:\n\tFonction objectif : %f\n", 
     get_objective(lp));


  /* Create a new LP model */
  /* lprec *lp2; */
  /* lp2 = make_lp(4, 0); */
  /* printf("Modèle initial avant insertion de données \n"); */
  /* write_lp(lp,NULL); */
  /* printf("Insertion de données \n"); */

  /* if(lp == NULL) { */
  /*   fprintf(stderr, "Unable to create new LP model\n"); */
  /*   return(1); */
  /* } */

  /* column[0] = 1.0; /\* the objective value *\/ */
  /* column[1] = 2.0; */
  /* column[2] = 0.0; */
  /* column[3] = 3.0; */
  /* add_column(lp, column); */

  /* int i; */
  /* for ( i = 0 ; i<3; i++) */
  /*   { */
  /*     rowno[0] = 0; sparsecolumn[0] = (3*(i+1)*1.0-1 ); /\* the objective value *\/ */
  /*     rowno[1] = (1+i)%3+1; sparsecolumn[1] = (3*(i+1)%3*2.0- 6); */
  /*     rowno[2] = i%3 +1; sparsecolumn[2] = 3.0; */
  /*     add_columnex(lp, 3, sparsecolumn, rowno); */
  /*   } */

  /* for ( i = 0 ; i<=3; i++) */
  /*     set_rh(lp, i, i-1.0); /\* sets the value 1.0 for RHS row 1 *\/ */
  /* set_bounds(lp, 1, 1.0, 2.0); */
  /* set_lowbo(lp, 1, 1.0); */

  /* set_mat(lp, 1, 2, 12); */
  /* //set_maxim(lp); */
  /* //print_lp(lp); */

  


  /* for (i=0; i<3; i++) */
  /*   printf("Variable %f: %f\n", var[i], ptr_var[i]);  */
  delete_lp(lp);

  // Exemple de création d'un graphe aléatoire.
  /*
  int n= 5;
  REAL ** Graph;
  create_random_graph(n, &Graph, 60);

  int  j;
  for(i = 0; i < n; i++){
    for(j=0; j < n; j++)
      printf("%f\t", Graph[i][j]);
    printf("\n");
  }
  */
}

		   
/**
 * Procédure permettant de créer un graphe aléatoire en fonction d'une
 * densité passée en paramètre.  La densité est entière (entre 0 et
 * 100).  Pour l'instant, cette procédure n'est pas "sécurisée". Il
 * faut donc l'utiliser 'le plus correctement possible'.
 */
void create_random_graph(int n, REAL*** PMat, int proba){
  int i, j;

  *PMat = (REAL**) malloc (n* sizeof(REAL*));
  
  for(i = 0; i< n ; i++)
    (*PMat)[i] = (REAL *) malloc(n* sizeof(REAL));

  for(i=0; i< n ; i++)
    {
      (*PMat)[i][i] = 0;
      for(j=i+1; j < n ; j++)
	{
	  int k = rand() %100;
	  if (k<proba){
	    (*PMat)[i][j] = 1;
	    (*PMat)[j][i] = 1;}
	  else{
	    (*PMat)[i][j] = 0;
	    (*PMat)[j][i] = 0;}
	    
	}
    }

}
