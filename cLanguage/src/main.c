//--------------------------------------------------------------
//      Implementation de la diffusion de chaleur en C
//--------------------------------------------------------------
// Maxime MULTARI SI4
//--------------------------------------------------------------

#include <stdio.h>
#include <time.h>
#include <string.h>
#include <pthread.h>
#include <unistd.h>
#include <stdlib.h>

#define QUEUESIZE 1

/**
     * Methode permettant de calculer C,
     * representant la fraction de degres perdus par rayonnement
     * @param lambda
     * @param mu
     * @param c
     * @return un double contenant la valeur de C calculee
     */

     float calculateC(float lambda, float mu, float c, float DT, float DX) {

     	return (lambda * DT) / (mu * c * DX * DX);

     }

//Temperatures en degré celsius
     const float T0 = 20;
     const float OUTSIDETEMP = 110;
     const float INSIDETEMP = 20;

    //Pas de temps (secondes)
     const float DT=600;

    //Pas d'espace (m)
     const float DX=0.04;


/////////////////////////////////////////////////////////////Partie Iterative ///////////////////////////////////////////////////////////////////////////////

     void monoThreadSimulation(){

        //Le C du materiau composant le mur
        const float wallC=calculateC(0.84, 1400, 840, DT, DX);

    //Le C du materiau composant l'isolant
        const float insulationC=calculateC(0.04, 30, 900, DT, DX);

        
    //Le tableau contenant les valeurs actuelles des couches
        float currentTemp[9];

    //Le tableau contenant les valeurs prochaines des couches
        float nextTemp[9];



    //Le nombre d'etapes de la simulation
        int maxStep=100000;

    //Le numero de l'etape courante
        int actualStep=0;

    //Le temps total d'execution de l'algorithme
        double execTime;    

//Variables pour le calcul de temps d'execution
        clock_t debut, fin;


//Pour savoir si la derniere couche a changee de temperature et a quelle etape
        int isChanged=0;
        int stepOfChange;


    //Initialisation du mur
        for (int i = 1; i < 8; ++i)
        {
         currentTemp[i]=nextTemp[i]=T0;     
     }

     currentTemp[0] = nextTemp[0] = OUTSIDETEMP;
     currentTemp[8] = nextTemp[8] = INSIDETEMP;

     //Affichage de l'etat du mur avant simulation
     printf("t=%d heure(s)",0 );

     for (int i = 0; i < 9; i++) {
        printf("%d,",(int)currentTemp[i]);
    }

    printf("\n");

//On fixe le temps d execution a zero
    execTime=0;


    //Simulation en monothread
    for (int cpt = 0; cpt < maxStep; ++cpt)
    {
       debut=clock();

        /**
         * La premiere et la dernière partie du mur (respectivement partie 0 et partie 8) sont des constantes,
         * on ne va donc pas modifier leur valeur de temperature
         */

        /* Etape 1 : modification des parties du mur composees du premier materiau, soit les parties 1 a 4 */
         for (int i = 1; i < 5; i++) {
            nextTemp[i] = currentTemp[i]+wallC*(currentTemp[i+1]+currentTemp[i-1]-2*(currentTemp[i]));
        }

        /* Etape 2 : modification de la temperature de la partie du milieu, soit la partie 5 */

        nextTemp[5] = currentTemp[5] + wallC * (currentTemp[4] - currentTemp[5]) + insulationC * (currentTemp[6] - currentTemp[5]);

        /* Etape 3 : modification de la temperature des dernieres parties du mur, soit les parties 6 et 7 */

        for (int i = 6; i < 8; i++) {
            nextTemp[i] = currentTemp[i]+insulationC*(currentTemp[i+1]+currentTemp[i-1]-2*(currentTemp[i]));
        }

        if (((int) nextTemp[7]) > 20 && isChanged==0) {
            stepOfChange = actualStep;
            isChanged = 1;
        }

        /*  Je met a jour les temperatures */
        for (int j = 1; j<8; j++) {
            currentTemp[j] = nextTemp[j];
        }

        //Le cycle est termine
        actualStep++;
        //Mesure tf
        fin=clock();
        double time_spent = (double)(fin - debut) * 1000.0 / CLOCKS_PER_SEC;
        execTime = execTime+time_spent;

        //On affiche que les dix premieres heures
        if((actualStep%6==0) && (actualStep <61) &&(actualStep>0)){

            int hour = ((actualStep)*DT)/3600;


            printf("t=%d heure(s)",hour );

            for (int i = 0; i < 5; i++) {
                printf("%d,",(int)currentTemp[i]);
            }

            printf("%d-%d,",(int)currentTemp[5],(int)currentTemp[5]);

            for (int i = 6; i < 8; i++) {
                printf("%d,",(int)currentTemp[i]);
            }

            printf("%d\n",(int)currentTemp[8]);

        }

        if(actualStep==maxStep-1){
            printf("Changement de temperature de la derniere couche a partir de l etape %d \n",stepOfChange);
            printf(" soit apres %d heures\n",(int)((stepOfChange*DT)/3600));
            printf("Temps d execution de la simulation : %d ms\n",(int)execTime);
        }

    }

}

/////////////////////////////////////////////////////////////Partie MultiThread ///////////////////////////////////////////////////////////////////////////////

void *producer (void *args);
void *consumer (void *args);

typedef struct {
    float buf[QUEUESIZE];
    long head, tail;
    int full, empty;
    pthread_mutex_t *mut;
    pthread_cond_t *notFull, *notEmpty;
} queue;


//Pour que deux couches puissent communiquer, elles doivent partager deux files


queue *queueInit (void);
void queueDelete (queue *q);
void queueAdd (queue *q, float in);
void queueDel (queue *q, float *out);

void* layerEngine(void *nb);

/* Le tableau contenant mes fifo
 * La case 0 contient la temperature pour le voisin de gauche
 * La case 1 contient la temperature pour le voisn de droite
 */
 queue* com[9][2];

    //Le tableau conteannt les temperatures calculee
 float savedTemp[100000][9];


 void multiThreadSimulation(){

//J'initialise le tableau contenant les communication entre threads
    for (int i = 0; i < 9; ++i)
    {
        com[i][0]=queueInit();
        com[i][1]=queueInit();
        if (com[i][0] ==  NULL || com[i][1] ==  NULL ) {
            fprintf (stderr, "main: Queue Init failed.\n");
            exit (1);
        }
    }

    pthread_t c1,c2,c3,c4,c5,c6,c7;

    int nb1=1;
    int nb2=2;
    int nb3=3;
    int nb4=4;
    int nb5=5;
    int nb6=6;
    int nb7=7;

    if(pthread_create (&c1, NULL, layerEngine, &nb1)==-1){
        perror("thread 1");
        exit(1);
    }
    if(pthread_create (&c2, NULL, layerEngine, &nb2)==-1){
        perror("thread 2");
        exit(1);
    }
    if(pthread_create (&c3, NULL, layerEngine, &nb3)==-1){
        perror("thread 3");
        exit(1);
    }
    if(pthread_create (&c4, NULL, layerEngine, &nb4)==-1){
        perror("thread 4");
        exit(1);
    }
    if(pthread_create (&c5, NULL, layerEngine, &nb5)==-1){
        perror("thread 5");
        exit(1);
    }
    if(pthread_create (&c6, NULL, layerEngine, &nb6)==-1){
        perror("thread 6");
        exit(1);
    }
    if(pthread_create (&c7, NULL, layerEngine, &nb7)==-1){
        perror("thread 7");
        exit(1);
    }

    pthread_join (c1, NULL); printf("Fin de la thread c1\n");
    pthread_join (c2, NULL); printf("Fin de la thread c2\n");
    pthread_join (c3, NULL); printf("Fin de la thread c3\n");
    pthread_join (c4, NULL); printf("Fin de la thread c4\n");
    pthread_join (c5, NULL); printf("Fin de la thread c5\n");
    pthread_join (c6, NULL); printf("Fin de la thread c6\n");
    pthread_join (c7, NULL); printf("Fin de la thread c7\n");

    for (int i = 0; i < 9; ++i)
    {
        queueDelete(com[i][0]);
        queueDelete(com[i][1]);
    }


}

void *layerEngine(void *nb)
{
    int number;

    number=*(int*)nb;

    printf("Je suis la couche %d\n",number);

    //Le C du materiau composant le mur
    const float wallC=calculateC(0.84, 1400, 840, DT, DX);

    //Le C du materiau composant l'isolant
    const float insulationC=calculateC(0.04, 30, 900, DT, DX);

    float leftPartTemp=20;
    float rightPartTemp=20;
    float currentPartTemp=20;
    float newTemp=0;

    queue *ecritureG;
    queue *ecritureD;

    queue *lectureG;
    queue *lectureD;

    ecritureG=com[number][0];
    ecritureD=com[number][1];

    lectureG=com[number-1][1];
    lectureD=com[number+1][0];

    for (int i = 0; i < 10; ++i)
    {

    //D'abord j'indique a mes voisins ma temperature a t

    if(number !=1){//le voisin de gauche
        pthread_mutex_lock(ecritureG->mut);
        while(ecritureG->full){
        //J'attend que mon voisin recupere ma temperature
            pthread_cond_wait(ecritureG->notFull, ecritureG->mut);
        }
        queueAdd(ecritureG,currentPartTemp);
        pthread_mutex_unlock(ecritureG->mut);
        pthread_cond_signal(ecritureG->notEmpty);
    }

    if(number !=7){
     pthread_mutex_lock(ecritureD->mut);
     while(ecritureD->full){
        //J'attend que mon voisin recupere ma temperature
        pthread_cond_wait(ecritureD->notFull, ecritureD->mut);
    }
    queueAdd(ecritureD,currentPartTemp);
    pthread_mutex_unlock(ecritureD->mut);
    pthread_cond_signal(ecritureD->notEmpty);   
}


    //Puis je recupere la temperature de mes voisins
    if(number != 1){//partie a gauche
        pthread_mutex_lock(lectureG->mut);
        while(lectureG->empty){
            //J'attend que mon voisin mette sa temperature
            pthread_cond_wait(lectureG->notEmpty, lectureG->mut);
        }
        queueDel(lectureG, &leftPartTemp);
        pthread_mutex_unlock (lectureG->mut);
        pthread_cond_signal (lectureG->notFull);
        printf ("%d: recieved %f.\n",number, leftPartTemp);
    }else{
        leftPartTemp = 110;
    }

    if(number!=7){
        //je recupere la valeur du voisin de droite
        pthread_mutex_lock(lectureD->mut);
        while(lectureD->empty){
            //J'attend que mon voisin mette sa temperature
            pthread_cond_wait(lectureD->notEmpty, lectureD->mut);
        }
        queueDel(lectureD, &rightPartTemp);
        pthread_mutex_unlock (lectureD->mut);
        pthread_cond_signal (lectureD->notFull);
        printf ("%d: recieved %f.\n",number, rightPartTemp);
    }else{
        rightPartTemp = 20;
    }    

    //Maintenant que j'ai toutes les temperatures, je peux mettre a jour la mienne

    if (number < 5) {
        newTemp = currentPartTemp + wallC * (rightPartTemp + leftPartTemp - 2 * (currentPartTemp));
    } else if (number > 5) {
        newTemp = currentPartTemp + insulationC * (rightPartTemp + leftPartTemp - 2 * (currentPartTemp));
        } else {//i==5
            newTemp = currentPartTemp+ wallC * (leftPartTemp - currentPartTemp) + insulationC * (rightPartTemp - currentPartTemp);
        }
        
        printf ("%d: new temp = %f\n",number,newTemp);

        currentPartTemp=newTemp;


    }

    return (NULL);
}

queue *queueInit (void)
{
    queue *q;

    q = (queue *)malloc (sizeof (queue));
    if (q == NULL) return (NULL);

    q->empty = 1;
    q->full = 0;
    q->head = 0;
    q->tail = 0;
    q->mut = (pthread_mutex_t *) malloc (sizeof (pthread_mutex_t));
    pthread_mutex_init (q->mut, NULL);
    q->notFull = (pthread_cond_t *) malloc (sizeof (pthread_cond_t));
    pthread_cond_init (q->notFull, NULL);
    q->notEmpty = (pthread_cond_t *) malloc (sizeof (pthread_cond_t));
    pthread_cond_init (q->notEmpty, NULL);
    
    return (q);
}


void queueDelete (queue *q)
{
    pthread_mutex_destroy (q->mut);
    free (q->mut);  
    pthread_cond_destroy (q->notFull);
    free (q->notFull);
    pthread_cond_destroy (q->notEmpty);
    free (q->notEmpty);
    free (q);
}

void queueAdd (queue *q, float in)
{
    q->buf[q->tail] = in;
    q->tail++;
    if (q->tail == QUEUESIZE)
        q->tail = 0;
    if (q->tail == q->head)
        q->full = 1;
    q->empty = 0;

    return;
}

void queueDel (queue *q, float *out)
{
    *out = q->buf[q->head];

    q->head++;
    if (q->head == QUEUESIZE)
        q->head = 0;
    if (q->head == q->tail)
        q->empty = 1;
    q->full = 0;

    return;
}


int main(int argc, char *argv[]){

	
    //monoThreadSimulation();
    multiThreadSimulation();


    return 0;
}