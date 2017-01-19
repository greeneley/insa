/**/
#include <rpc/rpc.h>
#include <signal.h>
/**/
#include <stdio.h>
#include <ctype.h>

/**/
#include "rdict.h"

#define	RMACHINE	"dell"
#define	PROTOCOL	"udp"
CLIENT	*handle;
/**/

#include "CodeClient.hc"
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>

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
	CLIENT* handle;

	struct sockaddr_in server;
	struct hostent* remote_host;
	int sock = RPC_ANYSOCK;

	server.sin_family = AF_INET;
	server.sin_addr.s_addr = INADDR_ANY;
	/* Ne fonctionne pas malgre l'aide de l'impetrant */
	//memcpy(&(server.sin_addr), remote_host->h_addr, remote_host->h_length );
	server.sin_port = htons(atoi(argv[2]));

	remote_host = gethostbyname(argv[1]);



	/*handle = clnt_create(RMACHINE, RDICTPROG, RDICTVERS, PROTOCOL);
	if (handle==0) {
		printf("Impossible contacter programme distant\n");
		exit(1);
	};
	*/

	handle = clnttcp_create(&server, RDICTPROG, RDICTVERS &sock, 0, 0);
	/**/

	if (handle==0) 
	{
		printf("Erreur handle\n");
		exit(1);
	}

	while (!fonctionClient())
        {
             replay=1;
        };

}
