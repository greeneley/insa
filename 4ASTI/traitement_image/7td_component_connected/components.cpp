/* ===========================
			INCLUDES
   =========================== */

#include "components.h"

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
             << "fp_image:  Filepath to source image" << endl;
        return 0;
    }

    Mat image = imread(argv[1], CV_LOAD_IMAGE_GRAYSCALE);
    if(!image.data)
    {
        printf("No image data \n");
        return -1;
    }

    // === Init
    binarize(image);
    Mat dst = Mat(image.size().height, image.size().width, CV_8UC1);

    // === Algo
    labelize(src, dst);

    // === Print
    afficheImage(image, "Source");
    waitKey(0);


	return 0;
}

/* ===========================
			FONCTIONS
   =========================== */

// =========================== PRINT
void afficheImage(const Mat& src, string name)
{
    namedWindow(name, WINDOW_AUTOSIZE );
    imshow(name, src);
}

// =========================== CONNECTED-COMPONENT
void labelize(const cv::Mat& src, cv::Mat& dst)
{
	for(int y=1; y<(src.size().width-1); y++)
	{
		for(int x=0; x<(src.size().height-1); x++)
		{
			// TODO
		}
	}
}

void firstPass(const cv::Mat& src, cv::Mat& dst)
{
	//TODO
}
void secondPass(const cv::Mat& src, cv::Mat& dst);

// =========================== UTILITIES
void binarize(Mat& src)
{
	for(int y=0; y<src.size().width; y++)
		for(int x=0; x<src.size().height; x++)
			if((int)src.at<uchar>(x,y) < 128)
				src.at<uchar>(x,y) = 255;
			else src.at<uchar>(x, y) = 0;
}
