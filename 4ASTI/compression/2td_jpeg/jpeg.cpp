/* ===========================
            TODO
   =========================== 
    Faire la procedure JPEG
DCT (pas la fonction, la procedure)
Algo de zigzag
RLE
Ne pas coder Huffmann


=== Fonctions
Faire une fonction qui decoupe l'image en 8x8


*/


/* ===========================
            INCLUDES
   =========================== */

#include "noeud.h"
#include "jpeg.h"
#include <vector>

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
        << "fp_image: Filepath to source image" << endl;
        return 0;
    }

    Mat image = imread(argv[1], CV_LOAD_IMAGE_GRAYSCALE);
    if(!image.data)
    {
        printf("No image data \n");
        return -1;
    }

    Mat norm_src = normalize_size_8x8(image); // Multiple de 8x8
    compress_write_jpeg(norm_src);

    return 0;
}


/* ===========================
            FONCTIONS
   =========================== */

// =========================== JPEG
void compress_write_jpeg(const Mat& src)
{
    int n_blocks_x(src.size().height / kBlockSize);
    int n_blocks_y(src.size().width  / kBlockSize);

    // Parcours en blocks 8x8
    for(int y=0; y<n_blocks_y; y++)
    {
        for(int x=0; x<n_blocks_x; x++)
        {
            Mat curr_block = get_block(src, x*kBlockSize, y*kBlockSize);
            Mat block      = Mat(kBlockSize, kBlockSize, CV_32FC1);
            
            center_zero(curr_block);
            dct(curr_block, block);

            quantify_block(block);

            int res_zigzag[kBlockSize*kBlockSize];
            zigzag_block_write(block, res_zigzag);

            affiche_array(res_zigzag, 64);

            String code = rle_block(res_zigzag);

            //Huffmann
        }
    }
    // Enregistrement dans un fichier binaire
}

// =========================== UTILITIES
Mat normalize_size_8x8(const Mat& src)
{
    int width(src.size().width / kBlockSize);
    int height(src.size().height / kBlockSize);

    Mat res = Mat(height, width, CV_32FC1);

    for(int y=0; y<width; y++)
        for(int x=0; x<height; x++)
            res.at<float>(x,y) = (float)src.at<uchar>(x,y);

    return res;
}

template <class T>
void affiche_array(T* tab, int taille)
{
    for(int i=0; i<taille; i++)
        cout << tab[i] << " ";
    cout << endl;
}

// =========================== BLOCK
Mat get_block(const Mat& src, const int x, const int y)
{
    Mat res = Mat(kBlockSize, kBlockSize, CV_32FC1);

    for(int j=0; j<kBlockSize; j++)
        for(int i=0; i<kBlockSize; i++)
            res.at<float>(i,j) = (float)src.at<uchar>(x+i,y+j);

    return res;
}

void center_zero(Mat& src)
{
    for(int y=0; y<src.size().width; y++)
        for(int x=0; x<src.size().height; x++)
            src.at<float>(x,y) -= 128.0;
}

void quantify_block(Mat& src)
{
    int dimX(src.size().height);

    for(int y=0; y<src.size().width; y++)
        for(int x=0; x<dimX; x++)
            src.at<float>(x,y) = (int)(src.at<float>(x,y)/kCoeff[x*dimX + y]);
}

// =========================== ZIGZAG
void zigzag_block_write(const Mat& src, int* dst)
{
    int x(0);
    int y(0);
    int limit = kBlockSize/2 - 1; // Servira pour le nombre de patterns

    // On enregistre la valeur a (0,0)
    *dst = (int)src.at<float>(x,y);
    dst++; 
    y++; // vers la droite

    // Premiere moitie du zigzag (superieur gauche)
    for(int i=0; i<limit; i++)
    {
        zigzag_diagonal_down_to_col(src, dst, 0, x, y);
        x++; // vers le bas
        zigzag_diagonal_up_to_row(src, dst, 0, x, y);
        y++;
    } 

    // Diagonal du bloc : haut-droit vers bas-gauche
    zigzag_diagonal_down_to_col(src, dst, 0, x, y);
    y++;

    // Deuxieme moitie du zigzag (inferieur droit)
    for(int i=0; i<limit; i++)
    {
        zigzag_diagonal_up_to_col(src, dst, kBlockSize-1, x, y);
        x++;
        zigzag_diagonal_down_to_row(src, dst, kBlockSize-1, x, y);
        y++;
    }

    *dst = (int)src.at<float>(x,y);
}

void zigzag_diagonal_down_to_col(const Mat& src, int* dst, const int col, int& x, int& y)
{
    while(y!=col)
    {
        *dst = (int)src.at<float>(x,y);
        dst++; x++; y--;
    }
    *dst = (int)src.at<float>(x,y);
    dst++;
}

void zigzag_diagonal_down_to_row(const Mat& src, int* dst, const int row, int& x, int& y)
{
    while(x!=row)
    {
        *dst = (int)src.at<float>(x,y);
        dst++; x++; y--;
    }
    *dst = (int)src.at<float>(x,y);
    dst++;
}

void zigzag_diagonal_up_to_col(const Mat& src, int* dst, const int col, int& x, int& y)
{
    while(y!=col)
    {
        *dst = (int)src.at<float>(x,y);
        dst++; x--; y++;
    }
    *dst = (int)src.at<float>(x,y);
    dst++;
}

void zigzag_diagonal_up_to_row(const Mat& src, int* dst, const int row, int& x, int& y)
{
    while(x!=row)
    {
        *dst = (int)src.at<float>(x,y);
        dst++; x--; y++;
    }
    *dst = (int)src.at<float>(x,y);
    dst++;
}

// =========================== RLE
string rle_block(int* src)
{
    string coded("");

    int compteur(0);
    int i(0);
    while(i<64)
    {
        if(src[i] == 0)
        {
            compteur = 0;
            while(src[i] == 0)
            {
                compteur++;
                i++;
            }
            coded += ",("+to_string(compteur)+")";
        }
        else
        {
            coded += ","+to_string(src[i]);
            i++;
        }
    }

    return coded;
}

// =========================== HUFFMAN

