/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system3;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;
import javax.jms.Topic;

/**
 *
 * @author miled
 */
public class BackUpThread extends Thread{
    @Resource(lookup="conn_factory")
    private ConnectionFactory connFactory;

    
    @Resource(lookup="subsystem_topic")
    private Topic request;
    
    @Override
    public void run(){
        JMSContext context =  connFactory.createContext();
        JMSProducer prod = context.createProducer();
        JMSConsumer cons = context.createConsumer(request);
        try {

            
            Thread.sleep(20000);
            
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
