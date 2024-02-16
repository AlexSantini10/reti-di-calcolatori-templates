#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define MAXLINE 1024

int main(int argc, char *argv[]) {
    if (argc != 3) {
        fprintf(stderr, "Usage: %s [server_ip] [server_port]\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    char *server_ip = argv[1];
    int server_port = atoi(argv[2]);

    while (1) {
        int sockfd;
        struct sockaddr_in servaddr;

        sockfd = socket(AF_INET, SOCK_STREAM, 0);
        if (sockfd == -1) {
            perror("socket creation failed");
            exit(EXIT_FAILURE);
        }

        bzero(&servaddr, sizeof(servaddr));

        servaddr.sin_family = AF_INET;
        servaddr.sin_addr.s_addr = inet_addr(server_ip);
        servaddr.sin_port = htons(server_port);

        if (connect(sockfd, (struct sockaddr*)&servaddr, sizeof(servaddr)) != 0) {
            perror("connection with the server failed");
            exit(EXIT_FAILURE);
        }

        char filename[MAXLINE];
        int line_number;

        printf("\nEnter filename: ");
        fgets(filename, sizeof(filename), stdin);
        strtok(filename, "\n"); // Rimuovi il newline

        printf("Enter line number to remove: ");
        scanf("%d", &line_number);
        getchar(); // Consuma il newline

        send(sockfd, filename, sizeof(filename), 0);
        send(sockfd, &line_number, sizeof(line_number), 0);

        long file_size;
        recv(sockfd, &file_size, sizeof(file_size), 0);

        char buffer[MAXLINE];
        int received_size = 0;

        // Scrivo su standard output e su un file locale
        FILE *fp = fopen("output.txt", "w");

        while (received_size < file_size) {
            int bytes_received = recv(sockfd, buffer, sizeof(buffer), 0);
            if (bytes_received <= 0) {
                break;
            }

            fwrite(buffer, 1, bytes_received, fp); // Scrivi su file
            fwrite(buffer, 1, bytes_received, stdout); // Stampa su standard output
            received_size += bytes_received;
        }

        // Se il file contiene solo "file non presente", allora non è stato trovato, elimino il mio in locale
        // printf("\nquesto è il buffer: '%s' %d\n", buffer, strcmp(buffer, "file non presente") );         // DEBUG
        if (strcmp(buffer, "file non presente") == 0) {
            remove("output.txt");
        }
        else {
            // Rinomino il file come il file della richiesta
            rename("output.txt", filename);
        }

        fclose(fp);

        // Chiudi il socket
        close(sockfd);
    }

    return 0;
}
