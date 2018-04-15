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
Noeud::Noeud(Noeud* pere, int val, char label='n', Noeud* fg=NULL, Noeud* fd=NULL)
{
    m_pere  = pere;
    m_label = label, // 'n' == noeud, autre == symbole associe
    m_value = val;   // poids numerique
    m_fg    = fg;    // fils gauche
    m_fd    = fd;    // fils droite
}


// =========================== GETTERS
char   Noeud::getLabel()      { return m_label; }
int    Noeud::getValue()      { return m_value; }
Noeud* Noeud::getFilsDroit()  { return m_fd;    }
Noeud* Noeud::getFilsGauche() { return m_fg;    }
Noeud* Noeud::getPere()       { return m_pere;  }

// =========================== SETTERS
void Noeud::setLabel(char newLabel)     { m_label = newLabel; }
void Noeud::setValue(int newVal)        { m_value = newVal;   }
void Noeud::setFilsDroit(Noeud* newFd)  { m_fd    = newFd;    }
void Noeud::setFilsGauche(Noeud* newFg) { m_fg    = newFg;    }
void Noeud::setPere(Noeud* newPere)     { m_pere  = newPere;  }

// =========================== PRINT
// =========================== UTILITIES
bool Noeud::hasFilsDroit()  { return m_fd   == NULL; }
bool Noeud::hasFilsGauche() { return m_fg   == NULL; }
bool Noeud::hasPere()       { return m_pere == NULL; }

