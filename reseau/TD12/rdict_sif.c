#include <rpc/rpc.h>
#include <stdio.h>

#include "rdict.h"

static int retcode;

int *
initdict_1_svc(void *rien, struct svc_req *req)
{
	retcode = initdict();
	return &retcode;
}

int *
insertw_1_svc(DictRecord *record,  struct svc_req *req)
{
	retcode= insertw(*record);
	return &retcode;
}

int *
deletew_1_svc(DictRecord *record,  struct svc_req *req)
{
	retcode= deletew(*record);
	return &retcode;
}

int *
lookupw_1_svc(DictRecord *record,  struct svc_req *req)
{
	retcode= lookupw(*record);
	return &retcode;
}
