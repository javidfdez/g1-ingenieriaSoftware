package es.fuengiuma;

import es.fuengiuma.Domain.*;
import jakarta.persistence.LockModeType;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public final class Stores {
    private Stores() {}
    public interface Users extends JpaRepository<User,Long> { Optional<User> findByEmail(String email); }
    public interface Trips extends JpaRepository<Trip,Long> {
        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("select t from Trip t where t.id = :id")
        Optional<Trip> lock(@Param("id") Long id);
    }
    public interface Bookings extends JpaRepository<Booking,Long> {
        List<Booking> findByTripId(Long tripId);
        List<Booking> findByPassengerId(Long userId);
        Optional<Booking> findByTripIdAndPassengerId(Long tripId,Long userId);
    }
    public interface Messages extends JpaRepository<Message,Long> { List<Message> findByTripIdOrderByCreatedAtAsc(Long tripId); }
    public interface Reviews extends JpaRepository<Review,Long> {
        List<Review> findByTargetId(Long userId);
        boolean existsByBookingIdAndAuthorId(Long bookingId,Long userId);
    }
}
