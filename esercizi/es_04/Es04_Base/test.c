/* Server Select
 * 	Un solo figlio per tutti i file.
 */

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

#define DIM_BUFF 100
#define LENGTH_FILE_NAME 20
#define MAX_LEN 2000
#define max(a, b) ((a) > (b) ? (a) : (b))

/********************************************************/
int conta_sec(char *nome_dir, char listaFiles[MAX_LEN][MAX_LEN])
{
    DIR *dir;
    struct dirent *dd;
    dir = opendir(nome_dir);
    int count = 0;
    if (dir == NULL)
        return -1;

    DIR *in_dir;
    struct dirent *in_dd;
    struct stat path_stat;

    while ((dd = readdir(dir)) != NULL)
    {
        stat(dd->d_name, &path_stat);
        if (!S_ISREG(path_stat.st_mode))
        {
            printf("Trovato il dir %s\n", dd->d_name);
            strcpy(listaFiles[count], dd->d_name);

            count++;
        }
    }
    closedir(dir);
    return count;

    /*struct stat path;
    stat(fileName, &path);
    return S_ISREG(path.st_mode);
    */
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
int conta_file(char *name)
{
    DIR *dir;
    struct dirent *dd;
    int count = 0;
    dir = opendir(name);
    if (dir == NULL)
        return -1;
    while ((dd = readdir(dir)) != NULL)
    {
        printf("Trovato il file %s\n", dd->d_name);
        count++;
    }
    printf("Numero totale di file %d\n", count);
    closedir(dir);
    return count;
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
    char listaFiles[MAX_LEN][MAX_LEN];
    conta_sec("baolo", listaFiles);

    printf("CIAO%s", listaFiles[2]);
}
