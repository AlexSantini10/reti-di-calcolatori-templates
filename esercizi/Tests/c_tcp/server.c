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
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <port>\n", argv[0]);
        exit(1);
    }

    int port = atoi(argv[1]);
    if (port <= 0 || port > 65535) {
        fprintf(stderr, "Invalid port number\n");
        exit(1);
    }

    // Creazione del socket
    int server_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (server_socket == -1)
        error("Error opening socket");

    struct sockaddr_in server_address;
    memset(&server_address, 0, sizeof(server_address));

    server_address.sin_family = AF_INET;
    server_address.sin_addr.s_addr = INADDR_ANY;
    server_address.sin_port = htons(port);

    // Binding del socket
    if (bind(server_socket, (struct sockaddr *)&server_address, sizeof(server_address)) == -1)
        error("Error on binding");

    // Ascolto delle connessioni in ingresso
    if (listen(server_socket, 5) == -1)
        error("Error on listen");

    printf("Server listening on port %d...\n", port);

    while (1) {
        // Accettazione di una connessione
        int client_socket = accept(server_socket, NULL, NULL);
        if (client_socket == -1)
            error("Error on accept");

        // Invio del messaggio di benvenuto al client
        const char *message = "Hello, client! This is the server.";
        send(client_socket, message, strlen(message), 0);

        // Chiusura del socket del client
        close(client_socket);
    }

    // Chiusura del socket del server
    close(server_socket);

    return 0;
}
