package es.fuengiuma;

import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;

public class Problem extends RuntimeException {
    public final int status;
    public Problem(int status,String message) { super(message); this.status=status; }
    @RestControllerAdvice
    public static class Handler {
        @ExceptionHandler(Problem.class) ResponseEntity<?> problem(Problem e) { return ResponseEntity.status(e.status).body(Map.of("message",e.getMessage())); }
        @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<?> validation(MethodArgumentNotValidException e) {
            var error=e.getBindingResult().getFieldErrors().get(0);
            return ResponseEntity.badRequest().body(Map.of("message","Revisa el campo "+error.getField()+": "+error.getDefaultMessage()));
        }
        @ExceptionHandler(HttpMessageNotReadableException.class) ResponseEntity<?> malformed() { return ResponseEntity.badRequest().body(Map.of("message","Comprueba el formato de los datos, las fechas y las horas.")); }
        @ExceptionHandler(DataIntegrityViolationException.class) ResponseEntity<?> duplicate() { return ResponseEntity.status(409).body(Map.of("message","La operación entra en conflicto con un registro existente. Actualiza la página.")); }
    }
}
