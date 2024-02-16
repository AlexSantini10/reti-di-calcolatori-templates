#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define MAX_CLIENTS 10
#define PORT 8888

void handle_client(int client_socket);

int main() {
    int server_fd, client_socket;
    struct sockaddr_in address;
    int addrlen = sizeof(address);

    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
        perror("socket failed");
        exit(EXIT_FAILURE);
    }

    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons(PORT);

    if (bind(server_fd, (struct sockaddr *)&address, sizeof(address)) < 0) {
        perror("bind failed");
        exit(EXIT_FAILURE);
    }

    if (listen(server_fd, 3) < 0) {
        perror("listen");
        exit(EXIT_FAILURE);
    }

    printf("Server in ascolto sulla porta %d\n", PORT);

    while (1) {
        if ((client_socket = accept(server_fd, (struct sockaddr *)&address, (socklen_t*)&addrlen)) < 0) {
            perror("accept");
            exit(EXIT_FAILURE);
        }

        printf("Nuova connessione, socket fd: %d, ip: %s, porta: %d\n", client_socket, inet_ntoa(address.sin_addr), ntohs(address.sin_port));

        int pid = fork();

        if (pid < 0) {
            perror("fork");
            exit(EXIT_FAILURE);
        }

        if (pid == 0) {
            // Processo figlio
            close(server_fd);
            handle_client(client_socket);
            exit(0);
        } else {
            // Processo padre
            close(client_socket);
        }
    }

    return 0;
}

void handle_client(int client_socket) {
    char buffer[1024] = {0};
    int valread;

    while ((valread = read(client_socket, buffer, sizeof(buffer))) > 0) {
        printf("Messaggio ricevuto: %s\n", buffer);
        send(client_socket, buffer, strlen(buffer), 0);
        memset(buffer, 0, sizeof(buffer));
    }

    printf("Connessione chiusa.\n");
    close(client_socket);
}
