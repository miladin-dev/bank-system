/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author miled
 */
@Entity
@Table(name = "racun")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Racun.findAll", query = "SELECT r FROM Racun r"),
    @NamedQuery(name = "Racun.findById", query = "SELECT r FROM Racun r WHERE r.id = :id"),
    @NamedQuery(name = "Racun.findByDatumVreme", query = "SELECT r FROM Racun r WHERE r.datumVreme = :datumVreme"),
    @NamedQuery(name = "Racun.findByBrojTransakcija", query = "SELECT r FROM Racun r WHERE r.brojTransakcija = :brojTransakcija"),
    @NamedQuery(name = "Racun.findByDozvoljeniMinus", query = "SELECT r FROM Racun r WHERE r.dozvoljeniMinus = :dozvoljeniMinus"),
    @NamedQuery(name = "Racun.findByBlokiran", query = "SELECT r FROM Racun r WHERE r.blokiran = :blokiran"),
    @NamedQuery(name = "Racun.findByStanje", query = "SELECT r FROM Racun r WHERE r.stanje = :stanje")})
public class Racun implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "datum_vreme")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumVreme;
    @Column(name = "broj_transakcija")
    private Integer brojTransakcija;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "dozvoljeni_minus")
    private BigDecimal dozvoljeniMinus;
    @Column(name = "blokiran")
    private Integer blokiran;
    @Column(name = "stanje")
    private BigDecimal stanje;
//    @OneToMany(mappedBy = "idRacunNa")
//    private List<Transakcija> transakcijaList;
//    @OneToMany(mappedBy = "idRacunSa")
//    private List<Transakcija> transakcijaList1;
    @JoinColumn(name = "id_komitent", referencedColumnName = "id")
    @ManyToOne
    private Komitent idKomitent;

    public Racun() {
    }

    public Racun(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(Date datumVreme) {
        this.datumVreme = datumVreme;
    }

    public Integer getBrojTransakcija() {
        return brojTransakcija;
    }

    public void setBrojTransakcija(Integer brojTransakcija) {
        this.brojTransakcija = brojTransakcija;
    }

    public BigDecimal getDozvoljeniMinus() {
        return dozvoljeniMinus;
    }

    public void setDozvoljeniMinus(BigDecimal dozvoljeniMinus) {
        this.dozvoljeniMinus = dozvoljeniMinus;
    }

    public Integer getBlokiran() {
        return blokiran;
    }

    public void setBlokiran(Integer blokiran) {
        this.blokiran = blokiran;
    }

    public BigDecimal getStanje() {
        return stanje;
    }

    public void setStanje(BigDecimal stanje) {
        this.stanje = stanje;
    }

//    @XmlTransient
//    public List<Transakcija> getTransakcijaList() {
//        return transakcijaList;
//    }
//
//    public void setTransakcijaList(List<Transakcija> transakcijaList) {
//        this.transakcijaList = transakcijaList;
//    }
//
//    @XmlTransient
//    public List<Transakcija> getTransakcijaList1() {
//        return transakcijaList1;
//    }
//
//    public void setTransakcijaList1(List<Transakcija> transakcijaList1) {
//        this.transakcijaList1 = transakcijaList1;
//    }

    public Komitent getIdKomitent() {
        return idKomitent;
    }

    public void setIdKomitent(Komitent idKomitent) {
        this.idKomitent = idKomitent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Racun)) {
            return false;
        }
        Racun other = (Racun) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Racun[ id=" + id + " ] Vreme: " + datumVreme + ",  BrojTransakcija: " + brojTransakcija + ", Komitent:" + idKomitent.getId() + ", Dozvoljeni minus: " + dozvoljeniMinus + ", Blokiran: " + blokiran + ", Stanje: " + stanje;
    }
    
}
