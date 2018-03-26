/* ===========================
            INCLUDES
   =========================== */

#include "noeud.h"
#include "jpeg.h"
#include <vector>
#include <map>
#include <bitset>

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
    string code = to_string(n_blocks_x)+","+to_string(n_blocks_y)+",";

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

            code += rle_block(res_zigzag);

            if(y==(n_blocks_y-1) && x==(n_blocks_x-1))
                code += "#"; // indicateur de fin
            else
                code += "$"; // separateur de blocs
        }
    }
    huffman(code);
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
Mat get_block(const Mat& src, int x, int y)
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
    int index(0);
    int x(0);
    int y(0);
    int limit = kBlockSize/2 - 1; // Servira pour le nombre de patterns

    // On enregistre la valeur a (0,0)
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
        zigzag_diagonal_up_to_col(src, dst, kBlockSize-1, index, x, y);
        x++;
        zigzag_diagonal_down_to_row(src, dst, kBlockSize-1, index, x, y);
        y++;
    }

    dst[index] = (int)src.at<float>(x,y);
}

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

// =========================== RLE
string rle_block(int* src)
{
    string coded("");

    int compteur(0);
    int i(0);
    while(i<(kBlockSize*kBlockSize - 1))
    {
        if(src[i] == src[i+1])
        {
            compteur = 1;
            while(src[i] == src[i+1])
            {
                compteur++;
                i++;
            }
            coded += to_string(src[i])+"@"+to_string(compteur)+",";
        }
        else
        {
            coded += to_string(src[i])+",";
            i++;
        }
    }

    return coded;
}

vector<Noeud*> rle_block_to_vector(std::string src)
{
    vector<Noeud*> res = {};

    // Creation de la map
    map<char, int> m;
    map<char, int>::iterator it;
    for(uint i=0; i<src.length(); i++)
    {
        it = m.find(src.at(i));
        if(it == m.end()) m[src.at(i)] = 1;
        else              m[src.at(i)]++;
    }

    // Creation des noeuds et remplissage de l'output
    for(auto element : m)
    {
        Noeud* node = new Noeud(NULL, (int)element.second, (char)element.first, NULL, NULL);
        res.push_back(node);
    }

    return res;
}


// =========================== HUFFMAN
void huffman(string src)
{
    vector<Noeud*> ptr_noeuds = rle_block_to_vector(src);

    huffman_create_tree_recursive(ptr_noeuds);
    map<char, string> m;
    huffman_tree_to_binary_map_recursive(m, ptr_noeuds.at(0), "");

    cout << "TEST TREE<<<<<<<<<<<<<<<" << endl;
    for(uint i=0; i<ptr_noeuds.size(); i++)
    {
        Noeud n = *(ptr_noeuds.at(i));
        cout << i << " : " << n.getLabel() << " " << n.getValue() << endl;
    }

    cout << "TEST BINARY<<<<<<<<<<<<<" << endl;
    for(auto element : m)
    {
        cout << (char)element.first << " : " << (string)element.second << endl;
    }


    // faire l'iteration sur le vector pour construire l'arbre
    // itereren recursivite l'arbre pour consuitre une map 
    // parcourir la map pour pouvoir ecrire dans un fichier
}

void huffman_create_tree_recursive(vector<Noeud*>& v)
{
    if(v.size() > 1)
    {
        for(uint i=0; i<v.size(); i++)
        {
            cout << i << " : " << v.at(i)->getLabel() << " " << v.at(i)->getValue() << endl;
        }

        cout << "=====================" << endl;

        uint index_mins[2];
        huffman_get_mins(v, index_mins);
        huffman_fuse_min_nodes(v, index_mins);
        huffman_create_tree_recursive(v);
    }
}

void huffman_get_mins(vector<Noeud*>& v, uint mins[2])
{
    int min0;
    int min1; // min0 < min1
    if(v.at(0)->getValue() < v.at(1)->getValue())
    {
        min0    = v.at(0)->getValue();
        min1    = v.at(1)->getValue();
        mins[0] = 0; // mins[0] == min1
        mins[1] = 1;
    }
    else
    {
        min0 = v.at(1)->getValue();
        min1 = v.at(0)->getValue();
        mins[0] = 1;
        mins[1] = 0;
    }

    // On va garder l'organisation suivante :
    // min0 < min1
    for(uint i=2; i<v.size(); i++)
    {
        if(v.at(i)->getValue() < min0)
        {
            min1    = min0;
            min0    = v.at(i)->getValue();
            mins[1] = mins[0];
            mins[0] = i;
        }
        else if(v.at(i)->getValue() < min1)
        {
            min1    = v.at(i)->getValue();
            mins[1] = i;
        }
    }
}

void huffman_fuse_min_nodes(vector<Noeud*>& v, uint mins[2])
{
    uint min0, min1;
    if(mins[1] > mins[0])
    {
        min0 = mins[0];
        min1 = mins[1];
    }
    else
    {
        min0 = mins[1];
        min1 = mins[0];
    }

    int newVal      = v.at(min0)->getValue() + v.at(min1)->getValue();
    Noeud* newNoeud = new Noeud(NULL, newVal, 'n', v.at(min0), v.at(min1));
    v.erase(v.begin()+(int)min1);
    v.erase(v.begin()+(int)min0);
    v.push_back(newNoeud);
}

void huffman_tree_to_binary_map_recursive(map<char, string>& m, Noeud* v, string binary)
{
    if(v->getLabel() == 'n')
    {
        huffman_tree_to_binary_map_recursive(m, v->getFilsGauche(), binary+"0");
        huffman_tree_to_binary_map_recursive(m, v->getFilsDroit(), binary+"1");
    }
    else
    {
        m[v->getLabel()] = binary;
    }
}

void huffman_write_binary(std::map<char, std::string>& m)
{
    cout << "todo" << endl;
}

void huffman_free_memory(std::vector<Noeud*>& v)
{
    cout << "todo" << endl;
}