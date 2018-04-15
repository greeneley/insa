/* ===========================
            INCLUDES
   =========================== */

#include <iostream>
#include <fstream>

#include "noeud.h"
#include "jpeg.h"

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
        cout << "Usage: ./jpeg fp_image" << endl
        << "fp_image: Filepath to source image" << endl;
        return -1;
    }

    Mat image = imread(argv[1], CV_LOAD_IMAGE_GRAYSCALE);
    if(!image.data)
    {
        cout << "No image data" << endl;
        return -1;
    }

    // On normalise l'image en une matrice multiple de 8x8
    Mat norm_src = normalize_size(image);

    compress_write_jpeg(norm_src);
    print_taux(argv[1], G_COMPRESSED_FILE);

    return 0;
}


/* ===========================
            FONCTIONS
   =========================== */

// =========================== JPEG

/**
 *  Effectue un parcours par blocs de 8x8 et construit le RLE en eme temps.
 *
 *  Sur chaque bloc, effectue les operations suivantes :
 *    - centre les valeurs du bloc en 0 ;
 *    - applique la dct d'OpenCV 3 ;
 *    - quantifie le bloc ;
 *    - effectue un parcours en zigzag ;
 *    - ajoute le parcours dans le string du futur RLE.
 *
 *  A la fin du parcours de la matrice, applique le RLE sur le string construit.
 */
void compress_write_jpeg(const Mat& src)
{
    int n_blocks_x(src.size().height / G_BLOCK_SIZE);
    int n_blocks_y(src.size().width  / G_BLOCK_SIZE);

    // On ajoute la taille de l'image dans le string du RLE
    string code = to_string(n_blocks_x)+","+to_string(n_blocks_y)+",";

    // Parcours en blocks 8x8
    for(int x=0; x<n_blocks_x; x++)
    {
        for(int y=0; y<n_blocks_y; y++)
        {
            // Block courant
            Mat curr_block = get_block(src, x*G_BLOCK_SIZE, y*G_BLOCK_SIZE);

            // On initialise un nouveau bloc ou on effectue toutes les operations
            Mat block      = Mat(G_BLOCK_SIZE, G_BLOCK_SIZE, CV_32FC1);
            
            center_zero(curr_block);
            dct(curr_block, block);
            quantify_block(block);

            // Un tableau qui recevra la valeur des int du bloc
            int res_zigzag[G_BLOCK_SIZE*G_BLOCK_SIZE];

            // On effectue un parcours en zigzag et on stock tout dans res_zigzag
            zigzag_block_write(block, res_zigzag);

            // On convertie le bloc en string et on l'append au string du RLE
            code += rle_block(res_zigzag);
        }
    }

    // Toutes les operations de Huffman + ecriture fichier
    huffman_to_file(code);
}

// =========================== UTILITIES

/**
 *  Renvoie une matrice a la taille normalisee en multiple de 8x8
 */
Mat normalize_size(const Mat& src)
{
    // Pour ajouter des valeurs si la taille n'est pas multiple de 8x8
    bool pad_width  = src.size().width  % G_BLOCK_SIZE;
    bool pad_height = src.size().height % G_BLOCK_SIZE;

    int width  = src.size().width;
    int height = src.size().height;

    // Ajout des pixels si non multiple
    if(pad_width)  width  += (G_BLOCK_SIZE - pad_width);
    if(pad_height) height += (G_BLOCK_SIZE - pad_height);

    // Construction de la matrice normalisee
    Mat res = Mat(height, width, CV_32FC1);
    for(int row=0; row<src.size().height; row++)
    {
        // Copie des elements originaux
        for(int col=0; col<src.size().width; col++)
            res.at<float>(row,col) = (float)src.at<uchar>(row,col);

        // Ajout de pixels nuls pour normaliser les colonnes manquantes
        for(int col=src.size().width; col<width; col++)
            res.at<float>(row,col) = 0.0;
    }

    // Ajout de pixels nuls pour normaliser les lignes manquantes
    for(int row=src.size().height; row<height; row++)
        for(int col=0; col<width; col++)
            res.at<float>(row,col) = 0.0;

    return res;
}

/**
 *  Affiche les tailles source et compressee et le taux de compression.
 */
void print_taux(const string src, const string out)
{
    ifstream source(src, ifstream::ate | ios::binary);
    if (!source)
    {
        cout << "erreur creation" << endl;
        exit(1);
    }

    ifstream output(out, ifstream::ate | ios::binary);
    if (!output)
    {
        cout << "erreur creation" << endl;
        exit(1);
    }

    source.seekg (0, source.end);
    output.seekg (0, output.end);
    float res = (float)source.tellg() / (float)output.tellg();

    cout << "Taille originale  : " << (float)source.tellg() << endl;
    cout << "Taille compressee : " << (float)output.tellg() << endl;
    cout << "Taux de compression : " << res << endl;

    source.close();
    output.close(); 
}

// =========================== BLOCK
/**
 *  Renvoie le block 8x8 associe au pixel Mat(x,y)
 */
Mat get_block(const Mat& src, int x, int y)
{
    Mat res = Mat(G_BLOCK_SIZE, G_BLOCK_SIZE, CV_32FC1);

    for(int j=0; j<G_BLOCK_SIZE; j++)
        for(int i=0; i<G_BLOCK_SIZE; i++)
            res.at<float>(i,j) = (float)src.at<uchar>(x+i,y+j);

    return res;
}

void center_zero(Mat& src)
{
    for(int y=0; y<src.size().width; y++)
        for(int x=0; x<src.size().height; x++)
            src.at<float>(x,y) -= 128.0;
}

/**
 *  Quantifie le block 8x8 et arrondie a la valeur inferieure.
 */
void quantify_block(Mat& src)
{
    int dimX = src.size().height;

    for(int y=0; y<src.size().width; y++)
        for(int x=0; x<dimX; x++)
            src.at<float>(x,y) = (int)(src.at<float>(x,y)/kCoeff[x*dimX + y]);
}
