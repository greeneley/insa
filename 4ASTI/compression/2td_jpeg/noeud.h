#ifndef _NOEUD_H_
#define _NOEUD_H_

#include <string>
#include <stdlib.h>

/* ===========================
            CONSTANTES
   =========================== */

/* ===========================
            CLASS::NOEUD
   =========================== */
class Noeud
{
	Noeud*  m_pere;
	Noeud*  m_fg;
	Noeud*  m_fd;

	public:
	int     m_value;
	std::string  m_label;
		// =================== CONSTRUCTOR
		Noeud(Noeud* pere, int val, char label, Noeud* fg, Noeud* fd);

		// =================== GETTERS
		// =================== SETTERS
		// =================== PRINT
		// =================== UTILITIES
		int getMax() const;
};

#endif