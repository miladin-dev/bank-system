/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Filijala;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author miled
 */
@Path("system3")  
public class EPSystem3 {
    @Resource(lookup="conn_factory")
    private ConnectionFactory connFactory;

    @Resource(lookup="subsystem_topic")
    private Topic topic;
    
    @Resource(lookup="response_queue")
    private Queue response_queue;
    
    @GET
    @Path("getDiff")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiff(){
        JMSContext context =  connFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer cons = context.createConsumer(response_queue);
        
        
        TextMessage objMsg = context.createTextMessage();
        try {
            objMsg.setIntProperty("op", 16);
            producer.send(topic, objMsg);
            
            TextMessage o = (TextMessage)cons.receive();
            
            String s = o.getStringProperty("retDiff");
            
            
            return Response.status(Response.Status.OK).entity(s).build();
        } catch (JMSException ex) {
            Logger.getLogger(EPSystem2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.noContent().build();
    }
    
    @GET
    @Path("getBackup")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBackup(){
        JMSContext context =  connFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer cons = context.createConsumer(response_queue);
         
        TextMessage objMsg = context.createTextMessage();
        try {
            objMsg.setIntProperty("op", 15);
            
            producer.send(topic, objMsg);
            
            TextMessage obj = (TextMessage)cons.receive();
            String list = obj.getStringProperty("all");
            
            return Response.status(Response.Status.OK).entity(list).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(EPSystem2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.noContent().build();
    }
}
