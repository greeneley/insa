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


/* ===== GLOBALES ====== */
int glob_sockServ, glob_sockCli;

/* ===== FONCTIONS ===== */

static void child_handler(int signum)
{
    switch(signum) {
    case SIGALRM: exit(EXIT_FAILURE); break;
    case SIGUSR1: exit(EXIT_SUCCESS); break;
    case SIGCHLD: exit(EXIT_FAILURE); break;
    case SIGTERM:
        close(glob_sockServ);
        close(glob_sockCli);
        syslog(LOG_NOTICE, "Fin du daemon : SIGTERM");
        exit(EXIT_SUCCESS);
        break;
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
    signal(SIGCHLD,SIG_IGN); /* A child process dies */
    signal(SIGTSTP,SIG_IGN); /* Various TTY signals */
    signal(SIGTTOU,SIG_IGN);
    signal(SIGTTIN,SIG_IGN);
    signal(SIGHUP, SIG_IGN); /* Ignore hangup signal */
    signal(SIGTERM,SIG_DFL); /* Die on SIGTERM */

    /* Change the file mode mask */
    umask(0);

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

static int lireOctet()
{
    static int n;
    static char buffer[256];
    static char buffDest[256];
    static char buffLect[1024];

    bzero(buffer, sizeof(buffer));
    FILE *fp;


    n = read(glob_sockCli, buffer, 256);
    if(n < 0)
    {
        syslog( LOG_ERR, "Erreur LECTURE socket ou ECRITURE buffer, code %d (%s)",
                errno, strerror(errno) );
        return -1;
    }
    else if (n == 0)
    {
        syslog( LOG_NOTICE, "Fin du processus");
        return -1;
    }

    syslog( LOG_NOTICE, "Fichier a lire : %s", buffer);

    /* Alors le n-2, je pense que c'est du a la touche "entree"
     * qui doit etre interpretee comme une combinaison de deux characters
     */
    strncpy(buffDest, buffer, n-2);

    fp = fopen(buffDest, "rb");
    if(fp == NULL)
    {
        syslog( LOG_ERR, "Erreur LECTURE fichier, code %d (%s)",
                errno, strerror(errno) );

        if((write(glob_sockCli, "Erreur lecture du fichier\n", 26)) == -1)
        {
            syslog( LOG_ERR, "Erreur ECRITURE socket cliente, code %d (%s)",
                errno, strerror(errno) );
            return -1;
        }
        return 1;
    }

    n = fread(buffLect, sizeof(char), 1024, fp);
    syslog( LOG_NOTICE, "Octects lus : %d", n);

    if(write(glob_sockCli, buffLect, n) < 0)
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

    FILE *pidFd;
    mkdir("/var/lib/printserver", 0);
    /* Fichier PID */
    pidFd = fopen("/var/lib/printserver/printserver.pid", "w+");
    fclose(pidFd);
    chmod("/var/lib/printserver/printserver.pid", S_IRWXU|
                                                 S_IRGRP|S_IXGRP|S_IWGRP|
                                                 S_IROTH|S_IXOTH|S_IWOTH);

    /* Initialize the logging interface */
    openlog( DAEMON_NAME, LOG_PID, LOG_LOCAL5 );
    syslog( LOG_INFO, "starting" );

    /* One may wish to process command line arguments here */

    /* Daemonize */
    daemonize( "/var/lock/subsys/" DAEMON_NAME );

    /* Now we are a daemon -- do the work for which we were paid */

    /* ======== VARIABLES ======= */
    int pid;
    int port = 7000;
    struct sockaddr_in servAddr, cliAddr;

    socklen_t tailleCli;

    /* ===== INITIALISATION ===== */
    glob_sockServ = socket(AF_INET, SOCK_STREAM, 0);
    if(glob_sockServ == -1)
    {
        syslog( LOG_ERR, "Impossible d'ouvrir la socket, code %d (%s)",
                errno, strerror(errno) );
        write(1, "Erreur d'ouverture de la socket serveur\n", 41);
        exit(EXIT_FAILURE);
    }

    /* Socket UNIX */
    servAddr.sin_family = AF_INET;
    servAddr.sin_addr.s_addr = INADDR_ANY;
    servAddr.sin_port = htons(port);

    if(bind(glob_sockServ, (struct sockaddr *) &servAddr, sizeof(servAddr)) == -1) 
    {
        syslog( LOG_ERR, "Erreur de BINDING socket, code %d (%s)",
                errno, strerror(errno) );
        write(1, "Erreur de binding : port deja utilisee ?\n", 45);
        exit(EXIT_FAILURE);
    }

    pidFd = fopen("/var/lib/printserver/printserver.pid", "w+");
    fprintf(pidFd, "%d", (int)getpid());
    fclose(pidFd);

    listen(glob_sockServ,5);
    signal(SIGTERM, child_handler);

    while(1)
    {
	    syslog( LOG_NOTICE, "alive and listening" );
        tailleCli = sizeof(cliAddr);
        glob_sockCli = accept(glob_sockServ, (struct sockaddr *) &cliAddr, &tailleCli);
        if(glob_sockCli == -1) 
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
            close(glob_sockServ);
            signal(SIGTERM, child_handler);
            while(lireOctet() > 0){}
            close(glob_sockCli);
            exit(0);
        }
        else 
        {
            close(glob_sockCli);
        }
    }

    /* Finish up */
    syslog( LOG_NOTICE, "terminated" );
    closelog();
    return 0;
}
