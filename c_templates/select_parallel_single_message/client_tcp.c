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
        fprintf(stderr, "Utilizzo: %s <server_ip> <port>\n", argv[0]);
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

    // Create TCP socket
    int client_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (client_socket == -1)
    {
        perror("[client TCP] creazione della socket fallita");
        exit(EXIT_FAILURE);
    }

    // Variabili del server
    struct sockaddr_in server_addr;
    memset(&server_addr, 0, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = inet_addr(server_ip);
    server_addr.sin_port = htons(port);

    // Connessione al server
    if (connect(client_socket, (struct sockaddr *)&server_addr, sizeof(server_addr)) == -1)
    {
        perror("[client TCP] connessione al server fallita");
        close(client_socket);
        exit(EXIT_FAILURE);
    }

    printf("Connessione al server eseguita %s:%d\n", server_ip, port);
    
    // Chiedi all'utente di inserire un messaggio
    printf("Inserisci un messaggio: ");
    fgets(message, MAX_BUFFER_SIZE, stdin);

    // Send data to the server
    send(client_socket, message, strlen(message), 0);

    printf("Inviato al server: %s\n", message);

    char buffer[MAX_BUFFER_SIZE];

    // Receive data from the server
    ssize_t received_bytes = recv(client_socket, buffer, sizeof(buffer), 0);
    if (received_bytes > 0)
    {
        buffer[received_bytes] = '\0';
        printf("Dati ricevuti dal server: %s\n", buffer);
    }
    else
    {
        printf("Nessun dato ricevuto dal server\n");
    }

    // Close the socket
    close(client_socket);

    return 0;
}
