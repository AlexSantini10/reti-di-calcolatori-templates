package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Classe di utilità per la gestione dei file
 * 
 * @author Alex Santini
 * @version 1.0
 */
public class MyFileUtils {

    // Create file
    // ----------------------------------------------------------------------------------------------------
    /**
     * Crea un file vuoto
     * 
     * @param filePath
     */
    public static void createFile(String filePath) {
        try {
            Files.createFile(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea un file vuoto
     * 
     * @param file
     */
    public static void createFile(File file) {
        try {
            Files.createFile(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create directory
    // -----------------------------------------------------------------------------------------------
    /**
     * Crea una directory
     * 
     * @param dirPath
     */
    public static void createDirectory(String dirPath) {
        try {
            Files.createDirectory(Paths.get(dirPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea una directory
     * 
     * @param dir
     */
    public static void createDirectory(File dir) {
        try {
            Files.createDirectory(dir.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Delete file
    // ----------------------------------------------------------------------------------------------------
    /**
     * Cancella un file
     * 
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        try {
            Files.delete(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cancella un file
     * 
     * @param file
     */
    public static void deleteFile(File file) {
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Delete directory
    // -----------------------------------------------------------------------------------------------
    /**
     * Cancella una directory
     * 
     * @param dirPath
     */
    public static void deleteDirectory(String dirPath) {
        try {
            Files.delete(Paths.get(dirPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cancella una directory
     * 
     * @param dir
     */
    public static void deleteDirectory(File dir) {
        try {
            Files.delete(dir.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read file content
    // ----------------------------------------------------------------------------------------------
    /**
     * Legge il contenuto di un file e lo restituisce come stringa
     * 
     * @param filePath
     * @return file content
     */
    public static String readFileAsString(String filePath) {
        String fileContent = "";
        try {
            fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    /**
     * Legge il contenuto di un file e lo restituisce come stringa
     * 
     * @param file
     * @return file content
     */
    public static String readFileAsString(File file) {
        String fileContent = "";
        try {
            fileContent = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    /**
     * Legge il contenuto di un file e lo restituisce come array di stringhe (una
     * riga per elemento)
     * 
     * @param filePath
     * @return file content
     */
    public static String[] readFileAsArray(String filePath) {
        String[] fileContent = null;
        try {
            fileContent = new String(Files.readAllBytes(Paths.get(filePath))).split("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    /**
     * Legge il contenuto di un file e lo restituisce come array di stringhe (una
     * riga per elemento)
     * 
     * @param filePath
     * @return
     */
    public static String[] readFileAsArray(File filePath) {
        String[] fileContent = null;
        try {
            fileContent = new String(Files.readAllBytes(filePath.toPath())).split("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    /**
     * Legge il contenuto di un file e lo restituisce come byte array
     * 
     * @param filePath
     * @return file content
     */
    public static byte[] readFileAsByteArray(String filePath) {
        byte[] fileContent = null;
        try {
            fileContent = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    /**
     * Legge il contenuto di un file e lo restituisce come byte array
     * 
     * @param file
     * @return file content
     */
    public static byte[] readFileAsByteArray(File file) {
        byte[] fileContent = null;
        try {
            fileContent = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    /**
     * Legge il contenuto di un file e lo restituisce in codifica base64
     * 
     * @param filePath
     * @return file content
     */
    public static String readFileAsBase64(String filePath) {
        String fileContent = "";
        try {
            fileContent = java.util.Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    /**
     * Legge il contenuto di un file e lo restituisce in codifica base64
     * 
     * @param file
     * @return file content
     */
    public static String readFileAsBase64(File file) {
        String fileContent = "";
        try {
            fileContent = java.util.Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    // Write file content
    // ---------------------------------------------------------------------------------------------
    /**
     * Scrive il contenuto di una stringa su un file
     * 
     * @param filePath
     * @param fileContent
     */
    public static void writeFileString(String filePath, String fileContent) {
        try {
            Files.write(Paths.get(filePath), fileContent.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scrive il contenuto di una stringa su un file
     * 
     * @param file
     * @param fileContent
     */
    public static void writeFileString(File file, String fileContent) {
        try {
            Files.write(file.toPath(), fileContent.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scrive il contenuto di un array di stringhe su un file (una riga per
     * elemento)
     * 
     * @param filePath
     * @param fileContent
     */
    public static void writeFileStringArray(String filePath, String[] fileContent) {
        try {
            Files.write(Paths.get(filePath), String.join("\n", fileContent).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scrive il contenuto di un array di stringhe su un file (una riga per
     * elemento)
     * 
     * @param file
     * @param fileContent
     */
    public static void writeFileStringArray(File file, String[] fileContent) {
        try {
            Files.write(file.toPath(), String.join("\n", fileContent).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scrive il contenuto di un array di byte su un file
     * 
     * @param filePath
     * @param fileContent
     */
    public static void writeFileByteArray(String filePath, byte[] fileContent) {
        try {
            Files.write(Paths.get(filePath), fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scrive il contenuto di un array di byte su un file
     * 
     * @param file
     * @param fileContent
     */
    public static void writeFileByteArray(File file, byte[] fileContent) {
        try {
            Files.write(file.toPath(), fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scrive il contenuto di una stringa in codifica base64 su un file
     * 
     * @param filePath
     * @param fileContent
     */
    public static void writeFileBase64(String filePath, String fileContent) {
        try {
            Files.write(Paths.get(filePath), java.util.Base64.getDecoder().decode(fileContent));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scrive il contenuto di una stringa in codifica base64 su un file
     * 
     * @param file
     * @param fileContent
     */
    public static void writeFileBase64(File file, String fileContent) {
        try {
            Files.write(file.toPath(), java.util.Base64.getDecoder().decode(fileContent));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Append file content
    // ---------------------------------------------------------------------------------------------
    /**
     * Scrive il contenuto di una stringa su un file
     * 
     * @param filePath
     * @param fileContent
     */
    public static void appendFileString(String filePath, String fileContent) {
        try {
            Files.write(Paths.get(filePath), fileContent.getBytes(), java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scrive il contenuto di una stringa su un file
     * 
     * @param file
     * @param fileContent
     */
    public static void appendFileString(File file, String fileContent) {
        try {
            Files.write(file.toPath(), fileContent.getBytes(), java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scrive il contenuto di un array di stringhe su un file (una riga per
     * elemento)
     * 
     * @param filePath
     * @param fileContent
     */
    public static void appendFileStringArray(String filePath, String[] fileContent) {
        try {
            Files.write(Paths.get(filePath), String.join("\n", fileContent).getBytes(),
                    java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scrive il contenuto di un array di stringhe su un file (una riga per
     * elemento)
     * 
     * @param file
     * @param fileContent
     */
    public static void appendFileStringArray(File file, String[] fileContent) {
        try {
            Files.write(file.toPath(), String.join("\n", fileContent).getBytes(),
                    java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scrive il contenuto di un array di byte su un file
     * 
     * @param filePath
     * @param fileContent
     */
    public static void appendFileByteArray(String filePath, byte[] fileContent) {
        try {
            Files.write(Paths.get(filePath), fileContent, java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scrive il contenuto di un array di byte su un file
     * 
     * @param file
     * @param fileContent
     */
    public static void appendFileByteArray(File file, byte[] fileContent) {
        try {
            Files.write(file.toPath(), fileContent, java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scrive il contenuto di una stringa in codifica base64 su un file
     * 
     * @param filePath
     * @param fileContent
     */
    public static void appendFileBase64(String filePath, String fileContent) {
        try {
            Files.write(Paths.get(filePath), java.util.Base64.getDecoder().decode(fileContent),
                    java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scrive il contenuto di una stringa in codifica base64 su un file
     * 
     * @param file
     * @param fileContent
     */
    public static void appendFileBase64(File file, String fileContent) {
        try {
            Files.write(file.toPath(), java.util.Base64.getDecoder().decode(fileContent),
                    java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
