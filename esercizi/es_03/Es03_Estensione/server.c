#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <dirent.h>
#include <sys/stat.h>

#define MAXLINE 8192

#define DEBUG 1

// TODO: Rendere il server parallelo

int main(int argc, char *argv[]) {
    // Controllo argomenti
    if (argc != 2) {
        fprintf(stderr, "Usage: %s [port]\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    int port = atoi(argv[1]);

    // Creazione socket
    int sockfd, connfd;
    struct sockaddr_in servaddr, cli;
    int len = sizeof(cli);

    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd == -1) {
        perror("socket creation failed");
        exit(EXIT_FAILURE);
    }
    else
        printf("Socket created..\n");
    bzero(&servaddr, sizeof(servaddr));

    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
    servaddr.sin_port = htons(port);

    if ((bind(sockfd, (struct sockaddr*)&servaddr, sizeof(servaddr))) != 0) {
        perror("socket bind failed");
        exit(EXIT_FAILURE);
    }
    else
        printf("Socket successfully binded..\n");

    if ((listen(sockfd, 5)) != 0) {
        perror("Listen failed");
        exit(EXIT_FAILURE);
    }
    else
        printf("Server listening..\n");

    char command[MAXLINE];
    char dir[MAXLINE];

    // Loop di accettazione dei dati
    while (1){
        connfd = accept(sockfd, (struct sockaddr*)&cli, &len);

        // Inizializzo vuoto command e dir
        memset(command, 0, sizeof(command));
        memset(dir, 0, sizeof(dir));

        if (connfd < 0) {
            perror("server accept failed");
            exit(EXIT_FAILURE);
        }
        else
            printf("Server open connection with client...\n");

        // ---------------------------------- [INIZIO] Lettura dati dal client ---------------------------------- //

        // Ricezione comando dal client
        recv(connfd, command, sizeof(command), 0);
        if (DEBUG){
            printf("Client command: '%s'\n", command);
        }

        // Controllo se il comando esiste
        if (strcmp(command, "mget") != 0 && strcmp(command, "mput") != 0){
            printf("Command not found\n");
            send(connfd, "Command not found", sizeof("Command not found"), 0);
            continue;
        }

        // Invio conferma al client
        send(connfd, "OK", sizeof("OK"), 0);


        // Ricezione directory dal client
        recv(connfd, dir, sizeof(dir), 0);
        if (DEBUG)
            printf("Client directory: '%s'\n", dir);
        
        // Controllo se la directory esiste (se richiesto mget)
        DIR *d;
        struct dirent *dirEntry;
        struct stat statbuf;

        if ((d = opendir(dir)) == NULL && strcmp(command, "mget") == 0){
            printf("Directory not found\n");
            send(connfd, "Directory not found", sizeof("Directory not found"), 0);
            continue;
        }

        // Invio conferma al client
        send(connfd, "OK", sizeof("OK"), 0);


        // Ricevo start dal client
        char start[MAXLINE];
        recv(connfd, start, sizeof(start), 0);
        if (DEBUG)
            printf("Client start: '%s'\n", start);

        // Controllo se il client ha risposto OK
        if (strcmp(start, "OK") != 0){
            printf("Error: %s\n", start);
            continue;
        }

        // ---------------------------------- Scambio dati ---------------------------------- //

        // ---------------------------------- mget ---------------------------------- //
        if (strcmp(command, "mget") == 0){
            // Invio tutti i file all'interno della cartella
            while ((dirEntry = readdir(d)) != NULL){
                // Salto le directory . e ..
                if (strcmp(dirEntry->d_name, ".") == 0 || strcmp(dirEntry->d_name, "..") == 0)
                    continue;

                // Invio nome file
                send(connfd, dirEntry->d_name, sizeof(dirEntry->d_name), 0);
                if (DEBUG)
                    printf("Sending file '%s'\n", dirEntry->d_name);

                // Ricezione conferma dal client
                char response[MAXLINE];
                recv(connfd, response, sizeof(response), 0);

                // Controllo se il client ha risposto OK
                if (strcmp(response, "OK") != 0){
                    printf("Error: %s\n", response);
                    continue;
                }
                else if (DEBUG)
                    printf("Client response: %s\n", response);

                // Invio file
                char path[MAXLINE*3];
                sprintf(path, "%s/%s", dir, dirEntry->d_name);
                FILE *fp = fopen(path, "r");
                if (fp == NULL){
                    perror("fopen");
                    exit(EXIT_FAILURE);
                }

                char buffer[MAXLINE];
                while (fgets(buffer, sizeof(buffer), fp) != NULL){
                    send(connfd, buffer, sizeof(buffer), 0);
                }

                // Invio EOF
                send(connfd, "EOF", sizeof("EOF"), 0);

                // Chiusura file
                fclose(fp);

                // Ricezione conferma dal client
                recv(connfd, response, sizeof(response), 0);

                // Controllo se il client ha risposto OK
                if (strcmp(response, "OK") != 0){
                    printf("Error: %s\n", response);
                    continue;
                }
            }

            // Invio EOC
            send(connfd, "EOC", sizeof("EOC"), 0);
        }
        // ---------------------------------- mput ---------------------------------- //
        else if (strcmp(command, "mput") == 0){
            // Ricevo tutti i file e li inserisco all'interno della cartella dir
            DIR *d;
            struct dirent *dirEntry;
            struct stat statbuf;

            // Creo la cartella, se sono in modalità debug la creo con un nome diverso per differenziarla
            if (DEBUG){
                mkdir(strcat(dir, "_server"), 0777);
            }
            else{
                mkdir(dir, 0777);
            }

            while (1){
                // Ricezione nome file
                char filename[MAXLINE];
                recv(connfd, filename, sizeof(filename), 0);

                if (DEBUG)
                    printf("Receiving file '%s'\n", filename);

                // Controllo se il client ha risposto EOC
                if (strcmp(filename, "EOC") == 0)
                    break;

                // Invio conferma al client
                send(connfd, "OK", sizeof("OK"), 0);

                // Ricezione file
                char path[MAXLINE*3];
                sprintf(path, "%s/%s", dir, filename);

                if (DEBUG){
                    printf("Saving file in '%s'\n", path);
                }

                FILE *fp = fopen(path, "w");
                if (fp == NULL){
                    perror("fopen");
                    exit(EXIT_FAILURE);
                }

                char buffer[MAXLINE];
                while (1){
                    recv(connfd, buffer, sizeof(buffer), 0);

                    // Controllo se il client ha risposto EOF
                    if (strcmp(buffer, "EOF") == 0)
                        break;

                    // Scrivo nel file
                    fprintf(fp, "%s", buffer);
                }

                // Invio conferma al client
                send(connfd, "OK", sizeof("OK"), 0);

                // Chiusura file
                fclose(fp);
            }
        }


        // ---------------------------------- [FINE] Chiusura connfd ---------------------------------- //
        printf("Closing connection with client..\n");
        close(connfd);
    }

    close(sockfd);

    return 0;
}
