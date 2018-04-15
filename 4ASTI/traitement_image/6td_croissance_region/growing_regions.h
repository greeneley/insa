#ifndef _GROWING_REGIONS_H_
#define _GROWING_REGIONS_H_

#include <stdio.h>
#include <highgui.h>
#include <opencv2/opencv.hpp>

/* ===========================
            CONSTANTES
   =========================== */
const int G_DROITE           = 0;
const int G_HAUT             = 1;
const int G_GAUCHE           = 2;
const int G_BAS              = 3;
const int G_TOUS_LES_VOISINS = 4;

// Les valeurs d'index i correspondent aux constantes au-dessus
const int G_OFFSET_ROW[4] = {0, -1,  0, 1};
const int G_OFFSET_COL[4] = {1,  0, -1, 0};


/* ===========================
            PROTOTYPES
   =========================== */

// =========================== PRINT
void afficheImage(const cv::Mat& src, std::string name);

// =========================== GROWING-REGION
void growing_recursive(const cv::Mat& src, cv::Mat& dst, int row, int col, float& sum, int& n, float& threshold);
void update(float value, float& sum, int& n);

// =========================== BOOLEAN
bool inImage(int row, int col, int height, int width);
bool inArea(const cv::Mat& img, int row, int col, float mean, float threshold);
bool isMarked(cv::Mat& img, int row, int col);
bool isValid(cv::Mat& img, int row, int col, int height, int width);

#endif