#include <stdio.h>
#include <mqueue.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <string.h>
#include <errno.h>



/* name of the POSIX object referencing the queue */
#define MSGQOBJ_NAME    "/bal"
/* max length of a message (just for this process) */
#define MAX_MSG_LEN     8000

void usage (char *nom)
	{
printf ("Tapez  %s 1 \t\t\t\tpour créer la file de messages \n",nom);
printf ("Tapez  %s 2 \"Message\" priority[0..32]  pour déposer un message \n",nom);
printf ("Tapez  %s 3 \t\t\t\tpour lire un message \n",nom);
printf ("Tapez  %s 4 \t\t\t\tpour effacer la file de messages si elle est vide \n",nom);
exit(1);
	} 

mqd_t open_bal(void)
	{
mqd_t msgq_id;
		 	
	msgq_id = mq_open(MSGQOBJ_NAME, O_RDWR);
   			 if (msgq_id == (mqd_t)-1) {
       				 perror("In mq_open()");
        			exit(1);}
return (msgq_id);
}
int main(int argc, char *argv[]) {
    mqd_t msgq_id;
    char msgcontent[MAX_MSG_LEN];
    int msgsz;
    unsigned int sender;
    struct mq_attr msgq_attr;
if (argc==1) usage(argv[0]); 
 switch(atoi(argv[1])) {
                case 1: // create mqueue
			msgq_attr.mq_maxmsg=9;// Attention valeur maxi dans variables noyau=10 par défaut
					      // modif variable noyau sysctl -w fs.mqueue.msg_max=40
					      // attention limite par HARD_MSGMAX=50 
			msgq_attr.mq_flags=O_NONBLOCK;
			msgq_attr.mq_msgsize=MAX_MSG_LEN;
			msgq_attr.mq_curmsgs=0;
    			msgq_id = mq_open(MSGQOBJ_NAME, O_RDWR|O_CREAT|O_EXCL,0600,&msgq_attr);
    		//	msgq_id = mq_open(MSGQOBJ_NAME, O_RDWR|O_CREAT|O_EXCL,0600,NULL);
			printf("Valeur de sortie de mq_open %i\n\t numero error : %i\n",msgq_id,errno);
		if (errno==EEXIST)
		{
			printf("La file de messages %s existe déjà !!!!\n",MSGQOBJ_NAME);
		 	msgq_id = mq_open(MSGQOBJ_NAME, O_RDWR);
		}
   			 if (msgq_id == (mqd_t)-1) {
       				 perror("In mq_open()");
        			exit(1);}
printf("OK\n");
                        break;
		case 2: // send message
			if (argc==4)
			{
			msgq_id=open_bal();
			sender=atoi(argv[3]);
			msgsz=mq_send(msgq_id,argv[2],strlen(argv[2]),sender);
			if (msgsz==0)printf ("Le message a été déposé \n");
			else printf("Le message n'a pas été déposé !!!\n");
			}
			else usage(argv[0]);
			break;
			
                case 3:
			msgq_id=open_bal();
    			mq_getattr(msgq_id, &msgq_attr);
    printf("File \"%s\":\n\t- capacité maxi %ld messages\n\t- longueur maxi %ld \n\t- message courant %ld \n", MSGQOBJ_NAME, msgq_attr.mq_maxmsg, msgq_attr.mq_msgsize, msgq_attr.mq_curmsgs);
    			//getting a message 
			if ( msgq_attr.mq_curmsgs!=0) {

    			msgsz = mq_receive(msgq_id, msgcontent, MAX_MSG_LEN, &sender);
    			if (msgsz == -1) {
       			 perror("In mq_receive()");
       			 exit(1);
    			}
    			printf("Reçu le message (%d bytes) priorité %d: %s\n", msgsz, sender, msgcontent);
							}
			else printf ("Pas de message dans la file !!!\n");
    			mq_close(msgq_id);
			break;

		case 4:
			msgq_id=open_bal();
    			mq_getattr(msgq_id, &msgq_attr);

			if ( msgq_attr.mq_curmsgs==0) {
			printf("destruction\n");			 
			mq_close(msgq_id);
			mq_unlink(MSGQOBJ_NAME);
			return(1);
				}else
			printf ("La file de messages n'est pas vide. Effacement refusé !! \n");
			break;
		default: printf("Argument(s) non supporté(s) !!!\n");
			return(1);
  } 
    
    return 0;
}
