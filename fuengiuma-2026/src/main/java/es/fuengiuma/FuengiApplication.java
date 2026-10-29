package es.fuengiuma;

import java.time.Clock;
import java.time.ZoneId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableJpaRepositories(considerNestedRepositories = true)
public class FuengiApplication {
    public static void main(String[] args) { SpringApplication.run(FuengiApplication.class, args); }
    @Bean Clock clock() { return Clock.system(ZoneId.of("Europe/Madrid")); }
    @Bean BCryptPasswordEncoder passwords() { return new BCryptPasswordEncoder(); }
}
