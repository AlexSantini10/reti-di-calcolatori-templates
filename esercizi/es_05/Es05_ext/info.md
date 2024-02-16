# Estensione esercitazione 5
Trasferimento di un direttorio

## Descrizione
Sviluppare un'applicazione C/S basata su Java RMI e su socket con connessione (TCP) per il trasferimento di file di un direttorio dal server al client

2 modalità di trasferimento:
- Client attivo
    Il client effettua la connect
- Server attivo
    Il server effettua la connect

Per entrambe le modalità si prevede una interazione iniziale sincrona che:
- Trasferisce la lista file da inviare
- Invia l'endpoint (host e porta) di ascolto
- Seconda fase: trasferimento effettivo dei file dal server al client

### Modalità con client attivo
##### Il metodo accetta come argomenti:
- Nome del direttorio

##### Restituisce:
Una struttura dati contenente
- Endpoint di ascolto
- Lista dei nomi e lunghezza dei file

##### Funzionamento
- [x] Il client richiede all'utente il nome del direttorio
- [x] Il client effettua la chiamata RMI al server e riceve la struttura dati
- [x] Il client effettua la connect al server
- [x] Il client riceve i file dal server e li salva nel direttorio locale

- [x] Il server implementa il metodo remoto
- [x] Il server è realizzato in modo concoorrente
- [x] Per ogni nuova richiesta crea un thread a cui affidare la gestione della richiesta MA è il padre a restituire la struttura dati

### Modalità con server attivo
##### Il metodo accetta come argomenti:
- Nome del direttorio
- Endpoint di ascolto

##### Restituisce:
La lista dei nomi e lunghezza dei file

##### Funzionamento
- [ ] Il client richiede all'utente il nome del direttorio
- [ ] Il client crea la socket di ascolto
- [ ] Il client effettua la chiamata RMI al server e riceve la lista dei file
- [ ] Il client accetta la connect dal server e riceve i file dal server e li salva nel direttorio locale

- [ ] Il server implementa il metodo remoto
- [ ] Il server è realizzato in modo concoorrente
- [ ] Per ogni nuova richiesta crea un thread a cui affidare la gestione della richiesta