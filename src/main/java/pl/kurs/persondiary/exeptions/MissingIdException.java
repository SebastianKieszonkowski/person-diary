package pl.kurs.persondiary.exeptions;

public class MissingIdException extends RuntimeException{

    public MissingIdException(String message) {
        super(message);
    }
}
