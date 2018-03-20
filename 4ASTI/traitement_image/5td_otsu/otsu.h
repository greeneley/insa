#ifndef _OTSU_H_
#define _OTSU_H_


#include <stdio.h>
#include <highgui.h>
#include <opencv2/opencv.hpp>

/* ===========================
            CONSTANTES
   =========================== */

/* ===========================
            PROTOTYPES
   =========================== */

// =========================== OTSU
int     otsu(const int* h);
float   meanClass(const int* h, int begin, int stop);
float   varianceClass(const int* h, int begin, int stop);

cv::Mat segment(cv::Mat& src, int seuil);


// =========================== PRINT
void afficheImage(const cv::Mat& src, const cv::String name);

// =========================== UTILITIES
template <class T>
void printArray(T* src);

#endif
