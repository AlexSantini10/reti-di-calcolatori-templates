#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <dirent.h>
#include <sys/stat.h>

#define MAXLINE 8192
#define DEBUG 1

int main(int argc, char *argv[]) {
    if (argc != 3) {
        fprintf(stderr, "Usage: %s [server_ip] [server_port]\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    char *server_ip = argv[1];
    int server_port = atoi(argv[2]);


    while (1) {
        //  Creazione socket
        int sockfd = socket(AF_INET, SOCK_STREAM, 0);
        if (sockfd == -1) {
            perror("CLIENT: socket creation failed");
            exit(EXIT_FAILURE);
        }

        // Connessione al server    
        struct sockaddr_in servaddr;
        bzero(&servaddr, sizeof(servaddr));
        servaddr.sin_family = AF_INET;
        servaddr.sin_addr.s_addr = inet_addr(server_ip);
        servaddr.sin_port = htons(server_port);

        if (connect(sockfd, (struct sockaddr*)&servaddr, sizeof(servaddr)) != 0) {
            perror("CLIENT: connection with the server failed");
            exit(EXIT_FAILURE);
        }

        // Lettura directory e comando da inviare al server
        char command[MAXLINE];
        char dir[MAXLINE];

        // ---------------------------------- Comando ---------------------------------- //

        printf("Insert command (or exit): ");
        scanf("%s", command);

        // Controllo se l'utente vuole uscire
        if (strcmp(command, "exit") == 0)
            break;

        // Invio comando al server
        send(sockfd, command, strlen(command), 0);

        // Ricezione risposta dal server
        char responseCommand[MAXLINE];
        recv(sockfd, responseCommand, sizeof(responseCommand), 0);

        if (DEBUG)
            printf("Server response: %s\n", responseCommand);
        
        // Controllo se il server ha risposto OK
        if (strcmp(responseCommand, "OK") != 0) {
            printf("CLIENT Error: %s\n", responseCommand);
            continue;
        }

        // ---------------------------------- Directory ---------------------------------- //

        printf("Insert directory (or exit): ");
        scanf("%s", dir);

        // Controllo se l'utente vuole uscire
        if (strcmp(dir, "exit") == 0)
            break;

        // Invio directory al server
        send(sockfd, dir, strlen(dir), 0);

        // Ricezione risposta dal server
        char responseDir[MAXLINE];
        recv(sockfd, responseDir, sizeof(responseDir), 0);

        if (DEBUG)
            printf("Server response: %s\n", responseDir);

        // Controllo se il server ha risposto OK
        if (strcmp(responseDir, "OK") != 0) {
            printf("CLIENT Error: %s\n", responseDir);
            continue;
        }

        // ---------------------------------- Scambio dati ---------------------------------- //

        // Invio OK per iniziare lo scambio dati
        send(sockfd, "OK", sizeof("OK"), 0);

        // ---------------------------------- mget ---------------------------------- //
        if (strcmp(command, "mget") == 0){
            // Ricevo tutti i file e li inserisco in una cartella chiamata come la mia variabile dir
            DIR *d;
            struct dirent *dirEntry;
            struct stat statbuf;


            // Creo la cartella, se sto in debug la chiamo dir_client per differenziarla
            if (DEBUG)
                mkdir(strcat(dir, "_client"), 0777);
            else
                mkdir(dir, 0777);

            // Ricevo i file
            while (1){
                // Ricezione nome file
                char filename[MAXLINE];
                recv(sockfd, filename, sizeof(filename), 0);

                if (DEBUG)
                    printf("Received file '%s'\n", filename);
                
                // Se filename è EOC, allora ho ricevuto tutti i file
                if (strcmp(filename, "EOC") == 0)
                    break;

                // Invio OK
                send(sockfd, "OK", sizeof("OK"), 0);

                // Ricezione file
                char path[MAXLINE*3];
                sprintf(path, "%s/%s", dir, filename);

                if (DEBUG){
                    printf("Receiving file '%s'\n", path);
                }

                FILE *fp = fopen(path, "w");
                if (fp == NULL){
                    perror("CLIENT: Error opening file");
                    exit(EXIT_FAILURE);
                }
                else if (DEBUG)
                    printf("Opening file '%s'\n", filename);

                char buffer[MAXLINE];
                int n;
                while ((n = recv(sockfd, buffer, sizeof(buffer), 0)) > 0){

                    if (strcmp(buffer, "EOF") == 0)
                        break;
                    
                    if (DEBUG){
                        printf("Buffer: %s\n", buffer);
                        printf("Received %d bytes\n", n);
                    }

                    fprintf(fp, "%s", buffer); 
                }

                fclose(fp);

                // Invio OK
                send(sockfd, "OK", sizeof("OK"), 0);
            }
        }
        // ---------------------------------- mput ---------------------------------- //
        else if (strcmp(command, "mput") == 0){
            // Invio tutti i file all'interno della cartella dir
            DIR *d;
            struct dirent *dirEntry;
            struct stat statbuf;

            // Apro la cartella
            if ((d = opendir(dir)) == NULL){
                perror("CLIENT: Error opening directory");
                exit(EXIT_FAILURE);
            }

            // Invio tutti i file all'interno della cartella
            while ((dirEntry = readdir(d)) != NULL){
                // Salto le directory . e ..
                if (strcmp(dirEntry->d_name, ".") == 0 || strcmp(dirEntry->d_name, "..") == 0)
                    continue;

                // Invio nome file
                send(sockfd, dirEntry->d_name, sizeof(dirEntry->d_name), 0);
                if (DEBUG)
                    printf("Sending file '%s'\n", dirEntry->d_name);

                // Ricezione conferma dal server
                char response[MAXLINE];
                recv(sockfd, response, sizeof(response), 0);

                // Controllo se il server ha risposto OK
                if (strcmp(response, "OK") != 0){
                    printf("Error: %s\n", response);
                    continue;
                }
                else if (DEBUG)
                    printf("Server response: %s\n", response);

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
                    send(sockfd, buffer, sizeof(buffer), 0);
                }

                // Invio EOF
                send(sockfd, "EOF", sizeof("EOF"), 0);

                // Chiusura file
                fclose(fp);

                // Ricezione conferma dal server
                recv(sockfd, response, sizeof(response), 0);

                // Controllo se il server ha risposto OK
                if (strcmp(response, "OK") != 0){
                    printf("Error: %s\n", response);
                    continue;
                }
            }

            // Invio EOC
            send(sockfd, "EOC", sizeof("EOC"), 0);
        }

        // ---------------------------------- Chiusura socket ---------------------------------- //
        close(sockfd);  
    }


    return 0;
}
