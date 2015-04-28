//--------------------------------------------------------------
//      Implementation de la diffusion de chaleur en C
//--------------------------------------------------------------
// Maxime MULTARI SI4
//--------------------------------------------------------------

#include <stdio.h>
#include <time.h>

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


     int main(){

	//Temperatures en degré celsius
     	const float T0 = 20;
     	const float OUTSIDETEMP = 110;
     	const float INSIDETEMP = 20;

 	//Pas de temps (secondes)
     	const float DT=600;

    //Pas d'espace (m)
     	const float DX=0.04;

	//Le tableau contenant les valeurs actuelles des couches
     	int currentTemp[9];

	//Le tableau contenant les valeurs prochaines des couches
     	int nextTemp[9];

	//Le C du materiau composant le mur
     	const float wallC=calculateC(0.84, 1400, 840, DT, DX);

	//Le C du materiau composant l'isolant
     	const float insulationC=calculateC(0.04, 30, 900, DT, DX);

	//Le nombre d'etapes de la simulation
     	int maxStep=100000;

	//Le numero de l'etape courante
     	int actualStep=0;

    //Le temps total d'execution de l'algorithme
    double execTime=0; 	

//Variables pour le calcul de temps d'execution
time_t debut, fin;
float duree;
//debut=time(NULL);
//some code
//fin=time(NULL);
//duree=difftime(fin,debut);

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


//Simulation en monothread
for (int cpt = 0; cpt < maxStep; ++cpt)
{
	debut=time(NULL);

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
        fin=time(NULL);
        execTime += difftime(fin,debut);
}



     	return 0;
     }