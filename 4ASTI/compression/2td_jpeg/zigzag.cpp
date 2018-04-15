#include "jpeg.h"

/* ===========================
           NAMESPACES
   =========================== */

using namespace cv;
using namespace std;

/* ===========================
              ZIGZAG
   =========================== */

/**
 *  Effectue un parcours en zigzag suivant deux patterns reccurrents
 *
 *  Stock le resultat dans un tableau de int
 */
void zigzag_block_write(const Mat& src, int* dst)
{
    int index = 0; // Position dans le tableau
    int x     = 0;
    int y     = 0;
    int limit = G_BLOCK_SIZE/2 - 1; // Servira pour le nombre de patterns

    // On enregistre la valeur en (0,0)
    dst[index] = (int)src.at<float>(x,y);
    index++; 

    y++; // vers la droite
    // Premiere moitie du zigzag (superieur gauche)
    for(int i=0; i<limit; i++)
    {
        zigzag_diagonal_down_to_col(src, dst, 0, index, x, y);
        x++; // vers le bas
        zigzag_diagonal_up_to_row(src, dst, 0, index, x, y);
        y++;
    } 

    // Diagonal du bloc : haut-droit vers bas-gauche
    zigzag_diagonal_down_to_col(src, dst, 0, index, x, y);
    y++;

    // Deuxieme moitie du zigzag (inferieur droit)
    for(int i=0; i<limit; i++)
    {
        zigzag_diagonal_up_to_col(src, dst, G_BLOCK_SIZE-1, index, x, y);
        x++;
        zigzag_diagonal_down_to_row(src, dst, G_BLOCK_SIZE-1, index, x, y);
        y++;
    }

    dst[index] = (int)src.at<float>(x,y);
}

/**
 * Lis tous les pixels suivant la diagonale descendante jusqu'a la colonne 'col'
 */
void zigzag_diagonal_down_to_col(const Mat& src, int* dst, int col,  int& index, int& x, int& y)
{
    while(y!=col)
    {
        dst[index] = (int)src.at<float>(x,y);
        index++; x++; y--;
    }
    dst[index] = (int)src.at<float>(x,y);
    index++;
}

/**
 * Lis tous les pixels suivant la diagonale descendante jusqu'a la ligne 'row'
 */
void zigzag_diagonal_down_to_row(const Mat& src, int* dst, int row, int& index, int& x, int& y)
{
    while(x!=row)
    {
        dst[index] = (int)src.at<float>(x,y);
        index++; x++; y--;
    }
    dst[index] = (int)src.at<float>(x,y);
    index++;
}

/**
 * Lis tous les pixels suivant la diagonale ascendante jusqu'a la colonne 'col'
 */
void zigzag_diagonal_up_to_col(const Mat& src, int* dst, int col, int& index,  int& x, int& y)
{
    while(y!=col)
    {
        dst[index] = (int)src.at<float>(x,y);
        index++; x--; y++;
    }
    dst[index] = (int)src.at<float>(x,y);
    index++;
}

/**
 * Lis tous les pixels suivant la diagonale ascendante jusqu'a la ligne 'row'
 */
void zigzag_diagonal_up_to_row(const Mat& src, int* dst, int row,  int& index, int& x, int& y)
{
    while(x!=row)
    {
        dst[index] = (int)src.at<float>(x,y);
        index++; x--; y++;
    }
    dst[index] = (int)src.at<float>(x,y);
    index++;
}