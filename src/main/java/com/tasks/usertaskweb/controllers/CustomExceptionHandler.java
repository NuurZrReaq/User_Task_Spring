package com.tasks.usertaskweb.controllers;

import com.tasks.usertaskweb.exceptions.*;
import com.tasks.usertaskweb.models.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserControllerException.class)
    public final ResponseEntity<ErrorMessage> userNotFoundError(UserControllerException exception){
        ErrorMessage error = new ErrorMessage( HttpStatus.NOT_FOUND.value(),
                exception.getMessage(), "Not found");
        return new ResponseEntity<ErrorMessage>(error,new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskGettingException.class)
    public final ResponseEntity<ErrorMessage> taskNotFoundError(TaskGettingException exception){
        ErrorMessage error = new ErrorMessage( HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),"Not found");
        return new ResponseEntity<ErrorMessage>(error,new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserUpdateException.class)
    public final ResponseEntity<ErrorMessage> updateUserError(UserUpdateException exception ) {
        ErrorMessage error = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),"Couldn't update on the database");
        return new ResponseEntity<ErrorMessage>(error,new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TaskUpdateException.class)
    public final ResponseEntity<ErrorMessage> updateTaskError(TaskUpdateException exception ) {
        ErrorMessage error = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),"Couldn't update Task on the database");
        return new ResponseEntity<ErrorMessage>(error,new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserDeleteException.class)
    public final ResponseEntity<ErrorMessage> deleteUserError(UserDeleteException exception ) {
        ErrorMessage error = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),"Couldn't delete user from the database");
        return new ResponseEntity<ErrorMessage>(error,new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TaskDeleteException.class)
    public final ResponseEntity<ErrorMessage> deleteTaskError(TaskDeleteException exception ) {
        ErrorMessage error = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),"Couldn't delete Task from the database");
        return new ResponseEntity<ErrorMessage>(error,new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public final ResponseEntity<ErrorMessage> notAuthorized(NotAuthorizedException exception) {
        ErrorMessage error = new ErrorMessage(HttpStatus.UNAUTHORIZED.value(),
                exception.getMessage(),"Not Authorized Access");
        return new ResponseEntity<ErrorMessage>(error,new HttpHeaders(),HttpStatus.UNAUTHORIZED);
    }

}

