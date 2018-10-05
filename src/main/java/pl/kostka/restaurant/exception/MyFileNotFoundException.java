package pl.kostka.restaurant.exception;

public class MyFileNotFoundException extends RuntimeException {
    public MyFileNotFoundException(){
        super();
    }

    public MyFileNotFoundException(String message){
        super(message);
    }

    public MyFileNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
