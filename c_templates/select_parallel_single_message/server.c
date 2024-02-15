#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/select.h>

#define MAX_BUFFER_SIZE 1024

int main(int argc, char *argv[])
{
    // Controllo del numero di argomenti
    if (argc != 2)
    {
        fprintf(stderr, "Utilizzo: %s <port>\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    // Conversione della porta in intero
    int port = atoi(argv[1]);

    // Controllo del numero di porta
    if (port < 1024 || port > 65535)
    {
        fprintf(stderr, "Numero di porta non valido (1024-65535)\n");
        exit(EXIT_FAILURE);
    }

    // Creazione del socket TCP
    int tcp_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (tcp_socket == -1)
    {
        // Errore nella creazione del socket
        perror("[server TCP]");
        exit(EXIT_FAILURE);
    }

    // Creazione del socket UDP
    int udp_socket = socket(AF_INET, SOCK_DGRAM, 0);
    if (udp_socket == -1)
    {
        // Errore nella creazione del socket
        perror("[server UDP]");
        exit(EXIT_FAILURE);
    }

    // Inizializzazione dell'indirizzo del server per entrambi i socket
    struct sockaddr_in server_addr;
    memset(&server_addr, 0, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    server_addr.sin_port = htons(port);

    // Associazione dei socket all'indirizzo
    if (bind(tcp_socket, (struct sockaddr *)&server_addr, sizeof(server_addr)) == -1 ||
        bind(udp_socket, (struct sockaddr *)&server_addr, sizeof(server_addr)) == -1)
    {
        perror("[server] bind");
        close(tcp_socket);
        close(udp_socket);
        exit(EXIT_FAILURE);
    }

    // Configurazione per l'ascolto delle connessioni TCP
    if (listen(tcp_socket, 5) == -1)
    {
        perror("[server] listen");
        close(tcp_socket);
        close(udp_socket);
        exit(EXIT_FAILURE);
    }

    // Variabili per la gestione delle connessioni
    fd_set read_fds;
    int max_fd;
    int client_socket;
    pid_t child_pid;

    // Variabili per la gestione delle connessioni TCP
    struct sockaddr_in client_addr;
    socklen_t client_len = sizeof(client_addr);

    // Notifica l'avvenuta creazione dei socket e l'attesa di connessioni
    printf("Server in ascolto su %s:%d\n", inet_ntoa(server_addr.sin_addr), ntohs(server_addr.sin_port));

    // Ciclo di gestione delle connessioni
    while (1)
    {
        FD_ZERO(&read_fds);
        FD_SET(tcp_socket, &read_fds);
        FD_SET(udp_socket, &read_fds);
        max_fd = (tcp_socket > udp_socket) ? tcp_socket : udp_socket;

        // Select per gestire le connessioni TCP e UDP
        if (select(max_fd + 1, &read_fds, NULL, NULL, NULL) == -1)
        {
            perror("[server] select");
            exit(EXIT_FAILURE);
        }

        // Connessione TCP
        if (FD_ISSET(tcp_socket, &read_fds))
        {
            // Accetta una nuova connessione TCP
            client_socket = accept(tcp_socket, (struct sockaddr *)&client_addr, &client_len);
            if (client_socket == -1)
            {
                perror("[server] accept");
                continue;
            }

            // Notifica l'avvenuta connessione TCP
            printf("Connessione accettata (Client %s:%d) \n", inet_ntoa(client_addr.sin_addr), ntohs(client_addr.sin_port));

            // Crea un processo figlio per gestire la connessione TCP
            child_pid = fork();
            if (child_pid == -1)
            {
                perror("[server] fork");
                close(client_socket);
            }
            else if (child_pid == 0)
            {
                // Processo figlio
                close(tcp_socket);
                close(udp_socket);

                // TODO: Gestisci la connessione TCP

                char buffer[MAX_BUFFER_SIZE];

                // Ricevi i dati dal client
                int received_bytes = recv(client_socket, buffer, sizeof(buffer), 0);

                if (received_bytes > 0)
                    buffer[received_bytes] = '\0';
                else{
                    // Connessione chiusa dal client
                    printf("[server TCP] (Client %s:%d) Connessione chiusa dal client\n", inet_ntoa(client_addr.sin_addr), ntohs(client_addr.sin_port));
                    close(client_socket);
                    exit(EXIT_SUCCESS);
                }

                printf("[server TCP] Ricezione dal client: %s\n", buffer);

                send(client_socket, "Hello, client!", 14, 0);

                // Chiudi la connessione
                close(client_socket);
                printf("[server TCP] (Client %s:%d) Connessione chiusa\n", inet_ntoa(client_addr.sin_addr), ntohs(client_addr.sin_port));
                exit(EXIT_SUCCESS);
            }
            else
            {
                // Processo padre
                close(client_socket);
            }
        }
        // Fine connessione TCP



        // Connessione UDP
        if (FD_ISSET(udp_socket, &read_fds))
        {
            // Gestisci una connessione UDP
            // TODO: Gestisci la connessione UDP

            char buffer[MAX_BUFFER_SIZE];

            // Ricevi i dati dal client per UDP // TODO: Spostare fuori dal while
            struct sockaddr client_addr;
            socklen_t client_len = sizeof(client_addr);

            int received_bytes = recvfrom(udp_socket, buffer, sizeof(buffer), 0, (struct sockaddr *)&client_addr, &client_len);

            buffer[received_bytes] = '\0';
            printf("[server UDP] Ricezione dal client: %s\n", buffer);

            sendto(udp_socket, "Hello, client!", 14, 0, (struct sockaddr *)&client_addr, client_len);
        }
        // Fine connessione UDP

    } // while

    // Chiudi i socket TCP e UDP
    close(tcp_socket);
    close(udp_socket);

    return 0;
}
