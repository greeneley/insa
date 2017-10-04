#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

#define _GNU_SOURCE

int main(void)
{
	int input;
	
	while(1)
	{
		scanf("%s", &input);
		printf("%d", input);

		switch(input)
		{
			case 43:
				if(nice(1)) printf("Incrementation de nice \n");
				break;
			case 45:
				if(nice(-1)) printf("Decrementation de nice \n");
				break;
		}
	}	
	return 0;
}
