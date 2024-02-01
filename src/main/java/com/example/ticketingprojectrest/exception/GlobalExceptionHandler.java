package com.example.ticketingprojectrest.exception;

import com.example.ticketingprojectrest.annotation.DefaultExceptionMessage;
import com.example.ticketingprojectrest.dto.DefaultExceptionMessageDto;
import com.example.ticketingprojectrest.entiy.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import javax.security.sasl.AuthenticationException;
import java.lang.reflect.Method;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TicketingProjectException.class)
    public ResponseEntity<ResponseWrapper> serviceException(TicketingProjectException se){
        String message = se.getMessage();
        return new ResponseEntity<>(ResponseWrapper.builder().success(false).code(HttpStatus.CONFLICT.value()).message(message).build(),HttpStatus.CONFLICT);
    }


    @ExceptionHandler( {UsernameNotFoundException.class,BadCredentialsException.class,InsufficientAuthenticationException.class} )
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseWrapper> authenticationException(Exception se){
        String message = se.getMessage();
        return new ResponseEntity<>(ResponseWrapper.builder().success(false).code(HttpStatus.UNAUTHORIZED.value()).message(message).build(),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler( AccountStatusException.class )
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseWrapper> authenticationException(AccountStatusException se){
        String message = se.getMessage();
        return new ResponseEntity<>(ResponseWrapper.builder().success(false).code(HttpStatus.UNAUTHORIZED.value()).message(message).build(),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler( InvalidBearerTokenException.class )
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseWrapper> authenticationException(InvalidBearerTokenException se){
        String message = se.getMessage();
        return new ResponseEntity<>(ResponseWrapper.builder().success(false).code(HttpStatus.UNAUTHORIZED.value()).message(message).build(),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler( AccessDeniedException.class )
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseWrapper> accessDeniedException(AccessDeniedException se){
        String message = se.getMessage();
        return new ResponseEntity<>(ResponseWrapper.builder().success(false).code(HttpStatus.FORBIDDEN.value()).message(message).build(),HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseWrapper> genericException( HandlerMethod handlerMethod,Throwable e) {
        Optional<DefaultExceptionMessageDto> defaultMessage = getMessageFromAnnotation(handlerMethod.getMethod());
        if (defaultMessage.isPresent() && !ObjectUtils.isEmpty(defaultMessage.get().getMessage())) {
            ResponseWrapper response = ResponseWrapper
                    .builder()
                    .success(false)
                    .message(defaultMessage.get().getMessage())
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(ResponseWrapper.builder().success(false).message("Action failed: An error occurred!").code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }




    private Optional<DefaultExceptionMessageDto> getMessageFromAnnotation(Method method) {
        DefaultExceptionMessage defaultExceptionMessage = method.getAnnotation(DefaultExceptionMessage.class);
        if (defaultExceptionMessage != null) {
            DefaultExceptionMessageDto defaultExceptionMessageDto = DefaultExceptionMessageDto
                    .builder()
                    .message(defaultExceptionMessage.defaultMessage())
                    .build();
            return Optional.of(defaultExceptionMessageDto);
        }
        return Optional.empty();
    }
}