// Credit algos @https://www.cse.unr.edu/~bebis/CS791E/Notes/ConnectedComponents.pdf

/* ===========================
			INCLUDES
   =========================== */

#include "components.h"

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
	if(argc < 2)
    {
        cout << "Usage: ./app fp_image" << endl
             << "fp_image:  Filepath to source image" << endl;
        return 0;
    }

    Mat image = imread(argv[1], CV_LOAD_IMAGE_GRAYSCALE);
    if(!image.data)
    {
        printf("No image data \n");
        return -1;
    }

    // === Init
    Mat dst = Mat(image.size().height, image.size().width, CV_8UC1);
    binarizeFondBlanc(image);
    image.copyTo(dst);
    //copyMatNorm(image, dst);

    // === Algo
    labelize(dst);
    //labelizeRecursive(dst);
    cout << "Nombre de formes : " << FORMES << endl;

    // === Print
    afficheImage(image, "Source");
    afficheImage(dst, "Label");
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

// =========================== CONNECTED-COMPONENT
void labelize(Mat& src)
{
	firstPass(src);
	cout << "formes : " << FORMES << endl;
}

void firstPass(Mat& src)
{
	int labelA, labelB;

	for(int x=0; x<src.size().height; x++)
	{
		for(int y=0; y<src.size().width; y++)
		{
			// On verifie si le pixel courant est un pixel d'interet
			if(src.at<uchar>(x,y) == 255)
			{
				// On verifie le label des pixels voisins
				labelA = (int)src.at<uchar>(x-1,   y);
				labelB = (int)src.at<uchar>(x  , y-1);

				if( (labelA == 0) && (labelB == 0) )
				{
					src.at<uchar>(x,y) = LABEL;
					LABEL += INCR_LABEL;
					FORMES++;
				}
				else if( (labelA != 0) && (labelB == 0) )
				{
					src.at<uchar>(x,y) = labelA;
				}
				else if( (labelB != 0)  && (labelA == 0) )
				{
					src.at<uchar>(x,y) = labelB;
				}
				else if(labelA < labelB)
				{
					src.at<uchar>(x,y) = labelA;
					propagateUpLabel(src, (int)src.at<uchar>(x-1, y), labelA, x-1, y);
				}
				else
				{
					src.at<uchar>(x,y) = labelB;
					propagateUpLabel(src, (int)src.at<uchar>(x, y-1), labelB, x, y-1);
				}

				/*

				// Creation d'un label si les voisins sont des bg
				if((labelA == 0) && (labelB == 0))
				{
					labelA = LABEL;
					labelB = LABEL;
					LABEL += INCR_LABEL;
					FORMES++;
				}
				else if((labelA == 0) && (labelB != 255))
				{
					src.at<uchar>(x,y) = labelB;
				}
				else if((labelB == 0) && (labelA != 255))
				{
					src.at<uchar>(x,y) = labelA;
				}
				else if(labelB < labelA)
				{
					propagateLabel(src, labelB, x, y-1);
					FORMES--;
					src.at<uchar>(x,y) = labelB;
				}
				else
					src.at<uchar>(x,y) = labelB;
				*/
				
			}
		}
	}
}

void secondPass(Mat& src)
{
	//TODO
}

void initNeighbourLabel(Mat& src, int& labelA, int& labelB)
{
	if(labelA == 0) labelA = 255;
	if(labelB == 0) labelB = 255;
}

// =========================== CONNECTED-COMPONENT
void labelizeRecursive(cv::Mat& src)
{
	for(int y=0; y<src.size().width; y++)
	{
		for(int x=0; x<src.size().height; x++)
		{
			if(src.at<uchar>(x,y) == 255)
			{
				connectedComponentRecursive(src, x, y, LABEL);
				putText(src, "#"+to_string(FORMES),Point(y,x), FONT_HERSHEY_PLAIN, 1, 253);
				LABEL += INCR_LABEL;
				FORMES++;
			}
		}
	}
}

void connectedComponentRecursive(Mat& src, int x, int y, int label)
{
	src.at<uchar>(x,y) = label;

	for(int i=0; i<kNbVoisins; i++)
	{
		if(src.at<uchar>(x + kVoisinsX[i], y + kVoisinsY[i]) == 255)
		{
			connectedComponentRecursive(src, x + kVoisinsX[i], y + kVoisinsY[i], label);
		}
	}
}

// =========================== UTILITIES
void binarizeFondBlanc(Mat& src)
{
	for(int y=0; y<src.size().width; y++)
		for(int x=0; x<src.size().height; x++)
			if((int)src.at<uchar>(x,y) < 128)
				src.at<uchar>(x, y) = 255;
			else src.at<uchar>(x, y) = 0;
}

void copyMatNorm(const Mat& src, Mat& dst)
{
	for(int y=0; y<src.size().width; y++)
		dst.at<uchar>(0, y) = src.at<uchar>(0, y);

	for(int x=1; x<src.size().height; x++)
		dst.at<uchar>(x, 0) = src.at<uchar>(x, 0);
}

void propagateUpLabel(Mat& src, int oldLabel, int newLabel, int x, int y)
{
	src.at<uchar>(x,y) = newLabel;

	if((int)src.at<uchar>(x-1,y) == oldLabel)
		propagateUpLabel(src, oldLabel, newLabel, x-1, y);
}

void propagateLeftLabel(Mat& src, int oldLabel, int newLabel, int x, int y)
{
	src.at<uchar>(x,y) = newLabel;

	if((int)src.at<uchar>(x,y-1) == oldLabel)
		propagateLeftLabel(src, oldLabel, newLabel, x, y-1);
}

