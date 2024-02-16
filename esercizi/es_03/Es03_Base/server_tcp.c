#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define MAXLINE 1024

int main(int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s [port]\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    int port = atoi(argv[1]);

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

    while (1) {
        connfd = accept(sockfd, (struct sockaddr*)&cli, &len);
        if (connfd < 0) {
            perror("server accept failed");
            exit(EXIT_FAILURE);
        }
        else
            printf("server accept the client...\n");

        char filename[MAXLINE];
        int line_number;
        recv(connfd, filename, sizeof(filename), 0);
        recv(connfd, &line_number, sizeof(line_number), 0);

        printf("Received request to remove line %d from file %s\n", line_number, filename);

        FILE *fp = fopen(filename, "r");
        if (fp == NULL) {
            char error_message[] = "Errore: file non presente";
            send(connfd, error_message, strlen(error_message), 0);
            close(connfd);
            continue;
        }

        char temp_file[] = "temp.txt";
        FILE *temp_fp = fopen(temp_file, "w");
        if (temp_fp == NULL) {
            perror("Error creating temporary file");
            fclose(fp);
            exit(EXIT_FAILURE);
        }

        char buffer[MAXLINE];
        int current_line = 1;

        while (fgets(buffer, MAXLINE, fp) != NULL) {
            if (current_line != line_number) {
                fputs(buffer, temp_fp);
            }
            current_line++;
        }

        fclose(fp);
        fclose(temp_fp);

        remove(filename);
        rename(temp_file, filename);

        fp = fopen(filename, "r");
        if (fp == NULL) {
            perror("Error opening file");
            exit(EXIT_FAILURE);
        }

        fseek(fp, 0, SEEK_END);
        long file_size = ftell(fp);
        fseek(fp, 0, SEEK_SET);
        send(connfd, &file_size, sizeof(file_size), 0);

        while (fgets(buffer, MAXLINE, fp) != NULL) {
            send(connfd, buffer, strlen(buffer), 0);
        }

        fclose(fp);
        close(connfd);
    }

    close(sockfd);

    return 0;
}
