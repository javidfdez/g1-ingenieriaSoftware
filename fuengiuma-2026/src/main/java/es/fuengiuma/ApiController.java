package es.fuengiuma;

import es.fuengiuma.Domain.User;
import es.fuengiuma.Requests.*;
import jakarta.servlet.http.*;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api")
public class ApiController {
    private final RideService service; private final boolean demo;
    public ApiController(RideService s,@Value("${fuengiuma.demo.enabled:true}") boolean d) { service=s; demo=d; }
    private Long uid(HttpSession s) { return (Long)s.getAttribute("userId"); }
    private Map<String,Object> signIn(User u,HttpServletRequest request) {
        request.getSession(); request.changeSessionId(); request.getSession().setAttribute("userId",u.id); return service.profile(u,true);
    }
    @GetMapping("/bootstrap") public Map<String,Object> bootstrap(HttpSession session) {
        var r=new LinkedHashMap<String,Object>(); r.put("today",service.today().toString()); r.put("csrfToken",CsrfFilter.token(session)); r.put("demoEnabled",demo);
        r.put("locations",RideService.LOCATIONS.entrySet().stream().map(e->Map.of("id",e.getKey(),"name",e.getValue())).toList());
        r.put("me",uid(session)==null?null:service.profile(service.user(uid(session)),true)); return r;
    }
    @PostMapping("/auth/register") public Map<String,Object> register(@Valid @RequestBody Register r,HttpServletRequest q) { return signIn(service.register(r),q); }
    @PostMapping("/auth/login") public Map<String,Object> login(@Valid @RequestBody Login r,HttpServletRequest q) { return signIn(service.login(r),q); }
    @PostMapping("/auth/demo") public Map<String,Object> demo(HttpServletRequest q) { if(!demo) throw new Problem(404,"El modo demo está desactivado."); return signIn(service.demoUser(),q); }
    @PostMapping("/auth/logout") public Map<String,Object> logout(HttpSession s) { s.invalidate(); return Map.of("ok",true); }
    @PatchMapping("/me") public Map<String,Object> profile(@Valid @RequestBody Profile p,HttpSession s) { return service.updateProfile(uid(s),p); }
    @GetMapping("/users/{id}") public Map<String,Object> publicProfile(@PathVariable Long id) { return service.publicProfile(id); }
    @GetMapping("/trips") public List<Map<String,Object>> trips(HttpSession s,@RequestParam(required=false) String origin,@RequestParam(required=false) String destination,
        @RequestParam(required=false) LocalDate date,@RequestParam(required=false) BigDecimal maxPrice,@RequestParam(required=false) LocalTime departAfter,
        @RequestParam(required=false) LocalTime arriveBefore,@RequestParam(defaultValue="1") int seats) {
        return service.search(uid(s),origin,destination,date,maxPrice,departAfter,arriveBefore,seats);
    }
    @GetMapping("/trips/{id}") public Map<String,Object> trip(@PathVariable Long id,HttpSession s) { return service.getTrip(id,uid(s)); }
    @PostMapping("/trips") public Map<String,Object> create(@Valid @RequestBody TripInput t,HttpSession s) { return service.saveTrip(uid(s),null,t); }
    @PatchMapping("/trips/{id}") public Map<String,Object> edit(@PathVariable Long id,@Valid @RequestBody TripInput t,HttpSession s) { return service.saveTrip(uid(s),id,t); }
    @DeleteMapping("/trips/{id}") public Map<String,Object> cancel(@PathVariable Long id,HttpSession s) { service.cancelTrip(uid(s),id); return Map.of("ok",true); }
    @PostMapping("/trips/{id}/bookings") public Map<String,Object> book(@PathVariable Long id,@Valid @RequestBody SeatRequest b,HttpSession s) { return service.requestSeats(uid(s),id,b.seats()); }
    @PatchMapping("/bookings/{id}") public Map<String,Object> booking(@PathVariable Long id,@Valid @RequestBody BookingAction b,HttpSession s) { return service.actOnBooking(uid(s),id,b.action()); }
    @GetMapping("/me/trips") public Map<String,Object> mine(HttpSession s) { return service.myTrips(uid(s)); }
    @GetMapping("/conversations") public List<Map<String,Object>> conversations(HttpSession s) { return service.conversations(uid(s)); }
    @GetMapping("/trips/{id}/messages") public List<Map<String,Object>> messages(@PathVariable Long id,HttpSession s) { return service.messages(uid(s),id); }
    @PostMapping("/trips/{id}/messages") public List<Map<String,Object>> message(@PathVariable Long id,@Valid @RequestBody ChatInput m,HttpSession s) { return service.sendMessage(uid(s),id,m.text()); }
    @PostMapping("/bookings/{id}/reviews") public Map<String,Object> review(@PathVariable Long id,@Valid @RequestBody ReviewInput r,HttpSession s) { return service.review(uid(s),id,r); }
}
