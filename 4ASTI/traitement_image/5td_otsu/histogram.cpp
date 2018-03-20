/* ===========================
            INCLUDES
   =========================== */

#include "histogram.h"

/* ===========================
           NAMESPACES
   =========================== */

using namespace cv;
using namespace std;

/* ===========================
        CLASS::HISTOGRAM
   =========================== */

// =========================== CONSTRUCTOR
Histogram::Histogram(const Mat& src)
{
	m_height = src.size().height;
	m_width  = src.size().width;
	m_seuil  = 0;

	int curr(0);
    for(int y=0; y<src.size().width; y++)
    {
        for(int x=0; x<src.size().height; x++)
        {
            curr = (int)src.at<uchar>(x,y);
            m_histo[curr]++;
            m_size++;
        }
    }
}


// =========================== GETTERS
int* Histogram::getHisto()
{
	return m_histo;
}

int Histogram::getSize() const
{
	return m_size;
}

int Histogram::getWidth() const
{
	return m_width;
}

int Histogram::getHeight() const
{
	return m_height;
}

int Histogram::getSeuil() const
{
	return m_seuil;
}

// =========================== SETTERS
void Histogram::setSeuil(int k)
{
	m_seuil = k;
}


// =========================== PRINT
void Histogram::draw(int k=-1)
{
    Mat   histImage(kHistHeight, kMaxIntensity*kBarWidth, CV_8UC1, Scalar(0));
    int   max = getMax();
    float value(0.0);

    for(int i=1; i<kMaxIntensity; i++)
    {
    	value = (float)m_histo[i] / (float)max * 255.0;
        rectangle( histImage, Point((i-1)*kBarWidth + kBarPadding, kHistHeight),
                    Point(i*kBarWidth, kHistHeight - value), Scalar(255), CV_FILLED);
    }

    if(k)
    {
    	value = (float)m_histo[k] / (float)max * 255.0;
        rectangle( histImage, Point((k-1)*kBarWidth + kBarPadding, kHistHeight),
                    Point(k*kBarWidth, kHistHeight - value), Scalar(126), CV_FILLED);
    }

    namedWindow("greyscale", CV_WINDOW_AUTOSIZE );
    imshow("greyscale", histImage );
    waitKey(0);
}

void Histogram::printHistogram() const
{
    for(int i=0; i<kMaxIntensity; i++)
        cout << m_histo[i] << " ";
    cout << endl;
}


// =========================== UTILITIES
int Histogram::getMax() const
{
	int max(-1);
    for(int i=0; i<kMaxIntensity; i++)
        if(max < m_histo[i])
            max = m_histo[i];

    return max;
}

