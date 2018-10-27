package pl.kostka.restaurant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ProductIncompatibilityException extends RuntimeException{

    public ProductIncompatibilityException(){
        super();
    }

    public ProductIncompatibilityException(String message){
        super(message);
    }

    public ProductIncompatibilityException(String message, Throwable cause){
        super(message, cause);
    }
}
