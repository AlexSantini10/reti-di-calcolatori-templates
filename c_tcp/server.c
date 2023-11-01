#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

int main(int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <port>\n", argv[0]);
        return 1;
    }

    int port = atoi(argv[1]);
    int server_socket, client_socket;
    struct sockaddr_in server_addr, client_addr;
    socklen_t client_len = sizeof(client_addr);

    // Inizializzazione del socket del server
    server_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (server_socket == -1) {
        perror("Errore nella creazione del socket del server");
        return 1;
    }

    // Configurazione dell'indirizzo del server
    memset(&server_addr, 0, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    server_addr.sin_port = htons(port);

    // Binding del socket
    if (bind(server_socket, (struct sockaddr*)&server_addr, sizeof(server_addr)) == -1) {
        perror("Errore nel binding del socket");
        close(server_socket);
        return 1;
    }

    // Ascolto del socket
    if (listen(server_socket, 5) == -1) {
        perror("Errore nell'ascolto del socket");
        close(server_socket);
        return 1;
    }

    printf("Server in ascolto sulla porta %d...\n", port);

    // Loop principale del server
    while (1) {
        // Accettazione di una nuova connessione
        client_socket = accept(server_socket, (struct sockaddr*)&client_addr, &client_len);
        if (client_socket == -1) {
            perror("Errore nell'accettare la connessione");
            continue;
        }

        // Gestione della connessione con il client
        // ...
        
        // Chiusura della connessione con il client
        close(client_socket);
    }

    // Chiusura del socket del server (non raggiungerà mai questo punto)
    close(server_socket);

    return 0;
}
