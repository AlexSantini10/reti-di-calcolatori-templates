package Es01_Estensione;

public class MappingIndirizzi {

    private IndirizzoSwap[] indirizzi = new IndirizzoSwap[1000];
    private int numIndirizzi = 0;
    
    public MappingIndirizzi(){
        for(int i = 0; i < 1000; i++){
            indirizzi[i] = new IndirizzoSwap(null, -1, null);
        }

        numIndirizzi = 0;
    }

    public synchronized void addIndirizzo(String indirizzo, int porta, String nomeFile){
        indirizzi[numIndirizzi].setIndirizzo(indirizzo);
        indirizzi[numIndirizzi].setPorta(porta);
        indirizzi[numIndirizzi].setNomeFile(nomeFile);
        System.out.println("MappingIndirizzi: Aggiunto indirizzo: " + indirizzi[numIndirizzi].toString());
        
        numIndirizzi++;
    }

    public synchronized void removeIndirizzo(String fileName){
        // Rimozione
        int indirizzoRimosso = -1;

        for(int i = 0; i < numIndirizzi; i++){
            System.out.println("MappingIndirizzi: Controllando indirizzo: " + indirizzi[i].toString());
            if(indirizzi[i].getNomeFile().trim().equals(fileName.trim())){
                indirizzi[i].setIndirizzo(null);
                indirizzi[i].setPorta(-1);
                indirizzi[i].setNomeFile(null);
                System.out.println("MappingIndirizzi: Rimosso indirizzo: " + indirizzi[i].toString());
                numIndirizzi--;

                indirizzoRimosso = i;
                break;
            }
        }

        if (indirizzoRimosso == -1){
            System.out.println("MappingIndirizzi: Nome file non presente" + fileName);
            return;
        }

        // Riordinamento, se necessario
        for(int i = indirizzoRimosso; i < numIndirizzi; i++){
            indirizzi[i].setIndirizzo(indirizzi[i+1].getIndirizzo());
            indirizzi[i].setPorta(indirizzi[i+1].getPorta());
            indirizzi[i].setNomeFile(indirizzi[i+1].getNomeFile());
        }
        System.out.println("MappingIndirizzi: Riordinamento effettuato");
        System.out.println("MappingIndirizzi: Indirizzi rimanenti: " + this.toString());
    }

    public synchronized String toString(){
        String allIndirizzi = "";

        for(int i = 0; i < numIndirizzi; i++){
            allIndirizzi += indirizzi[i].getIndirizzo() + ":" + indirizzi[i].getPorta() + " File: " + indirizzi[i].getNomeFile() + "\n";
        }

        return allIndirizzi;
    }

    public synchronized int getNumIndirizzi(){
        return numIndirizzi;
    }

    public synchronized String getIndirizzo(String nomeFile){
        for(int i = 0; i < numIndirizzi; i++){
            if(indirizzi[i].getNomeFile().equals(nomeFile)){
                return indirizzi[i].getIndirizzo();
            }
        }
        return null;
    }

    public synchronized int getPorta(String nomeFile){
        for(int i = 0; i < numIndirizzi; i++){
            if(indirizzi[i].getNomeFile().equals(nomeFile)){
                return indirizzi[i].getPorta();
            }
        }
        return -1;
    }

    public synchronized boolean isNamePresent(String nomeFile){
        for(int i = 0; i < numIndirizzi; i++){
            if(indirizzi[i].getNomeFile().equals(nomeFile)){
                return true;
            }
        }
        return false;
    }

    public synchronized boolean areIndirizzoAndPortPresent(String indirizzo, int port){
        for(int i = 0; i < numIndirizzi; i++){
            if(indirizzi[i].getIndirizzo().equals(indirizzo) && indirizzi[i].getPorta() == port){
                return true;
            }
        }
        return false;
    }

}
