#include <fcntl.h>
#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <unistd.h>

int main(int argc, char** argv)
{
	int server_socket, client_socket;
	struct sockaddr_in server_address, client_address;

	// Apri un socket sulla porta 8080
	server_socket = socket(AF_INET, SOCK_STREAM, 0);
	if (server_socket == -1) {
		printf("Errore nell'apertura del socket");
		exit(EXIT_FAILURE);
	}

	// Imposta l'indirizzo del server
	memset(&server_address, 0, sizeof(server_address));
	server_address.sin_family = AF_INET;
	server_address.sin_port = htons(8080);
	server_address.sin_addr.s_addr = INADDR_ANY;

	// Collega il socket al nostro endpoint
	if (bind(server_socket, (struct sockaddr*)&server_address, sizeof(server_address)) != 0) {
		printf("Errore nella bind()");
		exit(EXIT_FAILURE);
	}

	// Ascolta le richieste in arrivo sul socket
	if (listen(server_socket, 5) != 0) {
		printf("Errore nella listen()");
		exit(EXIT_FAILURE);
	}

	// Ricevi una connessione dal client
	socklen_t client_lenght = sizeof(client_address);

	char* response[3];
	response[0] = "HTTP/1.1 200 OK\nContent-Type: text/html\n\n<html><h1>Hello</h1><p>you are connecting from port ";
	char ports[6];
	response[1] = ports;
	response[2] = " </p></html>";

	while (1) {
		client_socket = accept(server_socket, (struct sockaddr*)&client_address, &client_lenght);
		if (client_socket == -1) {
			printf("Errore nella accept()");
			exit(EXIT_FAILURE);
		}

		sprintf(response[1], "%i", ntohs(client_address.sin_port));
		printf("Client connected from port %i", ntohs(client_address.sin_port));

		// fork per inviare la risposta al client
		if (fork() == 0) {
			for (int i = 0; i < 3; i++)
				send(client_socket, response[i], strlen(response[i]), 0);
			shutdown(client_socket, SHUT_RDWR);
			exit(EXIT_SUCCESS);
		}
	}

	return 0;

	close(server_socket);

	return 0;
}
