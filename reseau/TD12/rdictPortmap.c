/**/
#include <rpc/rpc.h>
#include <signal.h>
/**/
#include <stdio.h>
#include <ctype.h>

/**/
#include "rdict.h"

#define	RMACHINE	"localhost"
#define	PROTOCOL	"udp"
CLIENT	*handle;
/**/

#include "CodeClient.hc"

/**/
Rupture()
{
        printf("Rupture connexion\n");
	signal(SIGPIPE,Rupture);
	handle=0;
	while (handle==0)
	{
	handle = clnt_create(RMACHINE, RDICTPROG, RDICTVERS, PROTOCOL);
		printf("Impossible contacter programme distant\n");
		sleep(1);
	};
}

/**/
int
main(argc,argv)
int argc;
char *argv[];
{

	/**/
	signal(SIGPIPE,Rupture);

	handle = clnt_create(RMACHINE, RDICTPROG, RDICTVERS, PROTOCOL);
	if (handle==0) {
		printf("Impossible contacter programme distant\n");
		exit(1);
	};
	/**/

	while (!fonctionClient())
        {
                replay=1;
		Rupture();
        };

}
