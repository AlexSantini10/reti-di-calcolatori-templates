# Template e Materiali di Supporto — Reti di Calcolatori

## Descrizione

Questo repository raccoglie template, esempi di codice e materiali di supporto per il corso di
**Reti di Calcolatori**, erogato nell'ambito della Laurea Triennale in Ingegneria Informatica
presso l'**Università di Bologna (UNIBO)**.

Lo scopo principale è fornire una base strutturata e riutilizzabile per lo sviluppo delle
esercitazioni di laboratorio, delle simulazioni d'esame e delle relative relazioni tecniche.
I template sono organizzati per linguaggio di programmazione e per tipologia di architettura
di rete trattata nel corso.

---

## Struttura del Repository

```
.
├── Template_Reti_di_Calcolatori-main/   # Template principali (C e Java)
│   ├── c/
│   ├── java/
│   └── template.txt
├── c_templates/                         # Template C per architetture con select()
│   ├── select_parallel_multiple_messages/
│   └── select_parallel_single_message/
├── java_templates/                      # Template Java per connessioni TCP
│   └── single_connection/
├── c_examples/                          # Esempi pratici in C (TCP, file transfer)
│   ├── tcp/
│   └── tcp_file/
├── c_tcp/                               # Implementazioni base client/server TCP in C
│   ├── client.c
│   └── server.c
├── c_libraries/                         # Librerie di utilità in C (header files)
├── java_libraries/                      # Librerie di utilità in Java
├── rmi_template/                        # Template per Java RMI
├── rpc_template/                        # Template per Sun RPC (rpcgen)
├── esercizi/                            # Esercizi svolti
├── simulazioni/                         # Tracce e soluzioni di simulazioni d'esame
├── Tests/                               # Test funzionali (TCP e UDP in Java)
└── docs/                                # Documentazione in formato PDF
```

---

## Contenuto dei Template

Il repository include le seguenti tipologie di materiale:

| Categoria | Descrizione |
|---|---|
| **Template C — TCP/UDP** | Scheletri di programmi client/server in C con socket POSIX |
| **Template C — `select()`** | Template per server concorrenti basati su multiplexing I/O |
| **Template Java — TCP** | Struttura base per comunicazione TCP con socket Java |
| **Template Java — RMI** | Scheletro per l'invocazione remota di metodi tramite Java RMI |
| **Template RPC** | File `.x` e sorgenti generati con `rpcgen` per Sun RPC |
| **Librerie C** | Header con funzioni di utilità per stringhe, file, date e numeri casuali |
| **Librerie Java** | Classi di utilità per stringhe, file, XML, JSON, matrici e ordinamento |
| **Esercizi** | Soluzioni a esercizi assegnati durante il corso |
| **Simulazioni** | Implementazioni complete di tracce di esame |
| **Documentazione** | PDF relativi a socket C, socket Java, RMI, RPC e materiale integrativo |

---

## Modalità di Utilizzo

### Prerequisiti

Prima di utilizzare i template, assicurarsi di avere installato i seguenti strumenti:

**Per i template in C:**
- Compilatore `gcc` (versione ≥ 7)
- Strumenti di build `make`
- Librerie POSIX standard (incluse in qualsiasi distribuzione Linux)
- `rpcgen` e `libtirpc-dev` per i template RPC

**Per i template in Java:**
- JDK (Java Development Kit) versione ≥ 11
- IDE compatibile (consigliato: Eclipse o IntelliJ IDEA) per i progetti con `.classpath` e `.project`

### Utilizzo dei Template C

1. Copiare la cartella del template desiderato in una directory di lavoro separata.
2. Esaminare i file sorgente (`.c`) e gli eventuali header (`.h`) presenti.
3. Completare le sezioni marcate con commenti `/* TODO */` o equivalenti.
4. Compilare con `gcc` o tramite `Makefile`, ove disponibile:

```bash
gcc -o server server.c -Wall
gcc -o client client.c -Wall
```

Per i template RPC, utilizzare il `Makefile` fornito:

```bash
make -f Makefile.add
```

### Utilizzo dei Template Java

1. Importare il progetto nell'IDE come progetto Java esistente (Eclipse: *Import → Existing Projects into Workspace*).
2. Verificare che il build path includa le librerie presenti in `java_libraries/`.
3. Completare l'implementazione nelle classi sorgente fornite.
4. Compilare ed eseguire direttamente dall'IDE oppure da riga di comando:

```bash
javac -cp . NomeClasse.java
java NomeClasse
```

Per i progetti RMI, avviare prima il registro RMI:

```bash
rmiregistry &
```

---

## Librerie di Utilità

### Librerie C (`c_libraries/`)

| File | Contenuto |
|---|---|
| `myStringUtils.h` | Funzioni per la manipolazione di stringhe |
| `myFilesUNIX.h` | Funzioni per operazioni su file in ambiente UNIX |
| `myBinFilesUtils.h` | Utilità per la gestione di file binari |
| `myDates.h` | Funzioni per la gestione delle date |
| `myRandomUtils.h` | Generazione di valori casuali |

### Librerie Java (`java_libraries/`)

| File | Contenuto |
|---|---|
| `MyStringUtils.java` | Utilità per la manipolazione di stringhe |
| `MyFileUtils.java` | Operazioni su file |
| `MyXMLUtils.java` / `XMLRecord.java` | Parsing e generazione XML |
| `MyJsonUtils.java` | Parsing e generazione JSON |
| `MyMatrixUtils.java` | Operazioni su matrici |
| `MySortingUtils.java` | Algoritmi di ordinamento |
| `MyRandomUtils.java` | Generazione di valori casuali |
| `MyDateUtils.java` | Gestione di date |
| `MyTimeUtils.java` | Gestione del tempo |

---

## Convenzioni Adottate

- I file sorgente C seguono lo standard **C99/C11** con estensioni POSIX.
- I file sorgente Java sono compatibili con **Java SE 11** o versioni successive.
- I nomi dei file e delle variabili adottano la convenzione **snake_case** per C e **camelCase** per Java.
- Le sezioni da completare sono segnalate con commenti espliciti nel codice.
- Ogni cartella di template contiene almeno un file `client` e un file `server`, o la struttura equivalente per l'architettura trattata.

---

## Contributi

Il repository è aperto a contributi. Per proporre modifiche o aggiungere nuovi template:

1. Eseguire il fork del repository.
2. Creare un branch dedicato con un nome descrittivo (es. `feature/template-udp-multicast`).
3. Apportare le modifiche mantenendo le convenzioni stilistiche e strutturali già adottate.
4. Aprire una Pull Request con una descrizione chiara delle modifiche introdotte.

Si prega di verificare che il codice aggiunto sia compilabile e privo di errori prima di inviare la Pull Request.

---

## Licenza

I contenuti di questo repository sono distribuiti sotto licenza **MIT**.
Si rimanda al file `LICENSE` per il testo completo dei termini di utilizzo.

---

## Disclaimer Accademico

I materiali contenuti in questo repository hanno esclusivamente finalità di **supporto allo studio**
e alle esercitazioni pratiche del corso di Reti di Calcolatori.

- Il presente materiale **non sostituisce** le slide ufficiali, i testi di riferimento, né le
  indicazioni fornite dai docenti durante le lezioni e i laboratori.
- Gli esercizi e le simulazioni inclusi rappresentano soluzioni a scopo didattico e potrebbero
  non riflettere la formulazione esatta delle prove d'esame ufficiali.
- L'utilizzo di questi template durante prove valutative è soggetto alle norme sull'integrità
  accademica stabilite dall'Ateneo.
- Gli autori declinano ogni responsabilità per eventuali inesattezze tecniche presenti nel
  materiale.
