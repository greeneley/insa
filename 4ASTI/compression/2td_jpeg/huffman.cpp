/* ===========================
            INCLUDES
   =========================== */

#include <vector>
#include <map>
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
               HUFFMAN
   =========================== */

/**
 *  Cree l'arbre de Huffman associe a la chaine RLE en argument.
 *
 *  Construit d'abord un vecteur contenant les noeuds deduits du string.
 *  Puis rempli une map qui associe un symbole a un code de Huffman.
 *  Enfin, ecrit les informations dans un fichier.
 */
void huffman_to_file(string src)
{
    // On construit les noeuds et les met dans un vector
    vector<Noeud*> ptr_noeuds = rle_block_to_vector(src);

    // A partir du vecteur, on consuit l'arbre
    huffman_create_tree_recursive(ptr_noeuds);

    /* L'arbre est le seul element restant du vector
     * On cree une map qui contient l'associatoin <symbole, code de Huffman>
     * L'operation est recursive
     */
    map<char, string> m;
    huffman_tree_to_binary_map_recursive(m, ptr_noeuds.at(0), "");

    cout << "CODE DE HUFFMAN<<<<<<<<<<<<<" << endl;
    for(auto element : m)
    {
        cout << (char)element.first << " : " << (string)element.second << endl;
    }

    huffman_write_binary(m, src);

}

void huffman_create_tree_recursive(vector<Noeud*>& v)
{
    /**
     * Si vrai, on retire les deux noeuds de plus faible poids
     * et on les fusionne en un nouveau noeud
     * qu'on ajoute au vector pour remplacer les deux enleves
     */
    if(v.size() > 1)
    {
        uint index_mins[2];
        huffman_get_mins(v, index_mins);
        huffman_fuse_min_nodes(v, index_mins);
        huffman_create_tree_recursive(v);
    }
}

/**
 *  Recupere l'index des deux noeuds de plus faible poids
 */
void huffman_get_mins(vector<Noeud*>& v, uint mins[2])
{
    // On veut garder la convention min0 < min 1
    int min0;
    int min1;

    // Initialisation
    if(v.at(0)->getValue() < v.at(1)->getValue())
    {
        min0    = v.at(0)->getValue();
        min1    = v.at(1)->getValue();
        mins[0] = 0; // mins[0] == min0
        mins[1] = 1;
    }
    else
    {
        min0 = v.at(1)->getValue();
        min1 = v.at(0)->getValue();
        mins[0] = 1;
        mins[1] = 0;
    }

    // On va garder l'organisation suivante : min0 < min1
    // Ca nous permet de 'coulisser' les mins chaque fois que l'on en rencontre un nouveau
    for(uint i=2; i<v.size(); i++)
    {
        // On 'coulisse' : min1 <- min0; min0 <- new; 
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

/**
 * Fusionne deux noeuds
 */
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

/**
 *  Construit recursivement la table de Huffman avec a gauche 0 et a droite 1
 *
 *  @param m      Map qui associe <symbole, code de Huffman>
 *  @param node   Noeud courant de l'arbre 
 *  @param binary String correspondant au code de Huffman du noeud courant. Il est construit recursivement par propagation.
 */
void huffman_tree_to_binary_map_recursive(map<char, string>& m, Noeud* node, string binary)
{
    if(node->getLabel() == 'n')
    {
        huffman_tree_to_binary_map_recursive(m, node->getFilsGauche(), binary+"0");
        huffman_tree_to_binary_map_recursive(m, node->getFilsDroit(), binary+"1");
    }
    else // C'est une feuille, on recupere le label
    {
        m[node->getLabel()] = binary;
    }
}

void huffman_write_binary(map<char, string>& m, string chaine)
{
    ofstream outputFile(G_COMPRESSED_FILE, ofstream::out | ios::binary);
    if (!outputFile)
    {
        cout << "erreur creation" << endl;
        exit(1);
    }

    // Ici on pourrait ameliorer en faisant une ecriture byte par byte
    // C'est la que la taille augmente au lieu de diminuer
    for(char c : chaine)
    {
        outputFile.write(m[c].c_str(), m[c].size());
    }

    outputFile.flush();
    outputFile.close(); 
}
