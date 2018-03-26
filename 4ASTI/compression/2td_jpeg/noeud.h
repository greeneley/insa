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
	Noeud* m_pere;
	Noeud* m_fg;
	Noeud* m_fd;
	int    m_value;
	char   m_label;

	public:
		// =================== CONSTRUCTOR
		Noeud(Noeud* pere, int val, char label, Noeud* fg, Noeud* fd);

		// =================== GETTERS
		char   getLabel();
		int    getValue();
		Noeud* getFilsDroit();
		Noeud* getFilsGauche();
		Noeud* getPere();

		// =================== SETTERS
		void setLabel(char newLabel);
		void setValue(int newVal);
		void setFilsDroit(Noeud* newFd);
		void setFilsGauche(Noeud* newFg);
		void setPere(Noeud* newPere);

		// =================== PRINT
		// =================== UTILITIES
		bool hasFilsDroit();
		bool hasFilsGauche();
		bool hasPere();
};

#endif