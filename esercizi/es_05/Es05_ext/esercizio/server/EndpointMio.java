package server;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EndpointMio implements Serializable{
    
    private List<Filedesc> files;
    private int ePort;
    private String eIp;

    public EndpointMio(int ePort) {
        this.ePort = ePort;
    }

    public EndpointMio(String eIp, int ePort) {
        this.eIp = eIp;
        this.ePort = ePort;
    }

    public EndpointMio(int ePort, List<Filedesc> files) {
        this.ePort = ePort;
        this.files = files;
    }

    public List<Filedesc> getFiles() {
        return files;
    }

    public void setFiles(List<Filedesc> files) {
        this.files = files;
    }

    public void setFilesFromDirName(String dirName){
        this.files = new ArrayList<Filedesc>();

        File dir = new File(dirName);

        System.out.println(dir.getAbsolutePath());

        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                this.files.add(new Filedesc(file.getName(), file.length()));
            }
        }
        else {
            System.out.println("Directory non esistente");
        }
    }

    public void addFile(Filedesc file) {
        files.add(file);
    }

    public int getEPort() {
        return ePort;
    }

    public void setEPort(int ePort) {
        this.ePort = ePort;
    }

    public String getEIp() {
        return eIp;
    }

    public void setEIp(String eIp) {
        this.eIp = eIp;
    }

    public String toString() {
        String s = "";
        for (Filedesc nomeFile : files) {
            s += nomeFile + ", ";
        }
        return "EndpointMio: " + ePort + " [" + s + "]";
    }

}
