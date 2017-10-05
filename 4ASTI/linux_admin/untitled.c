#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <syslog.h>
#include <errno.h>
#include <pwd.h>
#include <signal.h>

/* Include pour le serveur TCP */
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>

int main(int argc, char const *argv[])
{
	int test = mkdir("/var/lib/printserver", 0777);
	printf("%s", strerror(errno));
	return 0;
}