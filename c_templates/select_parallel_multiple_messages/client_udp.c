#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define MAX_BUFFER_SIZE 1024

int main(int argc, char *argv[])
{
    // Controllo del numero di argomenti
    if (argc != 3)
    {
        fprintf(stderr, "Usage: %s <server_ip> <port>\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    // Controllo che la porta sia un numero
    if (strspn(argv[2], "0123456789") != strlen(argv[2]))
    {
        fprintf(stderr, "La porta deve essere un numero\n");
        exit(EXIT_FAILURE);
    }

    // Cast degli argomenti
    char *server_ip = argv[1];
    int port = atoi(argv[2]);
    char message[MAX_BUFFER_SIZE];

    // Controllo del numero di porta
    if (port < 1024 || port > 65535)
    {
        fprintf(stderr, "Numero di porta non valido (1024-65535)\n");
        exit(EXIT_FAILURE);
    }

    // Create UDP socket
    int client_socket = socket(AF_INET, SOCK_DGRAM, 0);
    if (client_socket == -1)
    {
        perror("UDP socket creation failed");
        exit(EXIT_FAILURE);
    }

    // Variabili del server
    struct sockaddr_in server_addr;
    memset(&server_addr, 0, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = inet_addr(server_ip);
    server_addr.sin_port = htons(port);

    printf("Comunicazione col server %s:%d\n", server_ip, port);

    // TODO: Implementare la comunicazione con il server

    // Ciclo di comunicazione con il server
    while (1) {
        // Chiedi all'utente di inserire un messaggio
        printf("Inserisci un messaggio: ");
        fgets(message, MAX_BUFFER_SIZE, stdin);
        message[strlen(message) - 1] = '\0';

        // Send data to the server
        sendto(client_socket, message, strlen(message), 0, (struct sockaddr *)&server_addr, sizeof(server_addr));

        printf("Inviato al server: %s\n", message);

        char buffer[MAX_BUFFER_SIZE];
        struct sockaddr_in client_addr;
        socklen_t client_addr_len = sizeof(client_addr);

        // Receive data from the server
        ssize_t received_bytes = recvfrom(client_socket, buffer, sizeof(buffer), 0, (struct sockaddr *)&client_addr, &client_addr_len);
        if (received_bytes > 0)
        {
            buffer[received_bytes] = '\0';
            printf("Dati ricevuti dal server: %s\n", buffer);
        }
        else
        {
            perror("Failed to receive data from server");
        }
    } // while

    // Fine della comunicazione con il server

    // Close the socket
    close(client_socket);

    return 0;
}
