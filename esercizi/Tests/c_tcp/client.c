#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define MAX_BUFFER_SIZE 1024

void error(const char *msg) {
    perror(msg);
    exit(1);
}

int main(int argc, char *argv[]) {
    if (argc != 3) {
        fprintf(stderr, "Usage: %s <server_ip> <server_port>\n", argv[0]);
        exit(1);
    }

    char *server_ip = argv[1];
    int server_port = atoi(argv[2]);
    if (server_port <= 0 || server_port > 65535) {
        fprintf(stderr, "Invalid server port number\n");
        exit(1);
    }

    // Creazione del socket
    int client_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (client_socket == -1)
        error("Error opening socket");

    struct sockaddr_in server_address;
    memset(&server_address, 0, sizeof(server_address));

    server_address.sin_family = AF_INET;
    server_address.sin_port = htons(server_port);

    if (inet_pton(AF_INET, server_ip, &(server_address.sin_addr)) <= 0)
        error("Invalid server IP address");

    // Connessione al server
    if (connect(client_socket, (struct sockaddr *)&server_address, sizeof(server_address)) == -1)
        error("Error on connect");

    // Ricezione del messaggio dal server
    char buffer[MAX_BUFFER_SIZE];
    memset(buffer, 0, sizeof(buffer));

    if (recv(client_socket, buffer, sizeof(buffer) - 1, 0) == -1)
        error("Error on receive");

    printf("Message from server: %s\n", buffer);

    // Chiusura del socket del client
    close(client_socket);

    return 0;
}
