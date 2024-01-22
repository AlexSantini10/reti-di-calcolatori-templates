#ifndef MYDATESUTILS_H
#define MYDATESUTILS_H

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

// Struttura di data
struct Date {
    int day;
    int month;
    int year;
    int hour;
    int minute;
    int second;
};

// Inizializza una data con valori specifici
struct Date initializeDate(int day, int month, int year, int hour, int minute, int second) {
    struct Date newDate = {day, month, year, hour, minute, second};
    return newDate;
}

// Converte una data in una stringa formattata
char* dateToString(struct Date date) {
    // Allocazione dinamica di una stringa per la data formattata
    char* dateString = (char*)malloc(20 * sizeof(char));

    // Formattazione della data nella stringa
    snprintf(dateString, 20, "%04d-%02d-%02d %02d:%02d:%02d", date.year, date.month, date.day, date.hour, date.minute, date.second);

    // Ritorna la stringa formattata
    return dateString;
}

// Converte una stringa in una data
struct Date stringToDate(const char* dateString) {
    struct Date newDate;
    // Estrae i componenti della data dalla stringa
    sscanf(dateString, "%d-%d-%d %d:%d:%d", &newDate.year, &newDate.month, &newDate.day, &newDate.hour, &newDate.minute, &newDate.second);
    return newDate;
}

// Aggiunge giorni a una data
struct Date addDays(struct Date date, int days) {
    // Aggiunge il numero specificato di giorni alla data
    date.day += days;
    return date;
}

// Sottrae giorni da una data
struct Date subtractDays(struct Date date, int days) {
    // Sottrae il numero specificato di giorni dalla data
    date.day -= days;
    return date;
}

// Confronta due date
int compareDates(struct Date date1, struct Date date2) {
    // Compara le date per determinare l'ordine
    if (date1.year != date2.year) {
        return date1.year - date2.year;
    }
    if (date1.month != date2.month) {
        return date1.month - date2.month;
    }
    return date1.day - date2.day;
}

// Verifica la validità di una data
bool isValidDate(struct Date date) {
    // Verifica se i componenti della data sono entro i limiti accettabili
    return (date.year >= 0 && date.month >= 1 && date.month <= 12 && date.day >= 1 && date.day <= 31 &&
            date.hour >= 0 && date.hour <= 23 && date.minute >= 0 && date.minute <= 59 && date.second >= 0 && date.second <= 59);
}
#endif /* MYDATESUTILS_H */
