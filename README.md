Exercices :
    1 - Création des ressources Clients et Comptes
        Le but est d'initialiser un projet (idéalement Spring), et d'exposer pour la première fois deux ressources distinctes :
            -	Clients
                Le client est composé, a minima, des attributs suivants : identifiant, nom, prenom
                
            -	Comptes
                Le compte est composé, a minima, des attributs suivants : identifiant, nom, type, solde
                
        Vous pouvez bouchonner le code métier, ce qu'on l'on souhaite pour l'instant c'est retourner les bons codes HTTP, utiliser les bons verbes, ...

    2 - Création des controller métier
        Le but est de voir comment on passe des infos au controller métier (via les mapper) et de
        commencer a mettre en oeuvre une architecture en couche
    
        Créer un controller métier pour les clients et un autre pour les comptes, avec les 
        opérations suivantes :
            -	Listing
            -   Récupération d'une entrée unique
            -   Sauvegarde d'une entrée
            -   Suppression d'une entrée

        N'ayant pas encore de BDD, on stockera les objets dans des List ou des Map, comme vous
        préférez, en attribut du controller

    3 - Mise en oeuvre d'une BDD
        Le but est de voir créer la troisième couche de notre architecture : la couche infrastructure. 
        On va donc mettre en oeuvre une BDD H2 ou une mongoBD, et utiliser Spring Data JPA pour faire le lien entre la BDD et notre couche métier.

        Il faut impacter notre code métier afin d'utiliser la BDD en question

        PS : Pour lancer la mongo : mongo.exe --dbpath /c/Users/A388771/local/IMT/architecture/mongo

    4 - Mise en oeuvre des validateurs
        Maintenant que notre application est complète, nous allons ajouter des validateurs pour s'assurer que les données envoyées par le client sont correctes
        Le but est de commencer a mettre en oeuvre des pattern de conception

        Mettre en oeuvre une chain de responsabilité pour gérer plusieurs validateurs a la suite les uns des autres