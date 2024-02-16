#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>

#define PORT 8080

int main(int argc, char const *argv[]) {
    int sock = 0;
    struct sockaddr_in serv_addr;
    char *hello = "Ciao dal client!";
    char buffer[1024] = {0};

    // Creazione del socket
    if ((sock = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        perror("socket creation error");
        return -1;
    }

    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(PORT);

    // Converte l'indirizzo IPv4 e lo assegna alla struttura serv_addr
    if(inet_pton(AF_INET, "127.0.0.1", &serv_addr.sin_addr)<=0) {
        perror("indirizzo non valido o supporto non fornito");
        return -1;
    }

    // Connessione al server
    if (connect(sock, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0) {
        perror("connessione fallita");
        return -1;
    }

    // Invia un messaggio al server
    send(sock, hello, strlen(hello), 0);

    // Ricevi e stampa la risposta del server
    read(sock, buffer, 1024);
    printf("%s\n", buffer);
    return 0;
}
