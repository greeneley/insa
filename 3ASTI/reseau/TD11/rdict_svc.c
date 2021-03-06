/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#include "rdict.h"
#include <stdio.h>
#include <stdlib.h>
#include <rpc/pmap_clnt.h>
#include <string.h>
#include <memory.h>
#include <sys/socket.h>
#include <netinet/in.h>

#ifndef SIG_PF
#define SIG_PF void(*)(int)
#endif

static void
rdictprog_1(struct svc_req *rqstp, register SVCXPRT *transp)
{
	union {
		DictRecord insertw_1_arg;
		DictRecord deletew_1_arg;
		DictRecord lookupw_1_arg;
	} argument;
	char *result;
	xdrproc_t _xdr_argument, _xdr_result;
	char *(*local)(char *, struct svc_req *);

	switch (rqstp->rq_proc) {
	case NULLPROC:
		(void) svc_sendreply (transp, (xdrproc_t) xdr_void, (char *)NULL);
		return;

	case INITDICT:
		_xdr_argument = (xdrproc_t) xdr_void;
		_xdr_result = (xdrproc_t) xdr_int;
		local = (char *(*)(char *, struct svc_req *)) initdict_1_svc;
		break;

	case INSERTW:
		_xdr_argument = (xdrproc_t) xdr_DictRecord;
		_xdr_result = (xdrproc_t) xdr_int;
		local = (char *(*)(char *, struct svc_req *)) insertw_1_svc;
		break;

	case DELETEW:
		_xdr_argument = (xdrproc_t) xdr_DictRecord;
		_xdr_result = (xdrproc_t) xdr_int;
		local = (char *(*)(char *, struct svc_req *)) deletew_1_svc;
		break;

	case LOOKUPW:
		_xdr_argument = (xdrproc_t) xdr_DictRecord;
		_xdr_result = (xdrproc_t) xdr_int;
		local = (char *(*)(char *, struct svc_req *)) lookupw_1_svc;
		break;

	default:
		svcerr_noproc (transp);
		return;
	}
	memset ((char *)&argument, 0, sizeof (argument));
	if (!svc_getargs (transp, (xdrproc_t) _xdr_argument, (caddr_t) &argument)) {
		svcerr_decode (transp);
		return;
	}
	result = (*local)((char *)&argument, rqstp);
	if (result != NULL && !svc_sendreply(transp, (xdrproc_t) _xdr_result, result)) {
		svcerr_systemerr (transp);
	}
	if (!svc_freeargs (transp, (xdrproc_t) _xdr_argument, (caddr_t) &argument)) {
		fprintf (stderr, "%s", "unable to free arguments");
		exit (1);
	}
	return;
}

int
main (int argc, char **argv)
{
	register SVCXPRT *transp;

	//pmap_unset (RDICTPROG, RDICTVERS);

	transp = svcudp_create(RPC_ANYSOCK);
	if (transp == NULL) {
		fprintf (stderr, "%s", "cannot create udp service.");
		exit(1);
	}
	/* Modification de IPPROTO_UDP */
	if (!svc_register(transp, RDICTPROG, RDICTVERS, rdictprog_1, 0)) {
		fprintf (stderr, "%s", "unable to register (RDICTPROG, RDICTVERS, udp).");
		exit(1);
	}
	
	transp = svctcp_create(RPC_ANYSOCK, 0, 0);
	if (transp == NULL) {
		fprintf (stderr, "%s", "cannot create tcp service.");
		exit(1);
	}
	/* Modification de IPPROTO_TCP */
	if (!svc_register(transp, RDICTPROG, RDICTVERS, rdictprog_1, 0)) {
		fprintf (stderr, "%s", "unable to register (RDICTPROG, RDICTVERS, tcp).");
		exit(1);
	}

	svc_run ();
	fprintf (stderr, "%s", "svc_run returned");
	exit (1);
	/* NOTREACHED */
}
