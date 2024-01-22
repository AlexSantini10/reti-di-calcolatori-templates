#ifndef MYFILESUNIX_H
#define MYFILESUNIX_H

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

int openFile(const char *filename, const char *mode) {
    FILE *file = fopen(filename, mode);
    if (file == NULL) {
        handleError("Errore durante l'apertura del file");
        return -1; // Restituisce -1 in caso di errore
    }
    return fileno(file); // Restituisce il descrittore di file associato
}

void closeFile(int fileDescriptor) {
    if (close(fileDescriptor) == -1) {
        handleError("Errore durante la chiusura del file");
    }
}

char *readFileToString(const char *filename) {
    FILE *file = fopen(filename, "r");
    if (file == NULL) {
        handleError("Errore durante l'apertura del file");
        return NULL;
    }

    fseek(file, 0, SEEK_END);
    long fileSize = ftell(file);
    fseek(file, 0, SEEK_SET);

    char *content = (char *)malloc((fileSize + 1) * sizeof(char));
    if (content == NULL) {
        handleError("Errore durante l'allocazione della memoria");
        fclose(file);
        return NULL;
    }

    fread(content, sizeof(char), fileSize, file);
    content[fileSize] = '\0'; // Aggiunge il terminatore di stringa
    fclose(file);

    return content;
}

void writeStringToFile(const char *filename, const char *content) {
    FILE *file = fopen(filename, "w");
    if (file == NULL) {
        handleError("Errore durante l'apertura del file");
        return;
    }

    fprintf(file, "%s", content);
    fclose(file);
}

size_t readFileToBuffer(const char *filename, void *buffer, size_t bufferSize) {
    FILE *file = fopen(filename, "rb");
    if (file == NULL) {
        handleError("Errore durante l'apertura del file");
        return 0;
    }

    size_t bytesRead = fread(buffer, 1, bufferSize, file);
    fclose(file);

    return bytesRead;
}

void writeBufferToFile(const char *filename, const void *buffer, size_t bufferSize) {
    FILE *file = fopen(filename, "wb");
    if (file == NULL) {
        handleError("Errore durante l'apertura del file");
        return;
    }

    fwrite(buffer, 1, bufferSize, file);
    fclose(file);
}

void seekFile(int fileDescriptor, long offset, int whence) {
    if (lseek(fileDescriptor, offset, whence) == -1) {
        handleError("Errore durante lo spostamento nel file");
    }
}

int fileExists(const char *filename) {
    FILE *file = fopen(filename, "r");
    if (file != NULL) {
        fclose(file);
        return 1; // Restituisce 1 se il file esiste
    }
    return 0; // Restituisce 0 se il file non esiste
}

size_t getFileSize(const char *filename) {
    FILE *file = fopen(filename, "r");
    if (file == NULL) {
        handleError("Errore durante l'apertura del file");
        return 0;
    }

    fseek(file, 0, SEEK_END);
    size_t fileSize = ftell(file);
    fclose(file);

    return fileSize;
}

int isDirectory(const char *path) {
    // Implementazione semplificata: verifica se il percorso termina con un separatore di directory
    size_t pathLength = strlen(path);
    if (pathLength > 0 && (path[pathLength - 1] == '/' || path[pathLength - 1] == '\\')) {
        return 1; // Restituisce 1 se il percorso è una directory
    }
    return 0; // Restituisce 0 se il percorso non è una directory
}

void handleError(const char *errorMessage) {
    perror(errorMessage);
}

char *getAbsolutePath(const char *relativePath) {
    // Implementazione semplificata: restituisce il percorso relativo come percorso assoluto
    return realpath(relativePath, NULL);
}

char *readLineFromFile(const char *filename) {
    FILE *file = fopen(filename, "r");
    if (file == NULL) {
        handleError("Errore durante l'apertura del file");
        return NULL;
    }

    char *line = NULL;
    size_t len = 0;

    if (getline(&line, &len, file) == -1) {
        handleError("Errore durante la lettura della linea");
        free(line);
        fclose(file);
        return NULL;
    }

    fclose(file);
    return line;
}

void writeLineToFile(const char *filename, const char *line) {
    FILE *file = fopen(filename, "a");
    if (file == NULL) {
        handleError("Errore durante l'apertura del file");
        return;
    }

    fprintf(file, "%s\n", line);
    fclose(file);
}

int createDirectory(const char *path) {
    // Implementazione semplificata: crea la directory se non esiste
    if (mkdir(path, 0777) == -1) {
        handleError("Errore durante la creazione della directory");
        return 0; // Restituisce 0 se si verifica un errore
    }
    return 1; // Restituisce 1 se la directory è stata creata con successo
}

int removeDirectory(const char *path) {
    // Implementazione semplificata: rimuove la directory vuota
    if (rmdir(path) == -1) {
        handleError("Errore durante la rimozione della directory");
        return 0; // Restituisce 0 se si verifica un errore
    }
    return 1; // Restituisce 1 se la directory è stata rimossa con successo
}

#endif /* MYFILESUNIX_H */
