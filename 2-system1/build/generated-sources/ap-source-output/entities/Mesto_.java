package entities;

import entities.Filijala;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-06-26T02:18:27")
@StaticMetamodel(Mesto.class)
public class Mesto_ { 

    public static volatile SingularAttribute<Mesto, String> naziv;
    public static volatile SingularAttribute<Mesto, String> postanskiBroj;
    public static volatile SingularAttribute<Mesto, Integer> id;
    public static volatile ListAttribute<Mesto, Filijala> filijalaList;

}