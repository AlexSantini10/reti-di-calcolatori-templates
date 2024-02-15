#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/select.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <dirent.h>
#include <sys/stat.h>
#include <sys/types.h>

#define MAX_BUFFER_SIZE 1024
#define MAX_PRENOTAZIONI 7
#define COLUMNS 4
#define LIBERO "L"

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

    // Inizializzazione struttura dati prenotazioni
    char prenotazioni[MAX_PRENOTAZIONI][COLUMNS][MAX_BUFFER_SIZE];

    // Colonne: targa, patente, tipo veicolo (auto, camper), folder

    for (int i = 0; i < MAX_PRENOTAZIONI; i++)
    {
        strcpy(prenotazioni[i][0], LIBERO);
        strcpy(prenotazioni[i][1], "0");
        strcpy(prenotazioni[i][2], LIBERO);
        strcpy(prenotazioni[i][3], LIBERO);
    }

    // Inizializzazione per i test
    strcpy(prenotazioni[0][0], "AN745NL");
    strcpy(prenotazioni[0][1], "00003");
    strcpy(prenotazioni[0][2], "auto");
    strcpy(prenotazioni[0][3], "AN745NL_img");

    strcpy(prenotazioni[1][0], "FE457GF");
    strcpy(prenotazioni[1][1], "50006");
    strcpy(prenotazioni[1][2], "camper");
    strcpy(prenotazioni[1][3], "FE457GF_img");

    strcpy(prenotazioni[2][0], LIBERO);
    strcpy(prenotazioni[2][1], "0");
    strcpy(prenotazioni[2][2], LIBERO);
    strcpy(prenotazioni[2][3], LIBERO);

    strcpy(prenotazioni[3][0], LIBERO);
    strcpy(prenotazioni[3][1], "0");
    strcpy(prenotazioni[3][2], LIBERO);
    strcpy(prenotazioni[3][3], LIBERO);

    strcpy(prenotazioni[4][0], "NU547PL");
    strcpy(prenotazioni[4][1], "40063");
    strcpy(prenotazioni[4][2], "auto");
    strcpy(prenotazioni[4][3], "NU547PL_img");

    strcpy(prenotazioni[5][0], "LR897AH");
    strcpy(prenotazioni[5][1], "56832");
    strcpy(prenotazioni[5][2], "camper");
    strcpy(prenotazioni[5][3], "LR897AH_img");

    strcpy(prenotazioni[6][0], "MD506DW");
    strcpy(prenotazioni[6][1], "00100");
    strcpy(prenotazioni[6][2], "camper");
    strcpy(prenotazioni[6][3], "MD506DW_img");

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
            // Variabili per la gestione delle connessioni TCP
            struct sockaddr_in client_addr;
            socklen_t client_len = sizeof(client_addr);

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

                char buffer[MAX_BUFFER_SIZE];

                // Ciclo di gestione dei messaggi TCP
                while (1)
                {
                    // Ricevi il numero di targa dal client
                    int received_bytes = recv(client_socket, buffer, sizeof(buffer), 0);

                    if (received_bytes > 0 && received_bytes < sizeof(buffer))
                        buffer[received_bytes] = '\0';
                    else
                    {
                        // Connessione chiusa dal client
                        printf("[server TCP] (Client %s:%d) Connessione chiusa dal client\n", inet_ntoa(client_addr.sin_addr), ntohs(client_addr.sin_port));
                        close(client_socket);
                        exit(EXIT_SUCCESS);
                    }

                    printf("[server TCP] (Client %s:%d) Ricevuto: %s\n", inet_ntoa(client_addr.sin_addr), ntohs(client_addr.sin_port), buffer);

                    char dir[MAX_BUFFER_SIZE];
                    // Cerca la targa e recupero la directory
                    int found = 0;
                    for (int i = 0; i < MAX_PRENOTAZIONI; i++)
                    {
                        if (strcmp(prenotazioni[i][0], buffer) == 0)
                        {
                            found = 1;
                            strcpy(dir, prenotazioni[i][3]);
                            break;
                        }
                    }

                    if (found == 0)
                    {
                        printf("Targa non trovata\n");
                        send(client_socket, "KO", 2, 0);
                        continue;
                    }
                    else
                    {
                        printf("Targa trovata, la dir e %s\n", dir);
                        send(client_socket, "OK", 2, 0);
                    }

                    // Aspetto l'OK dal client
                    received_bytes = recv(client_socket, buffer, sizeof(buffer), 0);

                    if (received_bytes > 0 && received_bytes < sizeof(buffer))
                        buffer[received_bytes] = '\0';
                    else
                    {
                        // Connessione chiusa dal client
                        printf("[server TCP] (Client %s:%d) Connessione chiusa dal client\n", inet_ntoa(client_addr.sin_addr), ntohs(client_addr.sin_port));
                        close(client_socket);
                        exit(EXIT_SUCCESS);
                    }

                    // Apro la cartella e invio i file
                    DIR *directory = opendir(dir);
                    if (directory != NULL)
                    {
                        struct dirent *entry;
                        while ((entry = readdir(directory)) != NULL)
                        {
                            if (strcmp(entry->d_name, ".") != 0 && strcmp(entry->d_name, "..") != 0)
                            {
                                // Invio il file al client
                                printf("File: %s\n", entry->d_name);

                                // Invio il nome del file
                                send(client_socket, entry->d_name, strlen(entry->d_name), 0);

                                // Attendo l'OK dal client
                                received_bytes = recv(client_socket, buffer, sizeof(buffer), 0);

                                // Invio la dimensione del file
                                struct stat file_stat;
                                char file_path[MAX_BUFFER_SIZE] = "";
                                strcat(file_path, dir);
                                strcat(file_path, "/");
                                strcat(file_path, entry->d_name);

                                stat(file_path, &file_stat);
                                printf("Dimensione di %s: %ld\n", file_path, file_stat.st_size);

                                int file_size = file_stat.st_size;
                                char file_size_str[MAX_BUFFER_SIZE];
                                sprintf(file_size_str, "%d", file_size);

                                send(client_socket, file_size_str, strlen(file_size_str), 0);

                                // Attendo l'OK dal client
                                received_bytes = recv(client_socket, buffer, sizeof(buffer), 0);

                                // Invio il file
                                FILE *file = fopen(file_path, "r");
                                if (file == NULL)
                                {
                                    perror("Errore nell'apertura del file");
                                    exit(EXIT_FAILURE);
                                }

                                char file_buffer[MAX_BUFFER_SIZE];
                                int file_read = 0;
                                while ((file_read = fread(file_buffer, 1, sizeof(file_buffer), file)) > 0)
                                {
                                    send(client_socket, file_buffer, file_read, 0);
                                }

                                fclose(file);
                            }
                        }
                        closedir(directory);
                    }
                    else
                    {
                        printf("Errore nell'apertura della directory\n");
                    }

                    // Invio il messaggio di fine
                    send(client_socket, "FINE", 4, 0);
                }

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

            char buffer[MAX_BUFFER_SIZE];

            // Ricevi i dati dal client per UDP
            struct sockaddr_in client_addr;
            socklen_t client_len = sizeof(client_addr);

            int received_bytes = recvfrom(udp_socket, buffer, sizeof(buffer), 0, (struct sockaddr *)&client_addr, &client_len);

            if (received_bytes > 0 && received_bytes < sizeof(buffer))
                buffer[received_bytes] = '\0';
            printf("[server UDP] (Client %s:%d) Richiede aggiornamento: %s\n", inet_ntoa(client_addr.sin_addr), ntohs(client_addr.sin_port), buffer);

            char *token = strtok(buffer, "-");

            // Controllo se i dati sono OK
            char targa[MAX_BUFFER_SIZE];
            char patente[MAX_BUFFER_SIZE];

            if (token != NULL)
            {
                strcpy(targa, token);
                token = strtok(NULL, "-");
                strcpy(patente, token);

                printf("Targa: %s, Patente: %s\n", targa, patente);
            }
            else
            {
                sendto(udp_socket, "-1", 2, 0, (struct sockaddr *)&client_addr, client_len);
                continue;
            }

            int found = 0;
            // Cerco la targa e modifico la patente
            for (int i = 0; i < MAX_PRENOTAZIONI; i++)
            {
                if (strcmp(prenotazioni[i][0], targa) == 0)
                {
                    strcpy(prenotazioni[i][1], patente);
                    found = 1;
                    break;
                }
            }

            if (found == 1)
                sendto(udp_socket, "0", 1, 0, (struct sockaddr *)&client_addr, client_len);
            else
                sendto(udp_socket, "-1", 2, 0, (struct sockaddr *)&client_addr, client_len);
        }
        // Fine connessione UDP

    } // while

    // Chiudi i socket TCP e UDP
    close(tcp_socket);
    close(udp_socket);

    return 0;
}
