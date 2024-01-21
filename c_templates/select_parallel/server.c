#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define MAX_BUFFER_SIZE 1024

void handle_tcp_connection(int client_socket) {
    char buffer[MAX_BUFFER_SIZE];
    ssize_t bytes_received;

    while ((bytes_received = recv(client_socket, buffer, sizeof(buffer), 0)) > 0) {
        // Esegui il comando shell
        buffer[bytes_received] = '\0';

        FILE *command_output = popen(buffer, "r");
        if (command_output == NULL) {
            perror("popen");
            const char *error_message = "Errore nell'esecuzione del comando";
            send(client_socket, error_message, strlen(error_message), 0);
            break; // Esci dal ciclo in caso di errore
        }

        // Leggi l'output del comando
        char output_buffer[MAX_BUFFER_SIZE];
        size_t output_length;
        int sent = 0;
        while ((output_length = fread(output_buffer, 1, sizeof(output_buffer), command_output)) > 0) {
            if (sent == 0) 
                sent = 1;

            // Invia l'output al client
            int sent_bytes = send(client_socket, output_buffer, output_length, 0);
            if (sent_bytes < 0) {
                perror("send");
                break;
            }
        }

        // Controlla eventuali errori di lettura
        if (ferror(command_output)) {
            perror("fread");
            const char *error_message = "Errore nella lettura dell'output del comando";
            send(client_socket, error_message, strlen(error_message), 0);
        }

        // Chiudi il descrittore di file del comando
        int retVal = pclose(command_output);

        // Se non ho inviato niente, invio il valore di ritorno del comando
        if (sent == 0) {
            char output_buffer2[MAX_BUFFER_SIZE];
            sprintf(output_buffer2, "Return code: %d", retVal);
            send(client_socket, &output_buffer2, sizeof(output_buffer2), 0);
        }

        break; // Esci dal ciclo in ogni caso dopo aver gestito la singola richiesta
    }

    close(client_socket);
}




void handle_udp_connection(int udp_socket) {
    char buffer[MAX_BUFFER_SIZE];
    struct sockaddr_in client_addr;
    socklen_t client_len = sizeof(client_addr);

    ssize_t bytes_received = recvfrom(udp_socket, buffer, sizeof(buffer), 0, (struct sockaddr*)&client_addr, &client_len);
    if (bytes_received == -1) {
        perror("recvfrom");
        exit(EXIT_FAILURE);
    }

    // Execute the shell command
    buffer[bytes_received] = '\0';
    FILE *command_output = popen(buffer, "r");
    if (command_output == NULL) {
        perror("popen");
        exit(EXIT_FAILURE);
    }

    // Read the output of the command
    char output_buffer[MAX_BUFFER_SIZE];
    size_t output_length = fread(output_buffer, 1, sizeof(output_buffer) - 1, command_output);
    output_buffer[output_length] = '\0';

    // Close the command output
    int command_return_value = pclose(command_output);

    // Send the command output to the UDP client
    //sendto(udp_socket, output_buffer, output_length, 0, (struct sockaddr*)&client_addr, client_len);

    char output_buffer2[MAX_BUFFER_SIZE];
    sprintf(output_buffer2, "%d", command_return_value);
    printf("SONO ARRIVATO AL COMANDO DI RITORNO UDP %s\n", output_buffer2);

    // Send the return value of the command to the UDP client (optional)
    sendto(udp_socket, &output_buffer2, sizeof(output_buffer2), 0, (struct sockaddr*)&client_addr, client_len);

}


int main(int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <port>\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    int port = atoi(argv[1]);

    // Creazione del socket TCP
    int tcp_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (tcp_socket == -1) {
        perror("socket");
        exit(EXIT_FAILURE);
    }

    // Creazione del socket UDP
    int udp_socket = socket(AF_INET, SOCK_DGRAM, 0);
    if (udp_socket == -1) {
        perror("socket");
        close(tcp_socket);
        exit(EXIT_FAILURE);
    }

    // Inizializzazione dell'indirizzo del server per entrambi i socket
    struct sockaddr_in server_addr;
    memset(&server_addr, 0, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    server_addr.sin_port = htons(port);

    // Associazione dei socket all'indirizzo
    if (bind(tcp_socket, (struct sockaddr*)&server_addr, sizeof(server_addr)) == -1 ||
        bind(udp_socket, (struct sockaddr*)&server_addr, sizeof(server_addr)) == -1) {
        perror("bind");
        close(tcp_socket);
        close(udp_socket);
        exit(EXIT_FAILURE);
    }

    // Configurazione per l'ascolto delle connessioni TCP
    if (listen(tcp_socket, 5) == -1) {
        perror("listen");
        close(tcp_socket);
        close(udp_socket);
        exit(EXIT_FAILURE);
    }

    fd_set read_fds;
    int max_fd;

    while (1) {
        FD_ZERO(&read_fds);
        FD_SET(tcp_socket, &read_fds);
        FD_SET(udp_socket, &read_fds);
        max_fd = (tcp_socket > udp_socket) ? tcp_socket : udp_socket;

        // Select per gestire le connessioni TCP e UDP
        if (select(max_fd + 1, &read_fds, NULL, NULL, NULL) == -1) {
            perror("select");
            exit(EXIT_FAILURE);
        }

        if (FD_ISSET(tcp_socket, &read_fds)) {
            // Accetta una nuova connessione TCP
            struct sockaddr_in client_addr;
            socklen_t client_len = sizeof(client_addr);
            int client_socket = accept(tcp_socket, (struct sockaddr*)&client_addr, &client_len);
            if (client_socket == -1) {
                perror("accept");
                continue;
            }

            // Crea un processo figlio per gestire la connessione
            pid_t child_pid = fork();
            if (child_pid == -1) {
                perror("fork");
                close(client_socket);
            } else if (child_pid == 0) {
                // Processo figlio
                close(tcp_socket);
                close(udp_socket);
                handle_tcp_connection(client_socket);
                exit(EXIT_SUCCESS);
            } else {
                // Processo padre
                close(client_socket);
            }
        }

        if (FD_ISSET(udp_socket, &read_fds)) {
            // Gestisci una connessione UDP
            handle_udp_connection(udp_socket);
        }
    }

    // Chiudi i socket TCP e UDP
    close(tcp_socket);
    close(udp_socket);

    return 0;
}
