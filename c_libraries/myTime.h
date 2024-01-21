#include <stdio.h>
#include <stdlib.h>
#include <time.h>

// Funzione per confrontare due date
int compareDates(time_t date1, time_t date2) {
    return difftime(date1, date2);
}

// Funzione per aggiungere giorni a una data
time_t addDaysToDate(time_t date, int days) {
    struct tm *temp = localtime(&date);
    temp->tm_mday += days;
    mktime(temp); // Normalizza la data dopo l'aggiunta dei giorni
    return mktime(temp);
}

// Funzione per sottrarre giorni da una data
time_t subtractDaysFromDate(time_t date, int days) {
    struct tm *temp = localtime(&date);
    temp->tm_mday -= days;
    mktime(temp); // Normalizza la data dopo la sottrazione dei giorni
    return mktime(temp);
}

// Funzione per ottenere la data corrente
time_t getCurrentDate() {
    time_t currentTime;
    time(&currentTime);
    return currentTime;
}

// Funzione per verificare se una data è successiva a un'altra
int isDateAfter(time_t dateToCheck, time_t dateToCompare) {
    return difftime(dateToCheck, dateToCompare) > 0;
}

// Funzione per verificare se una data è precedente a un'altra
int isDateBefore(time_t dateToCheck, time_t dateToCompare) {
    return difftime(dateToCheck, dateToCompare) < 0;
}

// Funzione per verificare se due date sono uguali
int areDatesEqual(time_t date1, time_t date2) {
    return difftime(date1, date2) == 0;
}

int main() {
    // Esempi di utilizzo delle funzioni definite nella libreria
    time_t date1 = addDaysToDate(getCurrentDate(), 10);
    time_t date2 = getCurrentDate();

    printf("Date create:\n");
    printf("Date 1: %s", ctime(&date1));
    printf("Date 2 (current date): %s", ctime(&date2));

    printf("\nComparisons:\n");
    printf("Compare Dates: %d\n", compareDates(date1, date2));
    printf("Is Date 1 after Date 2: %d\n", isDateAfter(date1, date2));
    printf("Is Date 1 before Date 2: %d\n", isDateBefore(date1, date2));
    printf("Are Dates Equal: %d\n", areDatesEqual(date1, date2));

    printf("\nDate Operations:\n");
    time_t date3 = addDaysToDate(date1, 5);
    printf("Date 1 + 5 days: %s", ctime(&date3));

    time_t date4 = subtractDaysFromDate(date2, 3);
    printf("Date 2 - 3 days: %s", ctime(&date4));

    return 0;
}
