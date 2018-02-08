#ifndef _MAIN_H_
#define _MAIN_H_

/* ===========================
            CONSTANTES
   =========================== */

// Image test
const char* kFilepath = "../ressources/cat.jpg";

// Naming
const unsigned int kBlue  = 0;
const unsigned int kGreen = 1;
const unsigned int kRed   = 2;

// Histo
const unsigned int kMaxIntensity = 256;
const unsigned int kHistHeight   = 512;
const unsigned int kHistWidth    = 512;
const double       kPaddingBGR   = (double)kHistHeight / (double)kMaxIntensity;
const unsigned int kBarWidth     = 5;
const unsigned int kBarPadding   = 2;



#endif