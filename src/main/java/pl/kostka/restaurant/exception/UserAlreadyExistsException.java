package pl.kostka.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.IM_USED)
public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(){
        super();
    }

    public UserAlreadyExistsException(String message){
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable cause){
        super(message, cause);
    }
}
