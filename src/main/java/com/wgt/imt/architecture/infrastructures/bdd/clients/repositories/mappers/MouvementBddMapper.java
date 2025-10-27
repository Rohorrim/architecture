package com.wgt.imt.architecture.infrastructures.bdd.clients.repositories.mappers;

import com.wgt.imt.architecture.business.comptes.model.Mouvement;
import com.wgt.imt.architecture.business.common.model.MouvementCompteType;
import com.wgt.imt.architecture.infrastructures.bdd.clients.repositories.entities.MouvementEntity;
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
public class MouvementBddMapper extends AbstractBddMapper<Mouvement, MouvementEntity> {
    
    /**
     * Convertit une entité CompteEntity en objet métier Compte.
     *
     * @param input l'entité de base de données à convertir
     * @return l'objet métier Compte correspondant
     */
    @Override
    public Mouvement from(final MouvementEntity input) {
        return Mouvement.builder()
                     .identifier(UUID.fromString(input.getIdentifier()))
                     .message(input.getMessage())
                     .date(input.getDate())
                     .build();
    }
    
    /**
     * Convertit un objet métier Compte en entité CompteEntity.
     *
     * @param object l'objet métier à convertir
     * @return l'entité de base de données correspondante
     */
    @Override
    public MouvementEntity to(final Mouvement object) {
        return MouvementEntity.builder()
                .identifier(object.getIdentifier().toString())
                .message(object.getMessage())
                .date(object.getDate())
                .build();
    }
}
