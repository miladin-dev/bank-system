package entities;

import entities.Filijala;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.0.v20170811-rNA", date="2022-06-24T00:31:57")
@StaticMetamodel(Mesto.class)
public class Mesto_ { 

    public static volatile SingularAttribute<Mesto, String> naziv;
    public static volatile SingularAttribute<Mesto, String> postanskiBroj;
    public static volatile SingularAttribute<Mesto, Integer> id;
    public static volatile ListAttribute<Mesto, Filijala> filijalaList;

}