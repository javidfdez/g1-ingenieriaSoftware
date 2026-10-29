package es.fuengiuma;

import es.fuengiuma.Domain.*;
import es.fuengiuma.Requests.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class RideService {
    @PersistenceContext private EntityManager entityManager;
    private final Stores.Users users; private final Stores.Trips trips; private final Stores.Bookings bookings;
    private final Stores.Messages messages; private final Stores.Reviews reviews; private final BCryptPasswordEncoder passwords;
    private final Clock clock;
    public static final Map<String,String> LOCATIONS;
    static { var places=new LinkedHashMap<String,String>();
        places.put("FUENGIROLA","Fuengirola"); places.put("MIJAS","Mijas Costa"); places.put("BENALMADENA","Benalmádena");
        places.put("TORREMOLINOS","Torremolinos"); places.put("CAMPUS_TEATINOS","Campus de Teatinos"); places.put("CAMPUS_EL_EJIDO","Campus de El Ejido");
        LOCATIONS=Collections.unmodifiableMap(places);
    }
    public RideService(Stores.Users u,Stores.Trips t,Stores.Bookings b,Stores.Messages m,Stores.Reviews r,BCryptPasswordEncoder p,Clock c) {
        users=u; trips=t; bookings=b; messages=m; reviews=r; passwords=p; clock=c;
    }
    public LocalDate today() { return LocalDate.now(clock); }
    private LocalDateTime now() { return LocalDateTime.now(clock); }
    private void require(boolean condition,int status,String message) { if(!condition) throw new Problem(status,message); }
    public User user(Long id) { if(id==null) throw new Problem(401,"Inicia sesión para continuar."); return users.findById(id).orElseThrow(()->new Problem(401,"La sesión ha caducado.")); }
    private Trip trip(Long id) { return trips.findById(id).orElseThrow(()->new Problem(404,"El viaje no existe.")); }
    private Trip locked(Long id) { return trips.lock(id).orElseThrow(()->new Problem(404,"El viaje no existe.")); }
    private void future(Trip t) { require(!t.cancelled,409,"Este viaje se ha cancelado."); require(t.departure.isAfter(now()),409,"El viaje ya ha comenzado."); }
    private boolean active(Booking b) { return b.status==Status.PENDING || b.status==Status.ACCEPTED; }
    private int occupied(Trip t) { return bookings.findByTripId(t.id).stream().filter(b->b.status==Status.ACCEPTED).mapToInt(b->b.seats).sum(); }

    public User register(Register input) {
        String email=input.email().trim().toLowerCase(Locale.ROOT);
        require(users.findByEmail(email).isEmpty(),409,"Ya existe una cuenta con ese correo.");
        require(input.password().getBytes(StandardCharsets.UTF_8).length<=72,400,"La contraseña es demasiado larga para este formato.");
        User u=new User(); u.name=input.name().strip(); u.email=email; u.passwordHash=passwords.encode(input.password());
        require(!u.name.isEmpty(),400,"Indica tu nombre."); return users.save(u);
    }
    public User login(Login input) {
        User u=users.findByEmail(input.email().trim().toLowerCase(Locale.ROOT)).orElseThrow(()->new Problem(401,"El correo o la contraseña no son correctos."));
        require(passwords.matches(input.password(),u.passwordHash),401,"El correo o la contraseña no son correctos."); return u;
    }
    public User demoUser() { return users.findByEmail("alex@uma.es").orElseThrow(()->new Problem(404,"La cuenta de demostración no está disponible.")); }
    public Map<String,Object> updateProfile(Long uid,Profile input) {
        User u=user(uid); u.name=input.name().strip(); u.bio=input.bio().strip(); require(!u.name.isEmpty(),400,"Indica tu nombre."); users.save(u); return profile(u,true);
    }
    public Map<String,Object> profile(User u,boolean privateView) {
        List<Review> all=reviews.findByTargetId(u.id);
        var p=new LinkedHashMap<String,Object>(); p.put("id",u.id); p.put("name",u.name); p.put("bio",u.bio);
        p.put("rating",all.stream().filter(r->r.role.equals("DRIVER")).mapToInt(r->r.rating).average().orElse(0));
        p.put("reviewCount",all.stream().filter(r->r.role.equals("DRIVER")).count());
        p.put("passengerRating",all.stream().filter(r->r.role.equals("PASSENGER")).mapToInt(r->r.rating).average().orElse(0));
        p.put("passengerReviewCount",all.stream().filter(r->r.role.equals("PASSENGER")).count());
        p.put("reviews",all.stream().map(r->Map.of("author",r.author.name,"rating",r.rating,"comment",r.comment,"role",r.role)).toList());
        if(privateView) p.put("email",u.email); return p;
    }
    public Map<String,Object> publicProfile(Long id) { return profile(users.findById(id).orElseThrow(()->new Problem(404,"El perfil no existe.")),false); }

    private Map<String,Object> bookingView(Booking b,Long viewer) {
        var v=new LinkedHashMap<String,Object>(); v.put("id",b.id); v.put("passenger",profile(b.passenger,false)); v.put("seats",b.seats); v.put("status",b.status.name());
        boolean participant=Objects.equals(viewer,b.passenger.id)||Objects.equals(viewer,b.trip.driver.id);
        v.put("canReview",participant && b.status==Status.ACCEPTED && !b.trip.cancelled && b.trip.arrival.isBefore(now()) && !reviews.existsByBookingIdAndAuthorId(b.id,viewer));
        return v;
    }
    public Map<String,Object> tripView(Trip t,Long viewer) {
        var v=new LinkedHashMap<String,Object>(); boolean own=Objects.equals(t.driver.id,viewer);
        v.put("id",t.id); v.put("origin",t.origin); v.put("destination",t.destination); v.put("date",t.departure.toLocalDate().toString());
        v.put("departure",t.departure.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        v.put("arrival",t.arrival.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        v.put("duration",Duration.between(t.departure,t.arrival).toMinutes()); v.put("price",t.price); v.put("capacity",t.capacity);
        v.put("availableSeats",t.capacity-occupied(t)); v.put("car",t.car); v.put("notes",t.notes); v.put("driver",profile(t.driver,false));
        v.put("cancelled",t.cancelled); v.put("own",own); v.put("future",t.departure.isAfter(now()));
        v.put("booking",viewer==null?null:bookings.findByTripIdAndPassengerId(t.id,viewer).map(b->bookingView(b,viewer)).orElse(null));
        v.put("requests",own?bookings.findByTripId(t.id).stream().map(b->bookingView(b,viewer)).toList():List.of()); return v;
    }
    @Transactional(readOnly=true)
    public List<Map<String,Object>> search(Long viewer,String origin,String destination,LocalDate date,BigDecimal maxPrice,LocalTime departAfter,LocalTime arriveBefore,int seats) {
        require(seats>=1 && seats<=4,400,"Selecciona entre una y cuatro plazas.");
        if(origin!=null&&!origin.isBlank()) require(LOCATIONS.containsKey(origin),400,"El origen no es válido.");
        if(destination!=null&&!destination.isBlank()) require(LOCATIONS.containsKey(destination),400,"El destino no es válido.");
        return trips.findAll().stream().filter(t->!t.cancelled&&t.departure.isAfter(now()))
            .filter(t->origin==null||origin.isBlank()||t.origin.equals(origin)).filter(t->destination==null||destination.isBlank()||t.destination.equals(destination))
            .filter(t->date==null||t.departure.toLocalDate().equals(date)).filter(t->maxPrice==null||t.price.compareTo(maxPrice)<=0)
            .filter(t->departAfter==null||!t.departure.toLocalTime().isBefore(departAfter)).filter(t->arriveBefore==null||!t.arrival.toLocalTime().isAfter(arriveBefore))
            .filter(t->t.capacity-occupied(t)>=seats).sorted(Comparator.comparing(t->t.departure)).map(t->tripView(t,viewer)).toList();
    }
    @Transactional(readOnly=true) public Map<String,Object> getTrip(Long id,Long viewer) { return tripView(trip(id),viewer); }
    public Map<String,Object> saveTrip(Long uid,Long id,TripInput input) {
        User owner=user(uid); Trip t=id==null?new Trip():locked(id);
        if(id!=null) { require(t.driver.id.equals(uid),403,"Solo el conductor puede editar este viaje."); future(t);
            require(bookings.findByTripId(id).stream().noneMatch(this::active),409,"Gestiona o cancela las solicitudes antes de editar el viaje."); }
        require(LOCATIONS.containsKey(input.origin())&&LOCATIONS.containsKey(input.destination()),400,"Selecciona un origen y un destino de la lista.");
        require(input.origin().startsWith("CAMPUS_") != input.destination().startsWith("CAMPUS_"),400,"El trayecto debe conectar la costa con un campus de la UMA.");
        LocalDateTime dep=LocalDateTime.of(input.date(),input.departure()), arr=LocalDateTime.of(input.date(),input.arrival());
        require(dep.isAfter(now()),400,"La salida debe estar en el futuro."); require(arr.isAfter(dep),400,"La llegada debe ser posterior a la salida, el mismo día.");
        require(Duration.between(dep,arr).toHours()<5,400,"Revisa la duración del trayecto.");
        t.driver=owner; t.origin=input.origin(); t.destination=input.destination(); t.departure=dep; t.arrival=arr;
        t.price=input.price(); t.capacity=input.capacity(); t.car=input.car().strip(); t.notes=input.notes()==null?"":input.notes().strip();
        require(!t.car.isEmpty(),400,"Describe el coche."); return tripView(trips.saveAndFlush(t),uid);
    }
    public void cancelTrip(Long uid,Long id) {
        Trip t=locked(id); require(t.driver.id.equals(user(uid).id),403,"Solo el conductor puede cancelar este viaje."); future(t); t.cancelled=true;
        bookings.findByTripId(id).stream().filter(this::active).forEach(b->{b.status=Status.CANCELLED; bookings.save(b);}); trips.save(t);
    }
    public Map<String,Object> requestSeats(Long uid,Long id,int seats) {
        User passenger=user(uid); Trip t=locked(id); future(t);
        require(!t.driver.id.equals(uid),400,"Ya eres el conductor de este viaje."); require(seats>=1 && seats<=4,400,"El número de plazas no es válido.");
        require(t.capacity-occupied(t)>=seats,409,"Ya no quedan suficientes plazas.");
        Booking b=bookings.findByTripIdAndPassengerId(id,uid).orElseGet(Booking::new);
        require(b.id==null||!active(b),409,"Ya tienes una solicitud en este viaje.");
        b.trip=t; b.passenger=passenger; b.seats=seats; b.status=Status.PENDING; bookings.saveAndFlush(b); return tripView(t,uid);
    }
    public Map<String,Object> actOnBooking(Long uid,Long id,String action) {
        user(uid); Booking initial=bookings.findById(id).orElseThrow(()->new Problem(404,"La solicitud no existe.")); Trip t=locked(initial.trip.id);
        // Refresh the managed entity after the trip lock, including concurrent actions on this booking.
        entityManager.refresh(initial); Booking b=initial; future(t);
        boolean driver=t.driver.id.equals(uid), passenger=b.passenger.id.equals(uid);
        switch(action) {
            case "accept" -> { require(driver,403,"Solo el conductor puede aceptar solicitudes."); require(b.status==Status.PENDING,409,"Esta solicitud ya se ha gestionado.");
                require(t.capacity-occupied(t)>=b.seats,409,"No quedan plazas suficientes para aceptar esta solicitud."); b.status=Status.ACCEPTED; }
            case "reject" -> { require(driver,403,"Solo el conductor puede rechazar solicitudes."); require(b.status==Status.PENDING,409,"Esta solicitud ya se ha gestionado."); b.status=Status.REJECTED; }
            case "remove" -> { require(driver,403,"Solo el conductor puede retirar una plaza."); require(b.status==Status.ACCEPTED,409,"La plaza no está confirmada."); b.status=Status.CANCELLED; }
            case "cancel" -> { require(passenger,403,"Solo el pasajero puede cancelar su solicitud."); require(active(b),409,"La solicitud ya está cerrada."); b.status=Status.CANCELLED; }
            default -> throw new Problem(400,"La acción no es válida.");
        }
        bookings.saveAndFlush(b); return tripView(t,uid);
    }
    @Transactional(readOnly=true) public Map<String,Object> myTrips(Long uid) {
        user(uid); return Map.of("driving",trips.findAll().stream().filter(t->t.driver.id.equals(uid)).sorted(Comparator.comparing((Trip t)->t.departure).reversed()).map(t->tripView(t,uid)).toList(),
            "passenger",bookings.findByPassengerId(uid).stream().sorted(Comparator.comparing((Booking b)->b.trip.departure).reversed()).map(b->tripView(b.trip,uid)).toList());
    }
    private void member(Trip t,Long uid,boolean writing) {
        user(uid); if(t.driver.id.equals(uid)) return;
        Booking b=bookings.findByTripIdAndPassengerId(t.id,uid).orElseThrow(()->new Problem(403,"Solo los participantes pueden acceder a la conversación."));
        require(active(b),403,"Tu solicitud ya no está activa.");
    }
    @Transactional(readOnly=true) public List<Map<String,Object>> conversations(Long uid) {
        user(uid); var mine=trips.findAll().stream().filter(t->t.driver.id.equals(uid)||bookings.findByTripIdAndPassengerId(t.id,uid).filter(this::active).isPresent()).sorted(Comparator.comparing((Trip t)->t.departure).reversed()).toList();
        return mine.stream().map(t->{var v=tripView(t,uid); var msgs=messages.findByTripIdOrderByCreatedAtAsc(t.id); v.put("lastMessage",msgs.isEmpty()?"Coordina aquí el punto de encuentro.":msgs.get(msgs.size()-1).text); return v;}).toList();
    }
    @Transactional(readOnly=true) public List<Map<String,Object>> messages(Long uid,Long id) {
        Trip t=trip(id); member(t,uid,false); return messages.findByTripIdOrderByCreatedAtAsc(id).stream().map(m->{var v=new LinkedHashMap<String,Object>();
            v.put("id",m.id); v.put("author",m.author.name); v.put("own",m.author.id.equals(uid)); v.put("text",m.text); v.put("time",m.createdAt.format(DateTimeFormatter.ofPattern("HH:mm"))); return (Map<String,Object>)v;}).toList();
    }
    public List<Map<String,Object>> sendMessage(Long uid,Long id,String text) {
        Trip t=trip(id); member(t,uid,true); require(!t.cancelled,409,"El viaje está cancelado.");
        require(!text.strip().isEmpty(),400,"Escribe un mensaje."); Message m=new Message(); m.trip=t; m.author=user(uid); m.text=text.strip(); m.createdAt=now(); messages.saveAndFlush(m); return messages(uid,id);
    }
    public Map<String,Object> review(Long uid,Long id,ReviewInput input) {
        User author=user(uid); Booking b=bookings.findById(id).orElseThrow(()->new Problem(404,"La reserva no existe."));
        require(b.passenger.id.equals(uid)||b.trip.driver.id.equals(uid),403,"No participaste en esta reserva.");
        require(b.status==Status.ACCEPTED&&!b.trip.cancelled&&b.trip.arrival.isBefore(now()),409,"Las valoraciones se publican después de un viaje confirmado y finalizado.");
        require(!reviews.existsByBookingIdAndAuthorId(id,uid),409,"Ya has valorado este viaje."); require(!input.comment().strip().isEmpty(),400,"Escribe un comentario.");
        Review r=new Review(); r.booking=b; r.author=author; r.target=b.passenger.id.equals(uid)?b.trip.driver:b.passenger;
        r.role=b.passenger.id.equals(uid)?"DRIVER":"PASSENGER"; r.rating=input.rating(); r.comment=input.comment().strip(); reviews.saveAndFlush(r); return profile(r.target,false);
    }
}
