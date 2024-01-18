package utils;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Classe di utilità per la gestione del tempo
 * 
 * @author Alex Santini
 * @version 1.0
 */
public class MyTimeUtils {

    // Actual time measurement
    // -----------------------------------------------------------------------------------------
    /**
     * Returns the actual time
     * 
     * @return
     */
    public static long getActualTimeInMilli() {
        return System.currentTimeMillis();
    }

    // Dates
    // -----------------------------------------------------------------------------------------------------------
    /**
     * Returns the current date
     * 
     * @return
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    /**
     * Returns the current year
     * 
     * @return
     */
    public static int getCurrentYear() {
        return LocalDate.now().getYear();
    }

    /**
     * Returns the current month
     * 
     * @return
     */
    public static int getCurrentMonth() {
        return LocalDate.now().getMonthValue();
    }

    /**
     * Returns the current day
     * 
     * @return
     */
    public static int getCurrentDay() {
        return LocalDate.now().getDayOfMonth();
    }

    // Dates operations
    // -----------------------------------------------------------------------------------------------------------
    /**
     * Add days to a date
     * 
     * @param date
     * @param days
     * @return
     */
    public static LocalDate addDays(LocalDate date, int days) {
        return date.plusDays(days);
    }

    /**
     * Add days to a date
     * 
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        return new Date(date.getTime() + days * 24 * 60 * 60 * 1000);
    }

    /**
     * Add months to a date
     * 
     * @param date
     * @param months
     * @return
     */
    public static LocalDate addMonths(LocalDate date, int months) {
        return date.plusMonths(months);
    }

    /**
     * Add months to a date
     * 
     * @param date
     * @param months
     * @return
     */
    public static Date addMonths(Date date, int months) {
        return new Date(date.getTime() + months * 30 * 24 * 60 * 60 * 1000);
    }

    /**
     * Add years to a date
     * 
     * @param date
     * @param years
     * @return
     */
    public static LocalDate addYears(LocalDate date, int years) {
        return date.plusYears(years);
    }

    /**
     * Add years to a date
     * 
     * @param date
     * @param years
     * @return
     */
    public static Date addYears(Date date, int years) {
        return new Date(date.getTime() + years * 365 * 24 * 60 * 60 * 1000);
    }

    /**
     * Add hours to a date
     * 
     * @param date1
     * @param hours
     * @return
     */
    public static Date addHours(Date date1, int hours) {
        return new Date(date1.getTime() + hours * 60 * 60 * 1000);
    }

    /**
     * Add minutes to a date
     * 
     * @param date1
     * @param minutes
     * @return
     */
    public static Date addMinutes(Date date1, int minutes) {
        return new Date(date1.getTime() + minutes * 60 * 1000);
    }

    /**
     * Add seconds to a date
     * 
     * @param date1
     * @param seconds
     * @return
     */
    public static Date addSeconds(Date date1, int seconds) {
        return new Date(date1.getTime() + seconds * 1000);
    }

    // Date difference
    // -------------------------------------------------------------------------------------------------
    /**
     * Calculate the difference between two dates
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static long dateDiff(LocalDate date1, LocalDate date2) {
        return date2.toEpochDay() - date1.toEpochDay();
    }

    // Time conversion
    // --------------------------------------------------------------------------------------------------
    /**
     * Convert nano seconds to milli seconds
     * 
     * @param nano
     * @return
     */
    public static long nanoToMilli(long nano) {
        return nano / 1000000;
    }

    /**
     * Convert milli seconds to nano seconds
     * 
     * @param milli
     * @return
     */
    public static long milliToNano(long milli) {
        return milli * 1000000;
    }

    // Time calculation
    // -------------------------------------------------------------------------------------------------
    /**
     * Calculate the time difference between two times
     * 
     * @param startTime in milli seconds
     * @param endTime   in milli seconds
     * @return
     */
    public static long timeDiff(long startTime, long endTime) {
        return endTime - startTime;
    }

    // Time measurement
    // -------------------------------------------------------------------------------------------------
    /**
     * Measure the time taken to run a function
     * 
     * @param func
     * @return
     */
    public static long measureTime(Runnable func) {
        long startTime = System.currentTimeMillis();
        func.run();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    // Conversion
    // -------------------------------------------------------------------------------------------------------
    /**
     * Timestamp to date
     * 
     * @param args
     */
    public static Date timestampToDate(long timestamp) {
        return new Date(timestamp);
    }

    /**
     * Date to timestamp
     * 
     * @param args
     */
    public static long dateToTimestamp(Date date) {
        return date.getTime();
    }

    /**
     * Converte una data in un formato passato come parametro
     * 
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(LocalDate date, String format) {
        return date.format(java.time.format.DateTimeFormatter.ofPattern(format));
    }

    /**
     * Converte una data in un formato passato come parametro
     * 
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(Date date, String format) {
        return date.toLocalDate().format(java.time.format.DateTimeFormatter.ofPattern(format));
    }

}
