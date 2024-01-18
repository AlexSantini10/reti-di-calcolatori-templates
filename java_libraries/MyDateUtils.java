package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MyDateUtils {

    // Metodo per creare una data da una stringa nel formato specificato
    public static LocalDate createDateFromString(String dateString, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(dateString, formatter);
    }

    // Metodo per confrontare due date
    public static int compareDates(LocalDate date1, LocalDate date2) {
        return date1.compareTo(date2);
    }

    // Metodo per aggiungere giorni a una data
    public static LocalDate addDaysToDate(LocalDate date, int days) {
        return date.plusDays(days);
    }

    // Metodo per sottrarre giorni da una data
    public static LocalDate subtractDaysFromDate(LocalDate date, int days) {
        return date.minusDays(days);
    }

    // Metodo per ottenere la data corrente
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    // Metodo per verificare se una data è successiva a un'altra
    public static boolean isDateAfter(LocalDate dateToCheck, LocalDate dateToCompare) {
        return dateToCheck.isAfter(dateToCompare);
    }

    // Metodo per verificare se una data è precedente a un'altra
    public static boolean isDateBefore(LocalDate dateToCheck, LocalDate dateToCompare) {
        return dateToCheck.isBefore(dateToCompare);
    }

    // Metodo per verificare se due date sono uguali
    public static boolean areDatesEqual(LocalDate date1, LocalDate date2) {
        return date1.isEqual(date2);
    }
}
