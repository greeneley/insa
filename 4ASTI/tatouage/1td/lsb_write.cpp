#include "image.h"
#include "mtrand.h"

int main(int argc, char** argv)
{
	if(argc!=4)
	{
		cout << argv[0] << " src1.pnm src2.pnm dst.pnm\n"; 
		cout << "\t src1.pnm  : image hôte\n"; 
		cout << "\t src2.pgm  : image secrête\n"; 
		cout << "\t dst.pnm   : image marquée\n\n"; 
		
		return -1; 
	}
	
	mtsrand(78425UL);
	
	image<rgb> 		src1; 
	image<octet>	src2;
	
	src1.read(argv[1]); 
	src2.read(argv[2]);
	
	// Iteration sur chacun des pixels
	for(int y=0; y<src1.height(); y++)
	{
		for(int x=0; x<src1.width(); x++)
		{
			// Mise du LSB a 0 dans le canal vert (green)
			src1(x, y).g = (src1(x, y).g >> 1) << 1; 
			
			// Regarde si le pixel est superieur a 128
			const bool b = src2(x%src2.width(), y%src2.height()) > 128; 

			// On modifi le LSB en fonction de la comparaison precedente
			if(mtrand()%2)
			{
				if(b) src1(x, y).g++; 
			}
			else 
			{
				if(!b) src1(x, y).g++; 
			}
		}
	}
	
	src1.write(argv[3]); 
	
	return 1; 
}