package server;

import java.io.Serializable;

public class Filedesc implements Serializable {
    private String name;
    private long length;

    public Filedesc(String name, long length) {
        this.name = name;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public long getLength() {
        return length;
    }

    public void setLength(int newLength) {
        this.length = newLength;
    }

    public String toString() {
        return "Filedesc:{" + name + " " + length + "}";
    }
}
