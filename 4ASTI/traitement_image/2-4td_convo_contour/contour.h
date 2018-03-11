#ifndef _CONTOUR_H_
#define _CONTOUR_H_


#include <stdio.h>
#include <highgui.h>
#include <opencv2/opencv.hpp>

/* ===========================
            CONSTANTES
   =========================== */

// Masque
const float kMoyenneur = 1.0;
//
const float kMask1[]    = { -1.0, 0.0, 1.0,
						    -1.0, 0.0, 1.0,
						    -1.0, 0.0, 1.0 };
//
const float kMask2[]    = { -1.0, -1.0, -1.0,
                             0.0,  0.0,  1.0,
                             1.0,  1.0,  1.0 };
//
const float kMask3[]    = {-1.0, 1.0};
const float kMask4[]    = {-1.0, 1.0};                             


/* ===========================
            PROTOTYPES
   =========================== */

// === Mask
void init_mask_float(cv::Mat& mask, const float values[]);

// === Convolution
cv::Mat convolution_float(cv::Mat& src, cv::Mat& mask);
void calc_conv_grey_float(cv::Mat& src, cv::Mat& dst, cv::Mat& mask, int x, int y);

// === Misc
void normalize_float(cv::Mat& src);
cv::Mat square_float(cv::Mat& src);
cv::Mat module_gradient_float(cv::Mat& src, cv::Mat& h1, cv::Mat& h2);

// === Print
void affiche_image(cv::Mat& src, cv::String name);


#endif