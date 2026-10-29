package es.fuengiuma;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Domain entities are never serialized directly; the service builds public views. */
public final class Domain {
    private Domain() {}
    public enum Status { PENDING, ACCEPTED, REJECTED, CANCELLED }

    @Entity(name="AppUser") @Table(name="app_users")
    public static class User {
        @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
        @Column(nullable=false, length=60) public String name;
        @Column(nullable=false, unique=true, length=120) public String email;
        @Column(nullable=false) public String passwordHash;
        @Column(nullable=false, length=300) public String bio = "";
    }
    @Entity(name="Trip") @Table(name="trips", indexes=@Index(name="trip_route_date", columnList="origin,destination,departure"))
    public static class Trip {
        @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
        @ManyToOne(optional=false) public User driver;
        @Column(nullable=false) public String origin;
        @Column(nullable=false) public String destination;
        @Column(nullable=false) public LocalDateTime departure;
        @Column(nullable=false) public LocalDateTime arrival;
        @Column(nullable=false, precision=6, scale=2) public BigDecimal price;
        public int capacity;
        @Column(nullable=false, length=80) public String car;
        @Column(nullable=false, length=500) public String notes = "";
        public boolean cancelled;
    }
    @Entity(name="Booking") @Table(name="bookings", uniqueConstraints=@UniqueConstraint(columnNames={"trip_id","passenger_id"}))
    public static class Booking {
        @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
        @ManyToOne(optional=false) public Trip trip;
        @ManyToOne(optional=false) public User passenger;
        public int seats;
        @Enumerated(EnumType.STRING) @Column(nullable=false) public Status status;
    }
    @Entity(name="ChatMessage") @Table(name="chat_messages")
    public static class Message {
        @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
        @ManyToOne(optional=false) public Trip trip;
        @ManyToOne(optional=false) public User author;
        @Column(nullable=false, length=1000) public String text;
        @Column(nullable=false) public LocalDateTime createdAt;
    }
    @Entity(name="TripReview") @Table(name="trip_reviews", uniqueConstraints=@UniqueConstraint(columnNames={"booking_id","author_id"}))
    public static class Review {
        @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
        @ManyToOne(optional=false) public Booking booking;
        @ManyToOne(optional=false) public User author;
        @ManyToOne(optional=false) public User target;
        @Column(nullable=false) public String role;
        public int rating;
        @Column(nullable=false, length=500) public String comment;
    }
}
