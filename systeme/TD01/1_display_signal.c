#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>

#define _XOPEN_SOURCE

int main (void)
{
	int i;

	for (i = 1; i < _NSIG; i ++)
	fprintf(stdout, "signal %d (%s)\n",i, sys_siglist[i]);
}

