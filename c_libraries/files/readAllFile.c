#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int main(int argc, char *argv[]){
    
    FILE *file;
    char buffer[1024]; // Buffer per memorizzare i dati letti
    size_t elements_read;

    // Apertura del file in modalità lettura binaria
    file = fopen("file_to_read.txt", "rb");

    if (file == NULL) {
        printf("Impossibile aprire il file.\n");
        return 1;
    }

    // Legge dati dal file fino alla fine del file
    while ((elements_read = fread(buffer, sizeof(char), sizeof(buffer), file)) > 0) {
        // Stampa i dati letti
        printf("Numero di elementi letti: %zu\n", elements_read);
        buffer[elements_read] = '\0';
        printf("Dati letti: %s\n", buffer);
    }

    // Chiude il file
    fclose(file);

    return 0;
}