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
	int     m_value;
	std::string  m_label;

	public:
		// =================== CONSTRUCTOR
		Noeud(Noeud* pere, int val, std::string label, Noeud* fg, Noeud* fd);

		// =================== GETTERS
		// =================== SETTERS
		// =================== PRINT
		// =================== UTILITIES
		int getMax() const;
};

#endif