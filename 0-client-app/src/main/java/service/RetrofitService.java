/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import entities.Filijala;
import entities.Komitent;
import entities.Mesto;
import entities.Racun;
import entities.Transakcija;
import java.math.BigDecimal;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 *
 * @author miled
 */
public interface RetrofitService {
    @POST("mesto/{naziv}/{postanski_broj}")
    Call<List<String>> createPlace1(@Path("naziv") String naziv, @Path("postanski_broj") String post_broj);
    
    @POST("filijala/{naziv}/{adresa}/{id_mesto}")
    Call<String> createFilijala2(@Path("naziv") String naziv, @Path("adresa") String adresa, @Path("id_mesto") int id_mesto);
    
    @POST("komitent/dodaj/{naziv}/{adresa}/{id_mesto}")
    Call<String> createKomitent3(@Path("naziv") String naziv, @Path("adresa") String adresa, @Path("id_mesto") int id_mesto);
    
    @POST("komitent/{naziv}/{novo_mesto}")
    Call<String> changeKomitentPlace4(@Path("naziv") String naziv, @Path("novo_mesto") int mesto);
    
    @GET("svaMesta")
    Call<List<Mesto>> getAllPlaces10();
    
    @GET("sveFilijale")
    Call<List<Filijala>> getAllFilijala11();
    
    @GET("allComitents")
    Call<List<Komitent>> getAllComitents12();
    
    // -------------------
    
    @POST("openAccount/{idCom}/{allMinus}")
    Call<String> openAccount5(@Path("idCom") int idCom, @Path("allMinus") String allMinus);
    
    @POST("closeAccount/{idAcc}/")
    Call<String> closeAccount6(@Path("idAcc") int idAcc);
    
    @POST("getAllAccounts/{idCom}")
    Call<List<Racun>> getAllCommAccounts(@Path("idCom") int idCom);
    
    @POST("transaction/transfer/{idAccFrom}/{idAccTo}/{amount}/{purpose}")
    Call<String> createTransaction7(@Path("idAccFrom") int idAccFrom, @Path("idAccTo") int idAccTo, @Path("amount") String amount, @Path("purpose") String purpose);
    
    @POST("transaction/payment/{idAccTo}/{amount}/{purpose}/{idFil}")
    Call<String> createTransaction8(@Path("idAccTo") int idAccTo, @Path("amount") String amount, @Path("purpose") String purpose, @Path("idFil") int idFil);
    
    @POST("transaction/withdrawal/{idAccFrom}/{amount}/{purpose}/{idFil}")
    Call<String> createTransaction9(@Path("idAccFrom") int idAccFrom, @Path("amount") String amount, @Path("purpose") String purpose, @Path("idFil") int idFil);
    
    @POST("transaction/getAllTransactions/{idAcc}")
    Call<List<Transakcija>> getAllTransactions(@Path("idAcc") int idAcc);
    
    // -------------------
    @GET("getBackup")
    Call<String> getBackup();
    
    @GET("getDiff")
    Call<String> getDiff();
}
