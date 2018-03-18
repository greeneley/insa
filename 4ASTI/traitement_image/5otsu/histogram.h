#ifndef _HISTOGRAM_H_
#define _HISTOGRAM_H_

#include <stdio.h>
#include <highgui.h>
#include <opencv2/opencv.hpp>

/* ===========================
            CONSTANTES
   =========================== */

const int kMaxIntensity(256);
const unsigned int kHistHeight   = 512;
const unsigned int kHistWidth    = 512;
const float        kPaddingBGR   = (float)kHistHeight / 255.0;
const unsigned int kBarWidth     = 5;
const unsigned int kBarPadding   = 2;

/* ===========================
            HISTOGRAM
   =========================== */
class Histogram
{
	int m_histo[256] = {0};
	int m_size       = 0;
	int m_width      = 0;
	int m_height     = 0;
	int m_seuil      = 0;

	public:
		// =================== CONSTRUCTOR
		Histogram(const cv::Mat& src);

		// =================== GETTERS
		int* getHisto();
		int getSize() const;
		int getWidth() const;
		int getHeight() const;
		int getSeuil() const;

		// =================== SETTERS
		void setSeuil(int k);

		// =================== PRINT
		void draw(int k);

		void printHistogram() const;

		// =================== UTILITIES
		int getMax() const;
};

#endif