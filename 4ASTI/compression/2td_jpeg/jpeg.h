#ifndef _JPEG_H_
#define _JPEG_H_

#include <stdio.h>
#include <string>
#include <highgui.h>
#include <opencv2/opencv.hpp>
#include "noeud.h"

/* ===========================
            CONSTANTES
   =========================== */
const int G_BLOCK_SIZE = 8;

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

const std::string G_COMPRESSED_FILE = "out.bin";

const std::string G_HEX[16] = {"0","1","2","3","4","5","6","7",
                   "8","9","A","B","C","D","E","F"};

/* ===========================
            PROTOTYPES
   =========================== */

// =========================== JPEG
void compress_write_jpeg(const cv::Mat& src);

// =========================== UTILITIES
cv::Mat normalize_size(const cv::Mat& src);
void    print_taux(const std::string src, const std::string out);

template <class T>
void affiche_array(T* tab, int taille);

// =========================== BLOCK
cv::Mat get_block(const cv::Mat& src, int x, int y);
void    center_zero(cv::Mat& src);
void    quantify_block(cv::Mat& block_src);

// =========================== ZIGZAG
void zigzag_block_write(const cv::Mat& src, int* dst);
void zigzag_diagonal_down_to_col(const cv::Mat& src, int* dst, int col, int& index, int& x, int& y);
void zigzag_diagonal_down_to_row(const cv::Mat& src, int* dst, int row, int& index, int& x, int& y);
void zigzag_diagonal_up_to_col(const cv::Mat& src, int* dst, int col, int& index, int& x, int& y);
void zigzag_diagonal_up_to_row(const cv::Mat& src, int* dst, int row, int& index, int& x, int& y);

// =========================== RLE
std::string         rle_block          (int* src);
std::vector<Noeud*> rle_block_to_vector(std::string src);

// =========================== HUFFMAN
void huffman_to_file(std::string src);
void huffman_create_tree_recursive(std::vector<Noeud*>& v);
void huffman_tree_to_binary_map_recursive(std::map<char, std::string>& m, Noeud* v, std::string binary);
void huffman_get_mins(std::vector<Noeud*>& v, uint mins[2]);
void huffman_fuse_min_nodes(std::vector<Noeud*>& v, uint mins[2]);
void huffman_write_binary(std::map<char, std::string>& m, std::string chaine);
void huffman_free_memory(std::vector<Noeud*>& v);

#endif
