/* ===========================
             MEMO
   ===========================
Le type float prend des valeurs entre 0 et 1 !

*/

/* ===========================
            INCLUDES
   =========================== */

#include <stdio.h>
#include <highgui.h>
#include <opencv2/opencv.hpp>
#include <string>
#include <math.h>

#include "gamma.h"

/* ===========================
           NAMESPACES
   =========================== */

using namespace cv;
using namespace std;

/* ===========================
            FONCTIONS
   =========================== */

void normalize(Mat& src, bool colored)
{
	if(colored)
	{
		float bMax(0.0);
		float gMax(0.0);
		float rMax(0.0);

		// Recherche du max
		for(int x=0; x<src.size().width; x++)
	    {
	    	for(int y=0; y<src.size().height; y++)
	    	{
    			if(src.at<Vec3f>(x,y)[0] > bMax)
    			{
    				bMax = src.at<Vec3f>(x,y)[0];
    			}
    			if(src.at<Vec3f>(x,y)[1] > gMax)
    			{
    				gMax = src.at<Vec3f>(x,y)[1];
    			}
    			if(src.at<Vec3f>(x,y)[2] > rMax)
    			{
    				rMax = src.at<Vec3f>(x,y)[2];
    			}
	    	}
	    }

	    // Normalisation [-1, 1]
	    for(int x=0; x<src.size().width; x++)
	    {
	    	for(int y=0; y<src.size().height; y++)
	    	{
			    src.at<Vec3f>(x,y)[0] /= bMax;
			    src.at<Vec3f>(x,y)[1] /= gMax;
			    src.at<Vec3f>(x,y)[2] /= rMax;
	    	}
	    }
	}
	else
	{
		float max(0.0);

		// Recherche du max
		for(int x=0; x<src.size().width; x++)
	    {
	    	for(int y=0; y<src.size().height; y++)
	    	{
	    		if(src.at<float>(x,y)>max)
	    		{
	    			max = src.at<float>(x,y);
	    		}
	    	}
	    }

	    // Normalisation [-1, 1]
	    for(int x=0; x<src.size().width; x++)
	    {
	    	for(int y=0; y<src.size().height; y++)
	    	{
			    src.at<float>(x,y) /= max;
	    	}
	    }
	}
}

void rehaussement(Mat& src, const int c, const int gamma, const bool colored)
{
	Mat result;
	if(colored)
	{
		result = Mat(src.size().width, src.size().width, CV_32FC3);

		// Application de la formule pour chaque pixel de chaque canal
		for(int x=1; x<src.size().width; x++)
		{
			for(int y=1; y<src.size().height; y++)
			{
				for(int i=0; i<3; i++)
				{
					result.at<Vec3f>(x,y)[i] = (float)(c*pow(src.at<Vec3b>(x,y)[i],gamma));
				}
			}
		}
	}
	else
	{
		result = Mat(src.size().width, src.size().width, CV_32FC1);

		// Application de la formule pour chaque pixel de chaque canal
		for(int x=1; x<src.size().width; x++)
		{
			for(int y=1; y<src.size().height; y++)
			{
				result.at<float>(x,y) = (float)(c*pow(src.at<uchar>(x,y),gamma));
			}
		}
	}

	// Normalisation float dans [-1,1]
	normalize(result, colored);

	string titre("");
	titre += "(c, gamma) = (" + to_string(c) + ", " + to_string(gamma) + ")";
	namedWindow(titre, WINDOW_AUTOSIZE );
    imshow(titre, result);
}

/* ===========================
              MAIN 
   =========================== */

int main(int argc, char const *argv[])
{
	if(argc < 4)
	{
		cout << "Usage: ./app c gamma colored" << endl
		<< "c: int Gain d'amplitude de la loi gamma" << endl
		<< "gamma:  int Variance parametrique de la loi gamma" << endl
		<< "colored: int 0 == graysacale, >0 == colored" << endl;
		return 0;
	}
	const int kC        = strtol(argv[1], NULL, 10);
	const int kGamma    = strtol(argv[2], NULL, 10);
	const int kColored  = strtol(argv[3], NULL, 10);

	Mat image;
	if(kColored)
	{
		image = imread(kFilepath, CV_LOAD_IMAGE_COLOR);
	}
	else
	{
		image = imread(kFilepath, CV_LOAD_IMAGE_GRAYSCALE);
	}

    if(!image.data)
    {
        printf("No image data \n");
        return -1;
    }

	// Traitement
    rehaussement(image, kC, kGamma, kColored);

    namedWindow("Display Image", WINDOW_AUTOSIZE );
    imshow("Display Image", image);

    waitKey(0);

    return 0;
}