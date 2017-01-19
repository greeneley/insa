#include <rpc/rpc.h>
#include <stdio.h>

#include "rdict.h"

extern CLIENT *handle;

int
initdict(record)
DictRecord record;
{
	int *ret;
	printf("Dans initdict\n");
	ret=initdict_1(&record,handle);
	if (ret==NULL) return 0;
	return *ret; 
}

int
insertw(record)
DictRecord record;
{
	int *ret;
	ret=insertw_1(&record,handle);
	if (ret==NULL) return 0;
	return *ret; 
}

int
deletew(record)
DictRecord record;
{
	int *ret;
	ret=deletew_1(&record,handle);
	if (ret==NULL) return 0;
	return *ret; 
}

int
lookupw(record)
DictRecord record;
{
	int *ret;
	printf("Avant lookupw_1\n");
	ret=lookupw_1(&record,handle);
	printf("Après lookupw_1\n");
	if (ret==NULL) return 0;
	return *ret; 
}
