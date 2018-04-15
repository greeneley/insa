#ifndef _COMPONENTS_H_
#define _COMPONENTS_H_

/* ===========================
            CONSTANTES
   =========================== */

#include <stdio.h>
#include <highgui.h>
#include <opencv2/opencv.hpp>
#include <map>


/* ===========================
             GLOBAL
   =========================== */

int       g_label            = 1;
int       g_valeurGris       = 20;
int       g_nbFormes         = 1;
const int G_INCR_VALEUR_GRIS = 1;

const int G_NB_VOISINS_ITER      = 4;
const int G_VOISINS_ROW_ITER[4]  = {0, -1, -1, -1};
const int G_VOISINS_COL_ITER[4]  = {-1, -1, 0, +1};

const int G_NB_VOISINS_RECUR     = 8;
const int G_VOISINS_ROW_RECUR[8] = {0, -1,  0, 1,  1, 1, -1, -1};
const int G_VOISINS_COL_RECUR[8] = {1,  0, -1, 0, -1, 1, -1,  1};

/* ===========================
            PROTOTYPES
   =========================== */

// =========================== PRINT
void afficheImage(const cv::Mat& src, std::string name);

// =========================== CONNECTED-COMPONENT SEQUENTIAL
void labelizeIter(cv::Mat& src);

void firstPass(cv::Mat& src, std::map<int, int>& valeurLabel);
void secondPass(cv::Mat& src, std::map<int, int>& valeurLabel);

int  getMinLabel(int labelW, int labelNW, int labelN, int labelNE);
void changeParents(cv::Mat& src, int row, int col, int min, std::map<int, int>& valeurLabel);

// =========================== CONNECTED-COMPONENT RECURSIVE
void labelizeRecur(cv::Mat& src);
void connectedComponentRecur(cv::Mat& src, int row, int col, int label);

// =========================== UTILITIES
void binarizeFondBlanc(cv::Mat& src);
void binarizeFondNoir(cv::Mat& src);
bool inImage(const cv::Mat& src, int row, int col);

#endif