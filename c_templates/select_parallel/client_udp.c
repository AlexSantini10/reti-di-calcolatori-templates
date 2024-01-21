#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define MAX_BUFFER_SIZE 1024

int main(int argc, char *argv[]) {
    if (argc != 3) {
        fprintf(stderr, "Usage: %s <server_ip> <port>\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    char *server_ip = argv[1];
    int port = atoi(argv[2]);
    char message[MAX_BUFFER_SIZE];

    // Create UDP socket
    int client_socket = socket(AF_INET, SOCK_DGRAM, 0);
    if (client_socket == -1) {
        perror("UDP socket creation failed");
        exit(EXIT_FAILURE);
    }

    // Chiedi all'utente di inserire un messaggio
    printf("Inserisci un comando: ");
    fgets(message, MAX_BUFFER_SIZE, stdin);
    message[strlen(message) - 1] = '\0';

    struct sockaddr_in server_addr;
    memset(&server_addr, 0, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = inet_addr(server_ip);
    server_addr.sin_port = htons(port);

    printf("Connected to server %s:%d\n", server_ip, port);

    // Send data to the server
    sendto(client_socket, message, strlen(message), 0, (struct sockaddr*)&server_addr, sizeof(server_addr));

    printf("Sent to server: %s\n", message);

    char buffer[MAX_BUFFER_SIZE];
    struct sockaddr_in client_addr;
    socklen_t client_addr_len = sizeof(client_addr);

    // Receive data from the server
    ssize_t received_bytes = recvfrom(client_socket, buffer, sizeof(buffer), 0, (struct sockaddr*)&client_addr, &client_addr_len);
    if (received_bytes > 0) {
        buffer[received_bytes] = '\0';
        printf("Received from server: %s\n", buffer);
    } else {
        perror("Failed to receive data from server");
    }

    // Close the socket
    close(client_socket);

    return 0;
}
