/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system1;

import entities.Filijala;
import entities.Komitent;
import entities.Mesto;
import java.io.Serializable;
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
public class System1Main {
    
    
    @Resource(lookup="conn_factory")
    private static ConnectionFactory connFactory;
    
    @Resource(lookup="subsystem_topic")
    private static Topic topic;
    
    @Resource(lookup="response_queue")
    private static Queue response1_queue;
    
    @Resource(lookup="s1s2_queue")
    private static Queue s1s2_queue;

    
    public static void main(String[] args) {
        JMSContext context =  connFactory.createContext();
        JMSConsumer consumer = context.createConsumer(topic);
        JMSProducer producer = context.createProducer();
        EntityManagerFactory em_factory = Persistence.createEntityManagerFactory("bank1PU");
        
        while(true){
            Message msg = consumer.receive();
            TextMessage txt;
            if(msg instanceof TextMessage){
                txt = (TextMessage) msg;
            
                try {
                    int op = txt.getIntProperty("op");
                        if(op == 0 || op == -4){
                        EntityManager em = em_factory.createEntityManager();
                        List<Mesto> lr = em.createNamedQuery("Mesto.findAll", Mesto.class).getResultList();
                        List<Filijala> lt = em.createNamedQuery("Filijala.findAll", Filijala.class).getResultList();
                        List<Komitent> lk = em.createNamedQuery("Komitent.findAll", Komitent.class).getResultList();

                        List<Object> all = new ArrayList<>();
                        all.addAll(lk);
                        all.addAll(lr);
                        all.addAll(lt);

                        ObjectMessage obj = context.createObjectMessage((Serializable)all);
                        obj.setIntProperty("op", -1);

                        producer.send(topic, obj);
                    }
                    if(op == 1) {
                        EntityManager em = em_factory.createEntityManager();
                        String naziv = txt.getStringProperty("naziv");
                        String post_br = txt.getStringProperty("postanski_broj");

                        Mesto m = new Mesto();
                        m.setNaziv(naziv);
                        m.setPostanskiBroj(post_br);
                        em.getTransaction().begin();
                        em.persist(m);
                        em.getTransaction().commit();
                    }
                    else if(op == 2) {
                        EntityManager em = em_factory.createEntityManager();
                        TextMessage resTxt = context.createTextMessage();
                        String naziv = txt.getStringProperty("naziv");
                        String adresa = txt.getStringProperty("adresa");
                        int id_mesto = txt.getIntProperty("mesto");
                        Mesto m = em.find(Mesto.class, id_mesto);
                        if(m == null){
                            resTxt.setStringProperty("resTxt", "Mesto sa zadatim ID-em ne postoji.");
                            producer.send(response1_queue, resTxt);
                            continue;
                        }

                        Filijala f = new Filijala();
                        f.setNaziv(naziv);
                        f.setAdresa(adresa);
                        f.setIdMesto(m);
                        em.getTransaction().begin();
                        em.persist(f);
                        em.getTransaction().commit();
                        resTxt.setStringProperty("resTxt", "OK");
                        producer.send(response1_queue, resTxt);
                    }
                    else if(op == 3){
                        EntityManager em = em_factory.createEntityManager();
                        TextMessage resTxt = context.createTextMessage();
                        String naziv = txt.getStringProperty("naziv");
                        String adresa = txt.getStringProperty("adresa");
                        int id_mesto = txt.getIntProperty("mesto");
                        Mesto m = em.find(Mesto.class, id_mesto);
                        if(m == null){
                            resTxt.setStringProperty("resTxt", "Mesto sa zadatim ID-em ne postoji.");
                            producer.send(response1_queue, resTxt);
                            producer.send(s1s2_queue, resTxt);
                            continue;
                        }
                        Komitent k = new Komitent();
                        k.setNaziv(naziv);
                        k.setAdresa(adresa);
                        k.setIdMesto(id_mesto);
                        em.getTransaction().begin();
                        em.persist(k);
                        em.getTransaction().commit();
                        resTxt.setStringProperty("resTxt", "OK");
                        producer.send(response1_queue, resTxt);
                        producer.send(s1s2_queue, resTxt);
                    }
                    else if(op == 4){
                        EntityManager em = em_factory.createEntityManager();
                        TextMessage resTxt = context.createTextMessage();
                        String naziv = txt.getStringProperty("naziv");
                        int id_mesto = txt.getIntProperty("novo_mesto");
                        Mesto m = em.find(Mesto.class, id_mesto);
                        if(m == null){
                            resTxt.setStringProperty("resTxt", "Mesto sa zadatim ID-em ne postoji.");
                            producer.send(response1_queue, resTxt);
                            producer.send(s1s2_queue, resTxt);
                            continue;
                        }
                        
                        List<Komitent> lK = em.createNamedQuery("Komitent.findByNaziv", Komitent.class).setParameter("naziv", naziv).getResultList();

                        Komitent k = lK.get(0);
                        k.setIdMesto(id_mesto);

                        em.getTransaction().begin();
                        em.persist(k);
                        em.getTransaction().commit();
                        resTxt.setStringProperty("resTxt", "OK");
                        producer.send(response1_queue, resTxt);
                        producer.send(s1s2_queue, resTxt);
                    }
                    else if(op == 10) {
                        EntityManager em = em_factory.createEntityManager();
                        List<Mesto> listaMesta = em.createNamedQuery("Mesto.findAll", Mesto.class).getResultList();

                        for(Mesto m : listaMesta) {
                            m.setFilijalaList(null);
                            //m.setKomitentList(null);
                        }
                        
                        ObjectMessage objMsg = context.createObjectMessage();
                        objMsg.setObject((Serializable)listaMesta);

                        producer.send(response1_queue, objMsg);
                    }
                    else if(op == 11) {
                        EntityManager em = em_factory.createEntityManager();
                        List<Filijala> listaFil = em.createNamedQuery("Filijala.findAll", Filijala.class).getResultList();

                        for(Filijala f : listaFil){
                            f.getIdMesto().setFilijalaList(null);
                        }
                        
                        ObjectMessage objMsg = context.createObjectMessage();
                        objMsg.setObject((Serializable)listaFil);

                        producer.send(response1_queue, objMsg);
                    }
                    else if(op == 12){
                        EntityManager em = em_factory.createEntityManager();
                        List<Komitent> lista = em.createNamedQuery("Komitent.findAll", Komitent.class).getResultList();

                        ObjectMessage objMsg = context.createObjectMessage();
                        objMsg.setObject((Serializable)lista);

                        producer.send(response1_queue, objMsg);
                    }




                    System.out.println(txt.getIntProperty("op"));

                } catch (JMSException ex) {
                    Logger.getLogger(System1Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
