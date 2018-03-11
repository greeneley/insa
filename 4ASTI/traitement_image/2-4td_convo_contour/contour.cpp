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

#include "contour.h"

/* ===========================
		   NAMESPACES
   =========================== */

using namespace cv;
using namespace std;

/* ===========================
			FONCTIONS
   =========================== */
void init_mask_float(Mat& mask, const float values[])
{
	int nb_rows = mask.size().height;

	for(int y=0; y<mask.size().width; y++)
		for(int x=0; x<mask.size().height; x++)
			mask.at<float>(x,y) = values[x + y*nb_rows];

	cout << mask << endl;
}


void calc_conv_grey_float(Mat& src, Mat& dst, Mat& mask, int x, int y)
{   
	int dimMaskX = mask.size().height;
	int dimMaskY = mask.size().width;
	int offsetX  = dimMaskX/2;
	int offsetY  = dimMaskY/2;

	float res(0.0);
	for(int j=(-offsetY); j<(1+offsetY); j++)
		for(int i=(-offsetX); i<(1+offsetX); i++)
			res += (float)src.at<uchar>(x-i, y-j) * mask.at<float>(i+offsetX,j+offsetY);

	dst.at<float>(x,y) = res/kMoyenneur;
}

void normalize_float(Mat& src)
{
	float max(0.00000000001);

	// Recherche du max
	for(int y=0; y<src.size().width; y++)
		for(int x=0; x<src.size().height; x++)
			if(src.at<float>(x,y)>max)
				max = src.at<float>(x,y);


	// Normalisation [0, 1]
	for(int y=0; y<src.size().width; y++)
		for(int x=0; x<src.size().height; x++)
			src.at<float>(x,y) /= max;
}


Mat convolution_float(Mat& src, Mat& mask)
{
	// La convolution reduit la taille de 1 en x et y
	int dimX = src.size().height-1;  
	int dimY = src.size().width-1;

	Mat result = Mat(dimX, dimY, CV_32FC1);
	for(int y=1; y<dimY; y++)
		for(int x=1; x<dimX; x++)
			calc_conv_grey_float(src, result, mask, x, y);

	return result;
}

Mat square_float(Mat& src)
{
	Mat result = Mat(src.size().height, src.size().width, CV_32FC1);

	for(int y=1; y<src.size().width; y++)
		for(int x=1; x<src.size().height; x++)
			result.at<uchar>(x,y) = src.at<uchar>(x, y)*src.at<uchar>(x, y);

	return result;
}

Mat module_gradient_float(Mat& src, Mat& h1, Mat& h2)
{
	Mat result = square_float(h1);
	Mat sq_h2 = square_float(h2);

	result += sq_h2;
	for(int y=1; y<src.size().width; x++)
		for(int x=1; x<src.size().height; y++)
			result.at<float>(x,y) = sqrt(result.at<float>(x, y) + result.at<float>(x, y));

	return result;
}

void affiche_image(Mat& src, String name)
{
	namedWindow(name, WINDOW_AUTOSIZE );
	imshow(name, src);
}

/* ===========================
			  MAIN 
   =========================== */

int main(int argc, char const *argv[])
{
	if(argc < 2)
	{
		cout << "Usage: ./app fp_image" << endl
		//cout << "Usage: ./app fp_image colored" << endl
		<< "fp_image: Filepath to source image" << endl;
		//<< "colored:  0 for grayscale, >0 for colored" << endl;
		return 0;
	}
	//const int kColored  = strtol(argv[2], NULL, 10);

	Mat image = imread(argv[1], CV_LOAD_IMAGE_GRAYSCALE);
	if(!image.data)
	{
		printf("No image data \n");
		return -1;
	}

	//
	Mat mask1 = Mat(1, 2, CV_32FC1);
	init_mask_float(mask1, kMask3);
	Mat h1 = convolution_float(image, mask1);
	normalize_float(h1);
	affiche_image(h1, "h1");
	//
	Mat mask2 = Mat(2, 1, CV_32FC1);
	init_mask_float(mask2, kMask4);
	Mat h2 = convolution_float(image, mask2);
	normalize_float(h2);
	affiche_image(h2, "h2");
	//
	Mat result = h1 + h2;
	normalize_float(result);
	affiche_image(result, "h1+h2");
	//
	affiche_image(image, "image");


	waitKey(0);

	return 0;
}