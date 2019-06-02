# Movies

Expérimentation d'appels HTTP entre services suivant plusieurs approches (traitement séquentiel, parallélisme par CompletableFuture, découpage en plusieurs services invocable par HTTP avec scale-up, etc ...).

Le projet est composé de 3 modules :
* app : correspond au serveur HTTP exposant 2 routes (1 URL applicative + 1 URL de ping pour helth-check)
* client : un client qui requête toutes les secondes 
* loadClient : test de charge Gatling sur 'app'

# Lancement des différents modules
## Serveur HTTP 

Build du projet par Gradle
```
./gradlew :app:installDist
```

Lancement par docker-compose
```
docker-compose --compatibility up --build 
```

## Client HTTP témoin 

Build et exécution du module par Gradle
```
./gradlew :client:run
```

## Test de charge 

Lancement Gatling via gradle (via plugin)
```
./gradlew :loadClient:gatlingRun
```

# Configuration du serveur

Gestion de conf portée par Docker : 128 Mo de mémoire pour la JVM, 0.5 CPU, healtch-check (+ redémarrage des services _unhealthy_)
Monitoring possible par JMX (via mapping de port sur le port 9010 de localhost)