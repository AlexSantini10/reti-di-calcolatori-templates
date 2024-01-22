/* sala_c.c
 *
 */

#include "fattorex.h"
#include <rpc/rpc.h>
#include <stdio.h>
#include <string.h>

#define LUNGHFILA 7
#define NUMFILE   10

int main(int argc, char *argv[]) {
    char   *host;
    CLIENT *cl;
    int    *ris, *start_ok;
    void   *in;
    Input   input;
    Output  *out;
    char    str[5];
    int     i, j, fila, col;
    char    c, ok[256], canditato[256], tipo[256];

    if (argc != 2) {
        printf("usage: %s server_host\n", argv[0]);
        exit(1);
    }
    host = argv[1];

    cl = clnt_create(host, FATTOREX, FATTOREXVERS, "udp");
    if (cl == NULL) {
        clnt_pcreateerror(host);
        exit(1);
    }

    printf("Inserire:\nC) per vedere la classifica dei giudici\tV) per esprimere un voto\t^D per terminare: ");
    while (gets(ok)) {
        if (strcmp(ok, "V") == 0) {
            printf("Nome del candidato: \n");
            gets(canditato);

            printf("Inserisci tipologia (aggiunta/sottrazione): ");
            gets(tipo);

            while((strcmp(tipo,"aggiunta")!= 0) && (strcmp(tipo,"sottrazione")!= 0) ){
                printf("Operazione sbagliata! Inserisci aggiunta o sottrazione: \n");
                gets(tipo);
            }
            strcpy(input.candidato, canditato);
            strcpy(input.operazione,tipo);
            

            // Invocazione remota
            ris = esprimi_voto_1(&input, cl);

            if(ris == NULL){
                printf("Errore nella chiamata RPC\n");
                continue;
            }
            
            printf("Ris: %d\n", *ris);

            if(!(*ris)){
                printf("Voto registrato\n");
            }
            else{
                printf("Nome errato\n");
            }

            }
        
        // if P
        else if (strcmp(ok, "C") == 0)
        {
            // Invocazione remota
            out = classifica_giudici_1(in, cl);
            if (out == NULL) {
                clnt_perror(cl, host);
                exit(1);
            }
            printf("Ecco la classifica dei giudici:\n");
            for(int i = 0;i < 4; i++){
                printf("Giudice: %s \tPunteggio: %d\n", out -> punteggi[i].giudice, out->punteggi[i].voto);
            }
        } // if V
        else
        {
            printf("Argomento di ingresso errato!!\n");
        }
        printf("Inserire:\nC) per vedere la classifica dei giudici\tV) per esprimere un voto\t^D per terminare: ");
    } // while

    // Libero le risorse, distruggendo il gestore di trasporto
    clnt_destroy(cl);
    exit(0);
    }

