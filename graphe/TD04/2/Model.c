#include <stdlib.h>
#include <stdio.h>
#include <lpsolve/lp_lib.h>

// Ne pas oublier de modifier LD_LIBRARY_PATH pour inclure le bon
// chemin


int readPB(char * namefic, int * nbGroupes, int * nbProjets, int *** MatPoids);

int main(){

  printf("Hello\n");

  int nbG, nbP;
  int ** Poids;

  //  readPB("Proj.data", &nbG, &nbP, &Poids);
  readPB("PB.data", &nbG, &nbP, &Poids);
  printf("Après l'appel nbGroupes : %i\t nbProjets : %i\n", nbG, nbP );
  int i, j;
  /*
  for(i = 0; i<nbP; i++){
    for(j=0; j <nbG; j++) {
      printf("%i\t", Poids[i][j]);
    }
    printf("\n");
  }
  */

  
  lprec *lp;
  int nbVar = nbG * nbP;
  REAL line[1+nbVar];     


  /* Create a new LP model */
  lp = make_lp(0, nbVar);
  if(lp == NULL) {
    fprintf(stderr, "Unable to create new LP model\n");
    return(1);
  }


  for (i = 0; i < nbP ; i++)
    for(j = 0; j < nbG ; j++){
      // printf("%i (%i)\t", 1+ i * nbG + j,Poids[i][j] );
      line[1+ i * nbG + j] = Poids[i][j];
      char str[20];
      sprintf(str, "x_%i_%i", i, j);
      set_col_name(lp, 1+ i * nbG + j, str);
      // printf("var (%i) %s \t", 1+ i * nbG + j, str);
    }
  printf("\n");
  
  set_obj_fn(lp, line);
  set_maxim(lp);
  set_add_rowmode(lp, TRUE);


  // Ajout des contraintes d'unicité
  REAL SparseVect[1+nbP];  // On prend nbP car il est plus grand que nbG
  int colnums [nbP];
  // Un groupe max par projet
  for(i = 0; i < nbG ; i++){
    SparseVect[i] = 1;
    //printf("%i: %f\n", i, SparseVect[i+1]);
  }
  for(j = 0; j < nbP; j++){
    for(i = 0; i < nbG; i++)
      colnums[i] = i+1+j*nbG;
 
    add_constraintex(lp, nbG, SparseVect, colnums, LE, 1.0);
  }

  // Un projet par groupe
  for(i = 0; i < nbP ; i++){
    SparseVect[i] = 1;
    //printf("%i: %f\n", i, SparseVect[i+1]);
  }
  for(j = 0; j < nbG; j++){
    for(i = 0; i < nbP; i++)
      colnums[i] = j+1+i*nbG;
 
    add_constraintex(lp, nbP, SparseVect, colnums, EQ, 1.0);
  }

  set_add_rowmode(lp, FALSE);

  //print_lp(lp);
  //write_lp(lp, "model.lp");
  write_lp(lp, NULL);
  solve(lp);

  printf("Valeur optimale : %f\n",get_objective(lp));

  REAL var[nbP*nbG], *ptr_var;
  get_variables(lp, var);
  get_ptr_variables(lp, &ptr_var);
  for(i = 0 ; i < nbP * nbG ; i++){
    if (var[i]!=0){
      printf("Var(%i) = %f (Groupe : %i; Projet : %i) \n",
	     i , 
	     var[i],
	     i/nbG+1,
	     i%nbG+1);
    }
  }


  delete_lp(lp);

  
}

/**
 * Une fonction qui récupère les données dans un fichier bien
 * formaté. Il ne gère pas les erreurs.
 * @param namefic Nom du ficher d'entrée
 * @param pnbG    pour récupérer le nombre de groupes
 * @param pnbP    pour récupérer le nombre de projets
 * @param pPoids  pour récupérer la matrice des poids
 *
 **/

int readPB(char * namefic, int * pnbG, int * pnbP, int *** pPoids)
{
  FILE * fic = fopen(namefic, "r");
  char buf[1000];
  int i,j;

  int * coeffs;

  /* Lecture de la première ligne qui commence par # */
  fscanf(fic, "%s", buf);
  /* Nbre de projets */
  fscanf(fic, "%i", pnbP); 
  /* Lecture de la deuxième ligne qui commence par # */
  fscanf(fic, "%s", buf);
  /* Nbre de groupes */
  fscanf(fic, "%i", pnbG); 
  /* Lecture de la troisième ligne qui commence par # */
  fscanf(fic, "%s", buf);

  printf("%i\t%i\n", *pnbP, *pnbG);

  /* Allocation de la matrice de poids */
  int ** Mat = (int **) malloc(sizeof(int *) * (*pnbP));
  *pPoids = Mat;
  for( i = 0; i < *pnbP; i++)
    {
      //printf("\t%i\n",i);
      coeffs = (int *) malloc((*pnbG)* sizeof(int));
      //*pPoids[i] = coeffs;
      for(j = 0; j < *pnbG ; j++)
	{
	  char c;
	  fscanf(fic, "%i", &coeffs[j]);
	  fscanf(fic, "%c", &c);
	  /* printf("\t%i",coeffs[j]); */
	}
      Mat[i] = coeffs;
      /* printf("\n"); */
    }

  return 0;
}
