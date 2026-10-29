package es.fuengiuma;

import es.fuengiuma.Domain.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** All people, reviews, journeys and messages in this seed are fictional examples. */
@Component @ConditionalOnProperty(name="fuengiuma.demo.enabled",havingValue="true",matchIfMissing=true)
public class SeedData implements CommandLineRunner {
    private final Stores.Users users; private final Stores.Trips trips; private final Stores.Bookings bookings; private final Stores.Messages messages; private final Stores.Reviews reviews;
    private final BCryptPasswordEncoder encoder; private final Clock clock;
    public SeedData(Stores.Users u,Stores.Trips t,Stores.Bookings b,Stores.Messages m,Stores.Reviews r,BCryptPasswordEncoder e,Clock c) { users=u;trips=t;bookings=b;messages=m;reviews=r;encoder=e;clock=c; }
    private User person(String name,String email,String bio) { var u=new User();u.name=name;u.email=email;u.bio=bio;u.passwordHash=encoder.encode("DemoUMA2026!");return users.save(u); }
    private Trip ride(User driver,String origin,String destination,LocalDate date,int hour,int minute,int duration,String price,int seats,String car,String notes) {
        var t=new Trip();t.driver=driver;t.origin=origin;t.destination=destination;t.departure=date.atTime(hour,minute);t.arrival=t.departure.plusMinutes(duration);t.price=new BigDecimal(price);t.capacity=seats;t.car=car;t.notes=notes;return trips.save(t);
    }
    private Booking accepted(Trip trip,User passenger) { var b=new Booking();b.trip=trip;b.passenger=passenger;b.seats=1;b.status=Status.ACCEPTED;return bookings.save(b); }
    private void review(Booking b,User author,User target,int stars,String comment,String role) { var r=new Review();r.booking=b;r.author=author;r.target=target;r.rating=stars;r.comment=comment;r.role=role;reviews.save(r); }
    @Override @Transactional public void run(String...args) {
        if(users.count()>0)return;
        var alex=person("Álex Ruiz","alex@uma.es","Estudio Informática. Me gusta organizar el viaje con tiempo y llegar a clase sin prisas.");
        var lucia=person("Lucía Martín","lucia@uma.es","Salgo desde Fuengirola hacia Teatinos. Música tranquila y puntualidad.");
        var nora=person("Nora Santos","nora@uma.es","Comparto los trayectos de la mañana. Nos vemos en el campus.");
        var hugo=person("Hugo Vega","hugo@uma.es","Voy a El Ejido varios días a la semana. Podemos concretar la recogida por el chat.");
        var irene=person("Irene Castro","irene@uma.es","Desde la costa a clase, con buena compañía.");
        var tomorrow=LocalDate.now(clock).plusDays(1);
        var first=ride(lucia,"FUENGIROLA","CAMPUS_TEATINOS",tomorrow,7,40,40,"3.50",3,"Seat Ibiza azul","Nos encontramos en la estación de Fuengirola. Hay espacio para la mochila.");
        ride(nora,"FUENGIROLA","CAMPUS_TEATINOS",tomorrow,8,15,35,"3.00",2,"Toyota Yaris blanco","Salida desde Los Boliches. Llegamos a la zona de la biblioteca.");
        ride(hugo,"FUENGIROLA","CAMPUS_EL_EJIDO",tomorrow,8,0,50,"4.00",3,"Renault Clio gris","Punto de encuentro junto a la estación. Preferiría salir a la hora indicada.");
        ride(irene,"MIJAS","CAMPUS_TEATINOS",tomorrow,7,50,35,"2.50",3,"Hyundai i20 blanco","Recogida en Las Lagunas, junto al parque. Gastos compartidos por persona.");
        ride(lucia,"CAMPUS_TEATINOS","FUENGIROLA",tomorrow,14,30,40,"3.50",3,"Seat Ibiza azul","La vuelta sale desde la Facultad de Ciencias.");
        var own=ride(alex,"FUENGIROLA","CAMPUS_TEATINOS",tomorrow,9,0,40,"3.00",2,"Ford Fiesta rojo","Puedo recoger cerca de la estación. Confirmamos el punto en el chat.");
        var pending=new Booking();pending.trip=own;pending.passenger=nora;pending.seats=1;pending.status=Status.PENDING;bookings.save(pending);
        var past=ride(lucia,"FUENGIROLA","CAMPUS_TEATINOS",tomorrow.minusDays(2),7,45,40,"3.50",3,"Seat Ibiza azul","Viaje de ejemplo ya finalizado.");
        accepted(past,alex); var bn=accepted(past,nora);var bh=accepted(past,hugo);
        review(bn,nora,lucia,5,"Puntual y muy fácil coordinar la recogida.","DRIVER");review(bh,hugo,lucia,4,"Buen trayecto y buena conversación.","DRIVER");
        review(bn,lucia,nora,5,"Todo perfecto, estaba en el punto de encuentro a la hora.","PASSENGER");
        var confirmed=accepted(first,alex);
        var msg=new Message();msg.trip=first;msg.author=lucia;msg.text="¡Hola! Salimos por la entrada principal de la estación. Nos vemos mañana.";msg.createdAt=LocalDateTime.now(clock);messages.save(msg);
    }
}
