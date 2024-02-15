#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int main(int argc, char *argv[]){
    
    FILE *file;
    char buffer[1024]; // Buffer per memorizzare i dati letti
    size_t elements_read;

    // Apertura del file in modalità lettura binaria
    file = fopen("file_to_write.txt", "w");

    if (file == NULL) {
        printf("Impossibile aprire il file.\n");
        return 1;
    }

    // Scrive dati nel file
    fwrite("Ciao\n", sizeof(char), 5, file);

    // Chiude il file
    fclose(file);

    return 0;
}