# ADR-001: Quarkus kot ogrodje storitev
Status: sprejeto

## Kontekst
Gradimo aplikacijo Daily Planner, sestavljeno iz več storitev v Javi. Aplikacija bo postavljena  na Kubernetes. Predmet uporablja CDI in Jakarta REST

## Odločitev
Vse storitve gradimo z ogrodjem Quarkus

## Posledice
+ hiter dev mode, standardni API-ji
+ manjša poraba pomnilnika na Kubernetesu 
- manj primerov na spletu kot za Spring