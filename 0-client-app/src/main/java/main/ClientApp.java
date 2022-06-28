/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import entities.Filijala;
import entities.Komitent;
import entities.Mesto;
import entities.Racun;
import entities.Transakcija;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import service.RetrofitService;


/**
 *
 * @author miled
 */
public class ClientApp {
    
    public static void main(String[] args) throws IOException {
        while(true) {
            System.out.println("\n \n Unesite zahtev:");
            System.out.println("\t1. Kreiranje mesta\n" +
                "\t2. Kreiranje filijale u mestu\n" +
                "\t3. Kreiranje komitenta\n" +
                "\t4. Promena sedišta za zadatog komitenta\n" +
                "\t5. Otvaranje racuna\n" +
                "\t6. Zatvaranje racuna\n" +
                "\t7. Kreiranje transakcije koja je prenos sume sa jednog racuna na drugi racun\n" +
                "\t8. Kreiranje transakcije koja je uplata novca na račun\n" +
                "\t9. Kreiranje transakcije koja je isplata novca sa računa\n" +
                "\t10. Dohvatanje svih mesta\n" +
                "\t11. Dohvatanje svih filijala\n" +
                "\t12. Dohvatanje svih komitenata\n" +
                "\t13. Dohvatanje svih računa za komitenta\n" +
                "\t14. Dohvatanje svih transakcija za račun\n" +
                "\t15. Dohvatanje svih podataka iz rezervne kopije\n" +
                "\t16. Dohvatanje razlike u podacima u originalnim podacima i u rezervnoj kopiji\n");


            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int op = Integer.parseInt(br.readLine());
            String baseUrl = "http://localhost:8080/1-server-app/api/";
            
            if((op >= 1 && op <= 4) || (op >= 10 && op <= 12))
                baseUrl += "system1/";
            if((op >= 5 && op <= 9) || (op >= 13 && op <= 14))
                baseUrl += "system2/";
            if(op == 15 || op == 16)
                baseUrl += "system3/";
            
            
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
            
            RetrofitService service = retrofit.create(RetrofitService.class);

            System.out.println("\n");
            if(op == 1) {
                System.out.println("Unesite naziv mesta:");
                String naziv = br.readLine();
                System.out.println("Unesite postanski broj:");
                String broj = br.readLine();
                service.createPlace1(naziv, broj).execute();
            }
            else if(op == 2) {
                System.out.println("Unesite naziv filijale:");
                String naziv = br.readLine();
                System.out.println("Unesite adresu filijale:");
                String adresa = br.readLine();
                System.out.println("Unesite id mesta filijale:");
                int mesto = Integer.parseInt(br.readLine());
                String s = service.createFilijala2(naziv, adresa, mesto).execute().body();
                if(!s.equals("OK")){
                    System.out.println(s);
                }
            }
            else if(op == 3) {
                System.out.println("Unesite naziv komitenta:");
                String naziv = br.readLine();
                System.out.println("Unesite adresu komitenta:");
                String adresa = br.readLine();
                System.out.println("Unesite id mesta komitenta:");
                int mesto = Integer.parseInt(br.readLine());
                String s = service.createKomitent3(naziv, adresa, mesto).execute().body();
                if(!s.equals("OK")){
                    System.out.println(s);
                }
            }
            else if(op == 4) {
                System.out.println("Unesite naziv komitenta:");
                String naziv = br.readLine();
                System.out.println("Unesite novo mesto komitenta:");
                int mesto = Integer.parseInt(br.readLine());
                String s = service.changeKomitentPlace4(naziv,  mesto).execute().body();
                if(!s.equals("OK")){
                    System.out.println(s);
                }
            }
            else if(op == 5){
                System.out.println("Unesite komitenta koji otvara racun:");
                String id = br.readLine();
                System.out.println("Unesite dozvoljeni minus:");
                String dozv_minus = br.readLine();
                Call<String> call = service.openAccount5(Integer.parseInt(id), dozv_minus);
                String res = call.execute().body();
            }
            else if(op == 6){
                System.out.println("Unesite komitenta koji zatvara racun:");
                int id = Integer.parseInt(br.readLine());
                
                //ovde izlistati sve komitentove racune i odabrati koji da zatvori
                Call<List<Racun>> callK = service.getAllCommAccounts(id);
                List<Racun> listR = callK.execute().body();
                for(Racun r : listR){
                    System.out.println(r.getId() + " | " + r.getDatumVreme() + " | " + r.getStanje());
                }
                
                System.out.println("Izaberite racun za zatvaranje:");
                int racun = Integer.parseInt(br.readLine());
                service.closeAccount6(racun).execute();
            }
            else if(op == 7){
                System.out.println("Unesite racun sa:");
                int idSA = Integer.parseInt(br.readLine());
                System.out.println("Unesite racun na:");
                int idNA = Integer.parseInt(br.readLine());
                System.out.println("Unesite sumu:");
                String suma = br.readLine();
                System.out.println("Svrha uplate:");
                String svrha = br.readLine();
                
                service.createTransaction7(idSA, idNA, suma, svrha).execute();
            }
            else if(op == 8){
                System.out.println("Unesite racun uplate:");
                int idNA = Integer.parseInt(br.readLine());
                System.out.println("Unesite sumu:");
                String suma = br.readLine();
                System.out.println("Svrha uplate:");
                String svrha = br.readLine();
                System.out.println("Filijala uplate:");
                int idfil = Integer.parseInt(br.readLine());
                
                service.createTransaction8(idNA, suma, svrha, idfil).execute();
                //odgovor da je uplacen
            }
            else if(op == 9){
                System.out.println("Unesite racun isplate:");
                int idSA = Integer.parseInt(br.readLine());
                System.out.println("Unesite sumu:");
                String suma = br.readLine();
                System.out.println("Svrha isplate:");
                String svrha = br.readLine();
                System.out.println("Filijala isplate:");
                int idfil = Integer.parseInt(br.readLine());
                
                service.createTransaction9(idSA, suma, svrha, idfil).execute();
                //odogovor ako je isplacena suma, ili ako nema novca za isplatu
            }
            else if(op == 10){
                Call<List<Mesto>> call = service.getAllPlaces10();
                List<Mesto> odg = call.execute().body();

                for(Mesto m : odg) {
                    System.out.println(m.toString());
                }
            }
            else if(op == 11){
                Call<List<Filijala>> call = service.getAllFilijala11();
                List<Filijala> odg = call.execute().body();

                for(Filijala f : odg) {
                    System.out.println(f);
                }
            }
            else if(op == 12){
                Call<List<Komitent>> call = service.getAllComitents12();
                List<Komitent> odg = call.execute().body();

                for(Komitent k : odg) {
                    System.out.println(k);
                }
            }
            else if(op == 13){
                System.out.println("Unesite komitenta:");
                int id = Integer.parseInt(br.readLine());
                
                Call<List<Racun>> callK = service.getAllCommAccounts(id);
                List<Racun> listR = callK.execute().body();
                for(Racun r : listR) {
                    System.out.println(r);
                }
            }
            else if(op == 14){
                System.out.println("Unesite racun za koji dohvatate transakcije:");
                int id = Integer.parseInt(br.readLine());
                
                Call<List<Transakcija>> callK = service.getAllTransactions(id);
                List<Transakcija> listR = callK.execute().body();
                for(Transakcija r : listR){
                    System.out.println(r.toString());
                }
            }
            else if(op == 15){
                String listF = service.getBackup().execute().body();
                System.out.println(listF);
                    
            }
            else if(op == 16){
                String diff = service.getDiff().execute().body();
                System.out.println(diff);
            }
            
        }
        
    }
}