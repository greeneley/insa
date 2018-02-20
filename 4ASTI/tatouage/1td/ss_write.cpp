#include "dct.h"
#include "image.h"
#include "mtrand.h"

#include <math.h>
#include <vector>

#define N 		32 
#define NDCT	12
#define D       255.0
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
	vector<int> M(N); for(int i=0; i<N; i++) M[i] = (message>>i)%2;
	
	mtsrand(78425UL);
	
	image<octet> 		src, dst; 
	image<double>	src_dct, src_watson; 
	
	src.read(argv[1]); 
	
	// Transformée DCT de l'image hôte
	dct::analyse(src, src_dct); 
	dct::watson(src_dct, src_watson)
	
	// Génération du vecteur hôte 
	vector<double*> X(NDCT*src_dct.size()/64); 
	int cur(0); 

	// Generation du vecteur de Watson
	vector<double> S(NDCT*src_dct.size()/64);
	double moy_watson(0.0);

	for(int by=0; by<src.height()/8; by++)
	{
		for(int bx=0; bx<src.width()/8; bx++)
		{
			for(int i=0; i<NDCT; i++)
			{
				const unsigned int x  = 8*bx + C[i]%8;  
        		const unsigned int y  = 8*by + C[i]/8;
        
				S[cur]=&(src_watson(x,y));
				moy_watson += *S[cur];

				X[cur++]=&(src_dct(x, y)); 
			}
		}
	}

	moy_watson /= S.size();

	// Tatouage 

	/* Calcul du alpha optimal
	 * On veut calculer la formule suivante
	 * ALPHA = 10 ^ ( (10*log10(d*d/m * 64) - PSNR) / 20 )
	*/
	double num         = 10*log10(D*D*64/N/12) - PSNR;
	const double ALPHA = pow(10, num/20.0);

	// Normalisation du message : on remplace les 0 par des -1
	for(int i=0; i<M.size(); i++)
	{
		if(!M[i]) // <0
		{
			M[i] = -1;
		}
	}

	// Modulation des porteuses
	for(int i=0; i<X.size(); i++)
	{
		double wi(0.0);
		for(int j=0; j<N; j++)
		{
			if(mtrand()%2) // Generation du G(i,j)
			{
				wi += M[j]; // wi += 1 * M[j]
			}
			else
			{
				wi -= M[j]; // wi += -1 * M[j]
			}
		}
		*X[i] += wi * ALPHA * *S[i] / moy_watson;
	}

	// Transformée inverse 
	dct::synthese(src_dct, dst); 

	// Calcul du PSNR
	double eqm(0); for(int i=0; i<dst.size(); i++) eqm+=(dst[i] - src[i])*(dst[i] - src[i]); 
	cout << "EQM = " << eqm/dst.size() << " | PSNR = " << log(65025.0/eqm*dst.size())/0.23026 << " dB\n"; 
	cout << "ALPHA = " << ALPHA << endl;

	// Sauvegarde de l'image marquée 
	dst.write(argv[2]); 
	
	return 1; 
}