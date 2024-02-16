#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>

int main(int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Uso: %s [porta]\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    int serverSocket;
    struct sockaddr_in serverAddr, clientAddr;
    socklen_t addrSize = sizeof(struct sockaddr_in);

    serverSocket = socket(AF_INET, SOCK_DGRAM, 0);
    if (serverSocket == -1) {
        perror("Errore nella creazione del socket");
        exit(EXIT_FAILURE);
    }

    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(atoi(argv[1]));
    serverAddr.sin_addr.s_addr = INADDR_ANY;
    
    // Log the server address and port
    printf("Server avviato su %s:%d\n", inet_ntoa(serverAddr.sin_addr), ntohs(serverAddr.sin_port));

    if (bind(serverSocket, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) == -1) {
        perror("Errore nel binding del socket");
        close(serverSocket);
        exit(EXIT_FAILURE);
    }

    char nomeFile[1024];
    int lunghezzaMax;

    while (1) {
        memset(nomeFile, 0, sizeof(nomeFile));

        recvfrom(serverSocket, nomeFile, sizeof(nomeFile), 0, (struct sockaddr*)&clientAddr, &addrSize);

        FILE* file = fopen(nomeFile, "r");
        if (file == NULL) {
            perror("Errore nell'apertura del file");
            lunghezzaMax = -1;
        } else {
            char buffer[1024];
            char* separatori = " \n";
            char* token;
            lunghezzaMax = 0;

            while (fgets(buffer, sizeof(buffer), file) != NULL) {
                token = strtok(buffer, separatori);
                while (token != NULL) {
                    int lunghezzaCorrente = strlen(token);
                    if (lunghezzaCorrente > lunghezzaMax) {
                        lunghezzaMax = lunghezzaCorrente;
                    }
                    token = strtok(NULL, separatori);
                }
            }

            fclose(file);
        }

        sendto(serverSocket, &lunghezzaMax, sizeof(int), 0, (struct sockaddr*)&clientAddr, addrSize);

        printf("Richiesta da %s:%d per %s\n", inet_ntoa(clientAddr.sin_addr), ntohs(clientAddr.sin_port), nomeFile);
    }

    close(serverSocket);
    return 0;
}
