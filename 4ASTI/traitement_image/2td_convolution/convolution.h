#ifndef _CONVOLUTION_H_
#define _CONVOLUTION_H_

/* ===========================
            CONSTANTES
   =========================== */

// Masque
const int   kMaskRows  = 3;
const int   kMaskCols  = 3;
const float kMoyenneur = 10.0;
const float kMask[]    = { 1.0, 1.0, 1.0,
						   1.0, 2.0, 1.0,
						   1.0, 1.0, 1.0 };

#endif
