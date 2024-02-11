#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

int main(int argc, char *argv[]) {
    if (argc != 3) {
        fprintf(stderr, "Usage: %s <server_ip> <server_port>\n", argv[0]);
        return 1;
    }

    char *server_ip = argv[1];
    int server_port = atoi(argv[2]);
    int client_socket;
    struct sockaddr_in server_addr;

    // Inizializzazione del socket del client
    client_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (client_socket == -1) {
        perror("Errore nella creazione del socket del client");
        return 1;
    }

    // Configurazione dell'indirizzo del server
    memset(&server_addr, 0, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(server_port);
    inet_pton(AF_INET, server_ip, &(server_addr.sin_addr));

    // Connessione al server
    if (connect(client_socket, (struct sockaddr*)&server_addr, sizeof(server_addr)) == -1) {
        perror("Errore nella connessione al server");
        close(client_socket);
        return 1;
    }

    printf("Connesso al server %s:%d\n", server_ip, server_port);

    // Loop principale del client
    while (1) {
        // Lettura del messaggio da inviare
        char message[1024];
        printf("Inserisci un messaggio da inviare al server o exit: ");
        scanf("%s", message);

        // Chiusura del socket del client
        if (strcmp(message, "exit") == 0) {
            break;
        }

        // Invio del messaggio al server
        if (send(client_socket, message, strlen(message), 0) == -1) {
            perror("Errore nell'invio del messaggio al server");
            break;
        }

        // Ricezione della risposta dal server
        char response[1024];
        int response_len = read(client_socket, response, sizeof(response) - 1);

        if (response_len == -1) {
            perror("Errore nella ricezione della risposta dal server");
            break;
        }

        response[response_len] = '\0';
        printf("Risposta dal server: %s\n", response);
    }

    // Chiusura del socket del client (non raggiungerà mai questo punto)
    close(client_socket);

    return 0;
}
