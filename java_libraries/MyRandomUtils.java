package utils;

/**
 * Classe di utilità per la generazione di numeri casuali e stringhe casuali
 * 
 * @author Alex Santini
 * @version 1.0
 */
public class MyRandomUtils {

    /**
     * Returns a random integer between min and max
     * 
     * @param min
     * @param max
     * @return
     */
    public static int getRandomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    /**
     * Returns a random double between min and max
     * 
     * @param min
     * @param max
     * @return
     */
    public static double getRandomDouble(double min, double max) {
        return (Math.random() * (max - min + 1) + min);
    }

    /**
     * Returns a random boolean
     * 
     * @return
     */
    public static boolean getRandomBoolean() {
        return Math.random() < 0.5;
    }

    /**
     * Returns a random string of length len
     * 
     * @param len
     * @return
     */
    public static String getRandomString(int len) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return sb.toString();
    }

    /**
     * Returns a random string of length len
     * 
     * @param len
     * @return
     */
    public static String getRandomString(int len, String chars) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return sb.toString();
    }

    /**
     * Returns a random string of length len
     * 
     * @param len
     * @return
     */
    public static String getRandomString(int len, String[] chars) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars[(int) (Math.random() * chars.length)]);
        }
        return sb.toString();
    }

    /**
     * Returns a random string of length len
     * 
     * @param len
     * @return
     */
    public static String getRandomString(int len, char[] chars) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars[(int) (Math.random() * chars.length)]);
        }
        return sb.toString();
    }

}
