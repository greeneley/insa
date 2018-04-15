/* ===========================
            INCLUDES
   =========================== */

#include <vector>
#include <map>

#include "noeud.h"
#include "jpeg.h"

/* ===========================
           NAMESPACES
   =========================== */

using namespace cv;
using namespace std;

/* ===========================
                RLE
   =========================== */

/**
 *  Applique l'algorithme du RLE sur un tableau d'int.
 *
 *  Les nombres sont delimites par des caracteres ','
 *
 *  Renvoie un string correspondant au code RLE
 */
string rle_block(int* src)
{
    string coded("");

    int compteur = 0; // Compte les occurrences
    int i        = 0; // Incrementeur dans le tableau
    while(i<(G_BLOCK_SIZE*G_BLOCK_SIZE - 1))
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

/**
 *  Permet de construire un vecteur de noeud depuis un RLE au format string
 *  Ce vecteur servira a faire l'arbre de Huffman
 *
 *  Retourne le vecteur cree
 */
vector<Noeud*> rle_block_to_vector(std::string src)
{
    vector<Noeud*> res = {};

    // La map m est de structure <symbole, occurrence>
    map<char, int> m;
    map<char, int>::iterator it;
    for(uint i=0; i<src.length(); i++)
    {
        it = m.find(src.at(i));
        if(it == m.end()) m[src.at(i)] = 1; // nouveau symbole
        else              m[src.at(i)]++;   // incr de l'occurrence
    }

    // Creation des noeuds et remplissage de l'output
    for(auto element : m)
    {
        Noeud* node = new Noeud(NULL, (int)element.second, (char)element.first, NULL, NULL);
        res.push_back(node);
    }

    return res;
}