#ifndef _COMPONENTS_H_
#define _COMPONENTS_H_

/* ===========================
            CONSTANTES
   =========================== */

#include <stdio.h>
#include <highgui.h>
#include <opencv2/opencv.hpp>

/* ===========================
             GLOBAL
   =========================== */

int LABEL      = 1;
int INCR_LABEL = 1;
int FORMES     = 0;

const int kNbVoisins   = 8;
const int kVoisinsX[8] = {0, -1,  0, 1,  1, 1, -1, -1};
const int kVoisinsY[8] = {1,  0, -1, 0, -1, 1, -1,  1};

/* ===========================
            PROTOTYPES
   =========================== */

// =========================== PRINT
void afficheImage(const cv::Mat& src, std::string name);

// =========================== CONNECTED-COMPONENT
void labelize(cv::Mat& src);
void firstPass(cv::Mat& src);
void secondPass(cv::Mat& src);
void initNeighbourLabel(cv::Mat& src, int& labelA, int& labelB);
void propagateLeftLabel(cv::Mat& src, int oldLabel, int newLabel, int x, int y);
void propagateUpLabel(cv::Mat& src, int oldLabel, int newLabel, int x, int y);

// =========================== CONNECTED-COMPONENT
void labelizeRecursive(cv::Mat& src);
void connectedComponentRecursive(cv::Mat& src, int x, int y, int label);

// =========================== UTILITIES
void binarizeFondBlanc(cv::Mat& src);
void copyMatNorm(const cv::Mat& src, cv::Mat& dst);

#endif