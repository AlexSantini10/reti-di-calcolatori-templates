#include <dirent.h>
#include <errno.h>
#include <fcntl.h>
#include <netdb.h>
#include <netinet/in.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/select.h>
#include <dirent.h>

#define DIM_BUFF 100
#define LENGTH_FILE_NAME 20
#define MAX_LEN 2000
#define max(a, b) ((a) > (b) ? (a) : (b))

int count_files(char *name)
{
    DIR *dir;
    struct dirent *dd;
    int count = 0;
    dir = opendir(name);
    if (dir == NULL)
        return -1;
    while ((dd = readdir(dir)) != NULL)
    {
        if (strcmp(dd->d_name, ".") == 0 || strcmp(dd->d_name, "..") == 0)
            continue;

        printf("Trovato il file %s\n", dd->d_name);
        count++;
    }
    printf("Numero totale di file %d\n", count);
    closedir(dir);
    return count;
}

/********************************************************/
void lista_file_secondo_livello(char *listaFiles, char *nomeDir)
{
    DIR *dir;
    struct dirent *dd;

    // Apertura della directory principale
    dir = opendir(nomeDir);
    if (dir == NULL)
    {
        perror("Errore durante l'apertura della directory");
        return;
    }

    // Iterazione sui file e directory nella directory principale
    while ((dd = readdir(dir)) != NULL)
    {
        if (strcmp(dd->d_name, ".") == 0 || strcmp(dd->d_name, "..") == 0)
            continue;

        // Costruzione del percorso completo della directory di secondo livello
        char subdir_path[MAX_LEN];
        snprintf(subdir_path, sizeof(subdir_path), "%s/%s", nomeDir, dd->d_name);

        // Controllo se è una directory
        struct stat statbuf;
        if (stat(subdir_path, &statbuf) == -1)
        {
            perror("Errore durante la lettura delle informazioni sul file");
            continue;
        }

        if (!S_ISDIR(statbuf.st_mode))
            continue;

        // Apertura della directory di secondo livello
        DIR *subdir = opendir(subdir_path);
        if (subdir == NULL)
        {
            perror("Errore durante l'apertura della sotto-directory");
            continue;
        }

        // Iterazione sui file nella sotto-directory
        struct dirent *sub_dd;
        while ((sub_dd = readdir(subdir)) != NULL)
        {
            if (sub_dd->d_type == DT_REG)
            {
                // Se è un file, lo aggiungiamo alla lista
                strcat(listaFiles, sub_dd->d_name);
                strcat(listaFiles, " ");
            }
        }

        // Chiusura della sotto-directory
        closedir(subdir);
    }

    printf("Lista file: %s\n", listaFiles);

    // Chiusura della directory principale
    closedir(dir);
}

/********************************************************/
int dir_exists(char *name)
{
    DIR *dir;
    struct dirent *dd;
    int count = 0;
    dir = opendir(name);
    if (dir == NULL)
        return 0;

    return 1;
}

/********************************************************/
void gestore(int signo)
{
    int stato;
    printf("esecuzione gestore di SIGCHLD\n");
    wait(&stato);
}
/********************************************************/

int main(int argc, char **argv)
{
    int listenfd, connfd, udpfd, fd_file, nready, maxfdp1;
    const int on = 1;
    char zero = 0, buff[DIM_BUFF], nome_file[LENGTH_FILE_NAME], nome_dir[LENGTH_FILE_NAME];
    fd_set rset;
    int len, nwrite, num, port;
    struct sockaddr_in cliaddr, servaddr;

    /* CONTROLLO ARGOMENTI ---------------------------------- */
    if (argc != 2)
    {
        printf("Error: %s port\n", argv[0]);
        exit(1);
    }

    port = atoi(argv[1]);
    if (port < 1024 || port > 65535)
    {
        printf("Porta scorretta...");
        exit(2);
    }

    /* INIZIALIZZAZIONE INDIRIZZO SERVER ----------------------------------------- */
    memset((char *)&servaddr, 0, sizeof(servaddr));
    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr = INADDR_ANY;
    servaddr.sin_port = htons(port);

    printf("Server avviato\n");

    /* CREAZIONE SOCKET TCP ------------------------------------------------------ */
    listenfd = socket(AF_INET, SOCK_STREAM, 0);
    if (listenfd < 0)
    {
        perror("apertura socket TCP ");
        exit(1);
    }
    printf("Creata la socket TCP d'ascolto, fd=%d\n", listenfd);

    if (setsockopt(listenfd, SOL_SOCKET, SO_REUSEADDR, &on, sizeof(on)) < 0)
    {
        perror("set opzioni socket TCP");
        exit(2);
    }
    printf("Set opzioni socket TCP ok\n");

    if (bind(listenfd, (struct sockaddr *)&servaddr, sizeof(servaddr)) < 0)
    {
        perror("bind socket TCP");
        exit(3);
    }
    printf("Bind socket TCP ok\n");

    if (listen(listenfd, 5) < 0)
    {
        perror("listen");
        exit(4);
    }
    printf("Listen ok\n");

    /* CREAZIONE SOCKET UDP ------------------------------------------------ */
    udpfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (udpfd < 0)
    {
        perror("apertura socket UDP");
        exit(5);
    }
    printf("Creata la socket UDP, fd=%d\n", udpfd);

    if (setsockopt(udpfd, SOL_SOCKET, SO_REUSEADDR, &on, sizeof(on)) < 0)
    {
        perror("set opzioni socket UDP");
        exit(6);
    }
    printf("Set opzioni socket UDP ok\n");

    if (bind(udpfd, (struct sockaddr *)&servaddr, sizeof(servaddr)) < 0)
    {
        perror("bind socket UDP");
        exit(7);
    }
    printf("Bind socket UDP ok\n");

    /* AGGANCIO GESTORE PER EVITARE FIGLI ZOMBIE -------------------------------- */
    signal(SIGCHLD, gestore);

    /* PULIZIA E SETTAGGIO MASCHERA DEI FILE DESCRIPTOR ------------------------- */
    FD_ZERO(&rset);
    maxfdp1 = max(listenfd, udpfd) + 1;

    /* CICLO DI RICEZIONE EVENTI DALLA SELECT ----------------------------------- */
    for (;;)
    {
        FD_SET(listenfd, &rset);
        FD_SET(udpfd, &rset);

        if ((nready = select(maxfdp1, &rset, NULL, NULL, NULL)) < 0)
        {
            if (errno == EINTR)
                continue;
            else
            {
                perror("select");
                exit(8);
            }
        }

        /* GESTIONE RICHIESTE NOMI FILE DEI DIRETTORI DI SECONDO LIVELLO ------------------------------------- */
        // Intendiamo il nome di tutti i file di tutti i direttori di primo livello (ossia contenuti a loro volta nel direttorio indicato)

        if (FD_ISSET(listenfd, &rset))
        {
            // Richiesta TCP
            printf("Ricevuta richiesta di conteggio\n");
            len = sizeof(struct sockaddr_in);
            if ((connfd = accept(listenfd, (struct sockaddr *)&cliaddr, &len)) < 0)
            {
                if (errno == EINTR)
                    continue;
                else
                {
                    perror("accept");
                    exit(9);
                }
            }

            if (fork() == 0)
            { /* processo figlio che serve la richiesta di operazione */
                close(listenfd);
                printf("Dentro il figlio, pid=%i\n", getpid());

                for (;;)
                {
                    if ((read(connfd, &nome_dir, sizeof(nome_dir))) <= 0)
                    {
                        perror("read");
                        break;
                    }
                    printf("Richiesta directory %s\n", nome_dir);

                    if (!dir_exists(nome_dir))
                    {
                        printf("Directory inesistente\n");
                        write(connfd, "N", 1);
                    }
                    else
                    {
                        write(connfd, "S", 1);

                        /* lettura dal file (a blocchi) e scrittura sulla socket */
                        printf("Leggo e invio il conteggio dei file nelle subdir\n");

                        // Pulizia del buffer prima di utilizzarlo
                        memset(buff, 0, sizeof(buff));

                        lista_file_secondo_livello(buff, nome_dir);

                        printf("Invio lista file %s\n", buff);

                        // Utilizzo di strlen per ottenere la lunghezza effettiva del buffer
                        if ((nwrite = write(connfd, buff, strlen(buff))) < 0)
                        {
                            perror("write");
                            break;
                        }
                        printf("Terminato invio lista file\n");

                        /* invio al client segnale di terminazione: zero binario */
                        write(connfd, &zero, 1);
                    } // else
                }     // for
                printf("Figlio %i: chiudo connessione e termino\n", getpid());
                close(connfd);
                exit(0);
            } // figlio

        } /* fine gestione richieste di file */

        // ...
    } /* ciclo for della select */
}
