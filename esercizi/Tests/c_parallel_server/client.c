#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define PORT 8888

int main() {
    int sock = 0;
    struct sockaddr_in server_address;
    char buffer[1024] = {0};

    if ((sock = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        printf("\n Errore nella creazione del socket \n");
        return -1;
    }

    server_address.sin_family = AF_INET;
    server_address.sin_port = htons(PORT);

    if (inet_pton(AF_INET, "127.0.0.1", &server_address.sin_addr) <= 0) {
        printf("\nIndirizzo non valido/indirizzo non supportato \n");
        return -1;
    }

    if (connect(sock, (struct sockaddr *)&server_address, sizeof(server_address)) < 0) {
        printf("\n Connessione fallita \n");
        return -1;
    }

    char *message = "Ciao, sono il client!";
    send(sock, message, strlen(message), 0);
    printf("Messaggio inviato\n");

    int valread = read(sock, buffer, sizeof(buffer));
    printf("Messaggio ricevuto: %s\n", buffer);

    return 0;
}
