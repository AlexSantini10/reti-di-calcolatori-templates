#define MAX_PLATE_LENGTH 10
#define MAX_LICENSE_LENGTH 20
#define MAX_RESULTS 6

struct Reservation {
    char plate_number[MAX_PLATE_LENGTH];
    char license_number[MAX_LICENSE_LENGTH];
};

struct Results {
    Reservation reservations[MAX_RESULTS];
};

struct patenti {
    char numeroTarga[MAX_LICENSE_LENGTH];
    char nuovaPatente[MAX_LICENSE_LENGTH];
};

struct tipo_veicolo {
    char tipo[MAX_PLATE_LENGTH];
};

program VehiculeReservationService {
    version V1 {
        struct Results show_reservations(struct tipo_veicolo) = 1;
        int update_license(struct patenti) = 2;
    } = 1;
} = 0x20000001;
