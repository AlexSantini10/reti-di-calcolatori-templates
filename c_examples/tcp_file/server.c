#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>

int main(int argc, char* argv[]){
    // Controllo argomenti
    if(argc != 2){
        printf("Usage: %s <port>\n", argv[0]);
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

    // Loop di accettazione delle connessioni e gestione dei client
    while (1){
        // Accettazione della connessione
        client_socket = accept(server_socket, (struct sockaddr*)&client_addr, &client_len);

        // Crea un processo figlio per gestire la connessione
        pid_t child_pid = fork();
        if (child_pid == -1) {
            perror("fork");
            close(client_socket);
        } else if (child_pid == 0) {
            // Processo figlio
            if (client_socket == -1) {
                perror("Errore nell'accettare la connessione");
                continue;
            }

            printf("Connessione accettata da %s:%d\n", inet_ntoa(client_addr.sin_addr), ntohs(client_addr.sin_port));

            while (1){
                // Ricezione e stampa del messaggio del client
                char buffer[1024];
                int bytes_received = read(client_socket, buffer, sizeof(buffer));

                if (bytes_received == -1) {
                    perror("Errore nella ricezione del messaggio");
                } 
                else if (bytes_received == 0) {
                    printf("Il client %d ha chiuso la connessione\n", ntohs(client_addr.sin_port));
                    break;
                }
                else {
                    buffer[bytes_received] = '\0';
                    printf("File richiesto: %s\n", buffer);
                }

                FILE *file = fopen(buffer, "r");

                if (file == NULL) {
                    perror("Errore nell'apertura del file");
                    write(client_socket, "NOT_FOUND", 9);
                    continue;
                } else {
                    char file_buffer[1024];
                    int bytes_read;

                    write(client_socket, "OK", 2);
                    read(client_socket, buffer, sizeof(buffer));

                    while ((bytes_read = fread(file_buffer, 1, sizeof(file_buffer), file)) > 0) {
                        printf("Invio di %d byte\n", bytes_read);
                        if (write(client_socket, file_buffer, bytes_read) == -1) {
                            perror("Errore nell'invio del file");
                            break;
                        }
                        
                        read(client_socket, buffer, sizeof(buffer));
                    }


                    fclose(file);
                }

                write(client_socket, "FINE", 4);
            }


            close(client_socket);
            exit(EXIT_SUCCESS);
        } else {
            // Processo padre
            close(client_socket);
        }

        

    }

    return 0;
}