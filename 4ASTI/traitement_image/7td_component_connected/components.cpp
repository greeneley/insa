/* ===========================
			INCLUDES
   =========================== */

#include <set>

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
	if(argc < 4)
    {
        cout << "Usage: ./app fp_image color_bg mode" << endl
             << "fp_image: Filepath to source image" << endl
             << "color_bg: 0|1 (black|white) -- Color of the background in image" << endl
             << "mode    : 0|1 (iterative|recursive)" << endl;
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

    if(strtol(argv[2], NULL, 10) == 0) binarizeFondBlanc(image);
    else                               binarizeFondNoir(image);
    image.copyTo(dst);

    // === Algo
    if(strtol(argv[3], NULL, 10) == 0) labelizeIter(dst);
    else                               labelizeRecur(dst);

    // === Print
    afficheImage(image, "Source");
    afficheImage(dst, "Result");
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
void labelizeIter(Mat& src)
{
	map<int, int> m;
	firstPass(src, m);
	secondPass(src, m);
}

void firstPass(Mat& src, map<int, int>& valeurLabel)
{
	// NE, N, NW et W == position cardinaux des voisins
	int labelNE, labelN, labelNW, labelW;
	int minLabel;

	for(int x=0; x<src.size().height; x++)
	{
		for(int y=0; y<src.size().width; y++)
		{
			// On ignore les background pixels
			if(src.at<uchar>(x,y) == 0) continue;

			// On recupere les labels des voisins d'orientation 4 a 1
			labelW  = inImage(src, x,   y-1) ? src.at<uchar>(x,  y-1) : 0;
			labelNW = inImage(src, x-1, y-1) ? src.at<uchar>(x-1,y-1) : 0;
			labelN  = inImage(src, x-1, y)   ? src.at<uchar>(x-1,y)   : 0;
			labelNE = inImage(src, x-1, y+1) ? src.at<uchar>(x-1,y+1) : 0;

			// Recuperation du label minimum
			minLabel = getMinLabel(labelW, labelNW, labelN, labelNE);

			if(minLabel > 0) // Label existant
			{
				src.at<uchar>(x,y) = minLabel;

				// On modifie les labels root des voisins en cas de conflit de label
				changeParents(src, x, y, minLabel, valeurLabel);
			}
			else // Nouveau label
			{
				src.at<uchar>(x,y) = LABEL;      
			    valeurLabel[LABEL] = VALEUR_GRIS; // Gris associe au label

			    LABEL++;
			    VALEUR_GRIS += INCR_VALEUR_GRIS;
			}
		}
	}
}

void secondPass(Mat& src, map<int, int>& valeurLabel)
{
	int currentLabel;
	set<int> setLabel;

	for(int x=0; x<src.size().height; x++)
	{
		for(int y=0; y<src.size().width; y++)
		{
			if(src.at<uchar>(x,y) == 0) continue;

			currentLabel = src.at<uchar>(x,y);
			setLabel.insert(valeurLabel[currentLabel]);
			src.at<uchar>(x,y) = valeurLabel[currentLabel];
		}
	}

	cout << "Nombre de formes : " << setLabel.size() << endl;
}

int  getMinLabel(int labelW, int labelNW, int labelN, int labelNE)
{
	int min = 256;

	if( (labelW  != 0) && (labelW  < min) )  min = labelW;
	if( (labelNW != 0) && (labelNW < min) )  min = labelNW;
	if( (labelN  != 0) && (labelN  < min) )  min = labelN;
	if( (labelNE != 0) && (labelNE < min) )  min = labelNE;

	if(min == 256) return 0;
	return min;
}

void changeParents(cv::Mat& src, int x, int y, int min, std::map<int, int>& valeurLabel)
{
	int labelTemp;
	int offsetX, offsetY;

	for(int i=0; i<kNbVoisinsIter; i++)
	{
		// Pour rentrer tous les tests en une seule boucle for
		offsetX = kVoisinsXIter[i];
		offsetY = kVoisinsYIter[i];

		if(inImage(src, x+offsetX, y+offsetY))
		{
			// Si non background et label different du min
			if( (src.at<uchar>(x+offsetX, y+offsetY) > 0) && (src.at<uchar>(x+offsetX, y+offsetY) != min) )
			{
				labelTemp = src.at<uchar>(x+offsetX, y+offsetY);

				// Update du label root
				valeurLabel[labelTemp] = valeurLabel[min];

				// Update du label du pixel voisin pour eviter de futurs tests
				src.at<uchar>(x+offsetX, y+offsetY) = min;
			}
		}
	}
}


// =========================== CONNECTED-COMPONENT
void labelizeRecur(cv::Mat& src)
{
	for(int y=0; y<src.size().width; y++)
	{
		for(int x=0; x<src.size().height; x++)
		{
			if(src.at<uchar>(x,y) == 255)
			{
				connectedComponentRecur(src, x, y, VALEUR_GRIS);
				putText(src, "#"+to_string(FORMES),Point(y,x), FONT_HERSHEY_PLAIN, 1, 253);
				VALEUR_GRIS += INCR_VALEUR_GRIS;
				FORMES++;
			}
		}
	}

	cout << "Nombre de formes : " << FORMES - 1 << endl;
}

void connectedComponentRecur(Mat& src, int x, int y, int label)
{
	src.at<uchar>(x,y) = label;

	for(int i=0; i<kNbVoisinsRecur; i++)
	{
		if(src.at<uchar>(x + kVoisinsXRecur[i], y + kVoisinsYRecur[i]) == 255)
		{
			connectedComponentRecur(src, x + kVoisinsXRecur[i], y + kVoisinsYRecur[i], label);
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

void binarizeFondNoir(Mat& src)
{
	for(int y=0; y<src.size().width; y++)
		for(int x=0; x<src.size().height; x++)
			if((int)src.at<uchar>(x,y) < 128)
				src.at<uchar>(x, y) = 0;
			else src.at<uchar>(x, y) = 255;
}

bool inImage(Mat& src, int x, int y)
{
	if( (x > 0) && (x < src.size().height) )
		if( (y > 0) && (y < src.size().width) )
			return true;

	return false;
}