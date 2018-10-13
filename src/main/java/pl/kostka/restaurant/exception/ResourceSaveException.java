package pl.kostka.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ResourceSaveException extends RuntimeException {
    public ResourceSaveException(){
        super();
    }

    public ResourceSaveException(String message){
        super(message);
    }

    public ResourceSaveException(String message, Throwable cause){
        super(message, cause);
    }
}

