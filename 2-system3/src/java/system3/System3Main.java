/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system3;

import entities.*;
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
import javax.persistence.Query;
/**
 *
 * @author miled
 */
public class System3Main {

    
    @Resource(lookup="conn_factory")
    private static ConnectionFactory connFactory;
    
    @Resource(lookup="response_queue")
    private static Queue response_queue;
    
    
    @Resource(lookup="subsystem_topic")
    private static Topic subsystem_topic;
    
    private static void em_persist(EntityManager em, Object o){
            em.getTransaction().begin();
            em.persist(o);
            em.getTransaction().commit();
    }
    
    private static void em_remove(EntityManager em, Object o){
            em.getTransaction().begin();
            em.remove(o);
            em.getTransaction().commit();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JMSContext context =  connFactory.createContext();
        JMSConsumer consumer = context.createConsumer(subsystem_topic, null, true);
        JMSProducer producer = context.createProducer();
        EntityManagerFactory em_factory = Persistence.createEntityManagerFactory("bankPU");
        
        Thread t = new Thread() {
            @Override
            public void run(){
                try {
                    while(true){
                        Thread.sleep(120000);

                        TextMessage txt = context.createTextMessage();
                        txt.setIntProperty("op", 0);

                        producer.send(subsystem_topic, txt);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(System3Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JMSException ex) {
                    Logger.getLogger(System3Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }; 
        
        t.start();
       
        while(true){
            
            Message msg = consumer.receive();
            
            try {
                System.out.println(msg.getIntProperty("op"));
                if(msg instanceof TextMessage){
                    TextMessage txt = (TextMessage)msg;
                    int op = txt.getIntProperty("op");

                    if(op == 15) {
                        EntityManager em = em_factory.createEntityManager();

                        List<Filijala> lf = em.createNamedQuery("Filijala.findAll", Filijala.class).getResultList();
                        List<Komitent> lk = em.createNamedQuery("Komitent.findAll", Komitent.class).getResultList();
                        List<Mesto> lm = em.createNamedQuery("Mesto.findAll", Mesto.class).getResultList();
                        List<Racun> lr = em.createNamedQuery("Racun.findAll", Racun.class).getResultList();
                        List<Transakcija> lt = em.createNamedQuery("Transakcija.findAll", Transakcija.class).getResultList();

                        List<Object> all = new ArrayList<>();
                        all.addAll(lf);
                        all.addAll(lk);
                        all.addAll(lm);
                        all.addAll(lr);
                        all.addAll(lt);

                        String sf = "";
                        for(Filijala f : lf){
                            sf += f.toString() + "\n";
                        }
                        for(Komitent k : lk){
                            sf += k.toString() + "\n";
                        }
                        for(Mesto k : lm){
                            sf += k.toString() + "\n";
                        }
                        for(Racun k : lr){
                            sf += k.toString() + "\n";
                        }
                        for(Transakcija k : lt){
                            sf += k.toString() + "\n";
                        }
                        
                        
                        TextMessage txtmsgg = context.createTextMessage();
                        
                        txtmsgg.setStringProperty("all", sf);
                        //objMsg.setObjectProperty("komitent", lk);

                        producer.send(response_queue, txtmsgg);
                        
                        em.close();
                    }
                    else if(op == 16){
                        EntityManager em = em_factory.createEntityManager();
                        TextMessage text = context.createTextMessage();
                        text.setIntProperty("op", -4);
                        producer.send(subsystem_topic, text);
                        ObjectMessage o = (ObjectMessage)consumer.receive();
                        List l1 = (List)o.getObject();
                        o = (ObjectMessage)consumer.receive();
                        List l2 = (List)o.getObject();
                        
                        l1.addAll(l2);
                        
                        List<Filijala> lf = em.createNamedQuery("Filijala.findAll", Filijala.class).getResultList();
                        List<Komitent> lk = em.createNamedQuery("Komitent.findAll", Komitent.class).getResultList();
                        List<Mesto> lm = em.createNamedQuery("Mesto.findAll", Mesto.class).getResultList();
                        List<Racun> lr = em.createNamedQuery("Racun.findAll", Racun.class).getResultList();
                        List<Transakcija> lt = em.createNamedQuery("Transakcija.findAll", Transakcija.class).getResultList();
                        
                        List<Object> all = new ArrayList<>();
                        all.addAll(lf);
                        all.addAll(lk);
                        all.addAll(lm);
                        all.addAll(lr);
                        all.addAll(lt);
                        
                        List<Object> retList = new ArrayList<>();
                        String retS = "Originalni podaci - razlika: ";
                        boolean found = false;
                        
                        for(Object obj : l1){
                            System.out.println("--" + obj.toString());
                            for(Object a : all){
                                System.out.println(a.toString());
                                if(a.toString().equals(obj.toString())){
                                    System.out.println("NASAO");
                                    found = true;
                                    break;
                                }
                            }
                            
                            if(!found){
                                retS += obj.toString();
                                retS += "\n";
                            }
                            
                            found = false;
                        }
                        
                        retS += "Rezervni podaci - razlika u odnosu na originalne: ";
                        
                        for(Object obj : all){
                            for(Object a : l1){
                                if(a.toString().equals(obj.toString())){
                                    found = true;
                                    break;
                                }
                            }
                            
                            if(!found){
                                retS += obj.toString();
                                retS += "\n";
                            }
                            
                            found = false;
                        }
                        TextMessage retTxt = context.createTextMessage();
                        retTxt.setStringProperty("retDiff", retS);
                        producer.send(response_queue, retTxt);
                        
                    }
                }
                if(msg instanceof ObjectMessage){
                    ObjectMessage obj = (ObjectMessage)msg;
                    int op = obj.getIntProperty("op");
                    
                    if(op == -1){
                        EntityManager em = em_factory.createEntityManager();
                        List l = (List)obj.getObject();
                        em.getTransaction().begin();
                        
                        for(Object o : l){
                            //em_persist(em, o);
                            //System.out.println(o.toString());
                            
                            
                            if(o instanceof Komitent){
                                Komitent k = (Komitent)o;
                                Komitent kn = em.find(Komitent.class, k.getId());
                                if(kn == null)
                                    em.persist(k);
                                else if(!kn.toString().equals(k.toString())){
                                    kn.setIdMesto(k.getIdMesto());
                                    em.persist(kn);
                                            
                                }

                            }
                            else if(o instanceof Filijala){
                                Filijala k = (Filijala)o;
                                Filijala kn = em.find(Filijala.class, k.getId());
                                if(kn == null)
                                    em.persist(k);

                            }
                            else if(o instanceof Mesto){
                                Mesto k = (Mesto)o;
                                Mesto kn = em.find(Mesto.class, k.getId());
                                if(kn == null)
                                    em.persist(k);

                            }
                            else if(o instanceof Racun){
                                Racun k = (Racun)o;
                                Racun kn = em.find(Racun.class, k.getId());
                                if(kn == null)
                                    em.persist(k);

                            }
                            else if(o instanceof Transakcija){
                                Transakcija k = (Transakcija)o;
                                Transakcija kn = em.find(Transakcija.class, k.getId());
                                if(kn == null)
                                    em.persist(k);

                            }
                        }
                        em.getTransaction().commit();
                    }
                    
                    
                }
                
                

            } catch (JMSException ex) {
                Logger.getLogger(System3Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

class TT extends Thread {
    @Resource(lookup="conn_factory")
    private static ConnectionFactory connFactory;

    
    @Resource(lookup="subsystem_topic")
    private static Topic request;
    
    @Override
    public void run(){
        JMSContext context =  connFactory.createContext();
        JMSProducer prod = context.createProducer();
        JMSConsumer cons = context.createConsumer(request);
        try {

            
            Thread.sleep(60000);
            
            TextMessage txt = context.createTextMessage();
            txt.setIntProperty("op", 0);
            
            prod.send(request, txt);
            
//            ObjectMessage obj = (ObjectMessage)cons.receive();
            
            
            
        } catch (InterruptedException ex) {
            Logger.getLogger(TT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JMSException ex) {
            Logger.getLogger(TT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
