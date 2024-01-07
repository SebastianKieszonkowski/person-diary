package pl.kurs.persondiary.exeptions;

public class ImportConcurrencyException extends RuntimeException {
    private String task;

    public ImportConcurrencyException(String message) {
        super(message);
    }

    public ImportConcurrencyException(String message, String task) {
        super(message);
        this.task = task;
    }

    public String getTask() {
        return task;
    }
}
