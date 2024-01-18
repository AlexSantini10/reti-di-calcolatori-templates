package utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLRecord {
    private final Map<String, String> attributes;

    public XMLRecord() {
        this.attributes = new HashMap<>();
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public List<String> getAttributeNames() {
        return new ArrayList<>(this.attributes.keySet());
    }

    public boolean hasAttribute(String key) {
        return this.attributes.containsKey(key);
    }

    public String getAttribute(String key) {
        return this.attributes.get(key);
    }

    public void addAttribute(String key, String value) {
        this.attributes.put(key, value);
    }

    public <T> T toClass(Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();

            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                String attributeName = entry.getKey();
                String attributeValue = entry.getValue();

                String setterName = "set" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
                Method setterMethod = findSetterMethod(clazz, setterName);

                if (setterMethod != null) {
                    Class<?> parameterType = setterMethod.getParameterTypes()[0];
                    Object convertedValue = convertValue(attributeValue, parameterType);
                    setterMethod.invoke(instance, convertedValue);
                }
            }
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Method findSetterMethod(Class<?> clazz, String setterName) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(setterName) && method.getParameterCount() == 1) {
                return method;
            }
        }
        return null;
    }

    private Object convertValue(String value, Class<?> targetType) {
        if (targetType == String.class) {
            return value;
        } else if (targetType == int.class || targetType == Integer.class) {
            return Integer.parseInt(value);
        } else if (targetType == double.class || targetType == Double.class) {
            return Double.parseDouble(value);
        } else if (targetType == boolean.class || targetType == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        // Aggiungi altri tipi di dati se necessario...

        // Se non si riesce a convertire, restituire null
        return null;
    }
}