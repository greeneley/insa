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

#include "median.h"

/* ===========================
           NAMESPACES
   =========================== */

using namespace cv;
using namespace std;

/* ===========================
            CONSTANTES
   =========================== */
const int kMedian = round(kMaskRows * kMaskCols / 2);

/* ===========================
            FONCTIONS
   =========================== */

void median(Mat* src, Mat* dst, vector<uchar>* mask, int x, int y, bool colored)
{
	int dimMaskX = kMaskCols-1;
	int dimMaskY = kMaskRows-1;
	int offsetX  = dimMaskX/2;
	int offsetY  = dimMaskY/2;

	if(colored)
	{
		/* TODO
		for(int i=(-offsetX); i<(1+offsetX); i++)
		{
			for(int j=(-offsetY); j<(1+offsetY); j++)
			{
				blue  += (float)src->at<Vec3f>(x-i, y-j)[0] * mask->at<float>(i+offsetX,j+offsetY);
				green += (float)src->at<Vec3f>(x-i, y-j)[1] * mask->at<float>(i+offsetX,j+offsetY);
				red   += (float)src->at<Vec3f>(x-i, y-j)[2] * mask->at<float>(i+offsetX,j+offsetY);
			}
		}
		dst->at<Vec3f>(x, y)[0] = blue/kMoyenneur;
		dst->at<Vec3f>(x, y)[1] = green/kMoyenneur;
		dst->at<Vec3f>(x, y)[2] = red/kMoyenneur;
		*/
	}
	else
	{
		for(int i=(-offsetX); i<(1+offsetX); i++)
		{
			for(int j=(-offsetY); j<(1+offsetY); j++)
			{
				mask->at(i+offsetX + (j+offsetY)*kMaskRows) = src->at<uchar>(x-i, y-j);
			}
		}
		sort(mask->begin(), mask->end());	
		dst->at<uchar>(x, y) = (uchar)mask->at(kMedian);
	}
}

void filtre(Mat* src, vector<uchar>* mask, bool colored)
{
	int dimX = src->size().width-1;
	int dimY = src->size().height-1;

	Mat result;
	if(colored)
	{
		result = Mat(dimX, dimY, CV_8UC3);
	}
	else
	{
		result = Mat(dimX, dimY, CV_8UC1);
	}
	for(int x=1; x<dimX; x++)
	{
		for(int y=1; y<dimY; y++)
		{
			median(src, &result, mask, x, y, colored);
		}
	}

	namedWindow("Filtre median", WINDOW_AUTOSIZE );
    imshow("Filtre median", result);
}

/* ===========================
              MAIN 
   =========================== */

int main(int argc, char const *argv[])
{
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

    vector<uchar> mask(kMaskRows*kMaskCols, 0);
    filtre(&image, &mask, kColored);

    namedWindow("Display Image", WINDOW_AUTOSIZE );
    imshow("Display Image", image);

    waitKey(0);

    return 0;
}