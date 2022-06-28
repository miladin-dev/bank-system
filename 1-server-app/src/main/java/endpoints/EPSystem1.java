package endpoints;


import entities.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author miled
 */
@Path("system1")
public class EPSystem1 {
    
    @Resource(lookup="conn_factory")
    private ConnectionFactory connFactory;

    @Resource(lookup="subsystem_topic")
    private Topic topic;
    
    @Resource(lookup="response_queue")
    private Queue response_queue;
    

    
    @POST
    @Path("mesto/{naziv}/{postanski_broj}")
    public void createPlace1(@PathParam("naziv") String naziv, @PathParam("postanski_broj") String broj) {
        JMSContext context =  connFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        TextMessage objMsg = context.createTextMessage();
        try {
            objMsg.setIntProperty("op", 1);
            objMsg.setStringProperty("naziv", naziv);
            objMsg.setStringProperty("postanski_broj", broj);
            
            producer.send(topic, objMsg);
        } catch (JMSException ex) {
            Logger.getLogger(EPSystem1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @POST
    @Path("filijala/{naziv}/{adresa}/{id_mesto}")
    public Response createFilijala2(@PathParam("naziv") String naziv, @PathParam("adresa") String adresa, @PathParam("id_mesto") int id_mesto) {
        JMSContext context =  connFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(response_queue);
        
        TextMessage objMsg = context.createTextMessage();
        try {
            objMsg.setIntProperty("op", 2);
            objMsg.setStringProperty("naziv", naziv);
            objMsg.setStringProperty("adresa", adresa);
            objMsg.setIntProperty("mesto", id_mesto);
            
            producer.send(topic, objMsg);
            
            TextMessage resTxt = (TextMessage)consumer.receive();
            String res = resTxt.getStringProperty("resTxt");
            
            return Response.status(Response.Status.OK).entity(res).build();
        } catch (JMSException ex) {
            Logger.getLogger(EPSystem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.noContent().build();
    }
    
    @POST
    @Path("komitent/dodaj/{naziv}/{adresa}/{id_mesto}")
    public Response createKomitent3(@PathParam("naziv") String naziv, @PathParam("adresa") String adresa, @PathParam("id_mesto") int id_mesto) {
        JMSContext context =  connFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(response_queue);
        
        TextMessage objMsg = context.createTextMessage();
        try {
            objMsg.setIntProperty("op", 3);
            objMsg.setStringProperty("naziv", naziv);
            objMsg.setStringProperty("adresa", adresa);
            objMsg.setIntProperty("mesto", id_mesto);
            
            producer.send(topic, objMsg);
            
            TextMessage resTxt = (TextMessage)consumer.receive();
            String res = resTxt.getStringProperty("resTxt");
            
            return Response.status(Response.Status.OK).entity(res).build();
        } catch (JMSException ex) {
            Logger.getLogger(EPSystem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         return Response.noContent().build();
    }
    
    @POST
    @Path("komitent/{naziv}/{novo_mesto}")
    public Response changeKomitentPlace4(@PathParam("naziv") String naziv, @PathParam("novo_mesto") int mesto) {
        JMSContext context =  connFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(response_queue);
        
        TextMessage objMsg = context.createTextMessage();
        try {
            objMsg.setIntProperty("op", 4);
            objMsg.setStringProperty("naziv", naziv);
            objMsg.setIntProperty("novo_mesto", mesto);
            
            producer.send(topic, objMsg);
            
            
            TextMessage resTxt = (TextMessage)consumer.receive();
            String res = resTxt.getStringProperty("resTxt");
            
            return Response.status(Response.Status.OK).entity(res).build();
        } catch (JMSException ex) {
            Logger.getLogger(EPSystem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.noContent().build();
    }
    
    
    @GET
    @Path("svaMesta")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPlaces10() {
        JMSContext context =  connFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(response_queue);
        
        TextMessage objMsg = context.createTextMessage();
        try {
            objMsg.setIntProperty("op", 10);
            
            producer.send(topic, objMsg);
            
            ObjectMessage obj = (ObjectMessage)consumer.receive();
            List<Mesto> list = (List<Mesto>)obj.getObject();
            
            return Response.status(Response.Status.OK).entity(new GenericEntity<List<Mesto>>(list) {}).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(EPSystem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.noContent().build();
    }
    
    @GET
    @Path("sveFilijale")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFilijale11() {
        JMSContext context =  connFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(response_queue);
        
        TextMessage objMsg = context.createTextMessage();
        try {
            objMsg.setIntProperty("op", 11);
            
            producer.send(topic, objMsg);
            
            ObjectMessage obj = (ObjectMessage)consumer.receive();
            List<Filijala> list = (List<Filijala>)obj.getObject();
            
            return Response.status(Response.Status.OK).entity(new GenericEntity<List<Filijala>>(list) {}).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(EPSystem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.noContent().build();
    }
    
    @GET
    @Path("allComitents")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllComitents12() {
        JMSContext context =  connFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createConsumer(response_queue);
        
        TextMessage objMsg = context.createTextMessage();
        try {
            objMsg.setIntProperty("op", 12);
            
            producer.send(topic, objMsg);
            
            ObjectMessage obj = (ObjectMessage)consumer.receive();
            List<Komitent> list = (List<Komitent>)obj.getObject();
            
            return Response.status(Response.Status.OK).entity(new GenericEntity<List<Komitent>>(list) {}).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(EPSystem1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.noContent().build();
    }
    
    
    
}
