package com.tasks.usertaskweb.Controllers;

import com.tasks.usertaskweb.entities.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.tasks.usertaskweb.Exceptions.UserControllerException;
import com.tasks.usertaskweb.Exceptions.UserUpdateException;
import com.tasks.usertaskweb.Exceptions.UserDeleteException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserControllerException.class)
    public final ResponseEntity<ErrorMessage> notFoundError(UserControllerException exception){
        ErrorMessage error = new ErrorMessage( HttpStatus.NOT_FOUND.value(),exception.getMessage(),"Not found");
        return new ResponseEntity<ErrorMessage>(error,new HttpHeaders(), HttpStatus.NOT_FOUND);


    }
    @ExceptionHandler(UserUpdateException.class)
    public final ResponseEntity<ErrorMessage> updateError(UserUpdateException exception ) {
        ErrorMessage error = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(),"Couldn't update on the database");
        return new ResponseEntity<ErrorMessage>(error,new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserDeleteException.class)
    public final ResponseEntity<ErrorMessage> deleteError(UserDeleteException exception ) {
        ErrorMessage error = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(),"Couldn't delete user from the database");
        return new ResponseEntity<ErrorMessage>(error,new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

