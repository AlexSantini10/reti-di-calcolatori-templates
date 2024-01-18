package utils;

/**
 * Classe di utilità per l'ordinamento di array
 * 
 * @author Alex Santini
 * @version 1.0
 */
public class MySortingUtils {

    // Swap
    // -----------------------------------------------------------------------------------------------------------
    /**
     * Swap two elements in an array
     * 
     * @param arr
     * @param i
     * @param j
     */
    private static <T> void swap(T[] arr, int i, int j) {
        if (i != j) {
            T temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    /**
     * Swap two elements in an array
     * 
     * @param arr
     * @param i
     * @param j
     */
    private static void swap(int[] arr, int i, int j) {
        if (i != j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    /**
     * Swap two elements in an array
     * 
     * @param arr
     * @param i
     * @param j
     */
    private static void swap(String[] arr, int i, int j) {
        if (i != j) {
            String temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    // Generic array sorting
    // -------------------------------------------------------------------------------------------
    /**
     * Quick sort
     * 
     * @param arr
     * @param low
     * @param high
     */
    public static <T extends Comparable<T>> void quickSort(T[] arr, int low, int high) {
        if (low < high) {
            int pivot = partition(arr, low, high); // pi is partitioning index, arr[pi] is now at right place
            quickSort(arr, low, pivot - 1); // Recursively sort elements before partition and after partition
            quickSort(arr, pivot + 1, high);
        }
    }

    /**
     * Partition
     * 
     * @param arr
     * @param low
     * @param high
     * @return
     */
    private static <T extends Comparable<T>> int partition(T[] arr, int low, int high) {
        T pivot = arr[high]; // pivot
        int i = low - 1; // Index of smaller element and indicates the right position of pivot found so
                         // far

        for (int j = low; j < high; j++) {
            // If current element is smaller than or equal to pivot
            if (arr[j].compareTo(pivot) <= 0) {
                i++; // increment index of smaller element
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high); // swap arr[i+1] and arr[high] (or pivot)
        return i + 1;
    }

    // Integer array sorting
    // --------------------------------------------------------------------------------------------

    /**
     * Quick sort
     * 
     * @param arr
     * @param low
     * @param high
     */
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivot = partition(arr, low, high); // pi is partitioning index, arr[pi] is now at right place
            quickSort(arr, low, pivot - 1); // Recursively sort elements before partition and after partition
            quickSort(arr, pivot + 1, high);
        }
    }

    /**
     * Merge sort
     * 
     * @param arr
     * @param low
     * @param high
     */
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high]; // pivot
        int i = low - 1; // Index of smaller element and indicates the right position of pivot found so
                         // far

        for (int j = low; j < high; j++) {
            // If current element is smaller than or equal to pivot
            if (arr[j] <= pivot) {
                i++; // increment index of smaller element
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high); // swap arr[i+1] and arr[high] (or pivot)
        return i + 1;
    }

    // String array sorting
    // --------------------------------------------------------------------------------------------
    /**
     * Quick sort
     * 
     * @param arr
     * @param low
     * @param high
     */
    public static void quickSort(String[] arr, int low, int high) {
        if (low < high) {
            int pivot = partition(arr, low, high); // pi is partitioning index, arr[pi] is now at right place
            quickSort(arr, low, pivot - 1); // Recursively sort elements before partition and after partition
            quickSort(arr, pivot + 1, high);
        }
    }

    /**
     * Partition
     * 
     * @param arr
     * @param low
     * @param high
     * @return
     */
    private static int partition(String[] arr, int low, int high) {
        String pivot = arr[high]; // pivot
        int i = low - 1; // Index of smaller element and indicates the right position of pivot found so
                         // far

        for (int j = low; j < high; j++) {
            // If current element is smaller than or equal to pivot
            if (arr[j].compareTo(pivot) <= 0) {
                i++; // increment index of smaller element
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high); // swap arr[i+1] and arr[high] (or pivot)
        return i + 1;
    }
}
