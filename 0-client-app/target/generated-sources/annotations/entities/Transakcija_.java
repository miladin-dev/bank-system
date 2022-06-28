package entities;

import entities.Racun;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.0.v20170811-rNA", date="2022-06-24T00:31:57")
@StaticMetamodel(Transakcija.class)
public class Transakcija_ { 

    public static volatile SingularAttribute<Transakcija, BigDecimal> iznos;
    public static volatile SingularAttribute<Transakcija, String> svrha;
    public static volatile SingularAttribute<Transakcija, Racun> idRacunNa;
    public static volatile SingularAttribute<Transakcija, Date> vreme;
    public static volatile SingularAttribute<Transakcija, Racun> idRacunSa;
    public static volatile SingularAttribute<Transakcija, Integer> idFilijala;
    public static volatile SingularAttribute<Transakcija, Integer> tip;
    public static volatile SingularAttribute<Transakcija, Integer> id;
    public static volatile SingularAttribute<Transakcija, Integer> redniBroj;

}