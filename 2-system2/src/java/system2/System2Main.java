/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system2;


import entities.Komitent;
import entities.Racun;
import entities.Transakcija;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

/**
 *
 * @author miled
 */
public class System2Main {

    
    @Resource(lookup="conn_factory")
    private static ConnectionFactory connFactory;

    @Resource(lookup="s1s2_queue")
    private static Queue s1s2_queue;
    
    @Resource(lookup="subsystem_topic")
    private static Topic topic;
    
    @Resource(lookup="response_queue")
    private static Queue response_queue;
    
    private static void em_persist(EntityManager em, Object o){
        em.getTransaction().begin();
        em.persist(o);
        em.getTransaction().commit();
        em.close();
    }
    
    public static void main(String[] args) {
        JMSContext context =  connFactory.createContext();
        JMSConsumer consumer = context.createConsumer(topic);
        JMSProducer producer = context.createProducer();
        JMSConsumer consumerS1S2 = context.createConsumer(s1s2_queue);
        EntityManagerFactory em_factory = Persistence.createEntityManagerFactory("bank2PU");
        
        while(true){
            Message msg = consumer.receive();
            TextMessage txt;
            if(msg instanceof TextMessage){
                txt = (TextMessage) msg;
            
                try {
                    int op = txt.getIntProperty("op");
                    System.out.println(txt.getIntProperty("op"));
                    if(op == -4){
                        EntityManager em = em_factory.createEntityManager();
                        List<Racun> lr = em.createNamedQuery("Racun.findAll", Racun.class).getResultList();
                        List<Transakcija> lt = em.createNamedQuery("Transakcija.findAll", Transakcija.class).getResultList();

                        List<Object> all = new ArrayList<>();
                        all.addAll(lr);
                        all.addAll(lt);

                        ObjectMessage obj = context.createObjectMessage((Serializable)all);
                        obj.setIntProperty("op", -1);
                        producer.send(topic, obj);
                    }
                    else if(op == 0){
                        EntityManager em = em_factory.createEntityManager();
                        List<Racun> lr = em.createNamedQuery("Racun.findAll", Racun.class).getResultList();
                        List<Transakcija> lt = em.createNamedQuery("Transakcija.findAll", Transakcija.class).getResultList();
                        List<Komitent> lk = em.createNamedQuery("Komitent.findAll", Komitent.class).getResultList();

                        List<Object> all = new ArrayList<>();
                        all.addAll(lk);
                        all.addAll(lr);
                        all.addAll(lt);

                        ObjectMessage obj = context.createObjectMessage((Serializable)all);
                        obj.setIntProperty("op", -1);
                        producer.send(topic, obj);
                    }   
                    else if(op == 3){
                        EntityManager em = em_factory.createEntityManager();
                        String naziv = txt.getStringProperty("naziv");
                        String adresa = txt.getStringProperty("adresa");
                        int id_mesto = txt.getIntProperty("mesto");
                        
                        
                        TextMessage s1Res = (TextMessage) consumerS1S2.receive();
                        String s = s1Res.getStringProperty("resTxt");
                        System.out.println(s);
                        if(!s.equals("OK")){
                            continue;
                        }
                        
                        Komitent k = new Komitent();
                        k.setNaziv(naziv);
                        k.setAdresa(adresa);
                        k.setIdMesto(id_mesto);
                        em.getTransaction().begin();
                        em.persist(k);
                        em.getTransaction().commit();
                    } else if(op == 4){
                        EntityManager em = em_factory.createEntityManager();
                        TextMessage resTxt = context.createTextMessage();
                        String naziv = txt.getStringProperty("naziv");
                        int id_mesto = txt.getIntProperty("novo_mesto");

                        TextMessage s1Res = (TextMessage) consumerS1S2.receive();
                        String s = s1Res.getStringProperty("resTxt");
                        System.out.println(s);
                        if(!s.equals("OK")){
                            continue;
                        }
                        
                        List<Komitent> lK = em.createNamedQuery("Komitent.findByNaziv", Komitent.class).setParameter("naziv", naziv).getResultList();

                        Komitent k = lK.get(0);
                        k.setIdMesto(id_mesto);

                        em.getTransaction().begin();
                        em.persist(k);
                        em.getTransaction().commit();

                    }
                    else if(op == 5) {
                        EntityManager em = em_factory.createEntityManager();
                        int idCom = Integer.parseInt(txt.getStringProperty("idCom"));
                        BigDecimal minus = new BigDecimal(txt.getStringProperty("minus"));

                        Komitent k = em.find(Komitent.class, idCom);

                        Racun r = new Racun();
                        r.setBlokiran(0);
                        r.setBrojTransakcija(0);
                        r.setDatumVreme(new java.sql.Date(System.currentTimeMillis()));
                        r.setDozvoljeniMinus(minus);
                        r.setIdKomitent(k);
                        r.setStanje(new BigDecimal(0));

                        em_persist(em, r);
                    }
                    else if(op == 6) {
                        EntityManager em = em_factory.createEntityManager();
                        int idAcc = txt.getIntProperty("idAcc");

                        Racun r = em.find(Racun.class, idAcc);
                        
                        if(r != null){
                            em.getTransaction().begin();
                            
                            for(Transakcija t : r.getTransakcijaList()){
                                em.remove(t);
                            }
                            
                            for(Transakcija t : r.getTransakcijaList1()){
                                em.remove(t);
                            }
                            
                            em.remove(r);
                            em.getTransaction().commit();
                        }

                        em.close();
                    }
                    else if(op == 7){
                        EntityManager em = em_factory.createEntityManager();
                        int idAccF = txt.getIntProperty("idAccFrom");
                        int idAccT = txt.getIntProperty("idAccTo");
                        BigDecimal amount = new BigDecimal(txt.getStringProperty("amount"));
                        String purpose = txt.getStringProperty("purpose");

                        Racun accFrom = em.find(Racun.class, idAccF);
                        Racun accTo = em.find(Racun.class, idAccT);
                        
                        Transakcija t = new Transakcija();
                        t.setIdRacunSa(accFrom);
                        t.setIdRacunNa(accTo);
                        t.setIznos(amount);
                        t.setTip(1);
                        t.setSvrha(purpose);
                        t.setRedniBroj(accFrom.getBrojTransakcija() + 1);
                        t.setVreme(new java.util.Date(System.currentTimeMillis()));
                        
                        BigDecimal min = new BigDecimal(0).subtract(accFrom.getDozvoljeniMinus());
                        BigDecimal left = accFrom.getStanje().subtract(amount);
                        
                        if(left.compareTo(min) >= 0){
                            accFrom.setBrojTransakcija(accFrom.getBrojTransakcija() + 1);
                            accFrom.setStanje(left);
                            accTo.setBrojTransakcija(accTo.getBrojTransakcija() + 1);
                            accTo.setStanje(accTo.getStanje().add(amount));
                            if(left.compareTo(min) == 0){
                                //tacno koliko sme
                                accFrom.setBlokiran(1);
                            }
                            
                            em.getTransaction().begin();
                            em.persist(accFrom);
                            em.persist(accTo);
                            em.persist(t);
                            em.getTransaction().commit();
                        }
                        else if(left.compareTo(min) < 0){
                            //nista
                            //da se ne izvrsi transakcija
                        }
                    }
                    else if(op == 8){
                        EntityManager em = em_factory.createEntityManager();
                        int idAccT = txt.getIntProperty("idAccTo");
                        BigDecimal amount = new BigDecimal(txt.getStringProperty("amount"));
                        String purpose = txt.getStringProperty("purpose");
                        int idFil = txt.getIntProperty("idFil");
                        Racun accTo = em.find(Racun.class, idAccT);
                        
                        accTo.setBrojTransakcija(accTo.getBrojTransakcija() + 1);
                        accTo.setStanje(accTo.getStanje().add(amount));

                        Transakcija t = new Transakcija();
                        t.setIdRacunNa(accTo);
                        t.setIznos(amount);
                        t.setIdFilijala(idFil);
                        t.setTip(2);
                        t.setSvrha(purpose);
                        t.setRedniBroj(accTo.getBrojTransakcija());
                        t.setVreme(new java.util.Date(System.currentTimeMillis()));

                        em.getTransaction().begin();
                        em.persist(accTo);
                        em.persist(t);
                        em.getTransaction().commit();
                    }
                    else if(op == 9){
                        EntityManager em = em_factory.createEntityManager();
                        int idAccF = txt.getIntProperty("idAccFrom");
                        BigDecimal amount = new BigDecimal(txt.getStringProperty("amount"));
                        String purpose = txt.getStringProperty("purpose");
                        int idFil = txt.getIntProperty("idFil");
                        Racun accFrom = em.find(Racun.class, idAccF);

                        Transakcija t = new Transakcija();
                        t.setIdRacunSa(accFrom);
                        t.setIznos(amount);
                        t.setTip(3);
                        t.setIdFilijala(idFil);
                        t.setSvrha(purpose);
                        t.setRedniBroj(accFrom.getBrojTransakcija());
                        t.setVreme(new java.util.Date(System.currentTimeMillis()));
                        
                        BigDecimal min = new BigDecimal(0).subtract(accFrom.getDozvoljeniMinus());
                        BigDecimal left = accFrom.getStanje().subtract(amount);
                        if(left.compareTo(min) >= 0){
                            accFrom.setBrojTransakcija(accFrom.getBrojTransakcija() + 1);
                            accFrom.setStanje(left);
                            
                            if(left.compareTo(min) == 0){
                                //tacno koliko sme
                                accFrom.setBlokiran(1);
                            }
                            em.getTransaction().begin();
                            em.persist(accFrom);
                            em.persist(t);
                            em.getTransaction().commit();
                        }

                    }
                    else if(op == 13){
                        EntityManager em = em_factory.createEntityManager();
                        List<Racun> listaRacuna = em.createNamedQuery("Racun.findAll", Racun.class).getResultList();
                        int idKom = txt.getIntProperty("idCom");
                        
                        for(int i = listaRacuna.size() - 1; i >= 0; i--){
                            if(listaRacuna.get(i).getIdKomitent().getId() != idKom){
                                listaRacuna.remove(i);
                            }
                        }
                        
                        for(Racun r : listaRacuna) {
                            r.setTransakcijaList(null);
                            r.setTransakcijaList1(null);
                            r.getIdKomitent().setRacunList(null);
                            System.out.println(r.getId());
                        }

                        ObjectMessage objMsg = context.createObjectMessage();
                        objMsg.setObject((Serializable)listaRacuna);

                        producer.send(response_queue, objMsg);
                    }
                    else if(op == 14){
                        EntityManager em = em_factory.createEntityManager();
                        int idAcc = txt.getIntProperty("idAcc");
                        Racun r = em.find(Racun.class, idAcc);

                        List<Transakcija> t1 = r.getTransakcijaList();
                        List<Transakcija> t2 = r.getTransakcijaList1();
                        t1.addAll(t2);
                        
                        for(Transakcija t : t1){
                            if(t.getIdRacunNa() != null) {
                                t.getIdRacunNa().setTransakcijaList(null);
                                t.getIdRacunNa().setIdKomitent(null);
                                t.getIdRacunNa().setTransakcijaList1(null);   
                            }
                            if(t.getIdRacunSa() != null) {
                                t.getIdRacunSa().setTransakcijaList(null);
                                t.getIdRacunSa().setIdKomitent(null);
                                t.getIdRacunSa().setTransakcijaList1(null);
                            }
                        }

                        ObjectMessage objMsg = context.createObjectMessage();
                        objMsg.setObject((Serializable)t1);

                        producer.send(response_queue, objMsg);
                    }


                } catch (JMSException ex) {
                    Logger.getLogger(System2Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }   
        }
    }
    
}
