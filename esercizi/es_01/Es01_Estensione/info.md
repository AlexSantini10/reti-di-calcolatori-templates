# Informazioni

## Descrizione
- Si assuma che il discovery server e i RS non siano tutti in esecuzione sulla stessa macchina
- [x] Implementare SwapServer come un processo standalone che si collega al DiscoveryServer per registrarsi
- [x] Il DiscoveryServer deve poter gestire registrazione e disattivazione di SwapServer
- [x] Aggiungere le risposte all'avvenuta registrazione e disattivazione di SwapServer


## DiscoveryServer.java
Server che crea due thread:
- DiscoveryClientRequests
- DiscoveryRegisterSwap

Invocazione: ``` DiscoveryServer portaRichiesteClient portaRegistrazioneRS ``` 
- Parte principale finita

## DiscoveryClientRequests.java
Thread che gestisce le richieste di indirizzi da parte dei client

- [x] Implementare handler delle richieste

## DiscoveryRegisterSwap.java
Thread che gestisce le richieste di registrazione e disattivazione di SwapServer

- [x] Implementare handler delle richieste
- [x] Controllo se il RS è già registrato (oppure esiste un RS con lo stesso nome file)
- [x] Se non è registrato, aggiungere alla lista dei RS attivi
- [x] Se è già registrato e invia deregistrazione, rimuovere dalla lista dei RS attivi


## MappingIndirizzi.java
Risorsa condivisa tra DiscoveryServer e DiscoveryClientRequests

- Parte principale finita
- [x] Possibilità di disattivare un RS

## SwapServer.java
Invocazione: ``` RS IPDS portDS portRS nomeFile ```

Work in progress, sto ancora implementando il RegisterSwapServer

Da rifare completamente per non essere un thread ma un processo standalone
- [x] Implementare registrazione presso DiscoveryServer ```(IP-RS, portRS, nomeFile) ```
- [x] Implementare handler delle richieste
- [x] Possibilità di essere terminato e di inviare un messaggio di disattivazione al DiscoveryServer
- [x] Controllo se le righe esistono

## SwapClient.java
Client che si collega al DiscoveryServer per ottenere la lista dei nomi dei file e poi si collega al RS per richiedere lo swap

Invocazione: ``` Client IPDS portDiscoveruClientRequests ```
- [x] Cambio funzionamento:
    - [x] Richiesta del server presso DiscoveryClientRequests della lista nomi
    - [x] Scelta del nome da parte dell'utente
    - [x] Abilitazione comunicazione con il server
    - [x] Invio richieste di swap agli SwapServer