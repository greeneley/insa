#ifndef _GROWING_REGIONS_H_
#define _GROWING_REGIONS_H_

#include <stdio.h>
#include <highgui.h>
#include <opencv2/opencv.hpp>

/* ===========================
            CONSTANTES
   =========================== */
const int kGauche         = 0;
const int kHaut           = 1;
const int kDroite         = 2;
const int kBas            = 3;
const int kTousLesVoisins = 4;

const int kMvtX[4] = { 0, -1, 0, 1};
const int kMvtY[4] = {-1,  0, 1, 0};


/* ===========================
            PROTOTYPES
   =========================== */

// =========================== PRINT
void afficheImage(const cv::Mat& src, cv::String name);

// =========================== GROWING-REGION
void growing(const cv::Mat& src, cv::Mat& dst, int x, int y, float& sum, int& n, float& threshold);
void update(float value, float& sum, int& n, float& threshold);

// =========================== BOOLEAN
bool inImage(int x, int y, int sizeX, int sizeY);
bool inArea(const cv::Mat& src, int x, int y, float mean, float threshold);
bool isMarked(cv::Mat& dst, int x, int y);
bool isValid(cv::Mat& dst, int x, int y, int sizeX, int sizeY);

#endif