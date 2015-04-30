#!/bin/bash
echo "Lancement de la compilation"
gcc -Wall -std=c99 -pthread    main.c   -o main
echo "Pressez une touche pour fermer la fenetre..."
read mot
echo "Fin du script"
