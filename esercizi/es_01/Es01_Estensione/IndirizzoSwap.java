package Es01_Estensione;

public class IndirizzoSwap {

    private String indirizzo;
    private int porta;
    private String nomeFile;
    
    public IndirizzoSwap(String indirizzo, int porta, String nomeFile){
        this.indirizzo = indirizzo;
        this.porta = porta;
        this.nomeFile = nomeFile;
    }

    public String getIndirizzo(){
        return indirizzo;
    }

    public int getPorta(){
        return porta;
    }

    public String getNomeFile(){
        return nomeFile;
    }

    public void setIndirizzo(String indirizzo){
        this.indirizzo = indirizzo;
    }

    public void setPorta(int porta){
        this.porta = porta;
    }

    public void setNomeFile(String nomeFile){
        this.nomeFile = nomeFile;
    }

    public String toString(){
        return "Indirizzo: " + indirizzo + " porta: " + porta + " nome file: " + nomeFile;
    }

}
