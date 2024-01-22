const MAXNOME=64;
const N=20;
const NGIUDICI=4;

struct Candidato{
    char candidato[MAXNOME];
    char giudice[MAXNOME];
    char categoria;
    char nomeFile[MAXNOME];
    char fase;
    int voto;
};

struct Input{
    char candidato[MAXNOME];
    char operazione[MAXNOME];
};

struct Tabella{
    Candidato statistiche[N];
};

struct StatisticaGiudice{
    char giudice[MAXNOME];
    int voto;
};

struct Output{
    StatisticaGiudice punteggi[NGIUDICI];
};

program FATTOREX {
    version FATTOREXVERS{
            Output CLASSIFICA_GIUDICI(void) = 1;
            int ESPRIMI_VOTO(Input) = 2;
    } = 1;
} = 0x20000013;



