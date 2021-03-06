#include <limits.h>
#include "dct.h"
#include "image.h"
#include "mtrand.h"
#include "math.h"

#include <vector>

#define N 		32 
#define NDCT	12
//#define DELTA 2.0
#define D       256.0
#define PSNR    40.0

// Les coefficients DCT qui seront marqués 
static unsigned int C[NDCT] = {1, 2, 3, 4, 8, 9, 10, 11, 16, 17, 18, 24};


int main(int argc, char** argv)
{
	if(argc!=4)
	{
		cout << argv[0] << " src.pgm dst.pgm 12345 \n"; 
		cout << "\t src.pgm : image hôte\n"; 
		cout << "\t dst.pgm : image marquée\n"; 
		cout << "\t 12345   : message (32 bits)\n\n"; 
		
		return -1; 
	}
	
	long message = strtol(argv[3], NULL, 10); 
	
	// Génération du message 
	vector<int> M(N); 
	for(int i=0; i<N; i++) M[i] = (message>>i)%2;
	
	mtsrand(78425UL);
	
	image<octet> 		src, dst; 
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


	/** Calcul du delta optimal
	 * On veut calculer la formule suivante :
	 * DELTA = 10 ^ ( (10*log10(d*d*64) - PSNR ) / 20 )
	*/
	float num         = 10.0*log10(D*D*64) - PSNR;
	float x           = num / 20.0;
	const float DELTA = pow(10, x);


	// Tatouage
	/* 
	 * On veut calculer la formule suivante
	 * yi =  DELTA * round( (Xi + di + mi*DELTA/2) / DELTA ) - di - mi*DELTA/2
	*/

	long arrondi;
	long res;        // on y stockera le resultat
	unsigned long d; // dithering
	for(int i=0; i<X.size(); i++)
	{
		// On doit borner la valeur du dithering a cause de l'implementation en C++
		d       = float(mtrand()%INT_MAX)*DELTA/INT_MAX;
		arrondi = round((*X[i] + d + M[i%N]*DELTA/2) / DELTA);

		// On modifie le resultat final selon la formule
		*X[i]   = DELTA * arrondi - d - (M[i%N]*DELTA) / 2;
	}


	// Transformée inverse 
	dct::synthese(src_dct, dst); 

	// Calcul du PSNR
	double eqm(0); for(int i=0; i<dst.size(); i++) eqm+=(dst[i] - src[i])*(dst[i] - src[i]); 
	cout << "EQM = " << eqm/dst.size() << " | PSNR = " << log(65025.0/eqm*dst.size())/0.23026 << " dB\n"; 
	cout << "DELTA = " << DELTA << endl;

	// Sauvegarde de l'image marquée 
	dst.write(argv[2]); 
	
	return 1; 
}