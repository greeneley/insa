#include <limits.h>
#include "dct.h"
#include "image.h"
#include "mtrand.h"

#include <vector>

#define N 		32 
#define NDCT	12
//#define DELTA 	2.0 
#define D       256.0
#define PSNR    40.0

// Les coefficients DCT qui seront marqués 
static unsigned int C[NDCT] = {1, 2, 3, 4, 8, 9, 10, 11, 16, 17, 18, 24};


int main(int argc, char** argv)
{
	if(argc!=2)
	{
		cout << argv[0] << " src.pgm\n"; 
		cout << "\t src.pgm : image marquée\n\n"; 
		
		return -1; 
	}
	
	// Message 
	vector<int> M(N, 0); 
	
	mtsrand(78425UL);
	
	image<octet> 		src; 
	image<double>	src_dct; 
	
	src.read(argv[1]); 
	
	// Transformée DCT de l'image hôte
	dct::analyse(src, src_dct); 
	
	// Génération du vecteur hôte 
	vector<double*> X(NDCT*src_dct.size()/64); int cur(0); 

	for(int by=0; by<src.height()/8; by++)
	{
		for(int bx=0; bx<src.width()/8; bx++)
		{
			for(int i=0; i<NDCT; i++)
			{
				const unsigned int x  = 8*bx + C[i]%8;  
        		const unsigned int y  = 8*by + C[i]/8;
        
				X[cur++]=&(src_dct(x, y)); 
			}
		}
	}

	/* Calcul du delta optimal
	* On veut calculer la formule suivante :
	* 10^( (10*log10(d*d*64) - PSNR ) / 20 )
	*/
	float num         = 10.0*log10(D*D*64) - PSNR;
	float x           = num / 20.0;
	const float DELTA = pow(10, x);


	// Tatouage 
	/*
	 * On veut calculer la formule suivante
	 * yi =  DELTA * round( (Xi + di + mi*DELTA/2) / DELTA ) - di - mi*DELTA/2
	*/
	unsigned long d;     // dithering
	int           index; // On va stocker l'approximation de l'index
	for(int i=0; i<X.size(); i++)
	{
		// On doit borner la valeur du dithering a cause de l'implementation en C++
		d = float(mtrand()%INT_MAX)*DELTA/INT_MAX;

		// On recupere la valeur par arrondi lors de la lecture
		index = round( (d + *X[i]) / DELTA*2 );

		if(index%2) // Si bit est up
		{
			M[i%N]++;
		}
		else
		{
			M[i%N]--;
		}
	}
	
	
	// Affichage du message 
	long message(0); for(int i=0; i<N; i++) if(M[i]>0) message+=1<<i; 
		
	cout << "Message : " << message << endl; 
	cout << "DELTA : " << DELTA << endl;
	
	return 1; 
}

