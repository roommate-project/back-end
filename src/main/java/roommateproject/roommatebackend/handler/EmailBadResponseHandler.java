package roommateproject.roommatebackend.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roommateproject.roommatebackend.response.ResponseMessage;

@RestControllerAdvice
public class EmailBadResponseHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseMessage> validException (MethodArgumentNotValidException e){
        ResponseMessage returnMessage = new ResponseMessage(e);
        return new ResponseEntity<>(returnMessage, HttpStatus.BAD_REQUEST);
    }
}
