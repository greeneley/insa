/* ===========================
            INCLUDES
   =========================== */

#include "otsu.h"
#include "histogram.h"

/* ===========================
           NAMESPACES
   =========================== */

using namespace cv;
using namespace std;

/* ===========================
              MAIN 
   =========================== */

int main(int argc, char const *argv[])
{
    if(argc < 2)
    {
        cout << "Usage: ./app fp_image" << endl
             << "fp_image: Filepath to source image" << endl;
        return 0;
    }

    Mat image = imread(argv[1], CV_LOAD_IMAGE_GRAYSCALE);
    if(!image.data)
    {
        printf("No image data \n");
        return -1;
    }

    // === Init histogramme 
    Histogram histo(image);

    // === K optimal
    int k = otsu(histo.getHisto());
    cout << "K optimal : " << k << endl;

    // === Nouvelle image
    Mat out = segment(image, k);

    // === Print
    afficheImage(image, "source");
    afficheImage(out, "otsu");
    histo.draw(k);

    waitKey(0);

    return 0;
}


/* ===========================
            FONCTIONS
   =========================== */

// =========================== OTSU
int otsu(const int* h)
{
    int optiK(1);

    float var1(0.0);
    float var2(0.0);
    float curr = varianceClass(h, 0, 1) + varianceClass(h, 1, kMaxIntensity);
    for(int k=2; k<kMaxIntensity; k++)
    {
        var1 = varianceClass(h, 0, k);
        var2 = varianceClass(h, k, kMaxIntensity);
        if((var1+var2) < curr)
        {
            curr  = var1+var2;
            optiK = k;
        }
    }

    return optiK;
}

float meanClass(const int* h, int begin, int stop)
{
    float mean(0.0);

    float sum(0.0);
    for(int i=begin; i<stop; i++)
    {
        sum  += (float)h[i];
        mean += (float)(h[i] * i);
    }

    if(mean>0)
        mean /= sum;

    return mean;
}

float varianceClass(const int* h, int begin, int stop)
{
    float variance(0.0);

    float mean = meanClass(h, begin, stop);
    for(int i=begin; i<stop; i++)
    {
        variance += pow(i-mean, 2) * (float)h[i];
    }
    return variance;
}

Mat segment(Mat& src, int seuil)
{
    Mat dst = Mat(src.size().height, src.size().width, CV_8UC1);

    for(int y=0; y<src.size().width; y++)
    {
        for(int x=0; x<src.size().height; x++)
        {
            if((int)src.at<uchar>(x,y) < seuil)
                dst.at<uchar>(x,y) = 0;
            else
                dst.at<uchar>(x,y) = 255;
        }
    }

    return dst;
}


// =========================== PRINT
void afficheImage(const Mat& src, String name)
{
    namedWindow(name, WINDOW_AUTOSIZE );
    imshow(name, src);
}

// =========================== UTILITIES
template <class T>
void printArray(T* src)
{
    for(int i=0; i<256; i++)
        cout << src[i] << " ";
    cout << endl;
}
