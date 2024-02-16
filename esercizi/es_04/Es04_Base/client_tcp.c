#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <arpa/inet.h>

#define DIM_BUFF 100
#define LENGTH_FILE_NAME 20

int main(int argc, char **argv)
{
    int sockfd, n;
    char buff[DIM_BUFF], nome_dir[LENGTH_FILE_NAME];
    struct sockaddr_in servaddr;

    if (argc != 3)
    {
        printf("Usage: %s <server IP> <server port>\n", argv[0]);
        exit(1);
    }

    // Creazione del socket TCP
    if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0)
    {
        perror("Errore durante la creazione del socket");
        exit(2);
    }

    // Inizializzazione dell'indirizzo del server
    memset(&servaddr, 0, sizeof(servaddr));
    servaddr.sin_family = AF_INET;
    servaddr.sin_port = htons(atoi(argv[2]));

    if (inet_pton(AF_INET, argv[1], &servaddr.sin_addr) <= 0)
    {
        perror("Errore durante l'inizializzazione dell'indirizzo del server");
        exit(3);
    }

    // Connessione al server
    if (connect(sockfd, (struct sockaddr *)&servaddr, sizeof(servaddr)) < 0)
    {
        perror("Errore durante la connessione al server");
        exit(4);
    }

    // Lettura del nome della directory da inviare al server
    printf("Inserisci il nome della directory: ");
    scanf("%s", nome_dir);

    // Invio del nome della directory al server
    write(sockfd, nome_dir, sizeof(nome_dir));

    // Ricezione della risposta dal server
    n = read(sockfd, buff, sizeof(buff));

    if (n <= 0)
    {
        perror("Errore durante la lettura dalla socket");
        exit(5);
    }

    // Verifica se la directory esiste sul server
    if (buff[0] == 'N')
    {
        printf("Directory inesistente sul server\n");
        close(sockfd);
        exit(0);
    }

    // Azzero il buffer
    memset(buff, 0, sizeof(buff));

    // Ricezione e stampa della lista dei file di secondo livello
    n = read(sockfd, buff, sizeof(buff));

    if (n <= 0)
    {
        perror("Errore durante la lettura dalla socket");
        exit(6);
    }

    printf("Lista file di secondo livello:\n%s\n", buff);

    // Chiusura del socket
    close(sockfd);

    return 0;
}
