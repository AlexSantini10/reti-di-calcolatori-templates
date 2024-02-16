#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <arpa/inet.h>
#include <unistd.h>

#define BUFFER_SIZE 1024

int main(int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <port>\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    int port = atoi(argv[1]);

    // Creazione del socket
    int server_socket = socket(AF_INET, SOCK_DGRAM, 0);
    if (server_socket == -1) {
        perror("Error creating socket");
        exit(EXIT_FAILURE);
    }

    // Configurazione dell'indirizzo del server
    struct sockaddr_in server_address;
    server_address.sin_family = AF_INET;
    server_address.sin_addr.s_addr = INADDR_ANY;
    server_address.sin_port = htons(port);

    // Binding del socket all'indirizzo del server
    if (bind(server_socket, (struct sockaddr *)&server_address, sizeof(server_address)) == -1) {
        perror("Error binding socket");
        close(server_socket);
        exit(EXIT_FAILURE);
    }

    printf("Server listening on port %d...\n", port);

    // Ricezione dei dati dal client
    char buffer[BUFFER_SIZE];
    struct sockaddr_in client_address;
    socklen_t client_address_len = sizeof(client_address);

    while (1) {
        // Ricezione dei dati dal client
        int bytes_received = recvfrom(server_socket, buffer, sizeof(buffer), 0,
                                      (struct sockaddr *)&client_address, &client_address_len);
        if (bytes_received == -1) {
            perror("Error receiving data");
            break;
        }

        // Stampa dei dati ricevuti dal client
        buffer[bytes_received] = '\0';
        printf("Received from client: %s\n", buffer);

        // Invio della risposta al client
        const char *response = "World";
        int bytes_sent = sendto(server_socket, response, strlen(response), 0,
                                (struct sockaddr *)&client_address, client_address_len);
        if (bytes_sent == -1) {
            perror("Error sending data");
            break;
        }

        printf("Sent to client: %s\n", response);
    }

    // Chiusura del socket del server
    close(server_socket);

    return 0;
}
