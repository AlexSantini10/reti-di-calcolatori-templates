#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>

int main(int argc, char *argv[]) {
    if (argc != 3) {
        fprintf(stderr, "Uso: %s [indirizzo server] [porta server]\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    int clientSocket;
    struct sockaddr_in serverAddr;

    clientSocket = socket(AF_INET, SOCK_DGRAM, 0);
    if (clientSocket == -1) {
        perror("Errore nella creazione del socket");
        exit(EXIT_FAILURE);
    }

    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(atoi(argv[2]));
    serverAddr.sin_addr.s_addr = inet_addr(argv[1]);

    char nomeFile[1024];
    int lunghezzaParola;

    while (1) {
        printf("Inserisci il nome del file (o 'exit' per uscire): ");
        scanf("%s", nomeFile);

        if (strcmp(nomeFile, "exit") == 0) {
            break; // Uscire dal loop se l'utente inserisce 'exit'
        }

        sendto(clientSocket, nomeFile, strlen(nomeFile), 0, (struct sockaddr*)&serverAddr, sizeof(serverAddr));

        socklen_t addrSize = sizeof(serverAddr);
        recvfrom(clientSocket, &lunghezzaParola, sizeof(int), 0, (struct sockaddr*)&serverAddr, &addrSize);

        if (lunghezzaParola >= 0) {
            printf("La lunghezza della parola più lunga nel file è: %d\n", lunghezzaParola);
        } else {
            printf("Errore durante l'elaborazione del file.\n");
        }
    }

    close(clientSocket);
    return 0;
}
