/* ===========================
            INCLUDES
   =========================== */

#include "noeud.h"

/* ===========================
           NAMESPACES
   =========================== */

using namespace std;

/* ===========================
        CLASS::HISTOGRAM
   =========================== */

// =========================== CONSTRUCTOR
Noeud::Noeud(Noeud* pere, int val, char label=NULL, Noeud* fg=NULL, Noeud* fd=NULL)
{
    m_pere  = pere;
    m_label = label,
    m_value = val;
    m_fg    = fg;
    m_fd    = fd;
}


// =========================== GETTERS

// =========================== SETTERS

// =========================== PRINT

// =========================== UTILITIES
