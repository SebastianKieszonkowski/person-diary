package pl.kurs.persondiary.exeptions;

public class IncorrectDateRangeException extends RuntimeException {

    public IncorrectDateRangeException(String message) {
        super(message);
    }
}
