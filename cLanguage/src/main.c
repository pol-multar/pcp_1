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

//Variables pour le calcul de temps d'execution
time_t debut, fin;
float duree;
//debut=time(NULL);
//some code
//fin=time(NULL);
//duree=difftime(fin,debut);


	//Initialisation du mur
     	for (int i = 1; i < 8; ++i)
     	{
     		currentTemp[i]=nextTemp[i]=T0;		
     	}

     	currentTemp[0] = nextTemp[0] = OUTSIDETEMP;
     	currentTemp[8] = nextTemp[8] = INSIDETEMP;



     	return 0;
     }