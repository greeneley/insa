/* ===========================
			INCLUDES
   =========================== */

#include "growing_regions.h"

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
	if(argc < 5)
    {
        cout << "Usage: ./app fp_image x y threshold" << endl
             << "fp_image:  Filepath to source image" << endl
             << "x:         X position of the starting pixel" << endl
             << "y:         Y position of the starting pixel" << endl
             << "threshold: Value of minimal threshold" << endl;
        return 0;
    }

    Mat image = imread(argv[1], CV_LOAD_IMAGE_GRAYSCALE);
    if(!image.data)
    {
        printf("No image data \n");
        return -1;
    }

    // === Init des variables
    int   x     = stoi(argv[2]);
    int   y     = stoi(argv[3]);

    if(x > image.size().height || y > image.size().width)
    {
    	cout << "Position du pixel invalide" << endl
    	     << "data (x,y)   == (" << x << "," << y << ")" << endl
    	     << "taille image == " << image.size().height << "x" << image.size().width << endl;
             return -1;
    }

    Mat   dst       = Mat(image.size().height, image.size().width, CV_8UC1, Scalar(0));
    float seed      = (float)image.at<uchar>(x,y);
    float threshold = stof(argv[4]);
    int   n         = 1;

    // === Recursivite
    dst.at<uchar>(x,y) = 255; // situation initiale
    growing(image, dst, x, y, seed, n, threshold);

    // === Print
    afficheImage(image, "Source");
    afficheImage(dst, "Growing region");
    waitKey(0);

	return 0;
}

/* ===========================
			FONCTIONS
   =========================== */

// =========================== PRINT
void afficheImage(const Mat& src, String name)
{
    namedWindow(name, WINDOW_AUTOSIZE );
    imshow(name, src);
}


// =========================== GROWING-REGION
void growing(const Mat& src, Mat& dst, int x, int y, float& sum, int& n, float& threshold)
{
	float mean  = sum / (float)n;
    int   sizeX = src.size().height;
    int   sizeY = src.size().width;
    float value = 0;

    dst.at<uchar>(x, y) = 255; // On colorie celui sur lequel on est
    for(int voisin=kGauche; voisin<kTousLesVoisins; voisin++)
    {
        if(isValid(dst, x+kMvtX[voisin], y+kMvtY[voisin], sizeX, sizeY))
        {
            if(inArea(src, x+kMvtX[voisin], y+kMvtY[voisin], mean, threshold))
            {
                value = (float)src.at<uchar>(x+kMvtX[voisin], y+kMvtY[voisin]);
                update(value, sum, n, threshold);

                growing(src, dst, x+kMvtX[voisin], y+kMvtY[voisin], sum, n, threshold);
            }
            else dst.at<uchar>(x+kMvtX[voisin], y+kMvtY[voisin]) = 128; // rejected
        }
    }
}

void update(float value, float& sum, int& n, float& threshold)
{
    sum += value;
    n++;
    // threshold dynamique ...
}

// =========================== BOOLEAN

bool inImage(int x, int y, int sizeX, int sizeY)
{
    if(x < 0     || y < 0)     return false;
    if(x > sizeX || y > sizeY) return false;
    return true;
}

bool inArea(const Mat& src, int x, int y, float mean, float threshold)
{
    float res = abs((float)src.at<uchar>(x, y) - mean);

    return (res < threshold);
}

bool isMarked(Mat& dst, int x, int y)
{
    int value = (int)dst.at<uchar>(x, y);

    if(value == 255) return true;
    if(value == 128) return true;

    return false;
}

bool isValid(Mat& dst, int x, int y, int sizeX, int sizeY)
{
    if(inImage(x, y, sizeX, sizeY)) 
        return !isMarked(dst, x, y);
    return false;
}
