#include <stdio.h>
#include <stdlib.h>
//#include <time.h>
#include "rsa.h"

/* Standard C Function: Greatest Common Divisor */
Huge gcd (Huge a, Huge b)
{
  Huge c;
  while ( a != 0 ) 
  {
    	c = a; 
    	a = b%a;  
    	b = c;
  }
  return b;
}

Cles* keygen(Huge P, Huge Q)
{
	Cles* key = malloc(sizeof(Cles));
	Huge phi;

	key->n = P*Q;
	phi = (P-1)*(Q-1);

	//srand(time(NULL));
	//e = rand()%phi + 2;
	key->e = 2;
	while( (gcd(key->e,phi) != 1) && (key->e<phi) ) 
	{
		//e = rand()%phi + 2;
		key->e++;
	}

	//d = rand()%phi + 2;
	key->d = 2;
	while( ((key->e*key->d)%phi != 1) && (key->d<phi) )
	{
		//d = rand()%phi + 2;
		key->d++;
	}

	printf("Cle publique : (%lu, %lu)\n", key->e,key->n);
	printf("Cle privee   : %lu\n", key->d);

	return key;
}

static Huge modexp(Huge a, Huge b, Huge n)
{
	Huge y;
	y = 1;

	while(b != 0)
	{
		if(b & 1)
		{
			y = (y * a) % n;
		}
		a = (a * a) % n;
		b = b >> 1;
	}

	return y;
}


Huge rsa_crypt(Cles* key, Huge M)
{
	Huge chiffre;

	/* on suppose que M est donne */
	chiffre = modexp(M, key->e, key->n);

	return chiffre;
}

Huge rsa_decrypt(Cles* key, Huge C)
{
	return modexp(C, key->d, key->n);
}

int main(int argc, char const *argv[])
{
	Huge M1 = 3333;
	Huge M2 = 1234;
	Huge C;

	Cles* key = keygen(31, 137);

	printf("===== Message 1 =====\n");
	printf("Clair   : %lu\n", M1);
	C = rsa_crypt(key, M1);
	printf("Chiffre : %lu\n",C);
	printf("Dechiffre : %lu\n",rsa_decrypt(key, C));


	printf("===== Message 2 =====\n");
	printf("Clair   : %lu\n", M2);
	C = rsa_crypt(key, M2);
	printf("Chiffre : %lu\n",C);
	printf("Dechiffre : %lu\n",rsa_decrypt(key, C));

	free(key);
	return 0;
}
