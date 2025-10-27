package com.wgt.imt.architecture.interfaces.rest.common.model.input;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.function.Function;

/**
 * Classe dédiée à la gestion d'une propriété qui peut être mise à jour via l'API REST.
 *
 * @param <T> Le type de la valeur de la propriété.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdatableProperty<T extends Serializable> implements Serializable {

    @Serial
    private static final long serialVersionUID = -1013314571672626519L;

    /**
     * Crée une instance d'UpdatableProperty sans modification.
     *
     * @param <T> Le type de la valeur de la propriété.
     * @return Une instance d'UpdatableProperty indiquant qu'aucune mise à jour n'a été effectuée.
     */
    public static <T extends Serializable> UpdatableProperty<T> empty() {
        return new UpdatableProperty<>(false, null);
    }

    /**
     * Crée une instance d'UpdatableProperty avec une nouvelle valeur.
     *
     * @param newValue La nouvelle valeur de la propriété.
     * @param <T>      Le type de la valeur de la propriété.
     * @return Une instance d'UpdatableProperty indiquant qu'une mise à jour a été effectuée avec la nouvelle valeur.
     */
    public static <T extends Serializable> UpdatableProperty<T> makesChanges(final T newValue){
        return new UpdatableProperty<>(true, newValue);
    }

    @Getter(AccessLevel.PROTECTED)
    @EqualsAndHashCode.Exclude
    private final boolean updated;
    private final T value;

    /**
     * Retourne la valeur de la propriété si elle a été mise à jour, sinon retourne la valeur par défaut fournie.
     *
     * @param defaultValue La valeur par défaut à retourner si la propriété n'a pas été mise à jour.
     * @return La valeur de la propriété ou la valeur par défaut.
     */
    public T defaultIfNotOverwrite(final T defaultValue){
        return this.isUpdated()
                ? this.getValue()
                : defaultValue;
    }

    /**
     * Applique une transformation à la valeur de la propriété si elle a été mise à jour, sinon retourne la valeur par défaut fournie.
     *
     * @param transform    La fonction de transformation à appliquer à la valeur de la propriété.
     * @param defaultValue La valeur par défaut à retourner si la propriété n'a pas été mise à jour.
     * @param <R>          Le type de la valeur retournée après transformation.
     * @return La valeur transformée de la propriété ou la valeur par défaut.
     */
    public <R> R defaultIfNotOverwrite(final Function<T, R> transform, final R defaultValue) {
        return this.isUpdated()
                ? transform.apply(this.getValue())
                : defaultValue;
    }

    /**
     * Permet de savoir comment on souhaite afficher la propriété dans les logs.
     */
    @Override
    public String toString(){
        return this.isUpdated()
                ? String.format("UpdatableProperty[%s]", this.getValue())
                : "UpdatableProperty.empty";
    }
}
