#ifndef _CONTOUR_H_
#define _CONTOUR_H_

/* ===========================
            CONSTANTES
   =========================== */

// Masque
const float kMoyenneur = 1.0;

//
const float kMask1[]    = { -1.0, 0.0, 1.0,
						    -1.0, 0.0, 1.0,
						    -1.0, 0.0, 1.0 };
//
const float kMask2[]    = { -1.0, -1.0, -1.0,
                             0.0,  0.0,  1.0,
                             1.0,  1.0,  1.0 };
//
const float kMask3[]    = {1.0, -1.0};
const float kMask4[]    = {-1.0, 1.0};                             
#endif
