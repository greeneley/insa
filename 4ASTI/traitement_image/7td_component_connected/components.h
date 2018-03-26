#ifndef _COMPONENTS_H_
#define _COMPONENTS_H_

/* ===========================
            CONSTANTES
   =========================== */

#include <stdio.h>
#include <highgui.h>
#include <opencv2/opencv.hpp>

/* ===========================
            CONSTANTES
   =========================== */


/* ===========================
            PROTOTYPES
   =========================== */

// =========================== PRINT
void afficheImage(const cv::Mat& src, std::string name);

// =========================== CONNECTED-COMPONENT
void labelize(const cv::Mat& src, cv::Mat& dst);
void firstPass(const cv::Mat& src, cv::Mat& dst);
void secondPass(const cv::Mat& src, cv::Mat& dst);

// =========================== UTILITIES
void binarize(cv::Mat& src);

#endif