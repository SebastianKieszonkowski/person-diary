package pl.kurs.persondiary.exeptions;

public class ImportConcurrencyException extends RuntimeException {

    public ImportConcurrencyException(String message) {
        super(message);
    }
}
