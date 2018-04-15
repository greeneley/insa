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
        cout << endl
             << "Usage: ./growing_region fp_image x y threshold" << endl
             << "fp_image:  Filepath to source image"         << endl
             << "x:         X position of the starting pixel in OpenCv Image format" << endl
             << "y:         Y position of the starting pixel in OpenCv Image format" << endl
             << "threshold: Value of minimal threshold"       << endl;
        return -1;
    }

    // Chargement de l'image source
    Mat image = imread(argv[1], CV_LOAD_IMAGE_GRAYSCALE);
    if(!image.data)
    {
        cout << "ERREUR : Image semble vide ou non lisible" << endl;
        return -1;
    }

    // Init des variables
    int col = stoi(argv[2]);
    int row = stoi(argv[3]);

    // Verification des effets de bord pixel initial
    if(row > image.size().height || col > image.size().width || row < 0 || col < 0)
    {
    	cout << "Position du pixel invalide" << endl
    	     << "Input (x,y)  == (" << col << "," << row << ")" << endl
    	     << "taille image == " << image.size().height << "x" << image.size().width << endl;
             return -1;
    }

    // Init de la situation initiale
    Mat   dst          = Mat(image.size().height, image.size().width, CV_8UC1, Scalar(0));
    float seed         = (float)image.at<uchar>(row, col);
    float threshold    = stof(argv[4]);
    int   n            = 1;

    // === Recursivite
    growing_recursive(image, dst, row, col, seed, n, threshold);

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
void afficheImage(const Mat& src, string name)
{
    namedWindow(name, WINDOW_AUTOSIZE );
    imshow(name, src);
}

// =========================== GROWING-REGION

/**
 * \brief   Applique l'algorithme récursive de Growing Region sur une image a partir d'une position (x,y)
 *
 * \param   src       Image source ou appliquer l'algorithme
 * \param   dst       Image destination contenant le resultat de l'algorithme
 * \param   row       Ligne correspondant au pixel das la Mat de l'image
 * \param   col       Colonne correspondant au pixel dans la Mat de l'image
 * \param   sum       Somme de tous les pixels rencontres par l'algorithme
 * \param   n         Nombre de pixels rencontres par l'algorithme
 * \param   threshold Seuil d'acceptation
 *
 * \return  void
*/
void growing_recursive(const Mat& src, Mat& dst, int row, int col, float& sum, int& n, float& threshold)
{
    dst.at<uchar>(row, col) = 255; // On valide celui sur lequel on est

	float mean   = sum / (float)n;
    int   width  = src.size().width;
    int   height = src.size().height;
    float value;

    // Pour dynamiquement calculer les voisins
    int   offsetRow, offsetCol;

    // On passe en revue tous les voisins
    for(int voisin=G_DROITE; voisin<G_TOUS_LES_VOISINS; voisin++)
    {
        // Valeurs egaux a -1, 0 ou +1. Voir dans le header.
        offsetRow = G_OFFSET_ROW[voisin];
        offsetCol = G_OFFSET_COL[voisin];

        // Si voisin non visite et effet de bord evite
        if(isValid(dst, row+offsetRow, col+offsetCol, height, width))
        {
            if(inArea(src, row+offsetRow, col+offsetCol, mean, threshold))
            {
                // On recupere la valeur du pixel et on met a jour toutes les donnees
                value = (float)src.at<uchar>(row+offsetRow, col+offsetCol);
                update(value, sum, n);

                // Voisin est dans la region : appel recursive sur le voisin
                growing_recursive(src, dst, row+offsetRow, col+offsetCol, sum, n, threshold);
            }
            // Voisin n'est pas dans la region : on le marque comme rejete
            else dst.at<uchar>(row+offsetRow, col+offsetCol) = 128;
        }
    }
}

// On calcule la moyenne a la volee donc il faut mettre a jour sum et n
void update(float value, float& sum, int& n)
{
    sum += value;
    n++;
}

// =========================== BOOLEAN
bool inImage(int row, int col, int height, int width)
{
    if(row < 0      || col < 0)     return false;
    if(row > height || col > width) return false;
    return true;
}

// Verifie si le pixel appartient a la region grace au threshold
bool inArea(const Mat& img, int row, int col, float mean, float threshold)
{
    float res = abs( (float)img.at<uchar>(row, col) - mean );
    res = res * res;

    return (res < threshold);
}

bool isMarked(Mat& img, int row, int col)
{
    int value = (int)img.at<uchar>(row, col);

    return value; // Premiere rencontre si > 0
}

// Verifie que le pixel est dans l'image et est non visite
bool isValid(Mat& img, int row, int col, int height, int width)
{
    if(inImage(row, col, height, width)) 
        return !isMarked(img, row, col);
    return false;
}
