/* ===========================
			INCLUDES
   =========================== */

#include "block_matching.h"

/* ===========================
		   NAMESPACES
   =========================== */

using namespace cv;
using namespace std;

/* ===========================
			  MAIN 
   =========================== */
int main(int argc, char const *argv[])
{
	if(argc < 6)
    {
        cout << "Usage: ./block_matching fp_img_1 fp_img_2 x y size_block" << endl
             << "fp_img_1   : Filepath to initial image" << endl
             << "fp_img_2   : Filepath to post-motion image" << endl
             << "x          : X-position of the search area in opencv Point format" << endl
             << "y          : Y-position of the search area in opencv Point format" << endl
             << "size_block : Size (in px) of the side of the searching area" << endl;
        return -1;
    }

    // ===== Verification des parametres
    Mat image = imread(argv[1], CV_LOAD_IMAGE_GRAYSCALE);
    if(!image.data)
    {
        cout << "fp_img_1: No image data" << endl;
        return -1;
    }

    Mat motion = imread(argv[2], CV_LOAD_IMAGE_GRAYSCALE);
    if(!motion.data)
    {
        cout << "fp_img_2: No image data" << endl;
        return -1;
    }

    int x = stoi(argv[3]);
    int y = stoi(argv[4]);
    if(!areaIsValid(image, x, y))
    {
    	cout << "ERROR : starting point (x,y) out of image " << endl;
    	return -1;
    }

    G_SIZE_SEARCH   = stoi(argv[5]);
    if(G_SIZE_SEARCH==0)
    {
    	cout << "ERROR : searching area is 0" << endl;
    	return -1;
    }
	G_OFFSET_SEARCH = G_SIZE_SEARCH / 2;

    ThreeStepSearch(image, motion, Point(x, y));

    afficheImage(image, "Source");
    afficheImage(motion, "Motion");
    waitKey(0);

	return 0;
}

/* ===========================
			FONCTIONS
   =========================== */

// =========================== PRINT
void afficheImage(const Mat& src, string name)
{
    namedWindow(name, WINDOW_AUTOSIZE );
    imshow(name, src);
}

// =========================== BLOCK-MATCHING
void ThreeStepSearch(cv::Mat& prevImg, cv::Mat& currImg, Point px)
{
	float min, tempMin; // Pour trouver le min des SAD
	int   index;        // Pour savoir ou sera le prochain centre
	int   dx, dy;       // Le centre du macro-block a tester

	// Le centre sera dynamique
	Point currentPx = Point(px.x, px.y);

	for(int s=4; s!=0; s/=2)
	{
		// Init avec la position (0,0)
		min   = SAD(prevImg, currImg, px, currentPx); 
		index = 0; // Index de (0,0) dans arrays globaux G_VOISIN_

		for(int i=1; i<9; i++)
		{
			// pour determiner la position du voisin
			// G_VOISIN_[i]*s correspond a l'offset en x ou y
			dx = G_VOISIN_X[i]*s;
			dy = G_VOISIN_Y[i]*s;

			if(areaIsValid(prevImg, currentPx.x + dx, currentPx.y + dy))
			{
				// Calcul de la Sum of Absolute Differences
				tempMin = SAD(prevImg, currImg, px, currentPx + Point(dx, dy));
				if(tempMin < min)
				{
					min   = tempMin;
					index = i; // Voir le header pour plus de precisions
				}
			}	
		}

		// On determine le prochain centre avec le min(SAD)
		currentPx += Point(G_VOISIN_X[index]*s, G_VOISIN_Y[index]*s);
	}

	// On trace les zones de recherche
	traceCarre(prevImg, px, G_SIZE_SEARCH + 14);
	traceCarre(prevImg, px, G_SIZE_SEARCH);
	traceCarre(prevImg, currentPx, G_SIZE_SEARCH);

	// Vecteur de deplacement trace sur prev et current
	line(prevImg, currentPx, px, Scalar(255), 3);
	line(prevImg, currentPx, px, Scalar(0), 2);

	line(currImg, currentPx, px, Scalar(255), 3);
	line(currImg, currentPx, px, Scalar(0), 2);
}

float SAD(const Mat& prevImg, const Mat& currImg, Point ref, Point test)
{
	float sad   = 0.0;

	// Dans le cas ou la zone de recherche n'est pas un carre centre autour du pixel de recherche
	int   limit = G_SIZE_SEARCH%2 ? G_OFFSET_SEARCH-1 : G_OFFSET_SEARCH;

	for(int i=(-limit); i<G_OFFSET_SEARCH; i++)
	{
		for(int j=(-limit); j<G_OFFSET_SEARCH; j++)
		{
			sad += abs( (float)prevImg.at<uchar>(ref.y+i, ref.x+j) - (float)currImg.at<uchar>(test.y+i, test.x+j) );
		}
	}

	return sad;
}

// =========================== UTILITIES
bool pixelIsValid(const Mat& src, int x, int y)
{
	// x et y sont en format Point
	if( (x > 0) && (x < src.size().width) )
		if( (y > 0) && (y < src.size().height) )
			return true;

	return false;
}

bool areaIsValid(const Mat& src, int x, int y)
{
	if(pixelIsValid(src, x-G_OFFSET_SEARCH, y-G_OFFSET_SEARCH))
		if(pixelIsValid(src, x-G_OFFSET_SEARCH, y+G_OFFSET_SEARCH))
		if(pixelIsValid(src, x+G_OFFSET_SEARCH, y-G_OFFSET_SEARCH))
		if(pixelIsValid(src, x+G_OFFSET_SEARCH, y+G_OFFSET_SEARCH))
		return true;

	return false;
}

void traceCarre(Mat& src, Point px, int size)
{
	line(src, px + Point(-size, -size), px + Point(-size, +size), Scalar(0), 2);
	line(src, px + Point(-size, +size), px + Point(+size, +size), Scalar(0), 2);
	line(src, px + Point(+size, +size), px + Point(+size, -size), Scalar(0), 2);
	line(src, px + Point(+size, -size), px + Point(-size, -size), Scalar(0), 2);
}



