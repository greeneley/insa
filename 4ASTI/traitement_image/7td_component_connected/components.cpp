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
        cout << "Usage: ./components fp_image color_bg mode" << endl
             << "fp_image: Filepath to source image" << endl
             << "color_bg: 0|1 (black|white) -- Color of the background in image" << endl
             << "mode    : 0|1 (iterative|recursive)" << endl;
        return -1;
    }

    Mat image = imread(argv[1], CV_LOAD_IMAGE_GRAYSCALE);
    if(!image.data)
    {
        cout << "No image data" << endl;
        return -1;
    }

    // === Init
    Mat dst = Mat(image.size().height, image.size().width, CV_8UC1);

    if(stoi(argv[2])) binarizeFondBlanc(image);
    else              binarizeFondNoir(image);                
    image.copyTo(dst);

    // === Algo
    if(stoi(argv[3])) labelizeRecur(dst);
    else              labelizeIter(dst);

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

// =========================== CONNECTED-COMPONENT ITERATIF

// Algorithme two-steps qui fonctionne via la reconnaissance en 8-connectivity
void labelizeIter(Mat& src)
{
	map<int, int> m;    // Permet d'associer une valeur de gris a un label
	firstPass(src, m);  // On construit la map
	secondPass(src, m); // On cree l'image resultante et on compte les formes
}

/**
 *	Parcourt tous les pixels a partir du pixel dans le coin superieur gauche,
 *  jusqu'au pixel dans le coin inferieur droit.
 *
 *  Applique le masque de reconnaissance en forme de "L" vu en cours.
 *
 *  Pour chaque pixel, associe un label parent. Creer un nouveau label parent 
 *  si aucun label parent sur les pixels voisins. Affecte le label parent le
 *  plus petit si presence sur les voisins. Met a jour les labels parents des
 *  voisins si plusieurs labels parents presents.
 *
 */
void firstPass(Mat& src, map<int, int>& valeurLabel)
{
	// NE, N, NW et W == position cardinaux des voisins
	// On obtient un masque en "L"
	int labelNE, labelN, labelNW, labelW;
	int minLabel;

	for(int row=0; row<src.size().height; row++)
	{
		for(int col=0; col<src.size().width; col++)
		{
			// On ignore les background pixels
			if(src.at<uchar>(row,col) == 0) continue;

			// On recupere les labels des voisins d'orientation 4 a 1 en 8-connectivity
			// 0 signifie qu'il est hors image et est alors considere comme du background
			labelW  = inImage(src, row,   col-1) ? src.at<uchar>(row,  col-1) : 0;
			labelNW = inImage(src, row-1, col-1) ? src.at<uchar>(row-1,col-1) : 0;
			labelN  = inImage(src, row-1, col)   ? src.at<uchar>(row-1,col)   : 0;
			labelNE = inImage(src, row-1, col+1) ? src.at<uchar>(row-1,col+1) : 0;

			// Recuperation du label minimum
			minLabel = getMinLabel(labelW, labelNW, labelN, labelNE);

			if(minLabel > 0) // Label existant
			{
				src.at<uchar>(row,col) = minLabel;

				// Si plusieurs labels existent, on modifie la map pour indiquer une fusion
				changeParents(src, row, col, minLabel, valeurLabel);
			}
			else // Nouveau label
			{
				src.at<uchar>(row,col) = g_label;      
			    valeurLabel[g_label]   = g_valeurGris; // Gris associe au label

			    g_label++;
			    g_valeurGris += G_INCR_VALEUR_GRIS;
			}
		}
	}
}

/**
 *  Affecte la valeur de gris final associe a chaque label parent.
 *  Compte le nombre de formes determinees.
 */
void secondPass(Mat& src, map<int, int>& valeurLabel)
{
	int currentLabel;

	// On considere : 1 niveau de gris = 1 label
	// On stock de maniere unique chaque niveau de gris comme des labels
	set<int> setLabel; 

	for(int row=0; row<src.size().height; row++)
	{
		for(int col=0; col<src.size().width; col++)
		{
			// Pixel background
			if(src.at<uchar>(row,col) == 0) continue;

			currentLabel = src.at<uchar>(row,col);

			// La valeur ne sera pas inseree si elle existe deja
			setLabel.insert(valeurLabel[currentLabel]);

			src.at<uchar>(row,col) = valeurLabel[currentLabel];
		}
	}

	cout << "Nombre de formes : " << setLabel.size() << endl;
}

/** 
 *  Recupere le label parent le plus vieux (donc a l'ID minimum)
 *  Renvoie 0 si aucun des voisins n'a de labels
 */
int getMinLabel(int labelW, int labelNW, int labelN, int labelNE)
{
	int min = 256;

	if( (labelW  != 0) && (labelW  < min) )  min = labelW;
	if( (labelNW != 0) && (labelNW < min) )  min = labelNW;
	if( (labelN  != 0) && (labelN  < min) )  min = labelN;
	if( (labelNE != 0) && (labelNE < min) )  min = labelNE;

	if(min == 256) return 0;
	return min;
}

/**
 *  Passe en revue tous les voisins appartenant au masque en "L" d'un pixel donne.
 *  Modifie la valeur du label parent des pixels qui ont une valeur differente du 'min'.
 */
void changeParents(Mat& src, int row, int col, int min, map<int, int>& valeurLabel)
{
	int labelTemp;
	int offsetRow, offsetCol;

	for(int i=0; i<G_NB_VOISINS_ITER; i++)
	{
		// Pour rentrer tous les tests en une seule boucle for
		offsetRow = G_VOISINS_ROW_ITER[i];
		offsetCol = G_VOISINS_COL_ITER[i];

		if(inImage(src, row+offsetRow, col+offsetCol))
		{
			// Si non background et label different du min
			// Note: on est sur a ce stade qu'on compare avec le min
			if( (src.at<uchar>(row+offsetRow, col+offsetCol) > 0) && (src.at<uchar>(row+offsetRow, col+offsetCol) != min) )
			{
				labelTemp = src.at<uchar>(row+offsetRow, col+offsetCol);

				// Update du label parent
				valeurLabel[labelTemp] = valeurLabel[min];

				// Update du label du pixel voisin pour eviter de futurs tests
				src.at<uchar>(row+offsetRow, col+offsetCol) = min;
			}
		}
	}
}


// =========================== CONNECTED-COMPONENT RECURSIF

/**
 *  Pour chaque pixel, si le pixel n'est pas marque, lui affecte un label puis
 *  propage ce pixel a tous les voisins non marques en 8-connectivity.
 */
void labelizeRecur(Mat& src)
{
	for(int col=0; col<src.size().width; col++)
	{
		for(int row=0; row<src.size().height; row++)
		{
			if(src.at<uchar>(row,col) == 255)
			{
				connectedComponentRecur(src, row, col, g_valeurGris);
				putText(src, "#"+to_string(g_nbFormes),Point(col,row), FONT_HERSHEY_PLAIN, 1, 253);
				g_valeurGris += G_INCR_VALEUR_GRIS;
				g_nbFormes++;
			}
		}
	}

	cout << "Nombre de formes : " << g_nbFormes - 1 << endl;
}

/**
 *  Marque le pixel comme appartenant a un label puis le propage a ses voisins non marques.
 */
void connectedComponentRecur(Mat& src, int row, int col, int label)
{
	src.at<uchar>(row,col) = label; // On marque le pixel visite

	// Sert a dynamiquement parcourir les voisins
	int rowVoisin, colVoisin;

	for(int i=0; i<G_NB_VOISINS_RECUR; i++)
	{
		rowVoisin = row + G_VOISINS_ROW_RECUR[i];
		colVoisin = col + G_VOISINS_COL_RECUR[i];

		if(inImage(src, rowVoisin, colVoisin))
			if(src.at<uchar>(rowVoisin, colVoisin) == 255) // Non visite
				connectedComponentRecur(src, rowVoisin, colVoisin, label);
	}
}

// =========================== UTILITIES

/**
 *  Binarise l'image source en considerant que le fond blanc est du background.
 *  A l'output : 0 == background et 255 == pixels d'interets
 */
void binarizeFondBlanc(Mat& src)
{
	for(int col=0; col<src.size().width; col++)
	{
		for(int row=0; row<src.size().height; row++)
		{
			if((int)src.at<uchar>(row,col) < 128)
				src.at<uchar>(row, col) = 255;
			else src.at<uchar>(row, col) = 0;
		}
	}
}

/**
 *  Binarise l'image source en considerant que le fond noir est du background.
 *  A l'output : 0 == background et 255 == pixels d'interets
 */
void binarizeFondNoir(Mat& src)
{
	for(int col=0; col<src.size().width; col++)
		for(int row=0; row<src.size().height; row++)
			if((int)src.at<uchar>(row,col) < 128)
				src.at<uchar>(row, col) = 0;
			else src.at<uchar>(row, col) = 255;
}

bool inImage(const Mat& src, int row, int col)
{
	if( (row > 0) && (row < src.size().height) )
		if( (col > 0) && (col < src.size().width) )
			return true;

	return false;
}

