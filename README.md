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