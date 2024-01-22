#ifndef MYRANDOMUTILS_H
#define MYRANDOMUTILS_H

#include <stdbool.h>
#include <stdlib.h>
#include <string.h>

int getRandomInt(int min, int max) {
    return (rand() % (max - min + 1)) + min;
}

double getRandomDouble(double min, double max) {
    return ((double)rand() / RAND_MAX) * (max - min) + min;
}

bool getRandomBoolean() {
    return rand() % 2 == 0;
}

char* getRandomString(int len) {
    const char chars[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    char* result = (char*)malloc((len + 1) * sizeof(char));

    for (int i = 0; i < len; i++) {
        result[i] = chars[rand() % (sizeof(chars) - 1)];
    }

    result[len] = '\0';
    return result;
}

char* getRandomStringCustom(int len, const char* chars) {
    char* result = (char*)malloc((len + 1) * sizeof(char));

    for (int i = 0; i < len; i++) {
        result[i] = chars[rand() % strlen(chars)];
    }

    result[len] = '\0';
    return result;
}

#endif /* MYRANDOMUTILS_H */
