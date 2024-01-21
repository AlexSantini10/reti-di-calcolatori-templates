#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>

// Funzione di creazione di un file vuoto
void createFile(const char *filePath) {
    FILE *file = fopen(filePath, "w");
    if (file != NULL) {
        fclose(file);
    } else {
        perror("Errore nella creazione del file");
    }
}

// Funzione di creazione di una directory
void createDirectory(const char *dirPath) {
    if (mkdir(dirPath, 0777) != 0) {
        perror("Errore nella creazione della directory");
    }
}

// Funzione di cancellazione di un file
void deleteFile(const char *filePath) {
    if (remove(filePath) != 0) {
        perror("Errore nella cancellazione del file");
    }
}

// Funzione di cancellazione di una directory
void deleteDirectory(const char *dirPath) {
    if (rmdir(dirPath) != 0) {
        perror("Errore nella cancellazione della directory");
    }
}

// Funzione di lettura del contenuto di un file come stringa
char *readFileAsString(const char *filePath) {
    FILE *file = fopen(filePath, "rb");
    if (file != NULL) {
        fseek(file, 0, SEEK_END);
        long size = ftell(file);
        rewind(file);

        char *content = (char *)malloc(size + 1);
        if (content != NULL) {
            fread(content, 1, size, file);
            content[size] = '\0';  // Aggiungi il terminatore di stringa
        } else {
            perror("Errore nell'allocazione di memoria per il contenuto del file");
        }

        fclose(file);
        return content;
    } else {
        perror("Errore nell'apertura del file per la lettura");
        return NULL;
    }
}

// Funzione di scrittura di una stringa su un file
void writeFileString(const char *filePath, const char *fileContent) {
    FILE *file = fopen(filePath, "wb");
    if (file != NULL) {
        fprintf(file, "%s", fileContent);
        fclose(file);
    } else {
        perror("Errore nell'apertura del file per la scrittura");
    }
}

// Funzione di append di una stringa su un file
void appendFileString(const char *filePath, const char *fileContent) {
    FILE *file = fopen(filePath, "ab");
    if (file != NULL) {
        fprintf(file, "%s", fileContent);
        fclose(file);
    } else {
        perror("Errore nell'apertura del file per l'append");
    }
}

// Funzione di lettura del contenuto di un file come array di stringhe
char **readFileAsArray(const char *filePath, int *lineCount) {
    FILE *file = fopen(filePath, "r");
    if (file != NULL) {
        char buffer[1024];
        int count = 0;
        while (fgets(buffer, sizeof(buffer), file) != NULL) {
            count++;
        }
        rewind(file);

        char **content = (char **)malloc(count * sizeof(char *));
        if (content != NULL) {
            for (int i = 0; i < count; i++) {
                content[i] = (char *)malloc(1024);
                if (content[i] == NULL) {
                    perror("Errore nell'allocazione di memoria per il contenuto del file");
                    for (int j = 0; j < i; j++) {
                        free(content[j]);
                    }
                    free(content);
                    fclose(file);
                    return NULL;
                }
            }

            int i = 0;
            while (fgets(content[i], 1024, file) != NULL) {
                content[i][strcspn(content[i], "\n")] = '\0';  // Rimuovi il carattere di nuova linea
                i++;
            }

            *lineCount = count;
        } else {
            perror("Errore nell'allocazione di memoria per il contenuto del file");
        }

        fclose(file);
        return content;
    } else {
        perror("Errore nell'apertura del file per la lettura");
        return NULL;
    }
}

// Funzione di scrittura di un array di stringhe su un file
void writeFileStringArray(const char *filePath, char **fileContent, int lineCount) {
    FILE *file = fopen(filePath, "w");
    if (file != NULL) {
        for (int i = 0; i < lineCount; i++) {
            fprintf(file, "%s\n", fileContent[i]);
        }
        fclose(file);
    } else {
        perror("Errore nell'apertura del file per la scrittura");
    }
}

// Funzione di lettura del contenuto di un file come array di byte
unsigned char *readFileAsByteArray(const char *filePath, long *fileSize) {
    FILE *file = fopen(filePath, "rb");
    if (file != NULL) {
        fseek(file, 0, SEEK_END);
        *fileSize = ftell(file);
        rewind(file);

        unsigned char *content = (unsigned char *)malloc(*fileSize);
        if (content != NULL) {
            fread(content, 1, *fileSize, file);
        } else {
            perror("Errore nell'allocazione di memoria per il contenuto del file");
        }

        fclose(file);
        return content;
    } else {
        perror("Errore nell'apertura del file per la lettura");
        return NULL;
    }
}

// Funzione di scrittura di un array di byte su un file
void writeFileByteArray(const char *filePath, unsigned char *fileContent, long fileSize) {
    FILE *file = fopen(filePath, "wb");
    if (file != NULL) {
        fwrite(fileContent, 1, fileSize, file);
        fclose(file);
    } else {
        perror("Errore nell'apertura del file per la scrittura");
    }
}

// Funzione di lettura del contenuto di un file in codifica Base64
char *readFileAsBase64(const char *filePath) {
    long fileSize;
    unsigned char *fileContent = readFileAsByteArray(filePath, &fileSize);
    if (fileContent != NULL) {
        // Implementazione della conversione in Base64 (non fornita qui per brevità)
        // Puoi utilizzare librerie di terze parti per questa conversione.
        // Assumiamo che il contenuto del file in Base64 sia rappresentato come una stringa di caratteri.
        char *base64Content = (char *)malloc(fileSize * 2);  // Approssimazione della lunghezza in Base64
        if (base64Content != NULL) {
            // Implementazione della conversione (da implementare)
            // ...
        } else {
            perror("Errore nell'allocazione di memoria per il contenuto Base64 del file");
        }

        free(fileContent);
        return base64Content;
    } else {
        return NULL;
    }
}

// Funzione di scrittura di una stringa in codifica Base64 su un file
void writeFileBase64(const char *filePath, const char *base64Content) {
    // Implementazione della decodifica Base64 (non fornita qui per brevità)
    // Puoi utilizzare librerie di terze parti come libbase64 per questa conversione.
    // Assume che il contenuto in Base64 sia già decodificato in un array di byte.
    unsigned char *decodedContent;  // Da implementare
    long decodedSize;  // Da implementare

    writeFileByteArray(filePath, decodedContent, decodedSize);
    free(decodedContent);
}

// Funzione di append di una stringa in codifica Base64 su un file
void appendFileBase64(const char *filePath, const char *base64Content) {
    // Implementazione della decodifica Base64 (non fornita qui per brevità)
    // Puoi utilizzare librerie di terze parti come libbase64 per questa conversione.
    // Assume che il contenuto in Base64 sia già decodificato in un array di byte.
    unsigned char *decodedContent;  // Da implementare
    long decodedSize;  // Da implementare

    // Implementa l'append del contenuto decodificato
    FILE *file = fopen(filePath, "ab");
    if (file != NULL) {
        fwrite(decodedContent, 1, decodedSize, file);
        fclose(file);
        free(decodedContent);
    } else {
        perror("Errore nell'apertura del file per l'append");
    }
}

// Libera la memoria allocata per l'array di stringhe
void freeStringArray(char **array, int count) {
    for (int i = 0; i < count; i++) {
        free(array[i]);
    }
    free(array);
}

int main() {
    // Esempi di utilizzo
    createFile("example.txt");
    createDirectory("example_directory");
    writeFileString("example.txt", "Contenuto del file di esempio.");
    appendFileString("example.txt", "\nAggiunta di una nuova riga.");
    
    int lineCount;
    char **contentArray = readFileAsArray("example.txt", &lineCount);
    if (contentArray != NULL) {
        printf("Contenuto letto dal file:\n");
        for (int i = 0; i < lineCount; i++) {
            printf("%s\n", contentArray[i]);
        }
        freeStringArray(contentArray, lineCount);
    }

    deleteFile("example.txt");
    deleteDirectory("example_directory");

    return 0;
}
