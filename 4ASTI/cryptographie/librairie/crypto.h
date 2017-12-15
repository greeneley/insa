/* ==================== */
/* ==== STRUCTURES ==== */
/* ==================== */
typedef struct TableauInt
{
	int *tab;
	int taille;
	int pos;
} TableauInt;


TableauInt* init_TableauInt(int taille);
void add_TableauInt(TableauInt* tableau, int valeur);
void free_TableauInt(TableauInt* tab);

/*
 *  Chiffrement utilisant le ou exclusif
 */
void xor_crypt(char* key, char* texte, char* chiffre);

/*
 *  Dechiffrement utilisant le ou exclusif
 */
void xor_decrypt(char* key, char* chiffre, char* clair);

/*
 *  Chiffrement utilisant cesar
 */
void cesar_crypt(int decalage, char* texte, char* chiffre);

/*
 *  Dechiffrement utilisant cesar
 */
void cesar_decrypt(int decalage, char* chiffre, char* clair);

/*
 *  Chiffrement utilisant vigenere
 */
void vigenere_crypt(char* key, char* texte, char* chiffre);

/*
 *  Dechiffrement utilisant vigenere
 */
void vigenere_decrypt(char* key, char* chiffre, char* clair);
