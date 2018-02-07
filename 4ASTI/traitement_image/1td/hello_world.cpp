/*
    Source : https://docs.opencv.org/2.4/doc/tutorials/introduction/linux_gcc_cmake/linux_gcc_cmake.html
    Compilation : pkg-config opencv --cflags --libs`
 */


/* ===========================
            INCLUDES
   =========================== */

#include <stdio.h>
#include <highgui.h>
#include <opencv2/opencv.hpp>


/* ===========================
           NAMESPACES
   =========================== */


/* ===========================
            FONCTIONS
   =========================== */

void affiche_colors(cv::Mat image)
{
    // Affichage d'une valeur 
    int a = image.at<uchar>(50,100);
    std::cout << "a     : " << a << std::endl;


    // Affichage des valeurs BGR avec Vec3b
    cv::Vec3b intensity = image.at<cv::Vec3b>(50, 50);
    int blue            = intensity.val[0];
    int green           = intensity.val[1];
    int red             = intensity.val[2];

    std::cout << "blue  : " << blue  << std::endl;
    std::cout << "green : " << green << std::endl;
    std::cout << "red   : " << red   << std::endl;

    std::cout << intensity << std::endl;
}

void histogram(cv::Mat image, int color)
{
    // Init array + zero all
    uchar tab[256] = {0};

    for (int x=0; x<image.size().width; x++)
    {
        for (int y=0;  y<image.size().height; y++)
        {
            tab[image.at<uchar>(x,y)] += 1;
        }
    }
}

/* ===========================
              MAIN
   =========================== */
int main(int argc, char** argv )
{
    if ( argc != 2 )
    {
        printf("usage: DisplayImage.out <Image_Path>\n");
        return -1;
    }

    cv::Mat image;

    // 1 = couleur = CV_LOAD_IMAGE_COLOR, 0 = gris = CV_LOAD_IMAGE_GRAYSCALE
    //image = cv::imread( argv[1], CV_LOAD_IMAGE_COLOR );
    image = cv::imread(argv[1], CV_LOAD_IMAGE_GRAYSCALE);

    if ( !image.data )
    {
        printf("No image data \n");
        return -1;
    }

    //affiche_colors(image);
    histogram(image, 0);

    cv::namedWindow("Display Image", cv::WINDOW_AUTOSIZE );
    cv::imshow("Display Image", image);

    cv::waitKey(0);

    return 0;
}