package utils;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

/**
 * Classe di utilità per la conversione da e verso JSON
 * 
 * @author Alex Santini
 * @version 1.0
 */
public class MyJsonUtils {

    /**
     * Restituisce una stringa JSON contenente l'errore
     * 
     * @param error
     * @return
     */
    public static String getJsonError(String error) {
        JsonObject json = new JsonObject();
        json.addProperty("error", error);
        return json.toString();
    }

    /**
     * Restituisce una stringa JSON contenente il messaggio di successo
     * 
     * @param success
     * @return
     */
    public static String getJsonSuccess(String success) {
        JsonObject json = new JsonObject();
        json.addProperty("success", success);
        return json.toString();
    }

    /**
     * Restituisce una istanza di una classe a partire da una stringa JSON
     * 
     * @param success
     * @param o
     * @return
     */
    public static <T> T jsonToClass(String json, Class<T> c) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, c);
        } catch (Exception e) {
            System.out.println("Errore nella conversione da JSON a classe:");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Restituisce una stringa JSON a partire da un oggetto
     * 
     * @param o
     * @return
     */
    public static String classToJson(Object o) {
        Gson gson = new Gson();
        return gson.toJson(o);
    }

    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> c) {
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            ArrayList<JsonObject> jsonObjects = gson.fromJson(json, listType);
            ArrayList<T> objects = new ArrayList<T>();
            for (JsonObject jsonObject : jsonObjects) {
                objects.add(gson.fromJson(jsonObject, c));
            }
            return objects;
        } catch (Exception e) {
            System.out.println("Errore nella conversione da JSON a ArrayList:");
            e.printStackTrace();
            return null;
        }
    }
}
