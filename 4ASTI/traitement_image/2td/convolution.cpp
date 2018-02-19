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
void init_mask(Mat mask)
{
	float matrix[][] = { {0.0,1.0,0.0},{1.0,2.0,1.0},{0.0,1.0,0.0}};

	
}

float calc_conv(Mat image, int x, int y, int dimMaskX, int dimMaskY, bool colored)
{
	float res = 0.0;
	if(colored)
	{
		cout << "TODO" << endl;
	}
	else
	{
		for(int i=(-dimMaskX/2); i<(1+dimMaskX/2); i++)
		{
			for(int j=(-dimMaskY/2); j<(1+dimMaskY/2); j++)
			{
				res += (float)image.at<uchar>(x-i, y-j) * (float)mask[i][j];
			}
		}
	}

	return res;
}

void normalize_float(Mat image)
{
	// Normalisation
    float max = 0.0;
    for(int x=0; x<image.size().width; x++)
    {
    	for(int y=0; y<image.size().height; y++)
    	{
    		if(image.at<float>(x,y)>max)
    		{
    			max = image.at<float>(x,y);
    		}
    	}
    }

    for(int x=0; x<image.size().width; x++)
    {
    	for(int y=0; y<image.size().height; y++)
    	{
		    image.at<float>(x,y) = ((float)image.at<float>(x,y)/(float)max) * 255;
    	}
    }
}

void convolution(Mat image, Mat mask, bool colored)
{
	Mat   result;
	float res  = 0.0;
	int   dimX = image.size().width-1;
	int   dimY = image.size().height-1;
	
	if(colored)
	{
		cout << "TODO" << endl;
	}
	else
	{
		Mat result = Mat(dimX, dimY, CV_32FC1);
		for(int x=1; x<image.size().width; x++)
		{
			for(int y=1; y<image.size().height; y++)
			{
				result.at<float>(x-1,y-1) = calc_conv(image, x, y, dimMaskX, dimMaskY, colored);
			}
		}
		normalize_float(result);
		namedWindow("Convolution", WINDOW_AUTOSIZE );
    	imshow("Convolution", result);
	}
}

/* ===========================
              MAIN 
   =========================== */

int main(int argc, char const *argv[])
{
	Mat image;
	bool colored  = false;

	if(colored)
	{
		image = imread( kFilepath, CV_LOAD_IMAGE_COLOR);
		//mask  = Mat(kMaskRows, kMaskCols, CV_U8C3);

	}
	else
	{
		image = imread(kFilepath, CV_LOAD_IMAGE_GRAYSCALE);
		//mask  = Math(kMaskRows, kMaskCols, CV_U8C1);
	}

    if( !image.data )
    {
        printf("No image data \n");
        return -1;
    }

    Mat mask = Mat(kMaskRows, kMaskCols, CV_32FC1);
    init_mask(mask);
    convolution(image, mask, colored);

    namedWindow("Display Image", WINDOW_AUTOSIZE );
    imshow("Display Image", image);

    waitKey(0);

    return 0;
}