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

/* Change this to whatever your daemon is called */
#define DAEMON_NAME "daemon_impression"

/* Change this to the user under which to run */
#define RUN_AS_USER "daemon"

#define EXIT_SUCCESS 0
#define EXIT_FAILURE 1


static void child_handler(int signum)
{
    switch(signum) {
    case SIGALRM: exit(EXIT_FAILURE); break;
    case SIGUSR1: exit(EXIT_SUCCESS); break;
    case SIGCHLD: exit(EXIT_FAILURE); break;
    }
}

static void daemonize( const char *lockfile )
{
    pid_t pid, sid, parent;
    int lfp = -1;

    /* already a daemon */
    if ( getppid() == 1 ) 
    {
        printf("Erreur : PID deja utilise\n");
        return;
    }

    /* Create the lock file as the current user */
    if ( lockfile && lockfile[0] ) {
        lfp = open(lockfile,O_RDWR|O_CREAT,0640);
        if ( lfp < 0 ) {
            syslog( LOG_ERR, "unable to create lock file %s, code=%d (%s)",
                    lockfile, errno, strerror(errno) );
            exit(EXIT_FAILURE);
        }
    }

    /* Drop user if there is one, and we were run as root */
    if ( getuid() == 0 || geteuid() == 0 ) {
        struct passwd *pw = getpwnam(RUN_AS_USER);
        if ( pw ) {
            syslog( LOG_NOTICE, "setting user to " RUN_AS_USER );
            setuid( pw->pw_uid );
        }
    }

    /* Trap signals that we expect to recieve */
    signal(SIGCHLD,child_handler);
    signal(SIGUSR1,child_handler);
    signal(SIGALRM,child_handler);

    /* Fork off the parent process */
    pid = fork();
    if (pid < 0) {
        syslog( LOG_ERR, "unable to fork daemon, code=%d (%s)",
                errno, strerror(errno) );
        exit(EXIT_FAILURE);
    }
    /* If we got a good PID, then we can exit the parent process. */
    if (pid > 0) {

        /* Wait for confirmation from the child via SIGTERM or SIGCHLD, or
           for two seconds to elapse (SIGALRM).  pause() should not return. */
        alarm(2);
        pause();

        exit(EXIT_FAILURE);
    }

    /* At this point we are executing as the child process */
    parent = getppid();

    /* Cancel certain signals */
    signal(SIGCHLD,SIG_DFL); /* A child process dies */
    signal(SIGTSTP,SIG_IGN); /* Various TTY signals */
    signal(SIGTTOU,SIG_IGN);
    signal(SIGTTIN,SIG_IGN);
    signal(SIGHUP, SIG_IGN); /* Ignore hangup signal */
    signal(SIGTERM,SIG_DFL); /* Die on SIGTERM */

    /* Change the file mode mask */
    umask(177);

    /* Create a new SID for the child process */
    sid = setsid();
    if (sid < 0) {
        syslog( LOG_ERR, "unable to create a new session, code %d (%s)",
                errno, strerror(errno) );
        exit(EXIT_FAILURE);
    }

    /* Change the current working directory.  This prevents the current
       directory from being locked; hence not being able to remove it. */
    if ((chdir("/")) < 0) {
        syslog( LOG_ERR, "unable to change directory to %s, code %d (%s)",
                "/", errno, strerror(errno) );
        exit(EXIT_FAILURE);
    }

    /* Redirect standard files to /dev/null */
    freopen( "/dev/null", "r", stdin);
    freopen( "/dev/null", "w", stdout);
    freopen( "/dev/null", "w", stderr);

    /* Tell the parent process that we are A-okay */
    kill( parent, SIGUSR1 );
}

static int lireOctet(int socketClient)
{
    int n;
    char buffer[256];
    char buffDest[256];
    char buffLect[1024];

    bzero(buffer, sizeof(buffer));
    FILE *fp;


    n = read(socketClient,buffer, 256);
    if(n < 0)
    {
        syslog( LOG_ERR, "Erreur LECTURE socket ou ECRITURE buffer, code %d (%s)",
                errno, strerror(errno) );
        return -1;
    }

    syslog( LOG_NOTICE, "Fichier a lire : %s", buffer);

    /* Alors le n-2, je pense que c'est du a la touche "entree"
     * qui doit etre interpretee comme une combinaison de deux lettres
     */
    strncpy(buffDest, buffer, n-2);

    fp = fopen(buffDest, "rb");
    if(fp == NULL)
    {
        syslog( LOG_ERR, "Erreur LECTURE fichier, code %d (%s)",
                errno, strerror(errno) );

        if((write(socketClient, "Erreur lecture du fichier\n", 26)) == -1)
        {
            syslog( LOG_ERR, "Erreur ECRITURE socket cliente, code %d (%s)",
                errno, strerror(errno) );
            return -1;
        }
        return 1;
    }

    n = fread(buffLect, sizeof(char), 1024, fp);
    syslog( LOG_NOTICE, "Octects lus : %d", n);

    if(write(socketClient,buffLect,n) < 0)
    {
        syslog( LOG_ERR, "Erreur ECRITURE socket cliente, code %d (%s)",
                errno, strerror(errno) );
        return -1;
    }

    if(fp != NULL)
    {
        fclose(fp);
    }

    return 1;
}

int main( int argc, char *argv[] ) {
    /* Initialize the logging interface */
    openlog( DAEMON_NAME, LOG_PID, LOG_LOCAL5 );
    syslog( LOG_INFO, "starting" );

    /* One may wish to process command line arguments here */

    /* Daemonize */
    daemonize( "/var/lock/subsys/" DAEMON_NAME );

    /* Now we are a daemon -- do the work for which we were paid */

    /* ======== VARIABLES ======= */
    int socketServ, socketCli;
    socklen_t tailleCli;
    int pid;
    int port = 7000;
    struct sockaddr_in servAddr, cliAddr;

    /* ===== INITIALISATION ===== */
    socketServ = socket(AF_INET, SOCK_STREAM, 0);
    if(socketServ == -1)
    {
        syslog( LOG_ERR, "Impossible d'ouvrir la socket, code %d (%s)",
                errno, strerror(errno) );
        write(1, "Erreur d'ouverture de la socket serveur\n", 41);
        exit(EXIT_FAILURE);
    }

    servAddr.sin_family = AF_INET;
    servAddr.sin_addr.s_addr = INADDR_ANY;
    servAddr.sin_port = htons(port);

    if(bind(socketServ, (struct sockaddr *) &servAddr, sizeof(servAddr)) == -1) 
    {
        syslog( LOG_ERR, "Erreur de BINDING socket, code %d (%s)",
                errno, strerror(errno) );
        write(1, "Erreur de binding : port deja utilisee ?\n", 45);
        exit(EXIT_FAILURE);
    }

    listen(socketServ,5);

    while(1)
    {
	    syslog( LOG_NOTICE, "alive and listening" );
        tailleCli = sizeof(cliAddr);
        socketCli = accept(socketServ, (struct sockaddr *) &cliAddr, &tailleCli);
        if(socketCli == -1) 
        {
            syslog( LOG_ERR, "Erreur d'initalisation socket cliente, code %d (%s)",
                errno, strerror(errno) );
            continue;
        }

        pid = fork();
        if(pid < 0)
        {
            syslog( LOG_ERR, "Erreur FORK, code %d (%s)",
                errno, strerror(errno) );
            exit(EXIT_FAILURE);
        }
        if(pid == 0)  
        {
            close(socketServ);
            while(lireOctet(socketCli)){}
            close(socketCli);
            exit(0);
            break;
        }
        else close(socketCli);
    }

    /* Finish up */
    syslog( LOG_NOTICE, "terminated" );
    closelog();
    return 0;
}
