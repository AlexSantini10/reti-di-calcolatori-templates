#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>


int main(int argc, char *argv[]){
    
    FILE *file;
    char filename[] = "file_to_read.txt";
    char buffer[1024];

    file = fopen(filename, "r");

    if(file == NULL){
        printf("Error: Could not open file %s\n", filename);
        return 1;
    }

    while (fgets(buffer, sizeof(buffer), file) != NULL){
        printf("RIGA: %s", buffer);
    }

    fclose(file);

    return 0;
}