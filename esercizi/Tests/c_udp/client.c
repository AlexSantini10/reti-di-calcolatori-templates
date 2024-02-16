#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <arpa/inet.h>
#include <unistd.h>

#define BUFFER_SIZE 1024

int main(int argc, char *argv[]) {
    if (argc != 3) {
        fprintf(stderr, "Usage: %s <server_ip> <server_port>\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    const char *server_ip = argv[1];
    int server_port = atoi(argv[2]);

    // Creazione del socket
    int client_socket = socket(AF_INET, SOCK_DGRAM, 0);
    if (client_socket == -1) {
        perror("Error creating socket");
        exit(EXIT_FAILURE);
    }

    // Configurazione dell'indirizzo del server
    struct sockaddr_in server_address;
    server_address.sin_family = AF_INET;
    server_address.sin_addr.s_addr = inet_addr(server_ip);
    server_address.sin_port = htons(server_port);

    // Invio della richiesta al server
    const char *request = "Hello";
    int bytes_sent = sendto(client_socket, request, strlen(request), 0,
                            (struct sockaddr *)&server_address, sizeof(server_address));
    if (bytes_sent == -1) {
        perror("Error sending data");
        close(client_socket);
        exit(EXIT_FAILURE);
    }

    printf("Sent to server: %s\n", request);

    // Ricezione della risposta dal server
    char buffer[BUFFER_SIZE];
    int bytes_received = recvfrom(client_socket, buffer, sizeof(buffer), 0, NULL, NULL);
    if (bytes_received == -1) {
        perror("Error receiving data");
        close(client_socket);
        exit(EXIT_FAILURE);
    }

    // Stampa della risposta ricevuta dal server
    buffer[bytes_received] = '\0';
    printf("Received from server: %s\n", buffer);

    // Chiusura del socket del client
    close(client_socket);

    return 0;
}
