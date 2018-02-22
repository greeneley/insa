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

#include "convolution.h"

/* ===========================
           NAMESPACES
   =========================== */

using namespace cv;
using namespace std;

/* ===========================
            FONCTIONS
   =========================== */
void init_mask(Mat& mask)
{
	for(int x=0; x<mask.size().width; x++)
	{
		for(int y=0; y<mask.size().height; y++)
		{
			mask.at<float>(x,y) = kMask[x+y*kMaskRows];
		}
	}
}


void calc_conv(Mat& src, Mat& dst, Mat& mask, int x, int y, bool colored)
{	
	int dimMaskX = mask.size().width;
	int dimMaskY = mask.size().height;
	int offsetX  = dimMaskX/2;
	int offsetY  = dimMaskY/2;

	if(colored)
	{
		float blue(0.0), green(0.0), red(0.0);
		for(int i=(-offsetX); i<(1+offsetX); i++)
		{
			for(int j=(-offsetY); j<(1+offsetY); j++)
			{
				blue  += (float)src.at<Vec3b>(x-i, y-j)[0] * mask.at<float>(i+offsetX,j+offsetY);
				green += (float)src.at<Vec3b>(x-i, y-j)[1] * mask.at<float>(i+offsetX,j+offsetY);
				red   += (float)src.at<Vec3b>(x-i, y-j)[2] * mask.at<float>(i+offsetX,j+offsetY);
			}
		}
		dst.at<Vec3f>(x, y)[0] = blue/kMoyenneur;
		dst.at<Vec3f>(x, y)[1] = green/kMoyenneur;
		dst.at<Vec3f>(x, y)[2] = red/kMoyenneur;
	}
	else
	{
		float res(0.0);
		for(int i=(-offsetX); i<(1+offsetX); i++)
		{
			for(int j=(-offsetY); j<(1+offsetY); j++)
			{
				res += (float)src.at<uchar>(x-i, y-j) * mask.at<float>(i+offsetX,j+offsetY);
			}
		}
		dst.at<float>(x,y) = res/kMoyenneur;
	}
}

void normalize_float(Mat& src, bool colored)
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

	    // Normalisation [0, 1]
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

	    // Normalisation [0, 1]
	    for(int x=0; x<src.size().width; x++)
	    {
	    	for(int y=0; y<src.size().height; y++)
	    	{
			    src.at<float>(x,y) /= max;
	    	}
	    }
	}
}


void convolution(Mat& src, Mat& mask, bool colored)
{
	
	int dimX = src.size().width-1;
	int dimY = src.size().height-1;

	Mat result;
	if(colored)
	{
		result = Mat(dimX, dimY, CV_32FC3);
	}
	else
	{
		result = Mat(dimX, dimY, CV_32FC1);
	}
	for(int x=1; x<dimX; x++)
	{
		for(int y=1; y<dimY; y++)
		{
			calc_conv(src, result, mask, x, y, colored);
		}
	}

	normalize_float(result, colored);
	namedWindow("Convolution", WINDOW_AUTOSIZE );
    imshow("Convolution", result);
}

/* ===========================
              MAIN 
   =========================== */

int main(int argc, char const *argv[])
{
	if(argc < 3)
	{
		cout << "Usage: ./app fp_image colored" << endl
		<< "fp_image: Filepath to source image" << endl
		<< "colored:  0 for grayscale, >0 for colored" << endl;
		return 0;
	}
	const int kColored  = strtol(argv[2], NULL, 10);

	Mat image, mask;
	if(kColored)
	{
		image = imread(argv[1], CV_LOAD_IMAGE_COLOR);
		mask  = Mat(kMaskRows, kMaskCols, CV_32FC3);

	}
	else
	{
		image = imread(argv[1], CV_LOAD_IMAGE_GRAYSCALE);
		mask  = Mat(kMaskRows, kMaskCols, CV_32FC1);
	}

    if(!image.data)
    {
        printf("No image data \n");
        return -1;
    }

    init_mask(mask);
    convolution(image, mask, kColored);

    namedWindow("Display Image", WINDOW_AUTOSIZE );
    imshow("Display Image", image);

    waitKey(0);

    return 0;
}