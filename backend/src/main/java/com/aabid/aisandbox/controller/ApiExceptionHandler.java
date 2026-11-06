package com.aabid.aisandbox.controller;
import org.springframework.http.*;import org.springframework.web.bind.MethodArgumentNotValidException;import org.springframework.web.bind.annotation.*;
import java.time.Instant;import java.util.Map;
@RestControllerAdvice public class ApiExceptionHandler{
 @ExceptionHandler(IllegalArgumentException.class) ResponseEntity<?> bad(IllegalArgumentException e){
  HttpStatus s=e.getMessage()!=null&&e.getMessage().contains("not found")?HttpStatus.NOT_FOUND:HttpStatus.BAD_REQUEST;
  return ResponseEntity.status(s).body(Map.of("timestamp",Instant.now(),"status",s.value(),"error",e.getMessage()));
 }
 @ExceptionHandler(IllegalStateException.class) ResponseEntity<?> unavailable(IllegalStateException e){return ResponseEntity.status(503).body(Map.of("timestamp",Instant.now(),"status",503,"error",e.getMessage()));}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<?> validation(MethodArgumentNotValidException e){return ResponseEntity.badRequest().body(Map.of("timestamp",Instant.now(),"status",400,"error","Validation failed"));}
}
