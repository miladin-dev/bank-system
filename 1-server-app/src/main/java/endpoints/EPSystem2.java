/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.*;
import java.math.BigDecimal;
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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author miled
 */
@Path("system2")
public class EPSystem2 {
    @Resource(lookup="conn_factory")
    private ConnectionFactory connFactory;

    @Resource(lookup="subsystem_topic")
    private Topic topic;
    
    @Resource(lookup="response_queue")
    private Queue response_queue;
    
    
    @POST
    @Path("rezkop")
    public void rezkopija(){
        JMSContext context =  connFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer cons = context.createConsumer(response_queue);
        
        
        TextMessage objMsg = context.createTextMessage();
        try {
            objMsg.setIntProperty("op", 0);
            
            producer.send(topic, objMsg);
            
        } catch (JMSException ex) {
            Logger.getLogger(EPSystem2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @POST
    @Path("openAccount/{idCom}/{allMinus}")
    public void openAccount5(@PathParam("idCom") int idCom, @PathParam("allMinus") String minus) {
        JMSContext context =  connFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer cons = context.createConsumer(response_queue);
        
        
        TextMessage objMsg = context.createTextMessage();
        try {
            objMsg.setIntProperty("op", 5);
            objMsg.setIntProperty("idCom", idCom);
            objMsg.setStringProperty("minus", minus);
            
            producer.send(topic, objMsg);
            
        } catch (JMSException ex) {
            Logger.getLogger(EPSystem2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @POST
    @Path("closeAccount/{idAcc}")
    public void closeAccount6(@PathParam("idAcc") int idAcc) {
        JMSContext context =  connFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer cons = context.createConsumer(response_queue);
        
        
        TextMessage objMsg = context.createTextMessage();
        try {
            objMsg.setIntProperty("op", 6);
            objMsg.setIntProperty("idAcc", idAcc);
            
            producer.send(topic, objMsg);
            
        } catch (JMSException ex) {
            Logger.getLogger(EPSystem2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @POST
    @Path("getAllAccounts/{idCom}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCommAccounts(@PathParam("idCom") int idCom) {
        JMSContext context =  connFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer cons = context.createConsumer(response_queue);
        
        
        TextMessage objMsg = context.createTextMessage();
        try {
            objMsg.setIntProperty("op", 13);
            objMsg.setIntProperty("idCom", idCom);
            
            producer.send(topic, objMsg);
            
            ObjectMessage obj = (ObjectMessage)cons.receive();
            List<Racun> list = (List<Racun>)obj.getObject();
            
            return Response.status(Response.Status.OK).entity(new GenericEntity<List<Racun>>(list) {}).build();
            
        } catch (JMSException ex) {
            Logger.getLogger(EPSystem2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.noContent().build();
    }
    
    @POST
    @Path("transaction/transfer/{idAccFrom}/{idAccTo}/{amount}/{purpose}")
    public void createTransaction7(@PathParam("idAccFrom") int idAccFrom, @PathParam("idAccTo") int idAccTo, @PathParam("amount") String amount, @PathParam("purpose") String purpose){
        JMSContext context =  connFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer cons = context.createConsumer(response_queue);
        
        TextMessage objMsg = context.createTextMessage();
        try {
            objMsg.setIntProperty("op", 7);
            objMsg.setIntProperty("idAccFrom", idAccFrom);
            objMsg.setIntProperty("idAccTo", idAccTo);
            objMsg.setStringProperty("amount", amount);
            objMsg.setStringProperty("purpose", purpose);
            
            producer.send(topic, objMsg);
            
        } catch (JMSException ex) {
            Logger.getLogger(EPSystem2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @POST
    @Path("transaction/payment/{idAccTo}/{amount}/{purpose}/{idFil}")
    public void createTransaction8(@PathParam("idAccTo") int idAccTo, @PathParam("amount") String amount, @PathParam("purpose") String purpose, @PathParam("idFil") int idFil){
        JMSContext context =  connFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer cons = context.createConsumer(response_queue);
        
        
        TextMessage objMsg = context.createTextMessage();
        try {
            objMsg.setIntProperty("op", 8);
            objMsg.setIntProperty("idAccTo", idAccTo);
            objMsg.setStringProperty("amount", amount);
            objMsg.setIntProperty("idFil", idFil);
            objMsg.setStringProperty("purpose", purpose);
            
            producer.send(topic, objMsg);
            
        } catch (JMSException ex) {
            Logger.getLogger(EPSystem2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @POST
    @Path("transaction/withdrawal/{idAccFrom}/{amount}/{purpose}/{idFil}")
    public void createTransaction9(@PathParam("idAccFrom") int idAccFrom, @PathParam("amount") String amount, @PathParam("purpose") String purpose, @PathParam("idFil") int idFil){
        JMSContext context =  connFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer cons = context.createConsumer(response_queue);
        
        
        TextMessage objMsg = context.createTextMessage();
        try {
            objMsg.setIntProperty("op", 9);
            objMsg.setIntProperty("idAccFrom", idAccFrom);
            objMsg.setStringProperty("amount", amount);
            objMsg.setIntProperty("idFil", idFil);
            objMsg.setStringProperty("purpose", purpose);
            
            producer.send(topic, objMsg);
            
        } catch (JMSException ex) {
            Logger.getLogger(EPSystem2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @POST
    @Path("transaction/getAllTransactions/{idAcc}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTransactions14(@PathParam("idAcc") int idAcc){
        JMSContext context =  connFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer cons = context.createConsumer(response_queue);
        
        
        TextMessage objMsg = context.createTextMessage();
        try {
            objMsg.setIntProperty("op", 14);
            objMsg.setIntProperty("idAcc", idAcc);

            
            producer.send(topic, objMsg);
            
            ObjectMessage obj = (ObjectMessage)cons.receive();
            List<Transakcija> list = (List<Transakcija>)obj.getObject();
            
            return Response.status(Response.Status.OK).entity(new GenericEntity<List<Transakcija>>(list) {}).build();
           
        } catch (JMSException ex) {
            Logger.getLogger(EPSystem2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.noContent().build();
    }
    
    
    
    
    
}
