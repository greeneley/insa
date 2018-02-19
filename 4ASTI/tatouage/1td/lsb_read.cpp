#include "image.h"
#include "mtrand.h"

int main(int argc, char** argv)
{
	if(argc!=3)
	{
		cout << argv[0] << " src.pnm dst.pgm\n"; 
		cout << "\t src.pnm  : image marquée\n"; 
		cout << "\t dst.pgm  : image lue\n\n"; 
		
		return -1; 
	}
	
	mtsrand(78425UL);
	
	image<rgb> 		src; 
	image<octet>	dst;
	
	src.read(argv[1]); 

	dst.resize(src.width(), src.height()); 

	// todo
	// Iteration sur l'image src
	for(int y=0; y<src.height(); y++)
	{
		for(int x=0; x<src.width(); x++)
		{
			// Valeur du LSB du canal vert
			const int lsb = src(x,y).g%2;

			if(mtrand()%2)
			{
				// Si LSB est up, alors le pixel etait > 128
				if(lsb)
				{
					dst(x,y) = 255;
				}
				else
				{
					dst(x,y) = 0;
				}
			}
			else
			{
				// Si le LSB est up, alors le pixel etait < 128
				if(lsb)
				{
					dst(x,y) = 0;
				}
				else
				{
					dst(x,y) = 255;
				}
			}
		}
	}
	
	
	
	dst.write(argv[2]); 
	
	return 1; 
}