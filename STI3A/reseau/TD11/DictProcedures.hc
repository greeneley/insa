DictRecord dict[DICTSIZ];
int 	nwords =0;

int
initdict()
{
	printf("initdict\n");
	nwords=0;
	return 1;
}

int
insertw(record)
DictRecord record;
{
	int ch;
	printf("insertw\n");

	strcpy(dict[nwords].donnee,record.donnee);
	printf("Donnee inseree:%s\n",dict[nwords].donnee);
	dict[nwords].longueur=record.longueur;
	printf("Longueur donnee inseree:%d\n",dict[nwords].longueur);
	nwords++;
	return nwords;
}

int
deletew(record)
DictRecord record;
{
	printf("deletew\n");
	return 1;
}

int
lookupw(record)
DictRecord record;
{
	int i;
	
	printf("lookupw\n");
	for (i=0;i<nwords;i++)
		if (strcmp(record.donnee,dict[i].donnee) == 0)
			return 1;
	return 2;
}
