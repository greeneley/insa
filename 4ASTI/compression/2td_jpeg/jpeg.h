#ifndef _JPEG_H_
#define _JPEG_H_

#include <stdio.h>
#include <string>
#include <highgui.h>
#include <opencv2/opencv.hpp>

/* ===========================
            CONSTANTES
   =========================== */
const int kBlockSize = 8;

const float kCoeff[64] = {
  16.0, 11.0, 10.0, 16.0,  24.0,  40.0,  51.0,  61.0,
  12.0, 12.0, 14.0, 19.0,  26.0,  58.0,  60.0,  55.0,
  14.0, 13.0, 16.0, 24.0,  40.0,  57.0,  69.0,  56.0,
  14.0, 17.0, 22.0, 29.0,  51.0,  87.0,  80.0,  62.0,
  16.0, 22.0, 37.0, 56.0,  68.0, 109.0, 103.0,  77.0,
  24.0, 35.0, 55.0, 64.0,  81.0, 104.0, 113.0,  92.0,
  49.0, 64.0, 78.0, 87.0, 103.0, 121.0, 120.0, 101.0,
  72.0, 92.0, 95.0, 98.0, 112.0, 100.0, 103.0,  99.0
};


/* ===========================
            PROTOTYPES
   =========================== */

// =========================== JPEG
void compress_write_jpeg(const cv::Mat& src);

// =========================== UTILITIES
cv::Mat normalize_size_8x8(const cv::Mat& src);

template <class T>
void affiche_array(T* tab, int taille);

// =========================== BLOCK
cv::Mat get_block(const cv::Mat& src, const int x, const int y);
void    center_zero(cv::Mat& src);
void    quantify_block(cv::Mat& block_src);

// =========================== ZIGZAG
void zigzag_block_write(const cv::Mat& src, int* dst);
void zigzag_diagonal_down_to_col(const cv::Mat& src, int* dst, const int col, int& index, int& x, int& y);
void zigzag_diagonal_down_to_row(const cv::Mat& src, int* dst, const int row, int& index, int& x, int& y);
void zigzag_diagonal_up_to_col(const cv::Mat& src, int* dst, const int col, int& index, int& x, int& y);
void zigzag_diagonal_up_to_row(const cv::Mat& src, int* dst, const int row, int& index, int& x, int& y);

// =========================== RLE
std::string rle_block(int* src);

// =========================== HUFFMAN



#endif
