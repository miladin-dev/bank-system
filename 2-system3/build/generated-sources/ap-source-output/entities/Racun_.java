package entities;

import entities.Komitent;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-06-25T17:22:05")
@StaticMetamodel(Racun.class)
public class Racun_ { 

    public static volatile SingularAttribute<Racun, Integer> brojTransakcija;
    public static volatile SingularAttribute<Racun, Date> datumVreme;
    public static volatile SingularAttribute<Racun, BigDecimal> stanje;
    public static volatile SingularAttribute<Racun, Komitent> idKomitent;
    public static volatile SingularAttribute<Racun, Integer> id;
    public static volatile SingularAttribute<Racun, BigDecimal> dozvoljeniMinus;
    public static volatile SingularAttribute<Racun, Integer> blokiran;

}