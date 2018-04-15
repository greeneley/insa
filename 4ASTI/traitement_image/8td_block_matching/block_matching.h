#ifndef _BLOCK_H_
#define _BLOCK_H_

#include <stdio.h>
#include <highgui.h>
#include <opencv2/opencv.hpp>

/* ===========================
            CONSTANTES
   =========================== */
int G_SIZE_SEARCH;   // Taille d'un cote du bloc de recherche
int G_OFFSET_SEARCH; // Demi-longueur du bloc de recherche

const int G_VOISIN_X[9]   = {0, 1,  1,  0, -1, -1, -1, 0, 1};
const int G_VOISIN_Y[9]   = {0, 0, -1, -1, -1,  0,  1, 1, 1};

/* ===========================
            PROTOTYPES
   =========================== */

// =========================== PRINT
void afficheImage(const cv::Mat& src, std::string name);

// =========================== BLOCK-MATCHING
void ThreeStepSearch(cv::Mat& prevImg, cv::Mat& currImg, cv::Point px);
float SAD(const cv::Mat& prevImg, const cv::Mat& currImg, cv::Point ref, cv::Point test);

// =========================== UTILITIES
bool pixelIsValid(const cv::Mat& src, int x, int y);
bool areaIsValid(const cv::Mat& src, int x, int y);
void traceCarre(cv::Mat& src, cv::Point px, int size);

#endif