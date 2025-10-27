package com.wgt.imt.architecture.infrastructures.bdd.clients.repositories.mappers;

import com.wgt.imt.architecture.business.common.model.TypeCompteEnum;
import com.wgt.imt.architecture.business.comptes.model.Compte;
import com.wgt.imt.architecture.infrastructures.bdd.clients.repositories.entities.CompteEntity;
import com.wgt.imt.architecture.infrastructures.bdd.common.model.mappers.AbstractBddMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Mapper pour convertir entre les objets métier Compte et les entités CompteEntity.
 *
 * @author Emmanuel WAGUET
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class CompteBddMapper extends AbstractBddMapper<Compte, CompteEntity> {
    
    /**
     * Convertit une entité CompteEntity en objet métier Compte.
     *
     * @param input l'entité de base de données à convertir
     * @return l'objet métier Compte correspondant
     */
    @Override
    public Compte from(final CompteEntity input) {
        return Compte.builder()
                     .identifier(UUID.fromString(input.getIdentifier()))
                     .name(input.getName())
                     .solde(input.getSolde())
                     .type(TypeCompteEnum.fromOrDefault(input.getType()))
                     .build();
    }
    
    /**
     * Convertit un objet métier Compte en entité CompteEntity.
     *
     * @param object l'objet métier à convertir
     * @return l'entité de base de données correspondante
     */
    @Override
    public CompteEntity to(final Compte object) {
        return CompteEntity.builder()
                           .identifier(object.getIdentifier().toString())
                           .name(object.getName())
                           .solde(object.getSolde())
                           .type(object.getType().name())
                           .build();
    }
}
