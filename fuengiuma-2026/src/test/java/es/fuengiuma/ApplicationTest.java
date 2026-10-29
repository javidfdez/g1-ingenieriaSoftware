package es.fuengiuma;

import com.fasterxml.jackson.databind.*;
import es.fuengiuma.Domain.*;
import es.fuengiuma.Requests.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.*;
import org.springframework.transaction.support.TransactionTemplate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties={"spring.datasource.url=jdbc:h2:mem:fuengi-test;DB_CLOSE_DELAY=-1","spring.jpa.hibernate.ddl-auto=create-drop"})
@AutoConfigureMockMvc
class ApplicationTest {
    @Autowired MockMvc mvc; @Autowired ObjectMapper json; @Autowired RideService service; @Autowired SeedData seed;
    @Autowired Stores.Users users; @Autowired Stores.Trips trips; @Autowired Stores.Bookings bookings;
    @Autowired Stores.Messages messages; @Autowired Stores.Reviews reviews; @Autowired TransactionTemplate tx;
    record Client(MockHttpSession session,String token) {}
    @BeforeEach void freshData(){tx.executeWithoutResult(s->{reviews.deleteAll();messages.deleteAll();bookings.deleteAll();trips.deleteAll();users.deleteAll();});seed.run();}
    Client client() throws Exception {var result=mvc.perform(get("/api/bootstrap")).andExpect(status().isOk()).andReturn();return new Client((MockHttpSession)result.getRequest().getSession(),json.readTree(result.getResponse().getContentAsString()).get("csrfToken").asText());}
    ResultActions call(Client c,String method,String path,Object body) throws Exception {
        var request=switch(method){case "POST"->post(path);case "PATCH"->patch(path);case "DELETE"->delete(path);default->get(path);};
        request.session(c.session()).header("X-CSRF-Token",c.token());if(body!=null)request.contentType("application/json").content(json.writeValueAsString(body));return mvc.perform(request);
    }
    Client as(String email) throws Exception {var c=client();call(c,"POST","/api/auth/login",Map.of("email",email,"password","DemoUMA2026!")).andExpect(status().isOk());return c;}
    Long uid(String email){return users.findByEmail(email).orElseThrow().id;}
    Long ride(String email,boolean future){return tx.execute(s->trips.findAll().stream().filter(t->t.driver.email.equals(email)&&t.origin.equals("FUENGIROLA")&&t.departure.isAfter(LocalDateTime.now())==future).findFirst().orElseThrow().id);}
    Long pending(){return tx.execute(s->bookings.findAll().stream().filter(b->b.status==Status.PENDING).findFirst().orElseThrow().id);}

    @Test void csrfAndSessionBoundaries() throws Exception {
        var c=client();mvc.perform(post("/api/auth/demo").session(c.session())).andExpect(status().isForbidden());
        call(c,"GET","/api/me/trips",null).andExpect(status().isUnauthorized());
        call(c,"POST","/api/auth/demo",null).andExpect(status().isOk()).andExpect(jsonPath("$.email").value("alex@uma.es")).andExpect(jsonPath("$.passwordHash").doesNotExist());
        call(c,"GET","/api/bootstrap",null).andExpect(header().string("X-Content-Type-Options","nosniff")).andExpect(jsonPath("$.me.name").value("Álex Ruiz"));
        call(c,"POST","/api/auth/logout",null).andExpect(status().isOk());
        mvc.perform(get("/api/me/trips")).andExpect(status().isUnauthorized());
    }
    @Test void credentialsAreValidatedAndStoredAsHashes() throws Exception {
        var c=client();call(c,"POST","/api/auth/login",Map.of("email","alex@uma.es","password","wrong")).andExpect(status().isUnauthorized());
        call(c,"POST","/api/auth/register",Map.of("name","Prueba","email","prueba@example.com","password","Prueba2026!")).andExpect(status().isBadRequest());
        call(c,"POST","/api/auth/register",Map.of("name","Prueba","email","prueba@alu.uma.es","password","Prueba2026!")).andExpect(status().isOk());
        assertTrue(users.findByEmail("prueba@alu.uma.es").orElseThrow().passwordHash.startsWith("$2"));
        call(c,"POST","/api/auth/register",Map.of("name","Prueba","email","prueba@alu.uma.es","password","Prueba2026!")).andExpect(status().isConflict());
        call(c,"GET","/api/users/"+uid("lucia@uma.es"),null).andExpect(jsonPath("$.email").doesNotExist()).andExpect(jsonPath("$.passwordHash").doesNotExist());
    }
    @Test void searchFiltersRouteTimePriceAndSeats() throws Exception {
        var c=client();String day=service.today().plusDays(1).toString();
        var r=call(c,"GET","/api/trips?origin=FUENGIROLA&destination=CAMPUS_TEATINOS&date="+day+"&maxPrice=3&arriveBefore=09:00&seats=2",null).andExpect(status().isOk()).andReturn();
        var list=json.readTree(r.getResponse().getContentAsString());assertEquals(1,list.size());assertEquals("08:15",list.get(0).get("departure").asText());
        call(c,"GET","/api/trips?seats=0",null).andExpect(status().isBadRequest());
        call(c,"GET","/api/trips?origin=UNKNOWN",null).andExpect(status().isBadRequest());
    }
    @Test void onlyDriverCanManageAndConfirmedSeatsAreCounted() throws Exception {
        var alex=as("alex@uma.es");var outsider=as("hugo@uma.es");Long booking=pending();
        call(outsider,"PATCH","/api/bookings/"+booking,Map.of("action","accept")).andExpect(status().isForbidden());
        call(alex,"PATCH","/api/bookings/"+booking,Map.of("action","accept")).andExpect(status().isOk()).andExpect(jsonPath("$.availableSeats").value(1));
        call(alex,"PATCH","/api/bookings/"+booking,Map.of("action","accept")).andExpect(status().isConflict());
        call(alex,"PATCH","/api/bookings/"+booking,Map.of("action","remove")).andExpect(status().isOk()).andExpect(jsonPath("$.availableSeats").value(2));
        call(outsider,"DELETE","/api/trips/"+ride("alex@uma.es",true),null).andExpect(status().isForbidden());
    }
    @Test void cannotBookOwnDuplicatePastOrOversizeTrip() throws Exception {
        var alex=as("alex@uma.es");
        call(alex,"POST","/api/trips/"+ride("alex@uma.es",true)+"/bookings",Map.of("seats",1)).andExpect(status().isBadRequest());
        Long lucia=ride("lucia@uma.es",true);
        call(alex,"POST","/api/trips/"+lucia+"/bookings",Map.of("seats",1)).andExpect(status().isConflict());
        var irene=as("irene@uma.es");call(irene,"POST","/api/trips/"+lucia+"/bookings",Map.of("seats",3)).andExpect(status().isConflict());
        call(irene,"POST","/api/trips/"+ride("lucia@uma.es",false)+"/bookings",Map.of("seats",1)).andExpect(status().isConflict());
    }
    @Test void chatIsPrivateAndRevokedWhenRequestCloses() throws Exception {
        var alex=as("alex@uma.es");var hugo=as("hugo@uma.es");Long lucia=ride("lucia@uma.es",true);
        call(hugo,"GET","/api/trips/"+lucia+"/messages",null).andExpect(status().isForbidden());
        call(alex,"POST","/api/trips/"+lucia+"/messages",Map.of("text","Nos vemos en la estación.")).andExpect(status().isOk());
        Long b=bookings.findByTripIdAndPassengerId(lucia,uid("alex@uma.es")).orElseThrow().id;
        call(alex,"PATCH","/api/bookings/"+b,Map.of("action","cancel")).andExpect(status().isOk());
        call(alex,"GET","/api/trips/"+lucia+"/messages",null).andExpect(status().isForbidden());
    }
    @Test void reviewsRequireCompletedAcceptedBookingAndAreUnique() throws Exception {
        var alex=as("alex@uma.es");var hugo=as("hugo@uma.es");Long past=ride("lucia@uma.es",false);
        Long b=bookings.findByTripIdAndPassengerId(past,uid("alex@uma.es")).orElseThrow().id;
        call(hugo,"POST","/api/bookings/"+b+"/reviews",Map.of("rating",5,"comment","Bien")).andExpect(status().isForbidden());
        call(alex,"POST","/api/bookings/"+b+"/reviews",Map.of("rating",5,"comment","Puntual y agradable.")).andExpect(status().isOk()).andExpect(jsonPath("$.reviewCount").value(3));
        call(alex,"POST","/api/bookings/"+b+"/reviews",Map.of("rating",5,"comment","Duplicada")).andExpect(status().isConflict());
        Long upcoming=bookings.findByTripIdAndPassengerId(ride("lucia@uma.es",true),uid("alex@uma.es")).orElseThrow().id;
        call(alex,"POST","/api/bookings/"+upcoming+"/reviews",Map.of("rating",5,"comment","Todavía no")).andExpect(status().isConflict());
    }
    @Test void concurrentAcceptancesCannotOverbook() throws Exception {
        Long alex=uid("alex@uma.es"),nora=uid("nora@uma.es"),hugo=uid("hugo@uma.es");
        var created=service.saveTrip(alex,null,new TripInput("FUENGIROLA","CAMPUS_TEATINOS",service.today().plusDays(2),LocalTime.of(8,0),LocalTime.of(8,40),new BigDecimal("3.00"),1,"Coche de prueba",""));Long id=(Long)created.get("id");
        service.requestSeats(nora,id,1);service.requestSeats(hugo,id,1);var pending=bookings.findByTripId(id);var start=new CountDownLatch(1);var pool=Executors.newFixedThreadPool(2);
        try{List<Future<Integer>> outcomes=new ArrayList<>();for(var b:pending)outcomes.add(pool.submit(()->{start.await();try{service.actOnBooking(alex,b.id,"accept");return 200;}catch(Problem e){return e.status;}}));start.countDown();
            var statuses=new ArrayList<Integer>();for(var f:outcomes)statuses.add(f.get(10,TimeUnit.SECONDS));Collections.sort(statuses);assertEquals(List.of(200,409),statuses);assertEquals(0,service.getTrip(id,alex).get("availableSeats"));
        }finally{pool.shutdownNow();}
    }
    @Test void tripValidationAndCancellationPropagate() throws Exception {
        var alex=as("alex@uma.es");var data=new LinkedHashMap<String,Object>();data.put("origin","FUENGIROLA");data.put("destination","MIJAS");data.put("date",service.today().plusDays(1).toString());data.put("departure","08:00");data.put("arrival","08:40");data.put("price",3);data.put("capacity",2);data.put("car","Ford rojo");
        call(alex,"POST","/api/trips",data).andExpect(status().isBadRequest());data.put("destination","CAMPUS_TEATINOS");data.put("arrival","07:00");call(alex,"POST","/api/trips",data).andExpect(status().isBadRequest());
        Long own=ride("alex@uma.es",true);call(alex,"DELETE","/api/trips/"+own,null).andExpect(status().isOk());assertTrue(bookings.findByTripId(own).stream().allMatch(b->b.status==Status.CANCELLED));
    }
}
