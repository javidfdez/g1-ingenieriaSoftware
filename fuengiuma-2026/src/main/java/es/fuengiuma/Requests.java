package es.fuengiuma;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;

public final class Requests {
    private Requests() {}
    public record Register(@NotBlank @Size(max=60) String name,
        @NotBlank @Email @Size(max=120) @Pattern(regexp="(?i)^[^\\s@]+@(?:[a-z0-9-]+\\.)?uma\\.es$",message="Utiliza una dirección de la UMA") String email,
        @NotNull @Size(min=8,max=64) String password) {}
    public record Login(@NotBlank @Email String email,@NotBlank @Size(max=64) String password) {}
    public record Profile(@NotBlank @Size(max=60) String name,@NotNull @Size(max=300) String bio) {}
    public record TripInput(@NotBlank String origin,@NotBlank String destination,
        @NotNull LocalDate date,@NotNull LocalTime departure,@NotNull LocalTime arrival,
        @NotNull @DecimalMin("0.00") @DecimalMax("25.00") @Digits(integer=2,fraction=2) BigDecimal price,
        @Min(1) @Max(4) int capacity,@NotBlank @Size(max=80) String car,@Size(max=500) String notes) {}
    public record SeatRequest(@Min(1) @Max(4) int seats) {}
    public record BookingAction(@NotBlank String action) {}
    public record ChatInput(@NotBlank @Size(max=1000) String text) {}
    public record ReviewInput(@Min(1) @Max(5) int rating,@NotBlank @Size(max=500) String comment) {}
}
