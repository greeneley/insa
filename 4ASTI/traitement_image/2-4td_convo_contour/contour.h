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
const float kMaskDerivation[] = {-1.0, 1.0 }; 

// Operateur de Robert
const float kMaskRobert1[]    = {1.0,  0.0,
                                 0.0, -1.0 };

const float kMaskRobert2[]    = { 0.0, 1.0,
                                 -1.0, 0.0 };

// Operateur de Prewitt
const float kMaskPrewitt1[]   = { -1.0, 0.0, 1.0,
                                  -1.0, 0.0, 1.0,
                                  -1.0, 0.0, 1.0 };

const float kMaskPrewitt2[]   = { -1.0, -1.0, -1.0,
                                   0.0,  0.0,  0.0,
                                   1.0,  1.0,  1.0 };

// OPerateur de Sobel
const float kMaskSobel1[]     = { -1.0, 0.0, 1.0,
                                  -2.0, 0.0, 2.0,
                                  -1.0, 0.0, 1.0 };

const float kMaskSobel2[]     = { -1.0, -2.0, -1.0,
                                   0.0,  0.0,  0.0,
                                   1.0,  2.0,  1.0 };


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

// === Operateurs
cv::Mat end_operateur(cv::Mat& src, cv::Mat& mask1, cv::Mat& mask2);
cv::Mat robert(cv::Mat& src);
cv::Mat prewitt(cv::Mat& src);
cv::Mat sobel(cv::Mat& src);

#endif