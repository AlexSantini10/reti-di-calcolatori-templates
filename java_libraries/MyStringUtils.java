package utils;

import java.util.ArrayList;

/**
 * Classe di utilità per la gestione delle stringhe
 * 
 * @author Alex Santini
 * @version 1.0
 */
public class MyStringUtils {

    // Concatenation
    // ----------------------------------------------------------------------------------------------------
    /**
     * Concatena due stringhe
     * 
     * @param str1
     * @param str2
     * @return
     */
    public static String concat(String str1, String str2) {
        return str1 + str2;
    }

    // String extraction
    // ------------------------------------------------------------------------------------------------
    /**
     * Estrae una sottostringa da una stringa
     * 
     * @param str
     * @param beginIndex
     * @param endIndex
     * @return sottostringa
     */
    public static String substring(String str, int beginIndex, int endIndex) {
        return str.substring(beginIndex, endIndex);
    }

    /**
     * Estrae una sottostringa da una stringa
     * 
     * @param str
     * @param beginIndex
     * @return sottostringa
     */
    public static String substring(String str, int beginIndex) {
        return str.substring(beginIndex);
    }

    /**
     * Estrae un array di sottostringhe da una stringa dividendo la stringa in base
     * ad una regex
     * 
     * @param str
     * @return array di sottostringhe
     */
    public static String[] split(String str, String regex) {
        return str.split(regex);
    }

    /**
     * Estrae un array di sottostringhe da una stringa dividendo la stringa in base
     * a un carattere
     * 
     * @param str
     * @return
     */
    public static String[] split(String str, char separatorChar) {
        return str.split(String.valueOf(separatorChar));
    }

    /**
     * Restituisce la stringa senza la prima occorrenza di una sottostringa
     * 
     * @param str
     * @param searchStr
     * @return
     */
    public static String removeFirst(String str, String searchStr) {
        return str.replaceFirst(searchStr, "");
    }

    /**
     * Restituisce la stringa senza l'ultima occorrenza di una sottostringa
     * 
     * @param str
     * @param searchStr
     * @return
     */
    public static String removeLast(String str, String searchStr) {
        return str.substring(0, str.lastIndexOf(searchStr))
                + str.substring(str.lastIndexOf(searchStr) + searchStr.length());
    }

    /**
     * Restituisce la stringa senza tutte le occorrenze di una sottostringa
     * 
     * @param str
     * @param searchStr
     * @return
     */
    public static String removeAll(String str, String searchStr) {
        return str.replaceAll(searchStr, "");
    }

    // Capitalize
    // ------------------------------------------------------------------------------------------------------

    /**
     * Capitalizza la prima lettera di una stringa
     * 
     * @param str
     * @return str capitalizzata
     */
    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Decapitalizza la prima lettera di una stringa
     * 
     * @param str
     * @return str decapitalizzata
     */
    public static String uncapitalize(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * Capitalizza la prima lettera di ogni parola di una stringa
     * 
     * @param str
     * @return str capitalizzata
     */
    public static String capitalizeAll(String str) {
        String[] words = str.split(" ");
        String result = "";
        for (String word : words) {
            result += capitalize(word) + " ";
        }
        return result.trim();
    }

    /**
     * Decapitalizza la prima lettera di ogni parola di una stringa
     * 
     * @param str
     * @return str decapitalizzata
     */
    public static String uncapitalizeAll(String str) {
        String[] words = str.split(" ");
        String result = "";
        for (String word : words) {
            result += uncapitalize(word) + " ";
        }
        return result.trim();
    }

    /**
     * Capitalizza la prima lettera di ogni parola di una stringa e sostituisce gli
     * spazi con underscore
     * 
     * @param str
     * @return str capitalizzata con underscore
     */
    public static String capitalizeAllAndUnderscore(String str) {
        String[] words = str.split(" ");
        String result = "";
        for (String word : words) {
            result += capitalize(word) + "_";
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * Decapitalizza la prima lettera di ogni parola di una stringa e sostituisce
     * gli spazi con underscore
     * 
     * @param str
     * @return str decapitalizzata con underscore
     */
    public static String uncapitalizeAllAndUnderscore(String str) {
        String[] words = str.split(" ");
        String result = "";
        for (String word : words) {
            result += uncapitalize(word) + "_";
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * Capitalizza la prima lettera di ogni parola di una stringa e sostituisce gli
     * spazi con trattini
     * 
     * @param str
     * @return str capitalizzata con trattini
     */
    public static String capitalizeAllAndHyphen(String str) {
        String[] words = str.split(" ");
        String result = "";
        for (String word : words) {
            result += capitalize(word) + "-";
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * Decapitalizza la prima lettera di ogni parola di una stringa e sostituisce
     * gli spazi con trattini
     * 
     * @param str
     * @return str decapitalizzata con trattini
     */
    public static String uncapitalizeAllAndHyphen(String str) {
        String[] words = str.split(" ");
        String result = "";
        for (String word : words) {
            result += uncapitalize(word) + "-";
        }
        return result.substring(0, result.length() - 1);
    }

    // Find in string
    // --------------------------------------------------------------------------------------------------
    /**
     * Controlla se una stringa contiene un'altra stringa (case INsensitive)
     * 
     * @param str
     * @param searchStr
     * @return true se la stringa contiene l'altra stringa, false altrimenti
     */
    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        final int length = searchStr.length();
        if (length == 0) {
            return true;
        }
        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Controlla se una stringa contiene un'altra stringa (case sensitive)
     * 
     * @param str
     * @param searchStr
     * @return true se la stringa contiene l'altra stringa, false altrimenti
     */
    public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        final int length = searchStr.length();
        if (length == 0) {
            return true;
        }
        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(false, i, searchStr, 0, length)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Restituisce la posizione della prima occorrenza di una stringa in un'altra
     * stringa
     * 
     * @param str
     * @param searchStr
     * @return -1 se non viene trovata alcuna occorrenza, altrimenti la posizione
     *         della prima occorrenza
     */
    public static int indexOf(String str, String searchStr) {
        return str.indexOf(searchStr);
    }

    /**
     * Restituisce la posizione della prima occorrenza di una stringa in un'altra
     * stringa (case insensitive)
     * 
     * @param str
     * @param searchStr
     * @return -1 se non viene trovata alcuna occorrenza, altrimenti la posizione
     *         della prima occorrenza
     */
    public static int indexOfIgnoreCase(String str, String searchStr) {
        return str.toLowerCase().indexOf(searchStr.toLowerCase());
    }

    /**
     * Conta quante volte una sottostringa è presente nella stringa di partenza
     * 
     * @param str
     * @param searchStr
     * @return Numero di volte che searchStr si trova in str
     */
    public static int countMatches(String str, String searchStr) {
        return str.split(searchStr).length - 1;
    }

    // Replace in string
    // ------------------------------------------------------------------------------------------------

    /**
     * Sostituisce tutte le occorrenze di una stringa con un'altra stringa (case
     * sensitive)
     * 
     * @param str
     * @param searchStr
     * @param replacement
     * @return la stringa modificata
     */
    public static String replace(String str, String searchStr, String replacement) {
        return str.replace(searchStr, replacement);
    }

    // Validate string
    // ------------------------------------------------------------------------------------------------

    /**
     * Controlla se una stringa è vuota
     * 
     * @param str
     * @return true se la stringa è vuota, false altrimenti
     */
    public static boolean isEmpty(String str) {
        return str.isEmpty();
    }

    /**
     * Controlla se una stringa è vuota o null
     * 
     * @param str
     * @return true se la stringa è vuota o null, false altrimenti
     */
    public static boolean isBlank(String str) {
        return str.isBlank();
    }

    /**
     * Controlla se una stringa è un numero
     * 
     * @param str
     * @return true se la stringa è un numero, false altrimenti
     */
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * Controlla se una stringa è un numero intero
     * 
     * @param str
     * @return true se la stringa è un numero intero, false altrimenti
     */
    public static boolean isInteger(String str) {
        return str.matches("-?\\d+");
    }

    /**
     * Controlla se una stringa è un numero decimale
     * 
     * @param str
     * @return true se la stringa è un numero decimale, false altrimenti
     */
    public static boolean isDecimal(String str) {
        return str.matches("-?\\d+\\.\\d+");
    }

    /**
     * Controlla se la stringa è solo composta da lettere
     * 
     * @param str
     * @return true se la stringa è solo composta da lettere, false altrimenti
     */
    public static boolean isAlpha(String str) {
        return str.matches("[a-zA-Z]+");
    }

    /**
     * Controlla se la stringa è solo composta da lettere e numeri
     * 
     * @param str
     * @return true se la stringa è solo composta da lettere e numeri, false
     *         altrimenti
     */
    public static boolean isAlphaNumeric(String str) {
        return str.matches("[a-zA-Z0-9]+");
    }

    // Gestione caratteri speciali
    // ------------------------------------------------------------------------------------------------
    /**
     * Rimpiazza \ con \\ in una stringa
     * 
     * @param str
     * @return stringa con \ rimpiazzati da \\
     */
    public static String escapeBackslash(String str) {
        return str.replace("\\", "\\\\");
    }

    /**
     * Rimpiazza " con \" in una stringa
     * 
     * @param str
     * @return stringa con " rimpiazzati da \"
     */
    public static String escapeDoubleQuote(String str) {
        return str.replace("\"", "\\\"");
    }

    /**
     * Rimpiazza ' con \' in una stringa
     * 
     * @param str
     * @return stringa con ' rimpiazzati da \'
     */
    public static String escapeSingleQuote(String str) {
        return str.replace("'", "\\'");
    }

    /**
     * Rimpiazza \n con \\n in una stringa
     * 
     * @param str
     * @return stringa con \n rimpiazzati da \\n
     */
    public static String escapeNewLine(String str) {
        return str.replace("\n", "\\n");
    }

    /**
     * Rimpiazza \r con \\r in una stringa
     * 
     * @param str
     * @return stringa con \r rimpiazzati da \\r
     */
    public static String escapeCarriageReturn(String str) {
        return str.replace("\r", "\\r");
    }

    /**
     * Rimpiazza \t con \\t in una stringa
     * 
     * @param str
     * @return stringa con \t rimpiazzati da \\t
     */
    public static String escapeTab(String str) {
        return str.replace("\t", "\\t");
    }

    /**
     * Rimpiazza \b con \\b in una stringa
     * 
     * @param str
     * @return stringa con \b rimpiazzati da \\b
     */
    public static String escapeBackspace(String str) {
        return str.replace("\b", "\\b");
    }

    /**
     * Rimpiazza \f con \\f in una stringa
     * 
     * @param str
     * @return stringa con \f rimpiazzati da \\f
     */
    public static String escapeFormFeed(String str) {
        return str.replace("\f", "\\f");
    }

    // Formattazione
    // ------------------------------------------------------------------------------------------------
    /**
     * Rimuove gli spazi bianchi all'inizio e alla fine di una stringa
     * 
     * @param str
     * @return stringa senza spazi bianchi all'inizio e alla fine
     */
    public static String trim(String str) {
        return str.trim();
    }

    // Inversione e palindromi
    // ------------------------------------------------------------------------------------------------
    /**
     * Inverte una stringa
     * 
     * @param str
     * @return stringa invertita
     */
    public static String reverse(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * Controlla se una stringa è palindroma
     * 
     * @param str
     * @return true se la stringa è palindroma, false altrimenti
     */
    public static boolean isPalindrome(String str) {
        return str.equals(reverse(str));
    }

    // Casting
    // ------------------------------------------------------------------------------------------------
    /**
     * Converte una stringa in un intero
     * 
     * @param str
     * @return intero
     */
    public static int toInt(String str) {
        return Integer.parseInt(str);
    }

    /**
     * Converte una stringa in un long
     * 
     * @param str
     * @return long
     */
    public static long toLong(String str) {
        return Long.parseLong(str);
    }

    /**
     * Converte una stringa in un float
     * 
     * @param str
     * @return float
     */
    public static float toFloat(String str) {
        return Float.parseFloat(str);
    }

    /**
     * Converte una stringa in un double
     * 
     * @param str
     * @return double
     */
    public static double toDouble(String str) {
        return Double.parseDouble(str);
    }

    /**
     * Converte una stringa in un boolean
     * 
     * @param str
     * @return boolean
     */
    public static boolean toBoolean(String str) {
        return Boolean.parseBoolean(str);
    }

    // Liste di stringhe
    // ------------------------------------------------------------------------------------------------
    /**
     * Concatena una lista di stringhe
     * 
     * @param strings
     * @return stringa concatenata
     */
    public static String concat(String[] strings) {
        String result = "";
        for (String string : strings) {
            result += string;
        }
        return result;
    }

    /**
     * Concatena una lista di stringhe con un separatore
     * 
     * @param separator
     * @param strings
     * @return stringa concatenata
     */
    public static String concatWithSeparator(String separator, String[] strings) {
        String result = "";
        for (String string : strings) {
            result += string + separator;
        }
        return result.substring(0, result.length() - separator.length());
    }

    // Permutazioni
    // ------------------------------------------------------------------------------------------------
    /**
     * Restituisce tutte le permutazioni di una stringa
     * 
     * @param str
     * @return lista di permutazioni
     */
    public static ArrayList<String> getAllPermutations(String str) {
        ArrayList<String> permutations = new ArrayList<>();
        if (str == null || str.length() == 0) {
            permutations.add("");
            return permutations;
        }
        char initial = str.charAt(0);
        String remaining = str.substring(1);
        ArrayList<String> words = getAllPermutations(remaining);
        for (String word : words) {
            for (int i = 0; i <= word.length(); i++) {
                String perm = insertCharAt(word, initial, i);
                permutations.add(perm);
            }
        }
        return permutations;
    }

    /**
     * Inserisce un carattere in una stringa in una determinata posizione
     * 
     * @param word
     * @param c
     * @param index
     * @return stringa con il carattere inserito
     */
    private static String insertCharAt(String word, char c, int index) {
        String start = word.substring(0, index);
        String end = word.substring(index);
        return start + c + end;
    }

}
