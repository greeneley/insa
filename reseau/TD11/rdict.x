
const MAXSTRING=255;
const MAXSTRING1=256;
const DICTSIZ=100;

struct DictRecord{
	int longueur;
	char donnee[MAXSTRING1];
};

program RDICTPROG {
	version RDICTVERS {
		int 	INITDICT() = 1;
		int 	INSERTW(DictRecord) = 2;
		int 	DELETEW(DictRecord) = 3;
		int 	LOOKUPW(DictRecord) = 4;
	} =1;
} = 0x30090949;
