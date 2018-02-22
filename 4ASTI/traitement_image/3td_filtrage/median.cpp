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

void median(Mat& src, Mat& dst, vector<vector<uchar>>& masks, const int x, const int y, const bool colored)
{
	int dimMaskX = kMaskCols-1;
	int dimMaskY = kMaskRows-1;
	int offsetX  = dimMaskX/2;
	int offsetY  = dimMaskY/2;

	if(colored)
	{
		for(int i=(-offsetX); i<(1+offsetX); i++)
		{
			for(int j=(-offsetY); j<(1+offsetY); j++)
			{
				masks[0].at(i+offsetX + (j+offsetY)*kMaskRows) = src.at<Vec3b>(x-i, y-j)[0];
				masks[1].at(i+offsetX + (j+offsetY)*kMaskRows) = src.at<Vec3b>(x-i, y-j)[1];
				masks[2].at(i+offsetX + (j+offsetY)*kMaskRows) = src.at<Vec3b>(x-i, y-j)[2];
			}
		}
		for(int i=0; i<3; i++)
		{
			sort(masks[i].begin(), masks[i].end());	
			dst.at<Vec3b>(x, y)[i] = (uchar)masks[i].at(kMedian);
		}

	}
	else
	{
		for(int i=(-offsetX); i<(1+offsetX); i++)
		{
			for(int j=(-offsetY); j<(1+offsetY); j++)
			{
				masks[3].at(i+offsetX + (j+offsetY)*kMaskRows) = src.at<uchar>(x-i, y-j);
			}
		}
		sort(masks[3].begin(), masks[3].end());	
		dst.at<uchar>(x, y) = (uchar)masks[3].at(kMedian);
	}
}

void filtre(Mat& src, vector<vector<uchar>>& masks, const bool colored)
{
	int dimX = src.size().width-1;
	int dimY = src.size().height-1;

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
			median(src, result, masks, x, y, colored);
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
	if(argc < 2)
	{
		cout << "Usage: ./app colored" << endl
		<< "colored: int 0 == graysacale, >0 == colored" << endl;
		return 0;
	}
	const int kColored = strtol(argv[1], NULL, 10);

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

    // Init des masques de filtre
	vector<vector<uchar>> masks(4);
	vector<uchar> maskB(kMaskRows*kMaskCols, 0);
	vector<uchar> maskV(kMaskRows*kMaskCols, 0);
	vector<uchar> maskR(kMaskRows*kMaskCols, 0);
	vector<uchar> maskG(kMaskRows*kMaskCols, 0);

	masks[0] = maskB;
	masks[1] = maskV;
	masks[2] = maskR;
	masks[3] = maskG;

	// Traitement
    filtre(image, masks, kColored);

    namedWindow("Display Image", WINDOW_AUTOSIZE );
    imshow("Display Image", image);

    waitKey(0);

    return 0;
}