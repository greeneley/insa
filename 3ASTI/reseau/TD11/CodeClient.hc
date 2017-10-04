

DictRecord record;
char cmd;
int wrdlen;
int ret;
int replay=0;



int fonctionClient()
{

        while (1) {

                if (!replay) 
                {
                wrdlen=nextin(&cmd, &record);
                if (wrdlen < 0)
                        exit(0);
                };

                switch (cmd) {
                case 'I':
                        ret=initdict();
			if (ret==0) 
			{ 
				printf("Exception systeme\n");
				return 0;
			}
			if (ret!=0) 
			{ 
                        	printf("Dictionnaire initialise\n");
				break;
			}
			replay=0;
                        break;
                case 'i':
                        ret=insertw(record);
			if (ret==0) 
			{ 
				printf("Exception systeme\n");
				return 0;
			}
			if (ret==1)
			{ 
                        	printf("%s insere\n",record.donnee);
                        	break;
			}
                        printf("Exception %s non insere\n",record.donnee);
			replay=0;
			break;

                case 'd':
                        ret=deletew(record);
			if (ret==0) 
			{ 
				printf("Exception systeme\n");
				return 0;
			}
			if (ret==1)
			{ 
                        	printf("%s supprime\n",record.donnee);
                        	break;
			}
                        printf("Exception %s non supprime\n",record.donnee);
			replay=0;
                        break;
                case 'l':
                        ret=lookupw(record);
			if (ret==0) 
			{ 
				printf("Exception systeme\n");
				return 0;
			}
			if (ret==1)
			{ 
                                printf("%s trouve\n",record.donnee);
                        	break;
			}
                        printf("%s non trouve\n",record.donnee);
			replay=0;
                        break;
                case 'q':
                        printf("sortie programme\n");
                        exit(0);
                default:
			replay=0;
                        printf("commande %c invalide\n",cmd);
                }
        }


}

int
nextin(char *cmd,DictRecord *record)
{
        int i,ch;

        ch=getc(stdin);
        while (ch == ' ') ch=getc(stdin);
        if (ch==EOF) return -1;
        *cmd= (char) ch;
        ch=getc(stdin);
        while (ch == ' ') ch=getc(stdin);
        if (ch==EOF) return -1;
        if (ch=='\n') return 0;

        i=0;
        while ((ch != ' ') && (ch != '\n')) {
                if (++i > MAXSTRING-1) {
                        printf("Mot trop long\n");
                        exit(1);
                };
                record->donnee[i-1] = ch;
                ch =getc(stdin);
        };
        ch='\0';
        record->donnee[i] = ch;
        record->longueur=i;


        printf("Fin saisie\n");
        return i;
}

