#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/stat.h>

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

    // TODO: Implementare la comunicazione con il server

    // Ciclo di comunicazione con il server
    while (1)
    {
        // Chiedi all'utente di inserire un messaggio
        printf("Inserisci la targa del veicolo del quale vuoi ricevere le immagini: ");
        scanf("%s", message);

        // Se ha fatto EOF, esci dal ciclo
        if (feof(stdin))
        {
            printf("\n--- Comunicazione terminata ---\n");
            break;
        }

        char folder[MAX_BUFFER_SIZE];
        strcpy(folder, message);
        strcat(folder, "_client_img");

        mkdir(folder, 0777);

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

            if (strcmp(buffer, "OK") == 0)
            {
                printf("Ricevuto OK dal server\n");
                printf("Ricezione immagini in corso...\n");

                char img_name[MAX_BUFFER_SIZE];
                char img_path[MAX_BUFFER_SIZE];
                char img_size_str[MAX_BUFFER_SIZE];
                int img_size = -1;

                // Invio OK al server
                send(client_socket, "OK", 2, 0);

                while (1)
                {
                    // Resetto i buffer
                    memset(img_name, 0, sizeof(img_name));
                    memset(img_path, 0, sizeof(img_path));
                    memset(img_size_str, 0, sizeof(img_size_str));

                    // Receive data from the server
                    received_bytes = recv(client_socket, img_name, sizeof(img_name), 0);
                    if (received_bytes > 0)
                    {
                        img_name[received_bytes] = '\0';
                        printf("Nome immagine ricevuto dal server: %s\n", img_name);

                        // Se ricevo "FINE" esco dal ciclo
                        if (strcmp(img_name, "FINE") == 0)
                        {
                            printf("Ricevuto FINE dal server\n");
                            break;
                        }

                        // Invio OK al server
                        send(client_socket, "OK", 2, 0);
                    }
                    else
                    {
                        printf("Nessun dato ricevuto dal server\n");
                        break;
                    }

                    // Receive data from the server
                    received_bytes = recv(client_socket, img_size_str, sizeof(img_size_str), 0);
                    if (received_bytes > 0)
                    {
                        printf("Dimensione immagine ricevuta dal server: %s\n", img_size_str);
                        img_size = atoi(img_size_str);
                    }
                    else
                    {
                        printf("Nessun dato ricevuto dal server\n");
                        break;
                    }

                    // Invio OK al server
                    send(client_socket, "OK", 2, 0);

                    strcpy(img_path, folder);
                    strcat(img_path, "/");
                    strcat(img_path, img_name);

                    printf("Salvataggio immagine %s in corso...\n", img_path);

                    FILE *img = fopen(img_path, "w");
                    if (img == NULL)
                    {
                        perror("Errore nell'apertura del file");
                        exit(EXIT_FAILURE);
                    }

                    // TODO: Ricevere l'immagine dal server e salvarla su disco
                    char img_buffer[MAX_BUFFER_SIZE];
                    int img_received = 0;
                    while (img_received < img_size)
                    {
                        received_bytes = recv(client_socket, img_buffer, sizeof(img_buffer), 0);
                        if (received_bytes > 0)
                        {
                            fwrite(img_buffer, sizeof(char), received_bytes, img);
                            img_received += received_bytes;
                        }
                        else
                        {
                            printf("Nessun dato ricevuto dal server\n");
                            break;
                        }
                    }

                    fclose(img);
                }
            }
            else
            {
                printf("Ricevuto KO dal server\n");
            }
        }
        else
        {
            printf("Nessun dato ricevuto dal server\n");
        }
    } // while

    // Fine della comunicazione con il server

    // Close the socket
    close(client_socket);

    return 0;
}
