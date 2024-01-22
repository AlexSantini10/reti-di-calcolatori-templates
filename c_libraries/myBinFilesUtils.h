#ifndef MY_BIN_FILES_UTILS_H
#define MY_BIN_FILES_UTILS_H

#include <stdio.h>
#include <stdlib.h>


// Funzione per leggere un file binario e restituire il contenuto in un buffer
unsigned char* leggiFileBinario(const char* nomeFile, size_t* size) {
    FILE* file = fopen(nomeFile, "rb");
    if (file == NULL) {
        perror("Errore nell'apertura del file");
        exit(EXIT_FAILURE);
    }

    fseek(file, 0, SEEK_END);
    *size = ftell(file);
    fseek(file, 0, SEEK_SET);

    unsigned char* buffer = (unsigned char*)malloc(*size);
    if (buffer == NULL) {
        perror("Errore nell'allocazione della memoria");
        exit(EXIT_FAILURE);
    }

    fread(buffer, 1, *size, file);
    fclose(file);

    return buffer;
}

// Funzione per scrivere un buffer in un file binario
void scriviFileBinario(const char* nomeFile, const unsigned char* buffer, size_t size) {
    FILE* file = fopen(nomeFile, "wb");
    if (file == NULL) {
        perror("Errore nell'apertura del file");
        exit(EXIT_FAILURE);
    }

    fwrite(buffer, 1, size, file);
    fclose(file);
}

#endif