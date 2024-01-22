/* sala_s.c
 * 	+implementazione delle procedure remote: "prenota" e "visualizza".
 *	+inizializzazione struttura.
 */

#include "fattorex.h"
#include <fcntl.h>
#include <rpc/rpc.h>
#include <stdio.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <string.h>

/* STATO SERVER */
static Tabella tab;
static int  inizializzato = 0;
char* giudici[NGIUDICI];

void bubbleSort(Output *out, int n);


void inizializza() {
    int i, j;

    if (inizializzato == 1) {
        return;
    }

    strcpy(tab.statistiche[0].candidato,"Bugo");
    strcpy(tab.statistiche[0].giudice,"Morgan");
    tab.statistiche[0].categoria = 'U';
    strcpy(tab.statistiche[0].nomeFile,"BugoProfile.txt");
    tab.statistiche[0].fase = 'A';
    tab.statistiche[0].voto = 50;

    strcpy(tab.statistiche[1].candidato,"Ozone");
    strcpy(tab.statistiche[1].giudice,"Fedez");
    tab.statistiche[1].categoria = 'U';
    strcpy(tab.statistiche[1].nomeFile,"OzoneProfile.txt");
    tab.statistiche[1].fase = 'B';
    tab.statistiche[1].voto = 0;

    giudici[0] = "Fedez";
    giudici[1] = "Morgan";
    giudici[2] = "Emingway";
    giudici[3] = "Emily Ratakowsky";

    inizializzato = 1;
    printf("Terminata inizializzazione struttura dati!\n");
}

Output *classifica_giudici_1_svc(void *in, struct svc_req *rqstp){
    static Output out;

    if (inizializzato == 0) {
        inizializza();
    }

    for(int i=0; i<NGIUDICI; i++){
        strcpy(out.punteggi[i].giudice, giudici[i]);
        out.punteggi[i].voto = 0;
    }
    //con questo ciclo ho i giudici di Output nella stessa posizione (indice) dell'array di giudici


    for(int j=0; j<NGIUDICI; j++){ 
        for(int i=0; i<N ;i++){
            if(strcmp(tab.statistiche[i].giudice, giudici[j]) == 0 && tab.statistiche[i].voto != -1){
                out.punteggi[j].voto += tab.statistiche[i].voto;
            }
        }
    }

    

    printf("Struttura output prima del sort: \n");
    for(int i=0; i<NGIUDICI; i++){
        printf("Giudice: %s \rvoto: %d\n", out.punteggi[i].giudice, out.punteggi[i].voto);
    }

    bubbleSort(&out, NGIUDICI);

    printf("\nStruttura output dopo il sort: \n");
    for(int i=0; i<NGIUDICI; i++){
        printf("Giudice: %s \rvoto: %d\n", out.punteggi[i].giudice, out.punteggi[i].voto);
    }

    return (&out);


}

int *esprimi_voto_1_svc(Input *in, struct svc_req *rqstp){
    static int ris = -1;

    if (inizializzato == 0) {
        inizializza();
    }


    for(int i=0;i<N; i++){
        if(strcmp(tab.statistiche[i].candidato, in->candidato) == 0){
            if(strcmp(in->operazione, "aggiunta")==0){
                tab.statistiche[i].voto++;
            }
            else tab.statistiche[i].voto--;

            ris = 0;
        }
    }

    printf("Tabella aggiornata:\n");
    for(int i=0; i<N; i++){
        if(tab.statistiche[i] != NULL)
        printf("%s %s %c %s %c %d\n", tab.statistiche[i].candidato, tab.statistiche[i].giudice, tab.statistiche[i].categoria, tab.statistiche[i].nomeFile, tab.statistiche[i].fase, tab.statistiche[i].voto);
    }

    return (&ris);
}

// Swap function 
void swap(Output* out, int i, int j) 
{ 
    StatisticaGiudice temp = out->punteggi[i]; 
    strcpy(out->punteggi[i].giudice, out->punteggi[j].giudice); 
    out->punteggi[i].voto = out->punteggi[j].voto;

    strcpy(out->punteggi[j].giudice, temp.giudice); 
    out->punteggi[j].voto = temp.voto;
} 
  
// A function to implement bubble sort 
void bubbleSort(Output *out, int n) 
{ 
    int i, j; 
    for (i = 0; i < n - 1; i++) {
        // Last i elements are already 
        // in place 
        for (j = 0; j < n - i - 1; j++) {
            if (out->punteggi[j].voto > out->punteggi[j + 1].voto){
                printf("Swapped %s and %s\n", out->punteggi[j].giudice, out->punteggi[j+1].giudice);
                swap(out, j, j + 1);
            }
        }
    }
} 

  