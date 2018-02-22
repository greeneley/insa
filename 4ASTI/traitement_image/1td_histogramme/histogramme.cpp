/*
    Source : https://docs.opencv.org/2.4/doc/tutorials/introduction/linux_gcc_cmake/linux_gcc_cmake.html
 */


/* ===========================
            INCLUDES
   =========================== */

#include <stdio.h>
#include <highgui.h>
#include <opencv2/opencv.hpp>

#include "histogramme.h"

/* ===========================
           NAMESPACES
   =========================== */

using namespace cv;
using namespace std;

/* ===========================
            FONCTIONS
   =========================== */

void affiche_colors(Mat image, int x, int y)
{
    // Affichage d'une valeur de pixel
    int a = image.at<uchar>(x,y);
    cout << "a     : " << a << endl;


    // Affichage des valeurs BGR avec Vec3b
    Vec3b intensity = image.at<Vec3b>(x, y);
    int blue        = intensity.val[0];
    int green       = intensity.val[1];
    int red         = intensity.val[2];

    cout << "blue  : " << blue  << endl;
    cout << "green : " << green << endl;
    cout << "red   : " << red   << endl;

    cout << intensity << endl;
}

void histogram(Mat image, bool colored)
{
    if(colored)
    {
        // BGR tab
        int colors[3][kMaxIntensity] = {{0}, {0}, {0}};

        // Recuperation des intensites
        Vec3b intensity;
        for(int x=0; x<image.size().width; x++)
        {
            for(int y=0; y<image.size().height; y++)
            {
                intensity = image.at<Vec3b>(y, x);

                colors[kBlue][intensity.val[0]] += 1;
                colors[kGreen][intensity.val[1]] += 1;
                colors[kRed][intensity.val[2]] += 1;
            }
        }

        // Normalisation
        int max = 0;
        for(int j=0; j<kMaxIntensity; j++)
        {
            for(int i=0; i<3; i++)
            {
                if(colors[i][j] > max)
                {
                    max = colors[i][j];
                }
            }
        }
        for(int j=0; j<kMaxIntensity; j++)
        {
            for(int i=0; i<3; i++)
            {
                colors[i][j] = ((double)colors[i][j]/(double)max) * kHistHeight;
            }
        }

        // Tracer
        Mat histImage(kHistHeight, kHistWidth, CV_8UC3, Scalar( 0,0,0));
        for(int i=1; i<kMaxIntensity; i++)
        {
            line( histImage, Point((i-1)*kPaddingBGR, kHistHeight - colors[kBlue][i-1]),
                    Point(i*kPaddingBGR, kHistHeight - colors[kBlue][i]),
                    Scalar( 255, 0, 0));
            line( histImage, Point((i-1)*kPaddingBGR, kHistHeight - colors[kGreen][i-1]),
                    Point(i*kPaddingBGR, kHistHeight - colors[kGreen][i]),
                    Scalar( 0, 255, 0));
            line( histImage, Point((i-1)*kPaddingBGR, kHistHeight - colors[kRed][i-1]),
                    Point(i*kPaddingBGR, kHistHeight - colors[kRed][i]),
                    Scalar( 0, 0, 255));
        }

        namedWindow("Histogramme couleur", CV_WINDOW_AUTOSIZE );
        imshow("Histogramme couleur", histImage );
        
    }
    else
    {
        // Grayscale tab
        int grays[256] = {0};

        // Recuperation des intensites
        for(int x=0; x<image.size().width; x++)
        {
            for (int y=0;  y<image.size().height; y++)
            {
                grays[image.at<uchar>(y,x)] += 1;
            }
        }

        // Normalisation
        int max = 0;
        for(int i=0; i<kMaxIntensity; i++)
        {
            if(max < grays[i])
            {
                max = grays[i];
            }
        }
        for(int i=0; i<kMaxIntensity; i++)
        {
            grays[i] = ((double)grays[i]/(double)max) * kHistHeight;
        }

        // Tracer
        Mat histImage(kHistHeight, kMaxIntensity*kBarWidth, CV_8UC1, Scalar(0));

        rectangle( histImage, Point(0, kHistHeight),
                Point(kBarWidth, kHistHeight - 255), Scalar(255), CV_FILLED);
        for(int i=2; i<kMaxIntensity; i++)
        {
            rectangle( histImage, Point((i-1)*kBarWidth + kBarPadding, kHistHeight),
                        Point(i*kBarWidth, kHistHeight - grays[i]), Scalar(255), CV_FILLED);
        }

        namedWindow("Histogramme couleur", CV_WINDOW_AUTOSIZE );
        imshow("Histogramme couleur", histImage );

    }
    
}

/* ===========================
              MAIN
   =========================== */
int main(int argc, char** argv )
{
    Mat image;

    // 1 = couleur = CV_LOAD_IMAGE_COLOR, 0 = gris = CV_LOAD_IMAGE_GRAYSCALE
    //image = imread( kFilepath, CV_LOAD_IMAGE_COLOR );
    image = imread(kFilepath, CV_LOAD_IMAGE_GRAYSCALE);

    if ( !image.data )
    {
        printf("No image data \n");
        return -1;
    }

    //affiche_colors(image, 50, 50);
    histogram(image, false);

    namedWindow("Display Image", WINDOW_AUTOSIZE );
    imshow("Display Image", image);

    waitKey(0);

    return 0;
}