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

int LABEL              = 1;
int VALEUR_GRIS        = 20;
int INCR_VALEUR_GRIS   = 1;
int FORMES             = 1;

const int kNbVoisinsIter   = 4;
const int kVoisinsXIter[4] = {0, -1, -1, -1};
const int kVoisinsYIter[4] = {-1, -1, 0, +1};

const int kNbVoisinsRecur   = 8;
const int kVoisinsXRecur[8] = {0, -1,  0, 1,  1, 1, -1, -1};
const int kVoisinsYRecur[8] = {1,  0, -1, 0, -1, 1, -1,  1};

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
void changeParents(cv::Mat& src, int x, int y, int min, std::map<int, int>& valeurLabel);

// =========================== CONNECTED-COMPONENT RECURSIVE
void labelizeRecur(cv::Mat& src);
void connectedComponentRecur(cv::Mat& src, int x, int y, int label);

// =========================== UTILITIES
void binarizeFondBlanc(cv::Mat& src);
void binarizeFondNoir(cv::Mat& src);
bool inImage(cv::Mat& src, int x, int y);

#endif